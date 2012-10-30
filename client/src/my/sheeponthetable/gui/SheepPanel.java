/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my.sheeponthetable.gui;

import java.awt.Color;
import java.util.*;
import javax.swing.*;
import my.sheeponthetable.tools.*;
import java.io.File;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.MouseInputListener;
import my.sheeponthetable.tools.map.FancyWaypointRenderer;
import my.sheeponthetable.tools.map.MyWaypoint;
import my.sheeponthetable.tools.map.RoutePainter;
import org.jdesktop.swingx.JXMapViewer;
import org.jdesktop.swingx.input.CenterMapListener;
import org.jdesktop.swingx.input.PanKeyListener;
import org.jdesktop.swingx.input.PanMouseInputListener;
import org.jdesktop.swingx.input.ZoomMouseWheelListenerCursor;
import org.jdesktop.swingx.mapviewer.DefaultTileFactory;
import org.jdesktop.swingx.mapviewer.GeoPosition;
import org.jdesktop.swingx.mapviewer.LocalResponseCache;
import org.jdesktop.swingx.mapviewer.wms.WMSService;
import org.jdesktop.swingx.mapviewer.wms.WMSTileFactory;
import org.jdesktop.swingx.mapviewer.WaypointPainter;
import org.jdesktop.swingx.painter.CompoundPainter;
import org.jdesktop.swingx.painter.Painter;

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
    //private ServerConnector connect;
    //private ServerPinger serverPinger;
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
     *
     * @param update
     */
    public void addSheepUpdateToList(SheepUpdate update) {
        sheepUpdatesShow.addElement(update);
    }

    /**
     * Creates new form SheepPanelfail
     *
     * @param connect
     */
    //public SheepPanel(ServerConnector connect) {
    public SheepPanel() {
        initComponents();
        this.setLocationRelativeTo(null);
        editSheepBtn.setEnabled(false);
        removeSheepBtn.setEnabled(false);
        txtNick.setVisible(false);
        txtComment.setEditable(false);
        txtBorn.setVisible(false);
        txtDead.setVisible(false);
        txtWeight.setVisible(false);
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
                    lblNick.setText("Not available");
                    txtComment.setText("Not available");
                    lblPulse.setText("Not available");
                    lblTemp.setText("Not available");
                    lblBornTxt.setText("Not available");
                    lblWeightTxt.setText("Not available");
                    lblDeadTxt.setText("Not available");


                    lblDeadTxt.setText(Integer.toString(sheepList.get(list.getSelectedIndex()).getDeceased()));
                    lblBornTxt.setText(Integer.toString(sheepList.get(list.getSelectedIndex()).getBorn()));
                    lblWeightTxt.setText(Double.toString((sheepList.get(list.getSelectedIndex()).getWeight())));
                    int id = sheepList.get(list.getSelectedIndex()).getID();
                    lblIDtxt.setText(Integer.toString(id));

                    txtComment.setText(sheepList.get(list.getSelectedIndex()).getComment());
                    globalId = id;
                    nickname = sheepList.get(list.getSelectedIndex()).getName();
                    comment = sheepList.get(list.getSelectedIndex()).getComment();
                    lblNick.setText(nickname);
                    System.out.println(nickname);
                    if (sheepList.get(list.getSelectedIndex()).getDeceased() == 0) {
                        txtDead.setText("Not Dead");
                        txtDead.setBackground(Color.green);
                        lblDeadTxt.setText("Not Dead");
                        lblDeadTxt.setBackground(Color.green);
                    }
                    /*if (!sheepList.get(list.getSelectedIndex()).isAlive())
                     setBackground(Color.red);
                     if (sheepList.get(list.getSelectedIndex()).getDeceased()== -1)
                     setBackground(Color.blue);
                     if (sheepList.get(list.getSelectedIndex()).isAlive())
                     setBackground(Color.green);*/
                    sheepUpdatesShow.removeAllElements();
                    sheepList.get(list.getSelectedIndex()).setUpdates(WebServiceClient.getSheepUpdate(Integer.toString(id), "100"));
                    if (!sheepList.get(list.getSelectedIndex()).getUpdates().isEmpty()) {
                        // Getting update index 0 because 0 is the latest (newest) update
                        double xpos = sheepList.get(list.getSelectedIndex()).getUpdates().get(0).getX();
                        double ypos = sheepList.get(list.getSelectedIndex()).getUpdates().get(0).getY();
                        lblPosTxt.setText(xpos + ", " + ypos);
                        Date formattedTimestamp = new Date(sheepList.get(list.getSelectedIndex()).getUpdates().get(0).getTimeStamp() * 1000);
                        lblUpdateTxt.setText(formattedTimestamp.toLocaleString());
                        lblPulse.setText(Integer.toString(sheepList.get(list.getSelectedIndex()).getUpdates().get(0).getPulse()));
                        lblTemp.setText(Double.toString(sheepList.get(list.getSelectedIndex()).getUpdates().get(0).getTemp()));
                        posX = xpos;
                        posY = ypos;
                        Set<MyWaypoint> waypoints = new HashSet<MyWaypoint>();
                        List<GeoPosition> track = new ArrayList();
                        for (int i = 0; i < sheepList.get(list.getSelectedIndex()).getUpdates().size(); i++) {
                            SheepUpdate update = sheepList.get(list.getSelectedIndex()).getUpdates().get(i);
                            Date formattedUpdateTimestamp = new Date(update.getTimeStamp() * 1000);
                            sheepUpdatesShow.addElement(formattedUpdateTimestamp.toLocaleString());
                            track.add(new GeoPosition(update.getX(), update.getY()));
                            Color color = Color.WHITE;
                            if(i == 0) {
                                color = Color.RED;
                            }
                            waypoints.add(new MyWaypoint(formattedUpdateTimestamp.toLocaleString(), color, new GeoPosition(update.getX(), update.getY())));
                        }
                        RoutePainter routePainter = new RoutePainter(track);
                        WaypointPainter<MyWaypoint> waypointPainter = new WaypointPainter<MyWaypoint>();
                        waypointPainter.setWaypoints(waypoints);
                        waypointPainter.setRenderer(new FancyWaypointRenderer());
                        
                        List<Painter<JXMapViewer>> painters = new ArrayList<Painter<JXMapViewer>>();
                        painters.add(routePainter);
                        painters.add(waypointPainter);
                        
                        CompoundPainter<JXMapViewer> painter = new CompoundPainter<JXMapViewer>(painters);
                        jXSheepMap.getMainMap().setOverlayPainter(painter);
                    }
                }
            }
        };
        sheepJList.addListSelectionListener(listSelectionListener);
        //jLabelUser.setText(this.connect.getUsername());
        //jLabelFarm.setText(this.connect.getFarmName());
        update();

        //this.serverPinger = new ServerPinger(this);
        //serverPinger.start();
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
        jLabel6 = new javax.swing.JLabel();
        txtNick = new javax.swing.JTextField();
        lblBorn = new javax.swing.JLabel();
        lblDead = new javax.swing.JLabel();
        lblWeight = new javax.swing.JLabel();
        lblBornTxt = new javax.swing.JLabel();
        lblDeadTxt = new javax.swing.JLabel();
        lblWeightTxt = new javax.swing.JLabel();
        txtWeight = new javax.swing.JTextField();
        txtBorn = new javax.swing.JTextField();
        txtDead = new javax.swing.JTextField();
        lblNick = new javax.swing.JLabel();
        jXSheepMap = new org.jdesktop.swingx.JXMapKit();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItemCloseProgram = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuFarmTools = new javax.swing.JMenuItem();
        jMenuProperties = new javax.swing.JMenuItem();

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

        jLabel6.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        jLabel6.setText("Right-click and drag to pan");

        lblBorn.setText("Born");

        lblDead.setText("Deceased");

        lblWeight.setText("Weight");

        lblBornTxt.setText("-");

        lblDeadTxt.setText("-");

        lblWeightTxt.setText("-");

        txtBorn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBornActionPerformed(evt);
            }
        });

        txtDead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDeadActionPerformed(evt);
            }
        });

        lblNick.setText("-");

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

        jMenu2.setText("Preferences");

        jMenuFarmTools.setText("Farm");
        jMenuFarmTools.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuFarmToolsActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuFarmTools);

        jMenuProperties.setText("User");
        jMenuProperties.setActionCommand("UserProperties");
        jMenuProperties.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuPropertiesActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuProperties);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jXSheepMap, javax.swing.GroupLayout.PREFERRED_SIZE, 621, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
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
                                    .addComponent(jSeparator1)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(addSheep, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(idLbl2)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(lblBorn, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(sheepCommentLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                            .addComponent(sheepNicknameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                            .addComponent(sheepUpdatedLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 63, Short.MAX_VALUE)
                                                            .addComponent(kordinateLbl, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                            .addComponent(idLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                            .addComponent(sheepPulseLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                            .addComponent(sheepTemperatureLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                                    .addComponent(lblWeight)
                                                    .addComponent(lblDead))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(layout.createSequentialGroup()
                                                        .addComponent(lblNick)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(txtNick, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(lblTemp, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
                                                        .addComponent(lblIDtxt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(lblPosTxt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(lblUpdateTxt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(lblPulse, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jScrollPane3)
                                                        .addGroup(layout.createSequentialGroup()
                                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                .addComponent(lblWeightTxt, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 4, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(lblBornTxt)
                                                                .addComponent(lblDeadTxt))
                                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                .addComponent(txtDead, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(txtWeight, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(txtBorn, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                            .addGap(20, 20, 20)))))))))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(editSheepBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(removeSheepBtn)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(refreshbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
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
                                        .addComponent(sheepTemperatureLabel))
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
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGap(27, 27, 27)
                                                .addComponent(sheepNicknameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                    .addComponent(txtNick, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(lblNick))))))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(lblBorn)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(lblBornTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtBorn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(3, 3, 3)))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblDeadTxt, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lblDead)
                                        .addComponent(txtDead, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lblWeightTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblWeight)
                                    .addComponent(txtWeight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(24, 24, 24)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(sheepCommentLabel)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(editSheepBtn)
                                            .addComponent(removeSheepBtn))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel5)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE))
                            .addComponent(jScrollPane1)))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jXSheepMap, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(deSelect)
                        .addComponent(refreshbtn))
                    .addComponent(jLabel6))
                .addGap(5, 5, 5))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void removeSheepBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeSheepBtnActionPerformed
        int option = JOptionPane.showConfirmDialog(null, "Do you really want to delete this sheep?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            WebServiceClient.removeSheep(sheepList.get(sheepJList.getSelectedIndex()));
            update();
        }
    }//GEN-LAST:event_removeSheepBtnActionPerformed

    private void addSheepActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addSheepActionPerformed
        new AddNewSheep(this).setVisible(true);        // TODO add your handling code here:
    }//GEN-LAST:event_addSheepActionPerformed

    private void refreshbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshbtnActionPerformed
        update();
    }//GEN-LAST:event_refreshbtnActionPerformed

    private void deSelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deSelectActionPerformed
        sheepJList.clearSelection();  // TODO add your handling code here:
        lblIDtxt.setText("");
        lblPosTxt.setText("");
        lblUpdateTxt.setText("");
        lblNick.setText("");
        txtComment.setText("");
        lblPulse.setText("");
        lblTemp.setText("");
        resetSelection();
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

        if (this.isEditingSheep) {
            // then we should save the sheep
            WebServiceClient.editSheep(sheepList.get(EditSheep()));
            this.editSheepBtn.setText("Edit Sheep");
            this.isEditingSheep = false;
            setVisible();
            update();

        } else {
            prepEditSheep();
            setInvisible();
            this.editSheepBtn.setText("Save");
            this.isEditingSheep = true;
        }


    }//GEN-LAST:event_editSheepBtnActionPerformed

    private void txtBornActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBornActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBornActionPerformed

    private void txtDeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDeadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDeadActionPerformed

    private void jMenuFarmToolsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuFarmToolsActionPerformed
       // this.setVisible(false);
        new FarmTools(this).setVisible(true);// TODO add your handling code here:
    }//GEN-LAST:event_jMenuFarmToolsActionPerformed

    private void jMenuPropertiesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuPropertiesActionPerformed
      //  this.setVisible(false);
        new UserProperties(this).setVisible(true);// TODO add your handling code here:
    }//GEN-LAST:event_jMenuPropertiesActionPerformed

    /**
     */
    public void update() {
        //sheepList = connect.getSheepList();
        sheepList = WebServiceClient.getSheepList();
        jLabelLastUpdate.setText(new Date().toLocaleString());
        resetSelection();
    }
    
    public void resetSelection() {
        sheepShow.removeAllElements();
                        Set<MyWaypoint> waypoints = new HashSet<MyWaypoint>();
        if (sheepList != null) {
            for (int i = 0; i < sheepList.size(); i++) {
                Sheep sheep;
                sheep = sheepList.get(i);
                sheepShow.addElement(sheep.getID() + " - " + sheep.getName());
                if(!sheep.getUpdates().isEmpty()) {
                    waypoints.add(new MyWaypoint(Integer.toString(sheep.getID()), Color.WHITE, new GeoPosition(sheep.getUpdates().get(0).getX(), sheep.getUpdates().get(0).getY())));
                }
            }
        }
        sheepJList.setSelectedIndex(0);
        
        WaypointPainter<MyWaypoint> waypointPainter = new WaypointPainter<MyWaypoint>();
        waypointPainter.setWaypoints(waypoints);
        waypointPainter.setRenderer(new FancyWaypointRenderer());
        
        List<Painter<JXMapViewer>> painters = new ArrayList<Painter<JXMapViewer>>();
        painters.add(waypointPainter);

        CompoundPainter<JXMapViewer> painter = new CompoundPainter<JXMapViewer>(painters);
        
        jXSheepMap.getMainMap().setOverlayPainter(painter);
    }

    public void setVisible() {
        txtNick.setVisible(false);
        txtComment.setEditable(false);
        txtBorn.setVisible(false);
        txtDead.setVisible(false);
        txtWeight.setVisible(false);

        lblNick.setVisible(true);
        lblBornTxt.setVisible(true);
        lblDeadTxt.setVisible(true);
        lblWeightTxt.setVisible(true);

        sheepJList.setEnabled(true);
        addSheep.setEnabled(true);
        removeSheepBtn.setEnabled(true);
        refreshbtn.setEnabled(true);
        deSelect.setEnabled(true);
    }

    public void setInvisible() {
        lblNick.setVisible(false);
        lblBornTxt.setVisible(false);
        lblDeadTxt.setVisible(false);
        lblWeightTxt.setVisible(false);

        txtNick.setVisible(true);
        txtComment.setEditable(true);
        txtBorn.setVisible(true);
        txtDead.setVisible(true);
        txtWeight.setVisible(true);

        sheepJList.setEnabled(false);
        addSheep.setEnabled(false);
        removeSheepBtn.setEnabled(false);
        refreshbtn.setEnabled(false);
        deSelect.setEnabled(false);
    }

    public void prepEditSheep() {
        int x = 0;
        for (int i = 0; i < sheepList.size(); i++) {
            if (sheepList.get(i).getID() == globalId) {
                x = i;
            }
        }

        Sheep change = sheepList.get(x);
        txtNick.setText(change.getName());
        txtComment.setText(change.getComment());
        txtDead.setText(Integer.toString(change.getDeceased()));
        txtWeight.setText(Double.toString(change.getWeight()));
    }

    public int EditSheep() {
        int x = 0;
        for (int i = 0; i < sheepList.size(); i++) {
            if (sheepList.get(i).getID() == globalId) {
                x = i;
            }
        }
        Sheep change = sheepList.get(x);
        /*String newComment, newName;
         int newDeceased;
         double newWeight;*/
        if (!txtNick.getText().equals(change.getName())) {
            change.setName(txtNick.getText());
        }
        if (!txtComment.getText().equals(change.getComment())) {
            change.setKommentar(txtComment.getText());
        }
        if (!txtDead.getText().equals(Integer.toString(change.getDeceased()))) {
            change.setDeceaced(Integer.parseInt(txtDead.getText()));
        }
        if (!txtWeight.getText().equals(Double.toString(change.getWeight()))) {
            change.setWeight(Double.parseDouble(txtDead.getText()));
        }
        lblNick.setText(change.getName());
        txtComment.setText(change.getComment());
        lblDeadTxt.setText(Integer.toString(change.getDeceased()));
        lblWeightTxt.setText(Double.toString(change.getWeight()));
        return x;
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
        jXSheepMap.getMiniMap().setVisible(false);
        
        // Add interactions
        MouseInputListener mia = new PanMouseInputListener(jXSheepMap.getMainMap());
        jXSheepMap.addMouseListener(mia);
        jXSheepMap.addMouseMotionListener(mia);

        jXSheepMap.addMouseListener(new CenterMapListener(jXSheepMap.getMainMap()));

        jXSheepMap.addMouseWheelListener(new ZoomMouseWheelListenerCursor(jXSheepMap.getMainMap()));

        jXSheepMap.addKeyListener(new PanKeyListener(jXSheepMap.getMainMap()));

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
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuFarmTools;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItemCloseProgram;
    private javax.swing.JMenuItem jMenuProperties;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private org.jdesktop.swingx.JXMapKit jXSheepMap;
    private javax.swing.JLabel kordinateLbl;
    private javax.swing.JLabel lblBorn;
    private javax.swing.JLabel lblBornTxt;
    private javax.swing.JLabel lblDead;
    private javax.swing.JLabel lblDeadTxt;
    private javax.swing.JLabel lblIDtxt;
    private javax.swing.JLabel lblNick;
    private javax.swing.JLabel lblPosTxt;
    private javax.swing.JLabel lblPulse;
    private javax.swing.JLabel lblTemp;
    private javax.swing.JLabel lblUpdateTxt;
    private javax.swing.JLabel lblWeight;
    private javax.swing.JLabel lblWeightTxt;
    private javax.swing.JButton refreshbtn;
    private javax.swing.JButton removeSheepBtn;
    private javax.swing.JLabel sheepCommentLabel;
    private javax.swing.JList sheepJList;
    private javax.swing.JLabel sheepNicknameLabel;
    private javax.swing.JLabel sheepPulseLabel;
    private javax.swing.JLabel sheepTemperatureLabel;
    private javax.swing.JList sheepUpdateJList;
    private javax.swing.JLabel sheepUpdatedLabel;
    private javax.swing.JTextField txtBorn;
    private javax.swing.JTextArea txtComment;
    private javax.swing.JTextField txtDead;
    private javax.swing.JTextField txtNick;
    private javax.swing.JTextField txtWeight;
    // End of variables declaration//GEN-END:variables
}
