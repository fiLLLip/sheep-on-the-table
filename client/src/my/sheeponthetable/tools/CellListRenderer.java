/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my.sheeponthetable.tools;

import java.awt.Color;
import java.awt.Component;
import java.util.List;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import my.sheeponthetable.gui.SheepPanel;

/**
 *
 * @author elias
 */
public class CellListRenderer extends DefaultListCellRenderer {
    
    SheepPanel sp;
    
    public CellListRenderer (SheepPanel sp) {
        super();
        this.sp = sp;
    }
    
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        Component c = super.getListCellRendererComponent(list,value,index,isSelected,cellHasFocus);
        Sheep s = sp.getSheepList().get(index);
        Color colour = Color.BLACK;
        // If there are no updates, make it grey
        if (s.getUpdates().isEmpty()) {
            colour = Color.GRAY;
        }
        // If the sheep is dead, make it red
        else if (s.getDeceased()>100000) {
            colour = Color.RED;
        }
        // If the sheep is sick, make it cyan
        else if (s.getUpdates().get(0).getPulse() >  90 ||
                s.getUpdates().get(0).getPulse() < 60 ||
                s.getUpdates().get(0).getTemp() < 35 ||
                s.getUpdates().get(0).getTemp() > 45 ) {
            colour = Color.CYAN;
        }
	c.setForeground(colour);        
        return c;
	}
}