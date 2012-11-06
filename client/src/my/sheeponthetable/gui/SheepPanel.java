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
import org.jdesktop.swingx.JXMapKit;
import org.jdesktop.swingx.JXMapKit.DefaultProviders;
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
 * @author Gruppe 7
 */
public class SheepPanel extends javax.swing.JFrame {

    private DefaultListModel sheepShow = new DefaultListModel();
    private DefaultListModel sheepUpdatesShow = new DefaultListModel();
    private List<Sheep> sheepList = new ArrayList();
    private List<SheepUpdate> sheepUpdateList;
    private Set<MyWaypoint> wayPointSet = new HashSet<>();
    private int farmID = Integer.parseInt(WebServiceClient.farmid);
    // mye sheep info
    private String nickname;
    private int globalId;
    ListSelectionListener sheepListSelectionListener;
    ListSelectionListener updateListSelectionListener;

    /* to know what do to when you press the edit or save button */
    private boolean isEditingSheep = false;

    public JXMapKit getMapKit()  {
        return jXSheepMap;
    }
    
    public Set<MyWaypoint> getWayPoints() {
        return wayPointSet;
    }
    
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

        mapInitialize();

        sheepListSelectionListener = new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent listSelectionEvent) {

                JList list = (JList) listSelectionEvent.getSource();
                int selectedIndex = list.getSelectedIndex();
                if (!listSelectionEvent.getValueIsAdjusting() && selectedIndex != -1) {
                    selectSheep(selectedIndex);
                }
            }
        };
        sheepJList.addListSelectionListener(sheepListSelectionListener);
        sheepJList.setCellRenderer(new SheepListCellRenderer(this));
       
        updateListSelectionListener = new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                JList list = (JList) e.getSource();
                int selectedIndex = list.getSelectedIndex();
                if (!e.getValueIsAdjusting() && selectedIndex != -1) {
                    selectUpdate(selectedIndex);
                }
            }
        };
        sheepUpdateJList.addListSelectionListener(updateListSelectionListener);
        
        update();
        
    }
    
    /**
     * Called by event listeners to handle what happens when a sheep is seleced
     * 
     * @param index 
     */
    private void selectSheep(int index) {
        // Settings textfields to "Not available" before update
        // because there may be no updates for selected Sheep
        
        lblSheepId.setText("Not available");
        lblSheepPosition.setText("Not available");
        lblSheepUpdate.setText("Not available");
        lblSheepNickname.setText("Not available");
        taSheepComment.setText("Not available");
        lblSheepPulse.setText("Not available");
        lblSheepTemperature.setText("Not available");
        lblSheepBorn.setText("Not available");
        lblSheepWeight.setText("Not available");
        lblSheepDeceased.setText("Not available");
        
        Sheep s = sheepList.get(index);
        
        lblSheepDeceased.setText(Integer.toString(s.getDeceased()));
        lblSheepBorn.setText(Integer.toString(s.getBorn()));
        lblSheepWeight.setText(Double.toString((s.getWeight())) + " kg");
        int id = s.getID();
        lblSheepId.setText(Integer.toString(id));

        taSheepComment.setText(s.getComment());
        globalId = id;
        nickname = s.getName();
        lblSheepNickname.setText(nickname);
        System.out.println(nickname);
        if (s.getDeceased() <= 86400) {
            lblSheepDeceased.setText("Not Dead");
            lblSheepDeceased.setBackground(Color.green);
        }

        sheepUpdatesShow.removeAllElements();
        s.setUpdates(WebServiceClient.getSheepUpdate(Integer.toString(id), "100"));
        if (!s.getUpdates().isEmpty()) {
            // Getting update index 0 because 0 is the latest (newest) update
            double xpos = s.getUpdates().get(0).getX();
            double ypos = s.getUpdates().get(0).getY();
            lblSheepPosition.setText(ypos + ", " + xpos);
            Date formattedTimestamp = new Date(s.getUpdates().get(0).getTimeStamp() * 1000);
            lblSheepUpdate.setText(formattedTimestamp.toLocaleString());
            lblSheepPulse.setText(Integer.toString(s.getUpdates().get(0).getPulse()) + " BPM");
            lblSheepTemperature.setText(Double.toString(s.getUpdates().get(0).getTemp()) + "C" + "\u00B0");
            Set<MyWaypoint> waypoints = new HashSet<>();
            List<GeoPosition> track = new ArrayList();
            for (int i = 0; i < s.getUpdates().size(); i++) {
                SheepUpdate update = s.getUpdates().get(i);
                Date formattedUpdateTimestamp = new Date(update.getTimeStamp() * 1000);
                sheepUpdatesShow.addElement(formattedUpdateTimestamp.toLocaleString());
                track.add(new GeoPosition(update.getY(), update.getX()));
                Color color = Color.WHITE;
                if (i == 0) {
                    color = Color.RED;
                }
                waypoints.add(new MyWaypoint(formattedUpdateTimestamp.toLocaleString(), color, new GeoPosition(update.getY(), update.getX()), i, false));
            }
            RoutePainter routePainter = new RoutePainter(track);
            WaypointPainter<MyWaypoint> waypointPainter = new WaypointPainter<>();
            waypointPainter.setWaypoints(waypoints);
            waypointPainter.setRenderer(new FancyWaypointRenderer());

            List<Painter<JXMapViewer>> painters = new ArrayList<>();
            painters.add(routePainter);
            painters.add(waypointPainter);

            CompoundPainter<JXMapViewer> painter = new CompoundPainter<>(painters);
            jXSheepMap.getMainMap().setOverlayPainter(painter);

            wayPointSet = waypoints;
            focusAccordingToWaypoints();
        }
        else {
            CompoundPainter<JXMapViewer> painter = new CompoundPainter<>();
            jXSheepMap.getMainMap().setOverlayPainter(painter);
        }
        sheepUpdateList = s.getUpdates();
    }
    
    /**
     * This method is called by the mouse listeners on the map, to handle these
     * events. What this method does is to select the shep on the list with 
     * the index corresponding to the input value. This selection then fires
     * the list event listeners which call the selectSheep method.
     * 
     * @param index 
     */
    public void mapSelectSheep(int index) {
        sheepJList.setSelectedIndex(index);
        sheepJList.ensureIndexIsVisible(index);
    }
    
    /**
     * Called by the select event listeners to handle what happens when a SU is 
     * seleced.
     * 
     * @param index 
     */
    private void selectUpdate(int index) {
        SheepUpdate su = sheepUpdateList.get(index);
        Date formattedTimestamp = new Date(su.getTimeStamp() * 1000);
        lblSheepUpdate.setText(formattedTimestamp.toLocaleString());
        lblSheepPulse.setText(Integer.toString(su.getPulse()) + " BPM");
        lblSheepTemperature.setText(Double.toString(su.getTemp()) + " C" + "\u00B0");
        lblSheepPosition.setText(su.getY() + ", " + su.getX());
        
        Set<MyWaypoint> waypoints = new HashSet<>();
        List<GeoPosition> track = new ArrayList();
        for (int i = 0; i < sheepUpdateList.size(); i++) {
            SheepUpdate update = sheepUpdateList.get(i);
            Date fmtTimestamp = new Date(update.getTimeStamp() * 1000);
            GeoPosition gp = new GeoPosition(update.getY(), update.getX());
            Color color = Color.WHITE;
            if (i == 0) {
                color = Color.RED;
            }
            if (i == index) {
                color = Color.BLUE;
            }
            MyWaypoint wp = new MyWaypoint(fmtTimestamp.toLocaleString(), color, gp, i, false);
            waypoints.add(wp);
            track.add(gp);

            RoutePainter routePainter = new RoutePainter(track);
            WaypointPainter<MyWaypoint> waypointPainter = new WaypointPainter<>();
            waypointPainter.setWaypoints(waypoints);
            waypointPainter.setRenderer(new FancyWaypointRenderer());

            List<Painter<JXMapViewer>> painters = new ArrayList<>();
            painters.add(routePainter);
            painters.add(waypointPainter);

            CompoundPainter<JXMapViewer> painter = new CompoundPainter<>(painters);
            jXSheepMap.getMainMap().setOverlayPainter(painter);
            
        }
    }
    
    /**
     * This method is called by the mouse listeners on the map, to handle these
     * events. What this method does is to select the update on the list with 
     * the index corresponding to the input value. This selection then fires
     * the list event listeners which call the selectUpdate method.
     * 
     * @param index 
     */
    public void mapSelectUpdate(int index) {
        sheepUpdateJList.setSelectedIndex(index);
        sheepUpdateJList.ensureIndexIsVisible(index);
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
        refreshbtn = new javax.swing.JButton();
        deSelect = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        sheepJList = new javax.swing.JList();
        idLbl2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        sheepUpdateJList = new javax.swing.JList();
        jLabel5 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel6 = new javax.swing.JLabel();
        jXSheepMap = new org.jdesktop.swingx.JXMapKit();
        jPanel1 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        taSheepComment = new javax.swing.JTextArea();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        lblSheepId = new javax.swing.JLabel();
        lblSheepNickname = new javax.swing.JLabel();
        lblSheepWeight = new javax.swing.JLabel();
        lblSheepBorn = new javax.swing.JLabel();
        lblSheepDeceased = new javax.swing.JLabel();
        lblSheepUpdate = new javax.swing.JLabel();
        lblSheepPosition = new javax.swing.JLabel();
        lblSheepTemperature = new javax.swing.JLabel();
        lblSheepPulse = new javax.swing.JLabel();
        lblSheepAlarm = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabelLastUpdate = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItemCloseProgram = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuFarmTools = new javax.swing.JMenuItem();
        jMenuProperties = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 0, 0));

        addSheep.setText("Add new sheep");
        addSheep.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addSheepActionPerformed(evt);
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

        sheepUpdateJList.setModel(sheepUpdatesShow);
        jScrollPane2.setViewportView(sheepUpdateJList);

        jLabel5.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        jLabel5.setText("Updates:");

        jLabel6.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        jLabel6.setText("Right-click and drag to pan");

        jPanel1.setMaximumSize(new java.awt.Dimension(310, 350));
        jPanel1.setSize(new java.awt.Dimension(310, 350));

        jLabel7.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel7.setText("ID:");

        jLabel8.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel8.setText("Nickname:");

        jLabel9.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel9.setText("Weight:");

        jLabel10.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel10.setText("Date of Rebirth:");

        jLabel11.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel11.setText("Comment:");

        jLabel12.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel12.setText("Date of Birth:");

        taSheepComment.setEditable(false);
        taSheepComment.setColumns(20);
        taSheepComment.setLineWrap(true);
        taSheepComment.setRows(5);
        taSheepComment.setMaximumSize(new java.awt.Dimension(240, 80));
        taSheepComment.setMinimumSize(new java.awt.Dimension(240, 80));
        jScrollPane3.setViewportView(taSheepComment);

        jLabel13.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel13.setText("Position:");

        jLabel14.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel14.setText("Temperature:");

        jLabel15.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel15.setText("Selected Update:");

        jLabel16.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel16.setText("Hearth Rate:");

        jLabel17.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel17.setText("Status:");

        lblSheepId.setText("No sheep selected");

        lblSheepNickname.setText("No sheep selected");

        lblSheepWeight.setText("No sheep selected");

        lblSheepBorn.setText("No sheep selected");

        lblSheepDeceased.setText("No sheep selected");

        lblSheepUpdate.setText("No update selected");

        lblSheepPosition.setText("No update selected");

        lblSheepTemperature.setText("No update selected");

        lblSheepPulse.setText("No update selected");

        lblSheepAlarm.setText("No update selected");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 247, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)
                                .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                            .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblSheepId)
                                    .addComponent(lblSheepNickname)
                                    .addComponent(lblSheepWeight)
                                    .addComponent(lblSheepBorn)
                                    .addComponent(lblSheepDeceased)
                                    .addComponent(lblSheepUpdate)
                                    .addComponent(lblSheepTemperature)
                                    .addComponent(lblSheepPulse)
                                    .addComponent(lblSheepAlarm))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(lblSheepPosition, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(lblSheepId))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(lblSheepNickname))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(lblSheepWeight))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(lblSheepBorn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(lblSheepDeceased))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(lblSheepUpdate))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(lblSheepPosition))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(lblSheepTemperature))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(lblSheepPulse))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(lblSheepAlarm))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel18.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        jLabel18.setText("Navnet til Sauefarmen");

        jLabel1.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel1.setText("Logged in as:");

        jLabel2.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel2.setText("Last update:");

        jLabel3.setText("username");

        jLabelLastUpdate.setText("Not Available");

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
                        .addComponent(jXSheepMap, javax.swing.GroupLayout.PREFERRED_SIZE, 551, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(layout.createSequentialGroup()
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(layout.createSequentialGroup()
                                                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addGap(16, 16, 16))
                                                .addComponent(jLabelLastUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                    .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(idLbl2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 69, Short.MAX_VALUE)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(refreshbtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(deSelect, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(addSheep, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 478, Short.MAX_VALUE)
                                .addGap(60, 60, 60))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jScrollPane1)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel1)
                                        .addComponent(jLabel3))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel2)
                                        .addComponent(jLabelLastUpdate))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(6, 6, 6)
                                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(97, 97, 97)
                                    .addComponent(idLbl2)))
                            .addComponent(jScrollPane2))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addComponent(refreshbtn)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(deSelect)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel5)
                                .addGap(27, 27, 27)))
                        .addComponent(addSheep))
                    .addComponent(jXSheepMap, javax.swing.GroupLayout.PREFERRED_SIZE, 571, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel6)
                .addGap(0, 17, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void addSheepActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addSheepActionPerformed
        new AddNewSheep(this).setVisible(true);        // TODO add your handling code here:
    }//GEN-LAST:event_addSheepActionPerformed

    private void refreshbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshbtnActionPerformed
        update();
    }//GEN-LAST:event_refreshbtnActionPerformed

    private void deSelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deSelectActionPerformed
        sheepJList.clearSelection();
        sheepUpdateJList.clearSelection();
        lblSheepId.setText("-");
        lblSheepPosition.setText("-");
        lblSheepUpdate.setText("-");
        lblSheepNickname.setText("-");
        taSheepComment.setText("-");
        lblSheepPulse.setText("-");
        lblSheepTemperature.setText("-");
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

    private void jMenuFarmToolsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuFarmToolsActionPerformed
       // this.setVisible(false);
        new FarmTools(this, farmID).setVisible(true);// TODO add your handling code here:
    }//GEN-LAST:event_jMenuFarmToolsActionPerformed

    private void jMenuPropertiesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuPropertiesActionPerformed
        new UserProperties(this).setVisible(true);
    }//GEN-LAST:event_jMenuPropertiesActionPerformed

    /**
     */
    public void update() {
        sheepList = WebServiceClient.getSheepList();
        jLabelLastUpdate.setText(new Date().toLocaleString());
        resetSelection();
    }
    
    public void resetSelection() {
        sheepShow.removeAllElements();
        sheepUpdatesShow.removeAllElements();
        
        Set<MyWaypoint> waypoints = new HashSet<>();
        
        if (sheepList != null) {
            for (int i = 0; i < sheepList.size(); i++) {
                Sheep sheep;
                sheep = sheepList.get(i);
                sheepShow.addElement(sheep.getID() + " - " + sheep.getName());
                if(!sheep.getUpdates().isEmpty()) {
                    GeoPosition gp = new GeoPosition(sheep.getUpdates().get(0).getY(), sheep.getUpdates().get(0).getX());
                    MyWaypoint wp = new MyWaypoint(Integer.toString(sheep.getID()), Color.WHITE, gp, i, true);
                    waypoints.add(wp);
                }
            }
        }
        
        WaypointPainter<MyWaypoint> waypointPainter = new WaypointPainter<>();
        waypointPainter.setWaypoints(waypoints);
        waypointPainter.setRenderer(new FancyWaypointRenderer());
        
        List<Painter<JXMapViewer>> painters = new ArrayList<>();
        painters.add(waypointPainter);

        CompoundPainter<JXMapViewer> painter = new CompoundPainter<>(painters);
        
        jXSheepMap.getMainMap().setOverlayPainter(painter);
        
        wayPointSet = waypoints;
        focusAccordingToWaypoints();
    }

    /**
     * Focuses the map so that it shows all the WayPoints on the screen, by
     * setting the focus point to the centroid of all the waypoints, and zooming
     * to the appropriate level.
     * 
     * @param wp - a set of waypoints to focus on
     */
    private void focusAccordingToWaypoints() {
        // If there are no waypoints to focus on, go to Oppdal
        if (wayPointSet.isEmpty()) {
            GeoPosition oppdal = new GeoPosition(62.573611,9.608889);
            jXSheepMap.setAddressLocation(oppdal);            
            return;
        }
        
        // Otherwise, convert to set of geoPositions
        Set<GeoPosition> gp = new HashSet<>();
        
        for (MyWaypoint w : wayPointSet) {
            gp.add(w.getPosition());
        }
        
        // Find and set the centre position by calculating the mean over all 
        // the positions.
        double x_sum = 0.0;
        double y_sum = 0.0;
        
        for (GeoPosition g : gp) {
            x_sum += g.getLongitude();
            y_sum += g.getLatitude();
        }
        
        GeoPosition focus = new GeoPosition(y_sum / gp.size(), x_sum / gp.size());
        jXSheepMap.setAddressLocation(focus);
        
        // Find the correct zoom. Because calculateZoomFrom finds the minimal
        // amount it has to zoom out before displaying all the geopositions, we 
        // first set zoom to level 1, to make sure we get the minimal focused zoom.
        jXSheepMap.getMainMap().setZoom(1);
        jXSheepMap.getMainMap().calculateZoomFrom(gp);
        // CalculateZoomFrom() finds a zoom level that barely touches upon all
        // the waypoints. In order to make sure all the waypoints are places 
        // well within the bounds of the map, zoom out one more level.
        jXSheepMap.getMainMap().setZoom(jXSheepMap.getMainMap().getZoom()+1);
    }
    
    public void setVisible() {
       
    }

    public void setInvisible() {
        
    }

    public void prepEditSheep() {
        int x = 0;
        for (int i = 0; i < sheepList.size(); i++) {
            if (sheepList.get(i).getID() == globalId) {
                x = i;
            }
        }

        Sheep change = sheepList.get(x);
        lblSheepNickname.setText(change.getName());
        taSheepComment.setText(change.getComment());
        lblSheepDeceased.setText(Integer.toString(change.getDeceased()));
        lblSheepWeight.setText(Double.toString(change.getWeight()));
    }

    public int EditSheep() {
        return -1;
    }

    private void mapInitialize() {
        // Map test
        WMSService wms = new WMSService();
        wms.setLayer("topo2_WMS");
        wms.setBaseUrl("http://openwms.statkart.no/skwms1/wms.topo2?");
        DefaultTileFactory fact = new WMSTileFactory(wms);
        
        jXSheepMap.setDefaultProvider(DefaultProviders.OpenStreetMaps);
        //jXSheepMap.getMainMap().setTileFactory(fact);

        // Use 8 threads in parallel to load the tiles
        fact.setThreadPoolSize(8);

        // Setup local file cache
        File cacheDir = new File(System.getProperty("user.home") + File.separator + ".jxmapviewer2");
        LocalResponseCache.installResponseCache(fact.getInfo().getBaseURL(), cacheDir, false);
        

        jXSheepMap.getMiniMap().setVisible(false);

        // Add interactions
        MouseInputListener mia = new PanMouseInputListener(jXSheepMap.getMainMap());
        jXSheepMap.addMouseListener(mia);
        jXSheepMap.addMouseMotionListener(mia);

        jXSheepMap.addMouseListener(new CenterMapListener(jXSheepMap.getMainMap()));

        jXSheepMap.addMouseWheelListener(new ZoomMouseWheelListenerCursor(jXSheepMap.getMainMap()));

        jXSheepMap.addKeyListener(new PanKeyListener(jXSheepMap.getMainMap()));
        
        jXSheepMap.addMouseListener(new MouseClickOnWayPointListener(this));

    }
    
    public List<Sheep> getSheepList() {
        return sheepList;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addSheep;
    private javax.swing.JButton deSelect;
    private javax.swing.JLabel idLbl2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelLastUpdate;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuFarmTools;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItemCloseProgram;
    private javax.swing.JMenuItem jMenuProperties;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private org.jdesktop.swingx.JXMapKit jXSheepMap;
    private javax.swing.JLabel lblSheepAlarm;
    private javax.swing.JLabel lblSheepBorn;
    private javax.swing.JLabel lblSheepDeceased;
    private javax.swing.JLabel lblSheepId;
    private javax.swing.JLabel lblSheepNickname;
    private javax.swing.JLabel lblSheepPosition;
    private javax.swing.JLabel lblSheepPulse;
    private javax.swing.JLabel lblSheepTemperature;
    private javax.swing.JLabel lblSheepUpdate;
    private javax.swing.JLabel lblSheepWeight;
    private javax.swing.JButton refreshbtn;
    private javax.swing.JList sheepJList;
    private javax.swing.JList sheepUpdateJList;
    private javax.swing.JTextArea taSheepComment;
    // End of variables declaration//GEN-END:variables
}
