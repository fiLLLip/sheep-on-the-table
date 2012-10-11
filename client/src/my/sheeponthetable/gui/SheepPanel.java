/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my.sheeponthetable.gui;

import java.awt.Color;
import java.util.*;
import javax.swing.*;
import my.sheeponthetable.tools.*;
import java.awt.event.*;
import java.io.File;
import java.text.DateFormat;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.MouseInputListener;
import org.jdesktop.swingx.OSMTileFactoryInfo;
import org.jdesktop.swingx.input.CenterMapListener;
import org.jdesktop.swingx.input.PanKeyListener;
import org.jdesktop.swingx.input.PanMouseInputListener;
import org.jdesktop.swingx.input.ZoomMouseWheelListenerCursor;
import org.jdesktop.swingx.mapviewer.DefaultTileFactory;
import org.jdesktop.swingx.mapviewer.GeoPosition;
import org.jdesktop.swingx.mapviewer.LocalResponseCache;
import org.jdesktop.swingx.mapviewer.TileFactory;
import org.jdesktop.swingx.mapviewer.TileFactoryInfo;
import org.jdesktop.swingx.mapviewer.wms.WMSService;
import org.jdesktop.swingx.mapviewer.wms.WMSTileFactory;

/**
 *
 * @author Alex
 */
public class SheepPanel extends javax.swing.JFrame {

    private DefaultListModel sheepShow = new DefaultListModel();
    private DefaultListModel sheepUpdatesShow = new DefaultListModel();
    private List<Sheep> sheepList = new ArrayList();
    private String serverURL;
    private int serverPort;
    private String username;
    private String password;
    private int farmID;
    private int userID;
    private ServerConnector connect;
    // mye sheep info
    private String nickname, comment;
    private int globalId, pulse, temp;
    private double posY, posX;

    /* to know what do to when you press the edit or save button */
    private boolean isEditingSheep = false;
    /**
     *
     * @param sheep
     */
    public void addSheepToList(Sheep sheep) {
        sheepShow.addElement(sheep);
    }

