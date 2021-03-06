/*
 * Autopsy Forensic Browser
 *
 * Copyright 2011 Basis Technology Corp.
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

package org.sleuthkit.autopsy.casemodule;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;

/**
 *
 */
public class CueBannerPanel extends javax.swing.JPanel {

    final private static String title = NbBundle.getMessage(CueBannerPanel.class, "CueBannerPanel.title.text");
    final private static JFrame frame = new JFrame(title);
    final static JDialog recentCasesWindow = new JDialog(frame, title, true); // to make the popUp Window to be modal

    // for error handling
    private static JPanel caller = new JPanel();
    private String className = this.getClass().toString();

    public CueBannerPanel() {
        initComponents();
    }

    public CueBannerPanel(String welcomeLogo) {
        initComponents();
        ClassLoader cl = Lookup.getDefault().lookup(ClassLoader.class);
        if (cl != null) {
            ImageIcon icon = new ImageIcon(cl.getResource(welcomeLogo));
            autopsyLogo.setIcon(icon);
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        closeButton = new javax.swing.JButton();
        editorPanel = new javax.swing.JPanel();
        newCaseButton = new javax.swing.JButton();
        openRecentButton = new javax.swing.JButton();
        createNewLabel = new javax.swing.JLabel();
        openRecentLabel = new javax.swing.JLabel();
        openCaseButton = new javax.swing.JButton();
        openLabel = new javax.swing.JLabel();
        autopsyLogo = new javax.swing.JLabel();
        this.autopsyLogo.setText("");

        closeButton.setText(org.openide.util.NbBundle.getMessage(CueBannerPanel.class, "CueBannerPanel.closeButton.text")); // NOI18N

        newCaseButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/sleuthkit/autopsy/casemodule/btn_icon_create_new_case.png"))); // NOI18N NON-NLS
        newCaseButton.setText(org.openide.util.NbBundle.getMessage(CueBannerPanel.class, "CueBannerPanel.newCaseButton.text")); // NOI18N
        newCaseButton.setBorder(null);
        newCaseButton.setBorderPainted(false);
        newCaseButton.setContentAreaFilled(false);
        newCaseButton.setPreferredSize(new java.awt.Dimension(70, 70));
        newCaseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newCaseButtonActionPerformed(evt);
            }
        });

        openRecentButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/sleuthkit/autopsy/casemodule/btn_icon_open_recent.png"))); // NOI18N NON-NLS
        openRecentButton.setText(org.openide.util.NbBundle.getMessage(CueBannerPanel.class, "CueBannerPanel.openRecentButton.text")); // NOI18N
        openRecentButton.setBorder(null);
        openRecentButton.setBorderPainted(false);
        openRecentButton.setContentAreaFilled(false);
        openRecentButton.setPreferredSize(new java.awt.Dimension(70, 70));
        openRecentButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openRecentButtonActionPerformed(evt);
            }
        });

        createNewLabel.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N NON-NLS
        createNewLabel.setText(org.openide.util.NbBundle.getMessage(CueBannerPanel.class, "CueBannerPanel.createNewLabel.text")); // NOI18N

        openRecentLabel.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N NON-NLS
        openRecentLabel.setText(org.openide.util.NbBundle.getMessage(CueBannerPanel.class, "CueBannerPanel.openRecentLabel.text")); // NOI18N

        openCaseButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/sleuthkit/autopsy/casemodule/btn_icon_open_existing.png"))); // NOI18N NON-NLS
        openCaseButton.setText(org.openide.util.NbBundle.getMessage(CueBannerPanel.class, "CueBannerPanel.openCaseButton.text")); // NOI18N
        openCaseButton.setBorder(null);
        openCaseButton.setBorderPainted(false);
        openCaseButton.setContentAreaFilled(false);
        openCaseButton.setMargin(new java.awt.Insets(1, 1, 1, 1));
        openCaseButton.setPreferredSize(new java.awt.Dimension(70, 70));
        openCaseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openCaseButtonActionPerformed(evt);
            }
        });

        openLabel.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N NON-NLS
        openLabel.setText(org.openide.util.NbBundle.getMessage(CueBannerPanel.class, "CueBannerPanel.openLabel.text")); // NOI18N

        javax.swing.GroupLayout editorPanelLayout = new javax.swing.GroupLayout(editorPanel);
        editorPanel.setLayout(editorPanelLayout);
        editorPanelLayout.setHorizontalGroup(
            editorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(editorPanelLayout.createSequentialGroup()
                .addGroup(editorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(editorPanelLayout.createSequentialGroup()
                        .addComponent(newCaseButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(createNewLabel))
                    .addGroup(editorPanelLayout.createSequentialGroup()
                        .addComponent(openRecentButton, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(openRecentLabel))
                    .addGroup(editorPanelLayout.createSequentialGroup()
                        .addComponent(openCaseButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(openLabel)))
                .addContainerGap(60, Short.MAX_VALUE))
        );
        editorPanelLayout.setVerticalGroup(
            editorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(editorPanelLayout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(editorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(createNewLabel)
                    .addComponent(newCaseButton, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(editorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(openRecentLabel)
                    .addComponent(openRecentButton, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(editorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(openLabel)
                    .addComponent(openCaseButton, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        autopsyLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/sleuthkit/autopsy/casemodule/welcome_logo.png"))); // NOI18N NON-NLS
        autopsyLogo.setText(org.openide.util.NbBundle.getMessage(CueBannerPanel.class, "CueBannerPanel.autopsyLogo.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(autopsyLogo)
                .addGap(29, 29, 29)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(editorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(closeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(autopsyLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(editorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(closeButton)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void newCaseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newCaseButtonActionPerformed
        Lookup.getDefault().lookup(CaseNewActionInterface.class).actionPerformed(evt);
    }//GEN-LAST:event_newCaseButtonActionPerformed

    private void openCaseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openCaseButtonActionPerformed
        Lookup.getDefault().lookup(CaseOpenAction.class).actionPerformed(evt);
    }//GEN-LAST:event_openCaseButtonActionPerformed

    private void openRecentButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openRecentButtonActionPerformed

        // open the recent cases dialog
        Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();

        // set the popUp window / JFrame
        recentCasesWindow.setSize(750, 400);

        int w = recentCasesWindow.getSize().width;
        int h = recentCasesWindow.getSize().height;

        // set the location of the popUp Window on the center of the screen
        recentCasesWindow.setLocation((screenDimension.width - w)/2, (screenDimension.height - h)/2);

        OpenRecentCasePanel welcomeWindow = OpenRecentCasePanel.getInstance();

        // add the command to close the window to the button on the Volume Detail Panel
        welcomeWindow.setCloseButtonActionListener( new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                recentCasesWindow.dispose();
            }
        });

        recentCasesWindow.add(welcomeWindow);
        recentCasesWindow.pack();
        recentCasesWindow.setResizable(false);
        recentCasesWindow.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        recentCasesWindow.setVisible(true);
    }//GEN-LAST:event_openRecentButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel autopsyLogo;
    private javax.swing.JButton closeButton;
    private javax.swing.JLabel createNewLabel;
    private javax.swing.JPanel editorPanel;
    private javax.swing.JButton newCaseButton;
    private javax.swing.JButton openCaseButton;
    private javax.swing.JLabel openLabel;
    private javax.swing.JButton openRecentButton;
    private javax.swing.JLabel openRecentLabel;
    // End of variables declaration//GEN-END:variables

    /**
     * Sets the Close button action listener.
     *
     * @param e  the action listener
     */
    public void setCloseButtonActionListener(ActionListener e){
        closeButton.addActionListener(e);
    }

    /**
     * Sets the Close button label (default is "Close").
     *
     * @param text The new label for the button.
     */
    public void setCloseButtonText(String text) {
        closeButton.setText(text);
    }       
    
    /**
     * Close the open recent cases window.
     */
    public static void closeOpenRecentCasesWindow(){
        //startupWindow.setVisible(false);
        recentCasesWindow.dispose();
    }
}
