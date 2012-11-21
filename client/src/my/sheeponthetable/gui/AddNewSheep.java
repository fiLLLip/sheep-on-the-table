package my.sheeponthetable.gui;

import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import my.sheeponthetable.tools.*;

/**
 * Panel to handle adding new sheep
 *
 * @author Gruppe 7
 */
public class AddNewSheep extends javax.swing.JFrame {

    SheepPanel sheepPanel;

    /**
     * Constructs the new form
     *
     * @param sp
     */
    public AddNewSheep(SheepPanel sp) {
        this.setLocationRelativeTo(null);
        initComponents();
        this.sheepPanel = sp;
        clear();
    }

    /**
     * Autogenerated code.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel7 = new javax.swing.JLabel();
        jToggleButton3 = new javax.swing.JToggleButton();
        jLabel1 = new javax.swing.JLabel();
        txtNick = new javax.swing.JTextField();
        txtWeight = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        cancel = new javax.swing.JToggleButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtComment = new javax.swing.JTextArea();
        clearbtn = new javax.swing.JButton();
        addSheepButton = new javax.swing.JButton();
        dcSheepAddBorn = new com.toedter.calendar.JDateChooser();

        jLabel7.setText("jLabel7");

        jToggleButton3.setText("jToggleButton3");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Register new Sheep");
        setResizable(false);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setText("Add new sheep");

        jLabel3.setText("Born ddmmyyyy:");

        jLabel4.setText("Nickname:");

        jLabel5.setText("Weight:");

        jLabel6.setText("Comment:");

        cancel.setText("Cancel");
        cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelActionPerformed(evt);
            }
        });

        txtComment.setColumns(20);
        txtComment.setLineWrap(true);
        txtComment.setRows(5);
        jScrollPane1.setViewportView(txtComment);
        txtComment.getAccessibleContext().setAccessibleParent(txtNick);

        clearbtn.setText("Clear");
        clearbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearbtnActionPerformed(evt);
            }
        });

        addSheepButton.setText("Add Sheep");
        addSheepButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addSheepButtonActionPerformed(evt);
            }
        });

        dcSheepAddBorn.setDateFormatString("dd.MM.yyyy");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(addSheepButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 146, Short.MAX_VALUE)
                        .addComponent(clearbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 381, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(txtNick, javax.swing.GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE)
                            .addComponent(jLabel6))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtWeight, javax.swing.GroupLayout.DEFAULT_SIZE, 103, Short.MAX_VALUE)
                            .addComponent(jLabel5))
                        .addGap(24, 24, 24)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE))
                            .addComponent(dcSheepAddBorn, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 227, Short.MAX_VALUE)))
                .addGap(45, 45, 45))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(59, 59, 59)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtNick, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtWeight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(dcSheepAddBorn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancel)
                    .addComponent(clearbtn)
                    .addComponent(addSheepButton))
                .addContainerGap(28, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Called when the cancel button is pressed.
     */
    private void cancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelActionPerformed
        this.dispose();
    }//GEN-LAST:event_cancelActionPerformed

    /**
     * Called when the clear button is pressed.
     */
    private void clearbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearbtnActionPerformed
        clear();
    }//GEN-LAST:event_clearbtnActionPerformed

    /**
     * Resets all the fields
     */
    private void clear() {
        dcSheepAddBorn.setDate(new Date());
        txtNick.setText("");
        txtWeight.setText("");
        txtComment.setText("");
    }

    /**
     *
     */
    private void addSheepButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addSheepButtonActionPerformed

        Double weight;

        final ImageIcon icon = new ImageIcon(getClass().getResource("/my/sheeponthetable/gui/resources/sadSheep.jpg"));

        // Make sure all the fields are filled
        if (txtNick.getText().equals("")
                || txtWeight.getText().equals("")
                || dcSheepAddBorn.getDate().before(new Date(84600000))) {

            // If some fields where not filled, ask the user to fill them
            JOptionPane.showMessageDialog(null, "Please fill in all the required fields.", "Information", JOptionPane.INFORMATION_MESSAGE);

            return;
        }

        // try to parse double
        try {
            weight = Double.parseDouble(txtWeight.getText().replace(",", "."));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "The weight has to be a number.", "Information", JOptionPane.INFORMATION_MESSAGE);

            return;
        }

        // If all fields are filled correctly, add the sheep
        Sheep newSheep = new Sheep(-1, -1, txtNick.getText(), dcSheepAddBorn.getDate().getTime(), 84600000, txtComment.getText(), null, weight);
        if (WebServiceClient.newSheep(newSheep)) {
            this.setVisible(false);
            sheepPanel.update();
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(null, "Sheep creation failed. Make sure you have the neccessary access level. \nIf the problem persists, please contact SysAdmin.", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_addSheepButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addSheepButton;
    private javax.swing.JToggleButton cancel;
    private javax.swing.JButton clearbtn;
    private com.toedter.calendar.JDateChooser dcSheepAddBorn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToggleButton jToggleButton3;
    private javax.swing.JTextArea txtComment;
    private javax.swing.JTextField txtNick;
    private javax.swing.JTextField txtWeight;
    // End of variables declaration//GEN-END:variables
}
