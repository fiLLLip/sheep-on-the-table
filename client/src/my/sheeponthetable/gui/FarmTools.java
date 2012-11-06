/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my.sheeponthetable.gui;

//import com.sun.xml.internal.bind.v2.schemagen.xmlschema.Import;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import my.sheeponthetable.tools.User;
import my.sheeponthetable.tools.WebServiceClient;
import sun.awt.resources.awt;
import java.awt.event.*;
/**
 *
 * @author Alex
 */
public class FarmTools extends javax.swing.JFrame {
   private SheepPanel sheepPanel;
   private List<User> users = new ArrayList();
   private DefaultListModel listModel = new DefaultListModel();
   private int farmID;
   private ListSelectionListener userSelect;
   

    /**
     * Creates new form AddNewSheep
     */
    public FarmTools(SheepPanel sp, int id) {
        initComponents();
        this.farmID = id;
        this.sheepPanel = sp;
        ClearanceChoice.add("Admin");
        ClearanceChoice.add("owner");
        ClearanceChoice.add("viewer/restricted");
        ClearanceChoice.add("viewer only");
        ClearanceChoice.add("-Not specified-");
        ClearanceChoice.select(4);
        setDisable();
        getUsers();
        update();
        System.out.println("Major Fail");
        System.out.println(jListUser.getSelectedValue());
        
        userSelect = new ListSelectionListener(){

            public void valueChanged(ListSelectionEvent lse) {
                 System.out.println("Major Fail");

                JList list = (JList) lse.getSource();
                int selectedIndex = list.getSelectedIndex();
                if (!lse.getValueIsAdjusting() && selectedIndex != -1) {
                    System.out.println("Major Fail");
                    selectedUser(selectedIndex);
                }}};
        jListUser.addListSelectionListener(userSelect);
    }
    
    private void setDisable() {
      ClearanceChoice.setEnabled(false);
      jAlertChkKilled1.setEnabled(false);
      jAlertChkKilled2.setEnabled(false);
      jAlertChkStationary1.setEnabled(false);
      jAlertChkStationary2.setEnabled(false);
      jAlertChkTemp1.setEnabled(false);
      jAlertChkTemp2.setEnabled(false);
      btnSave.setEnabled(false);
      
    }
    
    private void setEnable() {
      ClearanceChoice.setEnabled(true);
      jAlertChkKilled1.setEnabled(true);
      jAlertChkKilled2.setEnabled(true);
      jAlertChkStationary1.setEnabled(true);
      jAlertChkStationary2.setEnabled(true);
      jAlertChkTemp1.setEnabled(true);
      jAlertChkTemp2.setEnabled(true);
      btnSave.setEnabled(true);
      
    }
    
    public void getUsers(){
        
     if(WebServiceClient.getUsersForFarm(farmID) != null){  
     users = WebServiceClient.getUsersForFarm(farmID);}
      
      else{listModel.addElement("Troll");
            listModel.addElement("sold");
            listModel.addElement("old");
            listModel.addElement("cold");
      }
    }
    public void update(){
        System.out.println(users.size());
    for(int i= 0; i< users.size();i++)
    {listModel.addElement(users.get(i).getName());
    }
        }
    public void selectedUser(int selectedIndex){
    setEnable();
        
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
        btnCancel = new javax.swing.JButton();
        lblFarmName = new javax.swing.JLabel();
        lblUserName = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jAlertChkKilled1 = new javax.swing.JCheckBox();
        jAlertChkKilled2 = new javax.swing.JCheckBox();
        jAlertChkTemp1 = new javax.swing.JCheckBox();
        jAlertChkTemp2 = new javax.swing.JCheckBox();
        jAlertChkStationary2 = new javax.swing.JCheckBox();
        jAlertChkStationary1 = new javax.swing.JCheckBox();
        jLabel7 = new javax.swing.JLabel();
        lblClerance = new javax.swing.JLabel();
        ClearanceChoice = new java.awt.Choice();
        lblName = new javax.swing.JLabel();
        lblNameTxt = new javax.swing.JLabel();
        lblFarmNameTxt = new javax.swing.JLabel();

        jCheckBox9.setText("jCheckBox9");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jListUser.setModel(listModel);
        jScrollPane1.setViewportView(jListUser);

        btnSave.setText("Save ");

        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        lblFarmName.setText("Farm name:");

        lblUserName.setText("Username:");

        jLabel6.setText("Alert by E-mail:");

        jAlertChkKilled1.setText("Sheep killed");

        jAlertChkKilled2.setText("Sheep killed");

        jAlertChkTemp1.setText("Temp changed");

        jAlertChkTemp2.setText("Temp changed");

        jAlertChkStationary2.setText("Stationary");

        jAlertChkStationary1.setText("Stationary");

        jLabel7.setText("Alert by phone:");

        lblClerance.setText("Clearance:");

        ClearanceChoice.setName(""); // NOI18N

        lblName.setText("Name:");

        lblNameTxt.setText("            ");

        lblFarmNameTxt.setText("               ");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblUserName))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jAlertChkKilled1)
                            .addComponent(jAlertChkTemp1)
                            .addComponent(jAlertChkStationary1)
                            .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jAlertChkKilled2)
                            .addComponent(jAlertChkStationary2)
                            .addComponent(jAlertChkTemp2)
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
                                    .addComponent(lblNameTxt)
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
                    .addComponent(lblUserName)
                    .addComponent(lblFarmNameTxt))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblName)
                            .addComponent(lblNameTxt))
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
                            .addComponent(jAlertChkKilled1)
                            .addComponent(jAlertChkKilled2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jAlertChkTemp1)
                            .addComponent(jAlertChkTemp2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jAlertChkStationary2)
                            .addComponent(jAlertChkStationary1))
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

    /**
     * @param args the command line arguments
     */
  
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private java.awt.Choice ClearanceChoice;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnSave;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JCheckBox jAlertChkKilled1;
    private javax.swing.JCheckBox jAlertChkKilled2;
    private javax.swing.JCheckBox jAlertChkStationary1;
    private javax.swing.JCheckBox jAlertChkStationary2;
    private javax.swing.JCheckBox jAlertChkTemp1;
    private javax.swing.JCheckBox jAlertChkTemp2;
    private javax.swing.JCheckBox jCheckBox9;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JList jListUser;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblClerance;
    private javax.swing.JLabel lblFarmName;
    private javax.swing.JLabel lblFarmNameTxt;
    private javax.swing.JLabel lblName;
    private javax.swing.JLabel lblNameTxt;
    private javax.swing.JLabel lblUserName;
    // End of variables declaration//GEN-END:variables

    
}
