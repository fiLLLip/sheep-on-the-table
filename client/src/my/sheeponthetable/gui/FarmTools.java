package my.sheeponthetable.gui;

import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import my.sheeponthetable.tools.User;
import my.sheeponthetable.tools.WebServiceClient;

/**
 * A form used to give and edit user permissions for the given farm.
 * 
 * @author Gruppe 7
 */
public class FarmTools extends javax.swing.JFrame {

    private List<User> users;
    private DefaultListModel listModel = new DefaultListModel();
    private int farmID = Integer.parseInt(WebServiceClient.getFarmId());
    private User selectedUser;
    private boolean hasDoneChangesToSelectedUser = false; // used to determine if we should alert the user of unsaved changes
    private boolean dontShowConfirm = false; // so the popup doesnt come twice

    /**
     * Creates new form AddNewSheep
     *
     */
    public FarmTools() {
        initComponents();

        this.setTitle("Preferences for " + WebServiceClient.getFarmName());

        ClearanceChoice.add("Owner");
        ClearanceChoice.add("Administrator");
        ClearanceChoice.add("View Access only");
        setDisable();
        refreshUserList();

        this.setLocationRelativeTo(null);

    }

    /**
     * Disables input from the user
     */
    private void setDisable() {
        ClearanceChoice.setEnabled(false);
        cbxEmailAlertAttack.setEnabled(false);
        cbxSMSAlertAttack.setEnabled(false);
        cbxEmailAlertStationary.setEnabled(false);
        cbxSMSAlertStationary.setEnabled(false);
        cbxEmailAlertHealth.setEnabled(false);
        cbxSMSAlertHealth.setEnabled(false);
        btnSave.setEnabled(false);

    }

    /**
     * Enables input from user
     */
    private void setEnable() {
        cbxEmailAlertAttack.setEnabled(true);
        cbxSMSAlertAttack.setEnabled(true);
        cbxEmailAlertStationary.setEnabled(true);
        cbxSMSAlertStationary.setEnabled(true);
        cbxEmailAlertHealth.setEnabled(true);
        cbxSMSAlertHealth.setEnabled(true);
        btnSave.setEnabled(true);

    }