    /**
     * Creates new form SheepPanelfail
     *
     * @param connect
     */
    public SheepPanel(ServerConnector connect) {
        this.connect = connect;
        initComponents();
        this.setLocationRelativeTo(null);
        editSheepBtn.setEnabled(false);
        removeSheepBtn.setEnabled(false);
        
        mapInitialize();
        
        ListSelectionListener listSelectionListener = new ListSelectionListener() {
            /* public void mousePressed(MouseEvent e) {// dobbelclick

             //tomme variabler
             if( e.getClickCount() == 2){
             String Nickname ="";        
             String Comment ="";
             int pulse = 0;
             int Temp = 0;

             //sett variabler

             lblNickName.setText(Nickname);
             lblComment.setText(Comment);
             lblPulse.setText(Integer.toString(pulse));
             lblTemp.setText(Integer.toString(Temp));
             }
             }*/
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                // TODO add stuff to do when selected here
                JList list = (JList) listSelectionEvent.getSource();
                if (!listSelectionEvent.getValueIsAdjusting() && list.getSelectedIndex() != -1) {
                    // Settings textfields to "Not available" before update
                    // because there may be no updates for selected Sheep
                    editSheepBtn.setEnabled(true);
                    removeSheepBtn.setEnabled(true);
                    lblIDtxt.setText("Not available");
                    lblPosTxt.setText("Not available");
                    lblUpdateTxt.setText("Not available");
                    lblNickName.setText("Not available");
                    txtComment.setText("Not available");
                    lblPulse.setText("Not available");
                    lblTemp.setText("Not available");
                    int id = sheepList.get(list.getSelectedIndex()).getID();
                    lblIDtxt.setText(Integer.toString(id));
                    lblNickName.setText(sheepList.get(list.getSelectedIndex()).getName());
                    txtComment.setText(sheepList.get(list.getSelectedIndex()).getComment());
                    globalId = id;
                    nickname = sheepList.get(list.getSelectedIndex()).getName();
                    comment = sheepList.get(list.getSelectedIndex()).getComment();
                    /*if (!sheepList.get(list.getSelectedIndex()).isAlive())
                            setBackground(Color.red);
                    if (sheepList.get(list.getSelectedIndex()).getDeceased()== -1)
                            setBackground(Color.blue);
                    if (sheepList.get(list.getSelectedIndex()).isAlive())
                            setBackground(Color.green);*/
                    if (!sheepList.get(list.getSelectedIndex()).getUpdates().isEmpty()) {
                        // Getting update index 0 because 0 is the latest (newest) update
                        double xpos = sheepList.get(list.getSelectedIndex()).getUpdates().get(0).getX();
                        double ypos = sheepList.get(list.getSelectedIndex()).getUpdates().get(0).getY();
                        lblPosTxt.setText(xpos + ", " + ypos);
                        Date formattedTimestamp = new Date((long) sheepList.get(list.getSelectedIndex()).getUpdates().get(0).getTimeStamp() * 1000);
                        lblUpdateTxt.setText(formattedTimestamp.toLocaleString());
                        lblPulse.setText(Integer.toString(sheepList.get(list.getSelectedIndex()).getUpdates().get(0).getPulse()));
                        lblTemp.setText(Double.toString(sheepList.get(list.getSelectedIndex()).getUpdates().get(0).getTemp()));
                        posX = xpos;
                        posY = ypos;
                    }
                }
            }
        };
        sheepJList.addListSelectionListener(listSelectionListener);
        jLabelUser.setText(this.connect.getUsername());
        jLabelFarm.setText(this.connect.getFarmName());
        update();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        addSheep = new javax.swing.JButton();
        removeSheepBtn = new javax.swing.JButton();
        refreshbtn = new javax.swing.JButton();
        deSelect = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        sheepJList = new javax.swing.JList();
        idLbl = new javax.swing.JLabel();
        kordinateLbl = new javax.swing.JLabel();
        idLbl2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabelUser = new javax.swing.JLabel();
        jLabelFarm = new javax.swing.JLabel();
        jLabelLastUpdate = new javax.swing.JLabel();
        sheepUpdatedLabel = new javax.swing.JLabel();
        lblPulse = new javax.swing.JLabel();
        lblTemp = new javax.swing.JLabel();
        lblNickName = new javax.swing.JLabel();
        lblIDtxt = new javax.swing.JLabel();
        lblPosTxt = new javax.swing.JLabel();
        lblUpdateTxt = new javax.swing.JLabel();
        editSheepBtn = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        sheepPulseLabel = new javax.swing.JLabel();
        sheepTemperatureLabel = new javax.swing.JLabel();
        sheepNicknameLabel = new javax.swing.JLabel();
        sheepCommentLabel = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        sheepUpdateJList = new javax.swing.JList();
        jLabel5 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtComment = new javax.swing.JTextArea();
        jXSheepMap = new org.jdesktop.swingx.JXMapViewer();
        jLabel6 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItemCloseProgram = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        addSheep.setText("Add new sheep");
        addSheep.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addSheepActionPerformed(evt);
            }
        });

        removeSheepBtn.setText("Remove sheep");
        removeSheepBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeSheepBtnActionPerformed(evt);
            }
        });

        refreshbtn.setText("Refresh list");
        refreshbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshbtnActionPerformed(evt);
            }
        });

        deSelect.setText("Deselect");
        deSelect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deSelectActionPerformed(evt);
            }
        });

        sheepJList.setModel(sheepShow);
        jScrollPane1.setViewportView(sheepJList);

        idLbl.setText("ID");

        kordinateLbl.setText("Position");

        jLabel1.setText("User:");

        jLabel2.setText("Farm:");

        jLabel3.setText("Last update:");

        jLabelUser.setText("user");

        jLabelFarm.setText("farm");

        jLabelLastUpdate.setText("time");

        sheepUpdatedLabel.setText("Updated");

        lblPulse.setText("-");

        lblTemp.setText("-");

        lblNickName.setText("-");

        lblIDtxt.setText("-");

        lblPosTxt.setText("-");

        lblUpdateTxt.setText("-");

        editSheepBtn.setText("Edit sheep");
        editSheepBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editSheepBtnActionPerformed(evt);
            }
        });

        jLabel4.setText("Sheeps");

        sheepPulseLabel.setText("Pulse");

        sheepTemperatureLabel.setText("Temperature");

        sheepNicknameLabel.setText("Nickname");

        sheepCommentLabel.setText("Comment");

        sheepUpdateJList.setModel(sheepUpdatesShow);
        jScrollPane2.setViewportView(sheepUpdateJList);

        jLabel5.setText("Updates");

        txtComment.setEditable(false);
        txtComment.setColumns(20);
        txtComment.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        txtComment.setLineWrap(true);
        txtComment.setRows(5);
        txtComment.setTabSize(4);
        txtComment.setWrapStyleWord(true);
        jScrollPane3.setViewportView(txtComment);

        javax.swing.GroupLayout jXSheepMapLayout = new javax.swing.GroupLayout(jXSheepMap);
        jXSheepMap.setLayout(jXSheepMapLayout);
        jXSheepMapLayout.setHorizontalGroup(
            jXSheepMapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 613, Short.MAX_VALUE)
        );
        jXSheepMapLayout.setVerticalGroup(
            jXSheepMapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jLabel6.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        jLabel6.setText("Right-click and drag to pan");

        jMenu1.setText("File");

        jMenuItemCloseProgram.setText("Log out");
        jMenuItemCloseProgram.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCloseProgramActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItemCloseProgram);

        jMenuItem2.setText("Close program");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jXSheepMap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(14, 14, 14)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabelFarm, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabelLastUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabelUser, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(editSheepBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(removeSheepBtn))
                            .addComponent(jSeparator1)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(addSheep, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(idLbl2)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(sheepNicknameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(sheepUpdatedLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 63, Short.MAX_VALUE)
                                            .addComponent(kordinateLbl, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(idLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(sheepPulseLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(sheepTemperatureLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(sheepCommentLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(lblTemp, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
                                            .addComponent(lblIDtxt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(lblPosTxt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(lblUpdateTxt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(lblPulse, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(lblNickName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jScrollPane3)))
                                    .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(refreshbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(deSelect, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(jLabelUser)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabelFarm))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabelLastUpdate))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(addSheep)
                                .addGap(0, 0, 0)
                                .addComponent(idLbl2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(idLbl)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(kordinateLbl)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(sheepUpdatedLabel)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(sheepPulseLabel)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(sheepTemperatureLabel)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(sheepNicknameLabel))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(lblIDtxt)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lblPosTxt)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lblUpdateTxt)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lblPulse)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lblTemp)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lblNickName)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(editSheepBtn)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel5)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 341, Short.MAX_VALUE))
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(removeSheepBtn)
                                                .addGap(0, 0, Short.MAX_VALUE))))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(sheepCommentLabel)
                                        .addGap(0, 0, Short.MAX_VALUE))))
                            .addComponent(jScrollPane1)))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jXSheepMap, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(refreshbtn)
                        .addComponent(deSelect))
                    .addComponent(jLabel6))
                .addGap(5, 5, 5))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void removeSheepBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeSheepBtnActionPerformed
        new WarningBox( this.sheepList.get(this.sheepJList.getSelectedIndex()), this.connect).setVisible(true);
        
    }//GEN-LAST:event_removeSheepBtnActionPerformed

    private void addSheepActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addSheepActionPerformed
        new AddNewSheep(connect).setVisible(true);        // TODO add your handling code here:
    }//GEN-LAST:event_addSheepActionPerformed

    private void refreshbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshbtnActionPerformed
        sheepShow.removeAllElements();
        update();
    }//GEN-LAST:event_refreshbtnActionPerformed

    private void deSelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deSelectActionPerformed
        sheepJList.clearSelection();  // TODO add your handling code here:
        lblIDtxt.setText("");
        lblPosTxt.setText("");
        lblUpdateTxt.setText("");
        lblNickName.setText("");
        txtComment.setText("");
        lblPulse.setText("");
        lblTemp.setText("");
    }//GEN-LAST:event_deSelectActionPerformed

    private void jMenuItemCloseProgramActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemCloseProgramActionPerformed
        // TODO add your handling code here:
        this.setVisible(false);
        new PasswordScreen().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jMenuItemCloseProgramActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void editSheepBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editSheepBtnActionPerformed
      /*  if(this.isEditingSheep) {
            // then we should save the sheep
            this.editSheepBtn.setText("Edit Sheep");
            this.isEditingSheep = false;
        } else {
            this.editSheepBtn.setText("Save");
            this.isEditingSheep = true;
        }*/
         new EditSheep(connect, globalId).setVisible(true);
    }//GEN-LAST:event_editSheepBtnActionPerformed

    /**
     */
    public void update() {
        sheepList = connect.getSheepList();
        jLabelLastUpdate.setText(new Date().toLocaleString());
        if (sheepList != null) {
            for (int i = 0; i < sheepList.size(); i++) {
                Sheep sheep;
                sheep = sheepList.get(i);
                sheepShow.addElement(sheep.getID() + " - " + sheep.getName());
               
            }
        }

    }
    
    private void mapInitialize() {
        // Map test
        WMSService wms = new WMSService();
        wms.setLayer("topo2_WMS");
        wms.setBaseUrl("http://openwms.statkart.no/skwms1/wms.topo2?");
        DefaultTileFactory fact = new WMSTileFactory(wms);
        jXSheepMap.setTileFactory(fact);

        // Use 8 threads in parallel to load the tiles
        fact.setThreadPoolSize(8);

        // Setup local file cache
        File cacheDir = new File(System.getProperty("user.home") + File.separator + ".jxmapviewer2");
        LocalResponseCache.installResponseCache(fact.getInfo().getBaseURL(), cacheDir, false);

        // Set the focus to Tronheim
        GeoPosition trondheim = new GeoPosition(63.431935, 10.37899);

        jXSheepMap.setZoom(10);
        jXSheepMap.setAddressLocation(trondheim);
        
        // Add interactions
        MouseInputListener mia = new PanMouseInputListener(jXSheepMap);
        jXSheepMap.addMouseListener(mia);
        jXSheepMap.addMouseMotionListener(mia);

        jXSheepMap.addMouseListener(new CenterMapListener(jXSheepMap));

        jXSheepMap.addMouseWheelListener(new ZoomMouseWheelListenerCursor(jXSheepMap));

        jXSheepMap.addKeyListener(new PanKeyListener(jXSheepMap));

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addSheep;
    private javax.swing.JButton deSelect;
    private javax.swing.JButton editSheepBtn;
    private javax.swing.JLabel idLbl;
    private javax.swing.JLabel idLbl2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabelFarm;
    private javax.swing.JLabel jLabelLastUpdate;
    private javax.swing.JLabel jLabelUser;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItemCloseProgram;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private org.jdesktop.swingx.JXMapViewer jXSheepMap;
    private javax.swing.JLabel kordinateLbl;
    private javax.swing.JLabel lblIDtxt;
    private javax.swing.JLabel lblNickName;
    private javax.swing.JLabel lblPosTxt;
    private javax.swing.JLabel lblPulse;
    private javax.swing.JLabel lblTemp;
    private javax.swing.JLabel lblUpdateTxt;
    private javax.swing.JButton refreshbtn;
    private javax.swing.JButton removeSheepBtn;
    private javax.swing.JLabel sheepCommentLabel;
    private javax.swing.JList sheepJList;
    private javax.swing.JLabel sheepNicknameLabel;
    private javax.swing.JLabel sheepPulseLabel;
    private javax.swing.JLabel sheepTemperatureLabel;
    private javax.swing.JList sheepUpdateJList;
    private javax.swing.JLabel sheepUpdatedLabel;
    private javax.swing.JTextArea txtComment;
    // End of variables declaration//GEN-END:variables
}
