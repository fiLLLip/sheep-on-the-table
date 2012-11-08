/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my.sheeponthetable.gui;

import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import my.sheeponthetable.tools.User;
import my.sheeponthetable.tools.WebServiceClient;

/**
 *
 * @author Alex
 */
public class FarmTools extends javax.swing.JFrame {

    private SheepPanel sheepPanel;
    private List<User> users = new ArrayList();
    private DefaultListModel listModel = new DefaultListModel();
<<<<<<< HEAD
    private int farmID,selectedIndex;
    private ListSelectionListener userSelect;
    private User current;
=======
    private int farmID = Integer.parseInt(WebServiceClient.farmid);
    private ListSelectionListener userSelect;
    private User selectedUser;
    private boolean hasDoneChangesToSelectedUser = false; // used to determine if we should alert the user of unsaved changes
>>>>>>> 01e0ab037d82b8790cec6788c9eb5e21e6b4a376

    /**
     * Creates new form AddNewSheep
     */
    public FarmTools(SheepPanel sp, int id) {
        initComponents();

        this.setTitle("Preferences for " + WebServiceClient.getFarmName());
        
        ClearanceChoice.add("Owner");
        ClearanceChoice.add("Administrator");
        ClearanceChoice.add("View Access only");
        ClearanceChoice.select(2);
        setDisable();
        getUsers();
<<<<<<< HEAD
        update();
        

        userSelect = new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent lse) {
               
                JList list = (JList) lse.getSource();
                selectedIndex = list.getSelectedIndex();
                if (!lse.getValueIsAdjusting() && selectedIndex != -1) {
                   
                    selectedUser();
                }
            }
        };
        jListUser.addListSelectionListener(userSelect);
=======
        refreshUserList();

>>>>>>> 01e0ab037d82b8790cec6788c9eb5e21e6b4a376
    }

    /**
     * Disables input from the user
     */
    private void setDisable() {
        ClearanceChoice.setEnabled(false);
        cbxAlertEmailAttack.setEnabled(false);
        cbxAlertSMSAttack.setEnabled(false);
        cbxAlertEmailStationary.setEnabled(false);
        cbxAlertSMSStationary.setEnabled(false);
        cbxAlertEmailHealth.setEnabled(false);
        cbxAlertSMSHealth.setEnabled(false);
        btnSave.setEnabled(false);

    }

    /**
     * Enables input from user
     */
    private void setEnable() {
        ClearanceChoice.setEnabled(true);
        cbxAlertEmailAttack.setEnabled(true);
        cbxAlertSMSAttack.setEnabled(true);
        cbxAlertEmailStationary.setEnabled(true);
        cbxAlertSMSStationary.setEnabled(true);
        cbxAlertEmailHealth.setEnabled(true);
        cbxAlertSMSHealth.setEnabled(true);
        btnSave.setEnabled(true);

    }

    /**
     * Sets the users variable with the user array from the WebService
     */
    private void getUsers() {

        if (WebServiceClient.getUsersForFarm(farmID) != null) {
<<<<<<< HEAD
            users = WebServiceClient.getUsersForFarm(farmID);}
=======
            users = WebServiceClient.getUsersForFarm(farmID);
        } else {
            listModel.addElement("No users found.");
        }
>>>>>>> 01e0ab037d82b8790cec6788c9eb5e21e6b4a376
    }

    /**
     * Populates the userlist
     */
    private void refreshUserList() {
        for (int i = 0; i < users.size(); i++) {
            listModel.addElement(users.get(i).getName());
        }
    }