    /**
     * Sets the users variable and populates the list
     */
    private void refreshUserList() {

        users = WebServiceClient.getUsersForFarm(farmID);
        listModel.clear();

        if (users == null) {
            listModel.addElement("No users found.");
        } else {
            for (User user : users) {
                listModel.addElement(user.getUsername());
            }

            if (WebServiceClient.getUserDetails().getClearance(farmID) >= 2) {
                addUserToFarmButton.setEnabled(true);
                removeUserFromFarmButton.setEnabled(true);
            } else {
                addUserToFarmButton.setEnabled(false);
                removeUserFromFarmButton.setEnabled(false);
            }
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jCheckBox9 = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        jListUser = new javax.swing.JList();
        btnSave = new javax.swing.JButton();
        lblFarmName = new javax.swing.JLabel();
        lblUserNameListLabel = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        cbxEmailAlertAttack = new javax.swing.JCheckBox();
        cbxSMSAlertAttack = new javax.swing.JCheckBox();
        cbxEmailAlertHealth = new javax.swing.JCheckBox();
        cbxSMSAlertHealth = new javax.swing.JCheckBox();
        cbxSMSAlertStationary = new javax.swing.JCheckBox();
        cbxEmailAlertStationary = new javax.swing.JCheckBox();
        jLabel7 = new javax.swing.JLabel();
        lblClerance = new javax.swing.JLabel();
        ClearanceChoice = new java.awt.Choice();
        lblName = new javax.swing.JLabel();
        lblSelectedUserName = new javax.swing.JLabel();
        lblFarmNameTxt = new javax.swing.JLabel();
        addUserToFarmButton = new javax.swing.JButton();
        removeUserFromFarmButton = new javax.swing.JButton();

        jCheckBox9.setText("jCheckBox9");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jListUser.setModel(listModel);
        jListUser.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                FarmTools.this.valueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jListUser);

        btnSave.setText("Save ");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        lblFarmName.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblFarmName.setText("Farm name:");

        lblUserNameListLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblUserNameListLabel.setText("Username:");

        jLabel6.setText("Alert by E-mail:");

        cbxEmailAlertAttack.setText("Sheep killed");
        cbxEmailAlertAttack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                userSettingsChanged(evt);
            }
        });

        cbxSMSAlertAttack.setText("Sheep killed");
        cbxSMSAlertAttack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                userSettingsChanged(evt);
            }
        });

        cbxEmailAlertHealth.setText("Health");
        cbxEmailAlertHealth.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                userSettingsChanged(evt);
            }
        });

        cbxSMSAlertHealth.setText("Health");
        cbxSMSAlertHealth.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                userSettingsChanged(evt);
            }
        });

        cbxSMSAlertStationary.setText("Stationary");
        cbxSMSAlertStationary.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                userSettingsChanged(evt);
            }
        });

        cbxEmailAlertStationary.setText("Stationary");
        cbxEmailAlertStationary.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                userSettingsChanged(evt);
            }
        });

        jLabel7.setText("Alert by phone:");

        lblClerance.setText("Clearance:");

        ClearanceChoice.setName(""); // NOI18N
        ClearanceChoice.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                levelchanged(evt);
            }
        });

        lblName.setText("Name:");

        lblSelectedUserName.setText("            ");

        lblFarmNameTxt.setText("               ");

        addUserToFarmButton.setText("+");
        addUserToFarmButton.setEnabled(false);
        addUserToFarmButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addUserToFarmButtonActionPerformed(evt);
            }
        });

        removeUserFromFarmButton.setText("-");
        removeUserFromFarmButton.setEnabled(false);
        removeUserFromFarmButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeUserFromFarmButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblUserNameListLabel)))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(addUserToFarmButton, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(removeUserFromFarmButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(cbxEmailAlertAttack)
                            .addComponent(cbxEmailAlertHealth)
                            .addComponent(cbxEmailAlertStationary))
                        .addGap(18, 21, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(cbxSMSAlertAttack)
                            .addComponent(cbxSMSAlertStationary)
                            .addComponent(cbxSMSAlertHealth)
                            .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblClerance)
                            .addComponent(lblFarmName)
                            .addComponent(lblName))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ClearanceChoice, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblFarmNameTxt)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(lblSelectedUserName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblFarmName)
                    .addComponent(lblUserNameListLabel)
                    .addComponent(lblFarmNameTxt))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblName)
                            .addComponent(lblSelectedUserName))
                        .addGap(39, 39, 39)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblClerance)
                            .addComponent(ClearanceChoice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(23, 23, 23)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cbxEmailAlertAttack)
                            .addComponent(cbxSMSAlertAttack))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cbxEmailAlertHealth)
                            .addComponent(cbxSMSAlertHealth))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cbxSMSAlertStationary)
                            .addComponent(cbxEmailAlertStationary)))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSave)
                    .addComponent(addUserToFarmButton)
                    .addComponent(removeUserFromFarmButton))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Fires everytime a user clicks on a user in the list
     *
     * @param evt
     */
    private void valueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_valueChanged
        if (this.hasDoneChangesToSelectedUser && this.dontShowConfirm) {
            // make sure that the confirm button doesnt show up again if we press no or cancel
            this.dontShowConfirm = true;

            // show confirmation popup
            int confirm = JOptionPane.showConfirmDialog(null, "You have unsaved changes, are you sure you want to discard them?");

            // if user chooses no/cancel
            if (confirm != 0) {
                jListUser.setSelectedIndex(evt.getLastIndex());
                return;
            } else {
                // the user has discareded the changes
                this.hasDoneChangesToSelectedUser = false;
            }

        } else {
            this.dontShowConfirm = false;
        }

        if (jListUser.getSelectedIndex() != -1) {

            this.selectedUser = users.get(jListUser.getSelectedIndex());

            // disable the level setter
            ClearanceChoice.setEnabled(false);

            // If user is a farm owner
            if (WebServiceClient.getUserLevel(farmID, Integer.parseInt(WebServiceClient.getUserID())) >= 2) {
                ClearanceChoice.setEnabled(true); // owners can set levels
            }

            // enable input components
            setEnable();

            // set info
            lblSelectedUserName.setText(selectedUser.getName());
            ClearanceChoice.select(getClearenceIndex(this.selectedUser.getClearance(this.farmID)));

            cbxEmailAlertAttack.setSelected(selectedUser.getEmailAlarmAttack());
            cbxEmailAlertHealth.setSelected(selectedUser.getEmailAlarmHealth());
            cbxEmailAlertStationary.setSelected(selectedUser.getEmailAlarmStationary());

            cbxSMSAlertAttack.setSelected(selectedUser.getSMSAlarmAttack());
            cbxSMSAlertHealth.setSelected(selectedUser.getSMSAlarmHealth());
            cbxSMSAlertStationary.setSelected(selectedUser.getSMSAlarmStationary());

        }
    }//GEN-LAST:event_valueChanged

    /**
     * Fires every time there is a change to one of the checkboxes or clearence
     * level
     *
     * @param evt
     */
    private void userSettingsChanged(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_userSettingsChanged
        this.hasDoneChangesToSelectedUser = true;

        selectedUser.setEmailAlarmAttack(cbxEmailAlertAttack.isSelected());
        selectedUser.setEmailAlarmHealth(cbxEmailAlertHealth.isSelected());
        selectedUser.setEmailAlarmStationary(cbxEmailAlertStationary.isSelected());

        selectedUser.setSMSAlarmAttack(cbxSMSAlertAttack.isSelected());
        selectedUser.setSMSAlarmHealth(cbxSMSAlertHealth.isSelected());
        selectedUser.setSMSAlarmStationary(cbxSMSAlertStationary.isSelected());

    }//GEN-LAST:event_userSettingsChanged

    /**
     * Triggered when the Save-button is clicked. Sends the selectedUser object
     * to the WebServiceClient and saves the changes.
     *
     * @param evt
     */
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        if (this.hasDoneChangesToSelectedUser) {
            if (WebServiceClient.setUserOptions(selectedUser)) {
                // if logged in user is a owner, set clearence level
                if (WebServiceClient.getUserDetails().getClearance(farmID) == 2) {
                    if (!WebServiceClient.setUserPermission(selectedUser.getUserId(), getClearenceLevelFromIndex(ClearanceChoice.getSelectedIndex()))) {
                        JOptionPane.showMessageDialog(null, "The server returned an error while trying to change the user level.");
                    }
                }
                JOptionPane.showMessageDialog(null, "The changes to user " + selectedUser.getUsername() + " were saved successfully.");
                this.hasDoneChangesToSelectedUser = false;
                this.refreshUserList();
            } else {
                JOptionPane.showMessageDialog(null, "Failed to save settings.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "The changes to user " + selectedUser.getUsername() + " were saved successfully.");
        }
    }//GEN-LAST:event_btnSaveActionPerformed

    private void addUserToFarmButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addUserToFarmButtonActionPerformed

        String username = JOptionPane.showInputDialog(this, "Please enter the username of the user you want to grant access to this farm:", "Grant access to user", JOptionPane.QUESTION_MESSAGE);
        if (!WebServiceClient.addNewUserToFarm(username)) {
            JOptionPane.showMessageDialog(this, "The user could not be added, is the username correct?", "Beeeeh!", JOptionPane.ERROR_MESSAGE, null);
        } else {

            this.refreshUserList();
        }
    }//GEN-LAST:event_addUserToFarmButtonActionPerformed

    private void removeUserFromFarmButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeUserFromFarmButtonActionPerformed
        if (selectedUser != null) {
            if (JOptionPane.showConfirmDialog(this, "Are you sure you want to remove the access for selected user?", "Are you sure?", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                if (!WebServiceClient.removeUserFromFarm(selectedUser)) {
                    JOptionPane.showMessageDialog(this, "The user could not be removed, do you have the right permissions?", "Beeeeh!", JOptionPane.ERROR_MESSAGE, null);
                } else {
                    this.refreshUserList();
                }
            }
        }

    }//GEN-LAST:event_removeUserFromFarmButtonActionPerformed

    private void levelchanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_levelchanged
        this.hasDoneChangesToSelectedUser = true;
    }//GEN-LAST:event_levelchanged
    /**
     * @param args the command line arguments
     */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private java.awt.Choice ClearanceChoice;
    private javax.swing.JButton addUserToFarmButton;
    private javax.swing.JButton btnSave;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JCheckBox cbxEmailAlertAttack;
    private javax.swing.JCheckBox cbxEmailAlertHealth;
    private javax.swing.JCheckBox cbxEmailAlertStationary;
    private javax.swing.JCheckBox cbxSMSAlertAttack;
    private javax.swing.JCheckBox cbxSMSAlertHealth;
    private javax.swing.JCheckBox cbxSMSAlertStationary;
    private javax.swing.JCheckBox jCheckBox9;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JList jListUser;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblClerance;
    private javax.swing.JLabel lblFarmName;
    private javax.swing.JLabel lblFarmNameTxt;
    private javax.swing.JLabel lblName;
    private javax.swing.JLabel lblSelectedUserName;
    private javax.swing.JLabel lblUserNameListLabel;
    private javax.swing.JButton removeUserFromFarmButton;
    // End of variables declaration//GEN-END:variables
    /*
     * Helper method to set the right clearence level (selection box)
     */

    private int getClearenceIndex(int level) {
        switch (level) {
            case 2:
                return 0;
            case 1:
                return 1;
            default:
            case 0:
                return 2;
        }
    }

    private int getClearenceLevelFromIndex(int selectedIndex) {
        switch(selectedIndex) {
            case 0:
                return 2;
            case 1:
                return 1;
            case 2:
            default:
                return 0;

        }
    }
}
