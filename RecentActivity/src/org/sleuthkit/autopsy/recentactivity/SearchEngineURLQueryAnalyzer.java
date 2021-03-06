/*
 * Autopsy Forensic Browser
 * 
 * Copyright 2012-2014 Basis Technology Corp.
 * Contact: carrier <at> sleuthkit <dot> org
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sleuthkit.autopsy.recentactivity;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.openide.util.NbBundle;
import org.sleuthkit.autopsy.coreutils.Logger;
import org.sleuthkit.autopsy.coreutils.PlatformUtil;
import org.sleuthkit.autopsy.coreutils.XMLUtil;
import org.sleuthkit.autopsy.ingest.IngestJobContext;
import org.sleuthkit.autopsy.ingest.IngestModule.IngestModuleException;
import org.sleuthkit.autopsy.ingest.IngestServices;
import org.sleuthkit.autopsy.ingest.ModuleDataEvent;
import org.sleuthkit.datamodel.AbstractFile;
import org.sleuthkit.datamodel.BlackboardArtifact;
import org.sleuthkit.datamodel.BlackboardArtifact.ARTIFACT_TYPE;
import org.sleuthkit.datamodel.BlackboardAttribute;
import org.sleuthkit.datamodel.BlackboardAttribute.ATTRIBUTE_TYPE;
import org.sleuthkit.datamodel.Content;
import org.sleuthkit.datamodel.TskException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * This recent activity extractor attempts to extract web queries from major
 * search engines by querying the blackboard for web history and bookmark
 * artifacts, and extracting search text from them.
 *
 *
 * To add search engines, edit SearchEngines.xml under RecentActivity
 *
 */
class SearchEngineURLQueryAnalyzer extends Extract {

    private static final Logger logger = Logger.getLogger(SearchEngineURLQueryAnalyzer.class.getName());
    private static final String XMLFILE = "SEUQAMappings.xml"; //NON-NLS
    private static final String XSDFILE = "SearchEngineSchema.xsd"; //NON-NLS
    private static String[] searchEngineNames;
    private static SearchEngineURLQueryAnalyzer.SearchEngine[] engines;
    private static Document xmlinput;
    private static final SearchEngineURLQueryAnalyzer.SearchEngine NullEngine = new SearchEngineURLQueryAnalyzer.SearchEngine(
            NbBundle.getMessage(SearchEngineURLQueryAnalyzer.class, "SearchEngineURLQueryAnalyzer.engineName.none"),
            NbBundle.getMessage(SearchEngineURLQueryAnalyzer.class, "SearchEngineURLQueryAnalyzer.domainSubStr.none"),
            new HashMap<String,String>());
    private Content dataSource;
    private IngestJobContext context;

    SearchEngineURLQueryAnalyzer() {
    }

    private static class SearchEngine {

        private String _engineName;
        private String _domainSubstring;
        private Map<String, String> _splits;
        private int _count;

        SearchEngine(String engineName, String domainSubstring, Map<String, String> splits) {
            _engineName = engineName;
            _domainSubstring = domainSubstring;
            _splits = splits;
            _count = 0;
        }

        void increment() {
            ++_count;
        }

        String getEngineName() {
            return _engineName;
        }

        String getDomainSubstring() {
            return _domainSubstring;
        }

        int getTotal() {
            return _count;
        }

        Set<Map.Entry<String, String>> getSplits() {
            return this._splits.entrySet();
        }

        @Override
        public String toString() {
            String split = " ";
            for (Map.Entry<String, String> kvp : getSplits()) {
                split = split + "[ " + kvp.getKey() + " :: " + kvp.getValue() + " ]" + ", ";
            }
            return NbBundle.getMessage(this.getClass(), "SearchEngineURLQueryAnalyzer.toString",
                                       _engineName, _domainSubstring, _count, split);
        }
    }

