/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my.sheeponthetable.gui;

import java.awt.Rectangle;
import java.util.Map;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import my.sheeponthetable.tools.WebServiceClient;

/**
 *
 * @author Håkon
 */
public class ChooseFarm extends javax.swing.JFrame {

    private DefaultListModel farmListModel = new DefaultListModel();

    /**
     * Creates new form ChooseFarm which lists all available Farms that the user
     * who is logged in has access to.
     */
    public ChooseFarm() {
        initComponents();
        this.setLocationRelativeTo(null);
        farmListModel.clear();
        for (Map farmName : WebServiceClient.farmids) {
            farmListModel.addElement(farmName.get("id") + " - " + farmName.get("name"));
        }
        farmList.setModel(farmListModel);

        if (farmListModel.size() == 1) {
            farmList.setSelectedIndex(0);
            selectFarm();
        } else if (farmListModel.size() > 0) {
            farmList.setSelectedIndex(0);
        } else {
            new ErrorBox("You have no farms :/").setVisible(true);
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

        jScrollPane = new javax.swing.JScrollPane();
        farmList = new javax.swing.JList();
        buttonSelect = new javax.swing.JButton();
        buttonLogout = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

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

    private void buttonLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonLogoutActionPerformed
        // TODO add your handling code here:
        new PasswordScreen().setVisible(true);
        dispose();
    }//GEN-LAST:event_buttonLogoutActionPerformed

    private void buttonSelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSelectActionPerformed

        selectFarm();

    }//GEN-LAST:event_buttonSelectActionPerformed

    private void farmListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_farmListMouseClicked
        // TODO add your handling code here:
        JList list = (JList) evt.getSource();
        Rectangle r = list.getCellBounds(0, list.getLastVisibleIndex());
        if (r != null && r.contains(evt.getPoint())) {
            if (evt.getClickCount() == 2 || evt.getClickCount() == 3) {
                selectFarm();
            }
        }
    }//GEN-LAST:event_farmListMouseClicked

    /**
     * opens sheepPanel with the selected farm
     */
    private void selectFarm() {
        if (farmList.getSelectedIndex() != -1) {
            WebServiceClient.farmid = WebServiceClient.farmids.get(farmList.getSelectedIndex()).get("id").toString();
            new SheepPanel().setVisible(true);
            dispose();
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