<<<<<<< HEAD
    public void selectedUser() {
        setEnable();
        lblNameTxt.setText(users.get(selectedIndex).getName());
        
        if(users.get(selectedIndex).getEmailAlarmAttack()){jAlertChkKilled1.setSelected(true);}
           if(users.get(selectedIndex).getEmailAlarmTemperature()){jAlertChkTemp1.setSelected(true);}
              if(users.get(selectedIndex).getEmailAlarmStationary()){jAlertChkStationary1.setSelected(true);}
              
              if(users.get(selectedIndex).getSMSAlarmAttack()){jAlertChkKilled2.setSelected(true);}
           if(users.get(selectedIndex).getSMSAlarmTemperature()){jAlertChkTemp2.setSelected(true);}
        if(users.get(selectedIndex).getSMSAlarmStationary()){jAlertChkStationary2.setSelected(true);}
    }
    /**
     * Returns the user preferences
     *
     * 
     * @return User with new preferences
     */
    public void savingUserPreferences(){
        if(jAlertChkKilled1.isSelected()){users.get(selectedIndex).setEmailAlarmAttack(true);}
            else{users.get(selectedIndex).setEmailAlarmAttack(false);}
                if(jAlertChkTemp1.isSelected()){users.get(selectedIndex).setEmailAlarmTemperature(true);}
                    else{users.get(selectedIndex).setEmailAlarmTemperature(false);}
                        if(jAlertChkStationary1.isSelected()){users.get(selectedIndex).setEmailAlarmTemperature(true);}
                            else{users.get(selectedIndex).setEmailAlarmTemperature(false);}
        
                            if(jAlertChkKilled2.isSelected()){users.get(selectedIndex).setSMSAlarmAttack(true);}
                        else{users.get(selectedIndex).setSMSAlarmAttack(false);}
                    if(jAlertChkTemp2.isSelected()){users.get(selectedIndex).setSMSAlarmTemperature(true);}
                else{users.get(selectedIndex).setSMSAlarmTemperature(false);}
            if(jAlertChkStationary2.isSelected()){users.get(selectedIndex).setSMSAlarmStationary(true);}
        else{users.get(selectedIndex).setSMSAlarmStationary(false);}
    }

=======
>>>>>>> 01e0ab037d82b8790cec6788c9eb5e21e6b4a376
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
        btnCancel = new javax.swing.JButton();
        lblFarmName = new javax.swing.JLabel();
<<<<<<< HEAD
        lblUserName = new javax.swing.JLabel();
        lblAlertEmail = new javax.swing.JLabel();
        jAlertChkKilled1 = new javax.swing.JCheckBox();
        jAlertChkKilled2 = new javax.swing.JCheckBox();
        jAlertChkTemp1 = new javax.swing.JCheckBox();
        jAlertChkTemp2 = new javax.swing.JCheckBox();
        jAlertChkStationary2 = new javax.swing.JCheckBox();
        jAlertChkStationary1 = new javax.swing.JCheckBox();
        lblAlertPhone = new javax.swing.JLabel();
=======
        lblUserNameListLabel = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        cbxAlertEmailAttack = new javax.swing.JCheckBox();
        cbxAlertSMSAttack = new javax.swing.JCheckBox();
        cbxAlertEmailHealth = new javax.swing.JCheckBox();
        cbxAlertSMSHealth = new javax.swing.JCheckBox();
        cbxAlertSMSStationary = new javax.swing.JCheckBox();
        cbxAlertEmailStationary = new javax.swing.JCheckBox();
        jLabel7 = new javax.swing.JLabel();
>>>>>>> 01e0ab037d82b8790cec6788c9eb5e21e6b4a376
        lblClerance = new javax.swing.JLabel();
        ClearanceChoice = new java.awt.Choice();
        lblName = new javax.swing.JLabel();
        lblSelectedUserName = new javax.swing.JLabel();
        lblFarmNameTxt = new javax.swing.JLabel();

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

        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        lblFarmName.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblFarmName.setText("Farm name:");

        lblUserNameListLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblUserNameListLabel.setText("Username:");

        lblAlertEmail.setText("Alert by E-mail:");

        cbxAlertEmailAttack.setText("Sheep killed");

        cbxAlertSMSAttack.setText("Sheep killed");

        cbxAlertEmailHealth.setText("Temp changed");

        cbxAlertSMSHealth.setText("Temp changed");

        cbxAlertSMSStationary.setText("Stationary");

        cbxAlertEmailStationary.setText("Stationary");

        lblAlertPhone.setText("Alert by phone:");

        lblClerance.setText("Clearance:");

        ClearanceChoice.setName(""); // NOI18N

        lblName.setText("Name:");

