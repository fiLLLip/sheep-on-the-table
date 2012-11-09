package my.sheeponthetable.gui;

import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.Map;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import my.sheeponthetable.tools.WebServiceClient;

/**
 * Dialogue box to let the user pick which farm (s)he belongs to.
 * 
 * @author Håkon
 */
public class ChooseFarm extends javax.swing.JFrame {

    private DefaultListModel farmListModel;
    
    /**
     * Creates new form ChooseFarm which lists all available Farms that the user
     * who is logged in has access to.
     */
    public ChooseFarm() {
        
        initComponents();
        this.setLocationRelativeTo(null);
        
        farmListModel = new DefaultListModel();
        for (Map farmName : WebServiceClient.getFarmIds()) {
            farmListModel.addElement(farmName.get("id") + " - " + farmName.get("name"));
        }
        farmList.setModel(farmListModel);

        farmList.setSelectedIndex(0);
    }

    /**
     * Autogenerated Code
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane = new javax.swing.JScrollPane();
        farmList = new javax.swing.JList();
        buttonSelect = new javax.swing.JButton();
        buttonLogout = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setAlwaysOnTop(true);

        farmList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        farmList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                farmListMouseClicked(evt);
            }
        });
        farmList.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ChooseFarm.this.keyPressed(evt);
            }
        });
        jScrollPane.setViewportView(farmList);

        buttonSelect.setText("Log on to selected farm");
        buttonSelect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSelectActionPerformed(evt);
            }
        });

        buttonLogout.setText("Log out");
        buttonLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonLogoutActionPerformed(evt);
            }
        });

        jLabel1.setText("Please select the farm you would like to log on to:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(buttonSelect, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(buttonLogout))
                            .addComponent(jScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 419, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4))
                    .addComponent(jLabel1)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(137, 137, 137)
                        .addComponent(jLabel4))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(buttonLogout)
                            .addComponent(buttonSelect))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Called if the user presses the logout-button. Go back to password screen.
     */
    private void buttonLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonLogoutActionPerformed
        dispose();
        new PasswordScreen().setVisible(true);
    }//GEN-LAST:event_buttonLogoutActionPerformed

    /**
     * Called if the user presses the select-button.
     */
    private void buttonSelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSelectActionPerformed
        selectFarm();
    }//GEN-LAST:event_buttonSelectActionPerformed

    /**
     * Select the farm that has been clicked if the user clicks a farm.
     */
    private void farmListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_farmListMouseClicked
        JList list = (JList) evt.getSource();
        Rectangle r = list.getCellBounds(0, list.getLastVisibleIndex());
        if (r != null && r.contains(evt.getPoint())) {
            // A double-click automatically selects the farm
            if (evt.getClickCount() == 2 || evt.getClickCount() == 3) {
                selectFarm();
            }
        }
    }//GEN-LAST:event_farmListMouseClicked

    /**
     * Selects the selected farm if the ENTER key is pressed. Fires everytime a
     * key is pressed in the farmlist.
     */
    private void keyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_keyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            selectFarm();
        }
    }//GEN-LAST:event_keyPressed

    /**
     * Opens sheepPanel with the selected farm
     */
    private void selectFarm() {
        if (farmList.getSelectedIndex() != -1) {
            String farmId = WebServiceClient.getFarmIds().get(farmList.getSelectedIndex()).get("id").toString();
            WebServiceClient.setFarmId(farmId);
            dispose();
            new SheepPanel().setVisible(true);
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonLogout;
    private javax.swing.JButton buttonSelect;
    private javax.swing.JList farmList;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane;
    // End of variables declaration//GEN-END:variables
}