    private void createEngines() {
        NodeList nlist = xmlinput.getElementsByTagName("SearchEngine"); //NON-NLS
        SearchEngineURLQueryAnalyzer.SearchEngine[] listEngines = new SearchEngineURLQueryAnalyzer.SearchEngine[nlist.getLength()];
        for (int i = 0; i < nlist.getLength(); i++) {
            NamedNodeMap nnm = nlist.item(i).getAttributes();

            String EngineName = nnm.getNamedItem("engine").getNodeValue(); //NON-NLS
            String EnginedomainSubstring = nnm.getNamedItem("domainSubstring").getNodeValue(); //NON-NLS
            Map<String, String> splits = new HashMap<>();

            NodeList listSplits = xmlinput.getElementsByTagName("splitToken"); //NON-NLS
            for (int k = 0; k < listSplits.getLength(); k++) {
                if (listSplits.item(k).getParentNode().getAttributes().getNamedItem("engine").getNodeValue().equals(EngineName)) { //NON-NLS
                    splits.put(listSplits.item(k).getAttributes().getNamedItem("plainToken").getNodeValue(), listSplits.item(k).getAttributes().getNamedItem("regexToken").getNodeValue()); //NON-NLS
                }
            }

            SearchEngineURLQueryAnalyzer.SearchEngine Se = new SearchEngineURLQueryAnalyzer.SearchEngine(EngineName, EnginedomainSubstring, splits);
            //System.out.println("Search Engine: " + Se.toString());
            listEngines[i] = Se;
        }
        engines = listEngines;
    }

    /**
     * Returns which of the supported SearchEngines, if any, the given string
     * belongs to.
     *
     * @param domain domain as part of the URL
     * @return supported search engine the domain belongs to, if any
     *
     */
    private static SearchEngineURLQueryAnalyzer.SearchEngine getSearchEngine(String domain) {
        if (engines == null) {
            return SearchEngineURLQueryAnalyzer.NullEngine;
        }
        for (int i = 0; i < engines.length; i++) {
            if (domain.contains(engines[i].getDomainSubstring())) {
                return engines[i];
            }
        }
        return SearchEngineURLQueryAnalyzer.NullEngine;
    }

    private void getSearchEngineNames() {
        String[] listNames = new String[engines.length];
        for (int i = 0; i < listNames.length; i++) {
            listNames[i] = engines[i]._engineName;
        }
        searchEngineNames = listNames;
    }

    /**
     * Attempts to extract the query from a URL.
     *
     * @param url The URL string to be dissected.
     * @return The extracted search query.
     */
    private String extractSearchEngineQuery(String url) {
        String x = "NoQuery"; //NON-NLS
        SearchEngineURLQueryAnalyzer.SearchEngine eng = getSearchEngine(url);
        for (Map.Entry<String, String> kvp : eng.getSplits()) {
            if (url.contains(kvp.getKey())) {
                x = split2(url, kvp.getValue());
                break;
            }
        }
        try { //try to decode the url
            String decoded = URLDecoder.decode(x, "UTF-8"); //NON-NLS
            return decoded;
        } catch (UnsupportedEncodingException uee) { //if it fails, return the encoded string
            logger.log(Level.FINE, "Error during URL decoding ", uee); //NON-NLS
            return x;
        }
    }

    /**
     * Splits URLs based on a delimeter (key). .contains() and .split()
     *
     * @param url The URL to be split
     * @param value the delimeter value used to split the URL into its search
     * token, extracted from the xml.
     * @return The extracted search query
     *
     */
    private String split2(String url, String value) {
        String basereturn = "NoQuery"; //NON-NLS
        String v = value;
        //Want to determine if string contains a string based on splitkey, but we want to split the string on splitKeyConverted due to regex
        if (value.contains("\\?")) {
            v = value.replace("\\?", "?");
        }
        String[] sp = url.split(v);
        if (sp.length >= 2) {
            if (sp[sp.length - 1].contains("&")) {
                basereturn = sp[sp.length - 1].split("&")[0];
            } else {
                basereturn = sp[sp.length - 1];
            }
        }
        return basereturn;
    }

