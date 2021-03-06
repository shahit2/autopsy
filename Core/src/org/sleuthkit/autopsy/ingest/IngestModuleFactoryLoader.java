/*
 * Autopsy Forensic Browser
 *
 * Copyright 2014 Basis Technology Corp.
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
package org.sleuthkit.autopsy.ingest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import org.openide.util.Lookup;
import org.sleuthkit.autopsy.coreutils.Logger;

/**
 * Looks up loaded ingest module factories using the NetBeans global lookup.
 */
final class IngestModuleFactoryLoader {

    private static final Logger logger = Logger.getLogger(IngestModuleFactoryLoader.class.getName());
    private static IngestModuleFactoryLoader instance;

    private IngestModuleFactoryLoader() {
    }

    synchronized static IngestModuleFactoryLoader getInstance() {
        if (instance == null) {
            instance = new IngestModuleFactoryLoader();
        }
        return instance;
    }

    synchronized List<IngestModuleFactory> getIngestModuleFactories() {
        // Discover the ingest module factories, making sure that there are no
        // duplicate module display names. The duplicates requirement could be
        // eliminated if the enabled/disabled modules setting was by factory 
        // class name instead of module display name. Also note that that we are 
        // temporarily  hard-coding ordering of module factories until the 
        // module configuration file is reworked, so the discovered factories 
        // are initially mapped by class name.
        HashSet<String> moduleDisplayNames = new HashSet<>();
        HashMap<String, IngestModuleFactory> moduleFactoriesByClass = new HashMap<>();
        Collection<? extends IngestModuleFactory> factories = Lookup.getDefault().lookupAll(IngestModuleFactory.class);
        for (IngestModuleFactory factory : factories) {
            logger.log(Level.INFO, "Found ingest module factory: name = {0}, version = {1}", new Object[]{factory.getModuleDisplayName(), factory.getModuleVersionNumber()}); //NON-NLS
            moduleFactoriesByClass.put(factory.getClass().getCanonicalName(), factory);
            moduleDisplayNames.add(factory.getModuleDisplayName());
            if (!moduleDisplayNames.contains(factory.getModuleDisplayName())) {
                // Not popping up a message box to keep this class UI-indepdent.
                logger.log(Level.SEVERE, "Found duplicate ingest module display name (name = {0})", factory.getModuleDisplayName()); //NON-NLS
            }
        }

        // Kick out the sample modules factory.
        moduleFactoriesByClass.remove("org.sleuthkit.autopsy.examples.SampleIngestModuleFactory");

        // Do the core ingest module ordering hack described above.
        ArrayList<String> coreModuleOrdering = new ArrayList<String>() {
            {
                add("org.sleuthkit.autopsy.recentactivity.RecentActivityExtracterModuleFactory");
                add("org.sleuthkit.autopsy.ewfverify.EwfVerifierModuleFactory");
                add("org.sleuthkit.autopsy.hashdatabase.HashLookupModuleFactory");
                add("org.sleuthkit.autopsy.modules.filetypeid.FileTypeIdModuleFactory");
                add("org.sleuthkit.autopsy.modules.sevenzip.ArchiveFileExtractorModuleFactory");
                add("org.sleuthkit.autopsy.modules.exif.ExifParserModuleFactory");
                add("org.sleuthkit.autopsy.keywordsearch.KeywordSearchModuleFactory");
                add("org.sleuthkit.autopsy.thunderbirdparser.EmailParserModuleFactory");
                add("org.sleuthkit.autopsy.modules.fileextmismatch.FileExtMismatchDetectorModuleFactory");
            }
        };
        List<IngestModuleFactory> orderedModuleFactories = new ArrayList<>();
        for (String className : coreModuleOrdering) {
            IngestModuleFactory coreFactory = moduleFactoriesByClass.remove(className);
            if (coreFactory != null) {
                orderedModuleFactories.add(coreFactory);
            }
        }

        // Add in any non-core factories discovered. Order is not guaranteed!
        for (IngestModuleFactory nonCoreFactory : moduleFactoriesByClass.values()) {
            orderedModuleFactories.add(nonCoreFactory);
        }

        return orderedModuleFactories;
    }
}