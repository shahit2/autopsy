/*
 * Autopsy Forensic Browser
 * 
 * Copyright 2013 - 2014 Basis Technology Corp.
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
package org.sleuthkit.autopsy.modules.filetypeid;

import org.sleuthkit.autopsy.ingest.IngestModuleIngestJobSettings;
import org.sleuthkit.autopsy.ingest.IngestModuleIngestJobSettingsPanel;

/**
 * UI component used to set ingest job options for file type identifier ingest
 * modules.
 */
final class FileTypeIdModuleSettingsPanel extends IngestModuleIngestJobSettingsPanel {

    private final FileTypeIdModuleSettings settings;

    public FileTypeIdModuleSettingsPanel(FileTypeIdModuleSettings settings) {
        this.settings = settings;
        initComponents();
        customizeComponents();
    }

    private void customizeComponents() {
        skipKnownCheckBox.setSelected(settings.skipKnownFiles());
    }

    @Override
    public IngestModuleIngestJobSettings getSettings() {
        return settings;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        skipKnownCheckBox = new javax.swing.JCheckBox();

        skipKnownCheckBox.setSelected(true);
        skipKnownCheckBox.setText(org.openide.util.NbBundle.getMessage(FileTypeIdModuleSettingsPanel.class, "FileTypeIdModuleSettingsPanel.skipKnownCheckBox.text")); // NOI18N
        skipKnownCheckBox.setToolTipText(org.openide.util.NbBundle.getMessage(FileTypeIdModuleSettingsPanel.class, "FileTypeIdModuleSettingsPanel.skipKnownCheckBox.toolTipText")); // NOI18N
        skipKnownCheckBox.setLabel(org.openide.util.NbBundle.getMessage(FileTypeIdModuleSettingsPanel.class, "FileTypeIdModuleSettingsPanel.skipKnownCheckBox.text")); // NOI18N
        skipKnownCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                skipKnownCheckBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(skipKnownCheckBox)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addComponent(skipKnownCheckBox)
                .addContainerGap(47, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void skipKnownCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_skipKnownCheckBoxActionPerformed
        settings.setSkipKnownFiles(skipKnownCheckBox.isSelected());
    }//GEN-LAST:event_skipKnownCheckBoxActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox skipKnownCheckBox;
    // End of variables declaration//GEN-END:variables
}
