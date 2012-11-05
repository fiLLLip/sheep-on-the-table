/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my.sheeponthetable.tools;

import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import my.sheeponthetable.gui.SheepPanel;
import my.sheeponthetable.tools.map.MyWaypoint;
import org.jdesktop.swingx.JXMapViewer;

/**
 *
 * @author elias
 */
public class MouseClickOnWayPointListener implements MouseListener {

    JXMapViewer map;
    SheepPanel sp;
    
    public MouseClickOnWayPointListener(SheepPanel sp) {
        map = sp.getMapKit().getMainMap();
        this.sp = sp;
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        
        // Only care about left mouse clicks on this listener
        if (e.getButton() != 1) {
            return;
        }
        
        // Listen for clicks on waypoints
        Rectangle bounds = map.getViewportBounds();
        
        Point2D vorPoint;
        int x, y;  
        
        for (MyWaypoint w : sp.getWayPoints()) {
            vorPoint =  map.getTileFactory().geoToPixel(w.getPosition(), map.getZoom());
            x = (int) (vorPoint.getX() - bounds.getX());
            y = (int) (vorPoint.getY() - bounds.getY());            
        
            Rectangle vorBounds = new Rectangle(x-10, y-34, 20, 34);
            
            if (vorBounds.contains(e.getPoint())) {
                if (w.isSheepWaypoint()) {
                    sp.mapSelectSheep(w.getIndex());
                }
                else {
                     sp.mapSelectUpdate(w.getIndex());
                }
                // If there are more waypoints on this location, only care about
                // one of them, to make things simpler for everyone.
                break;
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
    
}
