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

    public List<SheepUpdate> getUpdateList() {
        return sheepUpdateList;
    }

    public JXMapKit getMapKit() {
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
        panelSheepEdit.setVisible(false);
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
        sheepUpdateJList.setCellRenderer(new UpdateListCellRenderer(this));

        update();

        for (Map farm : WebServiceClient.farmids) {
            if (farm.get("id").equals(WebServiceClient.farmid)) {
                lblFarmName.setText(farm.get("name").toString());
            }
        }


        /**
         * ***************
         */
        /* MENU SHORTCUTS */
        if (System.getProperty("os.name").equals("Mac OS X")) {
            menuLogout.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_W, java.awt.event.InputEvent.META_MASK));
            menuCloseProgram.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.META_MASK));
            menuPrefFarm.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_COMMA, java.awt.event.InputEvent.META_MASK));
            menuPrefUser.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_PERIOD, java.awt.event.InputEvent.META_MASK));
            menuAddSheep.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.META_MASK));
            menuEditSheep.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.META_MASK));
            menuRefresh.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.META_MASK));
        } else {
            menuLogout.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_W, java.awt.event.InputEvent.CTRL_DOWN_MASK));
            menuCloseProgram.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_DOWN_MASK));
            menuPrefFarm.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_COMMA, java.awt.event.InputEvent.CTRL_DOWN_MASK));
            menuPrefUser.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_PERIOD, java.awt.event.InputEvent.CTRL_DOWN_MASK));
            menuAddSheep.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_DOWN_MASK));
            menuEditSheep.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_DOWN_MASK));
            menuRefresh.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        }

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
        } else {
            CompoundPainter<JXMapViewer> painter = new CompoundPainter<>();
            jXSheepMap.getMainMap().setOverlayPainter(painter);
        }
        sheepUpdateList = s.getUpdates();
    }

    /**
     * This method is called by the mouse listeners on the map, to handle these
     * events. What this method does is to select the shep on the list with the
     * index corresponding to the input value. This selection then fires the
     * list event listeners which call the selectSheep method.
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
     * the index corresponding to the input value. This selection then fires the
     * list event listeners which call the selectUpdate method.
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

        jMenuItem1 = new javax.swing.JMenuItem();
        panelMain = new javax.swing.JPanel();
        jSeparator3 = new javax.swing.JSeparator();
        jLabelLastUpdate = new javax.swing.JLabel();
        lblUsername = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        sheepJList = new javax.swing.JList();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jXSheepMap = new org.jdesktop.swingx.JXMapKit();
        panelSheepInfo = new javax.swing.JPanel();
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
        lblFarmName = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        sheepUpdateJList = new javax.swing.JList();
        panelSheepEdit = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        taSheepEditComment = new javax.swing.JTextArea();
        txtSheepEditId = new javax.swing.JTextField();
        txtSheepEditNickname = new javax.swing.JTextField();
        txtSheepEditWeight = new javax.swing.JTextField();
        txtSheepEditBorn = new javax.swing.JTextField();
        cbxSheepEditDead = new javax.swing.JCheckBox();
        btnSheepEditSave = new javax.swing.JButton();
        btnSheepEditCancel = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        menuFile = new javax.swing.JMenu();
        menuLogout = new javax.swing.JMenuItem();
        menuCloseProgram = new javax.swing.JMenuItem();
        menuPreferences = new javax.swing.JMenu();
        menuPrefFarm = new javax.swing.JMenuItem();
        menuPrefUser = new javax.swing.JMenuItem();
        menuSheep = new javax.swing.JMenu();
        menuAddSheep = new javax.swing.JMenuItem();
        menuEditSheep = new javax.swing.JMenuItem();
        menuRefresh = new javax.swing.JMenuItem();

        jMenuItem1.setText("jMenuItem1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));

        panelMain.setBackground(new java.awt.Color(255, 255, 255));

        jLabelLastUpdate.setText("Not Available");

        lblUsername.setText("username");

        sheepJList.setModel(sheepShow);
        jScrollPane1.setViewportView(sheepJList);

        jLabel2.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel2.setText("Last update:");

        jLabel1.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel1.setText("Logged in as:");

        panelSheepInfo.setBackground(new java.awt.Color(255, 255, 255));
        panelSheepInfo.setMaximumSize(new java.awt.Dimension(310, 350));

        jLabel7.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel7.setText("ID:");

        jLabel8.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel8.setText("Nickname:");

        jLabel9.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel9.setText("Weight:");

        jLabel10.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel10.setText("Date of death:");

        jLabel11.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel11.setText("Comment:");

        jLabel12.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel12.setText("Date of birth:");

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

        javax.swing.GroupLayout panelSheepInfoLayout = new javax.swing.GroupLayout(panelSheepInfo);
        panelSheepInfo.setLayout(panelSheepInfoLayout);
        panelSheepInfoLayout.setHorizontalGroup(
            panelSheepInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSheepInfoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelSheepInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 311, Short.MAX_VALUE)
                    .addGroup(panelSheepInfoLayout.createSequentialGroup()
                        .addGroup(panelSheepInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                            .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelSheepInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelSheepInfoLayout.createSequentialGroup()
                                .addGroup(panelSheepInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblSheepId)
                                    .addComponent(lblSheepNickname)
                                    .addComponent(lblSheepWeight)
                                    .addComponent(lblSheepBorn)
                                    .addComponent(lblSheepDeceased)
                                    .addComponent(lblSheepTemperature)
                                    .addComponent(lblSheepPulse)
                                    .addComponent(lblSheepAlarm))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(lblSheepPosition, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblSheepUpdate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        panelSheepInfoLayout.setVerticalGroup(
            panelSheepInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSheepInfoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelSheepInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(lblSheepId))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelSheepInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(lblSheepNickname))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelSheepInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(lblSheepWeight))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelSheepInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(lblSheepBorn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelSheepInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(lblSheepDeceased))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelSheepInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(lblSheepUpdate))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelSheepInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(lblSheepPosition))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelSheepInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(lblSheepTemperature))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelSheepInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(lblSheepPulse))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelSheepInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(lblSheepAlarm))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        lblFarmName.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        lblFarmName.setText("Navnet til Sauefarmen");

        jLabel19.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        jLabel19.setText("Sheeps:");

        sheepUpdateJList.setModel(sheepUpdatesShow);
        jScrollPane2.setViewportView(sheepUpdateJList);

        panelSheepEdit.setBackground(new java.awt.Color(255, 255, 255));
        panelSheepEdit.setMaximumSize(new java.awt.Dimension(323, 350));
        panelSheepEdit.setMinimumSize(new java.awt.Dimension(323, 350));

        jLabel18.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel18.setText("ID:");

        jLabel20.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel20.setText("Nickname:");

        jLabel21.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel21.setText("Weight:");

        jLabel23.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel23.setText("Comment:");

        jLabel24.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel24.setText("Date of birth:");

        taSheepEditComment.setColumns(20);
        taSheepEditComment.setLineWrap(true);
        taSheepEditComment.setRows(5);
        taSheepEditComment.setMaximumSize(new java.awt.Dimension(240, 80));
        taSheepEditComment.setMinimumSize(new java.awt.Dimension(240, 80));
        jScrollPane4.setViewportView(taSheepEditComment);

        txtSheepEditId.setEditable(false);

        cbxSheepEditDead.setText("Check this box if the sheep is dead.");
        cbxSheepEditDead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxSheepEditDeadActionPerformed(evt);
            }
        });

        btnSheepEditSave.setText("Save changes");
        btnSheepEditSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSheepEditSaveActionPerformed(evt);
            }
        });

        btnSheepEditCancel.setText("Discard changes");
        btnSheepEditCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSheepEditCancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelSheepEditLayout = new javax.swing.GroupLayout(panelSheepEdit);
        panelSheepEdit.setLayout(panelSheepEditLayout);
        panelSheepEditLayout.setHorizontalGroup(
            panelSheepEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSheepEditLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelSheepEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbxSheepEditDead, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 311, Short.MAX_VALUE)
                    .addGroup(panelSheepEditLayout.createSequentialGroup()
                        .addGroup(panelSheepEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                            .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelSheepEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtSheepEditId)
                            .addComponent(txtSheepEditNickname, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 185, Short.MAX_VALUE)
                            .addComponent(txtSheepEditWeight, javax.swing.GroupLayout.DEFAULT_SIZE, 185, Short.MAX_VALUE)
                            .addComponent(txtSheepEditBorn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 185, Short.MAX_VALUE)))
                    .addGroup(panelSheepEditLayout.createSequentialGroup()
                        .addComponent(btnSheepEditSave)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnSheepEditCancel)))
                .addContainerGap())
        );
        panelSheepEditLayout.setVerticalGroup(
            panelSheepEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSheepEditLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelSheepEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(txtSheepEditId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelSheepEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(txtSheepEditNickname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelSheepEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(txtSheepEditWeight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelSheepEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(txtSheepEditBorn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbxSheepEditDead)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel23)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(panelSheepEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSheepEditSave)
                    .addComponent(btnSheepEditCancel))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panelMainLayout = new javax.swing.GroupLayout(panelMain);
        panelMain.setLayout(panelMainLayout);
        panelMainLayout.setHorizontalGroup(
            panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMainLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelMainLayout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblFarmName, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelMainLayout.createSequentialGroup()
                        .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblUsername, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabelLastUpdate, javax.swing.GroupLayout.DEFAULT_SIZE, 211, Short.MAX_VALUE)))
                    .addComponent(panelSheepInfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 316, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panelSheepEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jXSheepMap, javax.swing.GroupLayout.DEFAULT_SIZE, 778, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelMainLayout.setVerticalGroup(
            panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelMainLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jXSheepMap, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panelMainLayout.createSequentialGroup()
                        .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblFarmName)
                            .addComponent(jLabel19))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelMainLayout.createSequentialGroup()
                                .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel1)
                                    .addComponent(lblUsername))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabelLastUpdate))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(panelSheepInfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(panelSheepEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE))
                            .addComponent(jScrollPane1))))
                .addGap(14, 14, 14))
        );

        menuFile.setText("File");

        menuLogout.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_W, java.awt.event.InputEvent.META_MASK));
        menuLogout.setText("Log out");
        menuLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuLogoutActionPerformed(evt);
            }
        });
        menuFile.add(menuLogout);

        menuCloseProgram.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.META_MASK));
        menuCloseProgram.setText("Close program");
        menuCloseProgram.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuCloseProgramActionPerformed(evt);
            }
        });
        menuFile.add(menuCloseProgram);

        jMenuBar1.add(menuFile);

        menuPreferences.setText("Preferences");

        menuPrefFarm.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_COMMA, java.awt.event.InputEvent.META_MASK));
        menuPrefFarm.setText("Farm");
        menuPrefFarm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuPrefFarmActionPerformed(evt);
            }
        });
        menuPreferences.add(menuPrefFarm);

        menuPrefUser.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_PERIOD, java.awt.event.InputEvent.META_MASK));
        menuPrefUser.setText("User");
        menuPrefUser.setActionCommand("UserProperties");
        menuPrefUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuPrefUserActionPerformed(evt);
            }
        });
        menuPreferences.add(menuPrefUser);

        jMenuBar1.add(menuPreferences);

        menuSheep.setText("Sheep");

        menuAddSheep.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.META_MASK));
        menuAddSheep.setText("Add new sheep");
        menuAddSheep.setToolTipText("");
        menuAddSheep.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuAddSheepActionPerformed(evt);
            }
        });
        menuSheep.add(menuAddSheep);

        menuEditSheep.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.META_MASK));
        menuEditSheep.setText("Edit sheep");
        menuEditSheep.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuEditSheepActionPerformed(evt);
            }
        });
        menuSheep.add(menuEditSheep);

        menuRefresh.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.META_MASK));
        menuRefresh.setText("Refresh Sheeplist");
        menuRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuRefreshActionPerformed(evt);
            }
        });
        menuSheep.add(menuRefresh);

        jMenuBar1.add(menuSheep);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelMain, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void menuLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuLogoutActionPerformed
        // TODO add your handling code here:
        this.setVisible(false);
        new PasswordScreen().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_menuLogoutActionPerformed

    private void menuCloseProgramActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuCloseProgramActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_menuCloseProgramActionPerformed

    private void menuPrefFarmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuPrefFarmActionPerformed
        // this.setVisible(false);
        new FarmTools(this, farmID).setVisible(true);// TODO add your handling code here:
    }//GEN-LAST:event_menuPrefFarmActionPerformed

    private void menuPrefUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuPrefUserActionPerformed
        new UserProperties(this).setVisible(true);
    }//GEN-LAST:event_menuPrefUserActionPerformed

    private void menuAddSheepActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuAddSheepActionPerformed
        new AddNewSheep(this).setVisible(true);
    }//GEN-LAST:event_menuAddSheepActionPerformed

    private void menuRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuRefreshActionPerformed
        update();
    }//GEN-LAST:event_menuRefreshActionPerformed

    private void menuEditSheepActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuEditSheepActionPerformed
        if(!isEditingSheep && sheepJList.getSelectedIndex() != -1) {
            // Sets the proper values of the textboxes in the Edit Sheep Panel
            setEditPanelInfo();
            panelSheepInfo.setVisible(false);
            panelSheepEdit.setVisible(true);
            isEditingSheep = true;
        } else {
            panelSheepEdit.setVisible(false);
            panelSheepInfo.setVisible(true);
            isEditingSheep = false;
        }
    }//GEN-LAST:event_menuEditSheepActionPerformed

    private void cbxSheepEditDeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxSheepEditDeadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxSheepEditDeadActionPerformed

    /**
     * Save the changes to the sheep
     * 
     * @param evt 
     */
    private void btnSheepEditSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSheepEditSaveActionPerformed
        
        
        
    }//GEN-LAST:event_btnSheepEditSaveActionPerformed

    private void btnSheepEditCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSheepEditCancelActionPerformed

        // call the menuEdit keystroke action
        menuEditSheepActionPerformed(null);
        
    }//GEN-LAST:event_btnSheepEditCancelActionPerformed

  
    /**
     * Deselects the selection of sheep
     */
    private void deSelect() {
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
    }

    /**
     * updates the sheep list
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
                if (!sheep.getUpdates().isEmpty()) {
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
            GeoPosition oppdal = new GeoPosition(62.573611, 9.608889);
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
        jXSheepMap.getMainMap().setZoom(jXSheepMap.getMainMap().getZoom() + 1);
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
    private javax.swing.JButton btnSheepEditCancel;
    private javax.swing.JButton btnSheepEditSave;
    private javax.swing.JCheckBox cbxSheepEditDead;
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
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelLastUpdate;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator3;
    private org.jdesktop.swingx.JXMapKit jXSheepMap;
    private javax.swing.JLabel lblFarmName;
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
    private javax.swing.JLabel lblUsername;
    private javax.swing.JMenuItem menuAddSheep;
    private javax.swing.JMenuItem menuCloseProgram;
    private javax.swing.JMenuItem menuEditSheep;
    private javax.swing.JMenu menuFile;
    private javax.swing.JMenuItem menuLogout;
    private javax.swing.JMenuItem menuPrefFarm;
    private javax.swing.JMenuItem menuPrefUser;
    private javax.swing.JMenu menuPreferences;
    private javax.swing.JMenuItem menuRefresh;
    private javax.swing.JMenu menuSheep;
    private javax.swing.JPanel panelMain;
    private javax.swing.JPanel panelSheepEdit;
    private javax.swing.JPanel panelSheepInfo;
    private javax.swing.JList sheepJList;
    private javax.swing.JList sheepUpdateJList;
    private javax.swing.JTextArea taSheepComment;
    private javax.swing.JTextArea taSheepEditComment;
    private javax.swing.JTextField txtSheepEditBorn;
    private javax.swing.JTextField txtSheepEditId;
    private javax.swing.JTextField txtSheepEditNickname;
    private javax.swing.JTextField txtSheepEditWeight;
    // End of variables declaration//GEN-END:variables

    private void setEditPanelInfo() {
        
        Sheep selectedSheep = sheepList.get(sheepJList.getSelectedIndex());
        
        txtSheepEditId.setText(Integer.toString(selectedSheep.getID()));
        txtSheepEditNickname.setText(selectedSheep.getName());
        txtSheepEditWeight.setText(Double.toString(selectedSheep.getWeight()));
        
        if(selectedSheep.isDead()) {
            cbxSheepEditDead.setSelected(true);
            cbxSheepEditDead.setEnabled(false);
        }
        
        taSheepEditComment.setText(selectedSheep.getComment());
    }
}