    private void getURLs() {
        int totalQueries = 0;
        try {
            //from blackboard_artifacts
            Collection<BlackboardArtifact> listArtifacts = currentCase.getSleuthkitCase().getMatchingArtifacts("WHERE (`artifact_type_id` = '" + ARTIFACT_TYPE.TSK_WEB_BOOKMARK.getTypeID() //NON-NLS
                    + "' OR `artifact_type_id` = '" + ARTIFACT_TYPE.TSK_WEB_HISTORY.getTypeID() + "') ");  //List of every 'web_history' and 'bookmark' artifact NON-NLS
            logger.log(Level.INFO, "Processing {0} blackboard artifacts.", listArtifacts.size()); //NON-NLS
            getAll:
            for (BlackboardArtifact artifact : listArtifacts) {
                //initializing default attributes
                String query = "";
                String searchEngineDomain = "";
                String browser = "";
                long last_accessed = -1;

                long fileId = artifact.getObjectID();
                boolean isFromSource = tskCase.isFileFromSource(dataSource, fileId);
                if (!isFromSource) {
                    //File was from a different dataSource. Skipping.
                    continue;
                }

                AbstractFile file = tskCase.getAbstractFileById(fileId);
                if (file == null) {
                    continue;
                }

                SearchEngineURLQueryAnalyzer.SearchEngine se = NullEngine;
                //from blackboard_attributes
                Collection<BlackboardAttribute> listAttributes = currentCase.getSleuthkitCase().getMatchingAttributes("Where `artifact_id` = " + artifact.getArtifactID()); //NON-NLS
                getAttributes:
                for (BlackboardAttribute attribute : listAttributes) {
                    if (context.isJobCancelled()) {
                        break getAll;       //User cancled the process.
                    }
                    if (attribute.getAttributeTypeID() == BlackboardAttribute.ATTRIBUTE_TYPE.TSK_URL.getTypeID()) {
                        final String urlString = attribute.getValueString();
                        se = getSearchEngine(urlString);
                        if (!se.equals(NullEngine)) {
                            query = extractSearchEngineQuery(attribute.getValueString());
                            if (query.equals("NoQuery") || query.equals("")) {   //False positive match, artifact was not a query. NON-NLS
                                break getAttributes;
                            }
                        } else if (se.equals(NullEngine)) {
                            break getAttributes;    //could not determine type. Will move onto next artifact
                        }
                    } else if (attribute.getAttributeTypeID() == BlackboardAttribute.ATTRIBUTE_TYPE.TSK_PROG_NAME.getTypeID()) {
                        browser = attribute.getValueString();
                    } else if (attribute.getAttributeTypeID() == BlackboardAttribute.ATTRIBUTE_TYPE.TSK_DOMAIN.getTypeID()) {
                        searchEngineDomain = attribute.getValueString();
                    } else if (attribute.getAttributeTypeID() == BlackboardAttribute.ATTRIBUTE_TYPE.TSK_DATETIME_ACCESSED.getTypeID()) {
                        last_accessed = attribute.getValueLong();
                    }
                }

                if (!se.equals(NullEngine) && !query.equals("NoQuery") && !query.equals("")) { //NON-NLS
                    Collection<BlackboardAttribute> bbattributes = new ArrayList<>();
                    bbattributes.add(new BlackboardAttribute(ATTRIBUTE_TYPE.TSK_DOMAIN.getTypeID(),
                                                             NbBundle.getMessage(this.getClass(),
                                                                                 "SearchEngineURLQueryAnalyzer.parentModuleName"), searchEngineDomain));
                    bbattributes.add(new BlackboardAttribute(ATTRIBUTE_TYPE.TSK_TEXT.getTypeID(),
                                                             NbBundle.getMessage(this.getClass(),
                                                                                 "SearchEngineURLQueryAnalyzer.parentModuleName"), query));
                    bbattributes.add(new BlackboardAttribute(ATTRIBUTE_TYPE.TSK_PROG_NAME.getTypeID(),
                                                             NbBundle.getMessage(this.getClass(),
                                                                                 "SearchEngineURLQueryAnalyzer.parentModuleName"), browser));
                    bbattributes.add(new BlackboardAttribute(ATTRIBUTE_TYPE.TSK_DATETIME_ACCESSED.getTypeID(),
                                                             NbBundle.getMessage(this.getClass(),
                                                                                 "SearchEngineURLQueryAnalyzer.parentModuleName"), last_accessed));
                    this.addArtifact(ARTIFACT_TYPE.TSK_WEB_SEARCH_QUERY, file, bbattributes);
                    se.increment();
                    ++totalQueries;
                }
            }
        } catch (TskException e) {
            logger.log(Level.SEVERE, "Encountered error retrieving artifacts for search engine queries", e); //NON-NLS
        } finally {
            if (context.isJobCancelled()) {
                logger.info("Operation terminated by user."); //NON-NLS
            }
            IngestServices.getInstance().fireModuleDataEvent(new ModuleDataEvent(
                    NbBundle.getMessage(this.getClass(), "SearchEngineURLQueryAnalyzer.parentModuleName.noSpace"),
                    BlackboardArtifact.ARTIFACT_TYPE.TSK_WEB_SEARCH_QUERY));
            logger.log(Level.INFO, "Extracted {0} queries from the blackboard", totalQueries); //NON-NLS
        }
    }