<<<<<<< HEAD
        lblNameTxt.setText("            ");
        lblNameTxt.setInheritsPopupMenu(false);
=======
        lblSelectedUserName.setText("            ");
>>>>>>> 01e0ab037d82b8790cec6788c9eb5e21e6b4a376

        lblFarmNameTxt.setText("               ");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblUserNameListLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
<<<<<<< HEAD
                            .addComponent(lblAlertEmail)
                            .addComponent(jAlertChkKilled1)
                            .addComponent(jAlertChkTemp1)
                            .addComponent(jAlertChkStationary1)
                            .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblAlertPhone)
                            .addComponent(jAlertChkKilled2)
                            .addComponent(jAlertChkStationary2)
                            .addComponent(jAlertChkTemp2)
=======
                            .addComponent(jLabel6)
                            .addComponent(cbxAlertEmailAttack)
                            .addComponent(cbxAlertEmailHealth)
                            .addComponent(cbxAlertEmailStationary)
                            .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(cbxAlertSMSAttack)
                            .addComponent(cbxAlertSMSStationary)
                            .addComponent(cbxAlertSMSHealth)
>>>>>>> 01e0ab037d82b8790cec6788c9eb5e21e6b4a376
                            .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblClerance)
                            .addComponent(lblFarmName)
                            .addComponent(lblName))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ClearanceChoice, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblSelectedUserName)
                                    .addComponent(lblFarmNameTxt))
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addGap(22, 22, 22))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblFarmName)
                    .addComponent(lblUserNameListLabel)
                    .addComponent(lblFarmNameTxt))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblName)
                            .addComponent(lblSelectedUserName))
                        .addGap(39, 39, 39)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblClerance)
                            .addComponent(ClearanceChoice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(23, 23, 23)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblAlertEmail)
                            .addComponent(lblAlertPhone))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cbxAlertEmailAttack)
                            .addComponent(cbxAlertSMSAttack))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cbxAlertEmailHealth)
                            .addComponent(cbxAlertSMSHealth))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cbxAlertSMSStationary)
                            .addComponent(cbxAlertEmailStationary))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnSave)
                            .addComponent(btnCancel)))
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        this.dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_btnCancelActionPerformed

<<<<<<< HEAD
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        savingUserPreferences();
        WebServiceClient.setUserDetails(users.get(selectedIndex));    
        this.dispose();// TODO add your handling code here:
    }//GEN-LAST:event_btnSaveActionPerformed
=======
    /**
     * Fires everytime a user clicks on a user in the list
     *
     * @param evt
     */
    private void valueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_valueChanged
        if (jListUser.getSelectedIndex() != -1) {
            this.selectedUser = users.get(jListUser.getSelectedIndex());
            setEnable();
            lblSelectedUserName.setText(selectedUser.getName());
            ClearanceChoice.select(Math.abs(this.selectedUser.getClearance(this.farmID) - 2));

        }
    }//GEN-LAST:event_valueChanged
>>>>>>> 01e0ab037d82b8790cec6788c9eb5e21e6b4a376
    /**
     * @param args the command line arguments
     */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private java.awt.Choice ClearanceChoice;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnSave;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JCheckBox cbxAlertEmailAttack;
    private javax.swing.JCheckBox cbxAlertEmailHealth;
    private javax.swing.JCheckBox cbxAlertEmailStationary;
    private javax.swing.JCheckBox cbxAlertSMSAttack;
    private javax.swing.JCheckBox cbxAlertSMSHealth;
    private javax.swing.JCheckBox cbxAlertSMSStationary;
    private javax.swing.JCheckBox jCheckBox9;
    private javax.swing.JList jListUser;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblAlertEmail;
    private javax.swing.JLabel lblAlertPhone;
    private javax.swing.JLabel lblClerance;
    private javax.swing.JLabel lblFarmName;
    private javax.swing.JLabel lblFarmNameTxt;
    private javax.swing.JLabel lblName;
    private javax.swing.JLabel lblSelectedUserName;
    private javax.swing.JLabel lblUserNameListLabel;
    // End of variables declaration//GEN-END:variables
}
