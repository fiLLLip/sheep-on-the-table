package my.sheeponthetable.tools.map;

import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import my.sheeponthetable.gui.SheepPanel;
import org.jdesktop.swingx.JXMapViewer;

/**
 * Mouse Listener to handle clicking events on the map. To be precise, this 
 * listener checks whether left mouse clicks hits any waypoint.
 * 
 * @author elias
 */
public class MouseClickOnWayPointListener implements MouseListener {

    JXMapViewer map;
    SheepPanel sp;

    /**
     * Create a new MouseClickOnWayPointListener
     * 
     * @param sp 
     */
    public MouseClickOnWayPointListener(SheepPanel sp) {
        map = sp.getMapKit().getMainMap();
        this.sp = sp;
    }

    @Override
    /**
     * Fired when any mouse button is clicked on the map
     */
    public void mouseClicked(MouseEvent e) {
        // This listener only cares about left mouse clicks. 
        if (e.getButton() != 1) {
            return;
        }

        // Check whether the click hits a waypoint
        Rectangle bounds = map.getViewportBounds();

        Point2D vorPoint;
        int x, y;

        for (MyWaypoint w : sp.getWayPoints()) {
            vorPoint = map.getTileFactory().geoToPixel(w.getPosition(), map.getZoom());
            x = (int) (vorPoint.getX() - bounds.getX());
            y = (int) (vorPoint.getY() - bounds.getY());

            Rectangle vorBounds = new Rectangle(x - 10, y - 34, 20, 34);

            if (vorBounds.contains(e.getPoint())) {
                if (w.isSheepWaypoint()) {
                    sp.mapSelectSheep(w.getIndex());
                } else {
                    sp.mapSelectUpdate(w.getIndex());
                }
                // If there are more waypoints on this location, only care about
                // one of them, to make things simpler for everyone. You can't
                // have multiple sheep selected at any given time, anyhow.
                break;
            }
        }
    }

    @Override
    /**
     * Ignore mousePressed events
     */
    public void mousePressed(MouseEvent e) {
    }

    @Override
    /**
     * Ignore mouseReleased events
     */
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    /**
     * Ignore mouseEntered events
     */
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    /**
     * Ignore mouseExited events
     */
    public void mouseExited(MouseEvent e) {
    }
}