    private String getTotals() {
        String total = "";
        if (engines == null) {
            return total;
        }
        for (SearchEngineURLQueryAnalyzer.SearchEngine se : engines) {
            total += se.getEngineName() + " : " + se.getTotal() + "\n";
        }
        return total;
    }

    @Override
    public void process(Content dataSource, IngestJobContext context) {
        this.dataSource = dataSource;
        this.context = context;
        this.getURLs();
        logger.log(Level.INFO, "Search Engine stats: \n{0}", getTotals()); //NON-NLS
    }

    @Override
    void init() throws IngestModuleException {
        try {
            PlatformUtil.extractResourceToUserConfigDir(SearchEngineURLQueryAnalyzer.class, XMLFILE, false);
            init2();
        } catch (IOException e) {
            String message = NbBundle
                    .getMessage(this.getClass(), "SearchEngineURLQueryAnalyzer.init.exception.msg", XMLFILE);
            logger.log(Level.SEVERE, message, e);
            throw new IngestModuleException(message);
        }
    }

    private void init2() {
        try {
            String path = PlatformUtil.getUserConfigDirectory() + File.separator + XMLFILE;
            File f = new File(path);
            logger.log(Level.INFO, "Load successful"); //NON-NLS
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document xml = db.parse(f);
            xmlinput = xml;

            if (!XMLUtil.xmlIsValid(xml, SearchEngineURLQueryAnalyzer.class, XSDFILE)) {
                logger.log(Level.WARNING, "Error loading Search Engines: could not validate against [" + XSDFILE + "], results may not be accurate."); //NON-NLS
            }
            createEngines();
            getSearchEngineNames();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Was not able to load SEUQAMappings.xml", e); //NON-NLS
        } catch (ParserConfigurationException pce) {
            logger.log(Level.SEVERE, "Unable to build XML parser", pce); //NON-NLS
        } catch (SAXException sxe) {
            logger.log(Level.SEVERE, "Unable to parse XML file", sxe); //NON-NLS
        }
    }

    @Override
    public void complete() {
        logger.info("Search Engine URL Query Analyzer has completed."); //NON-NLS
    }

    @Override
    public void stop() {
        logger.info("Attempted to stop Search Engine URL Query Analyzer, but operation is not supported; skipping..."); //NON-NLS
    }
}
