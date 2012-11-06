package my.sheeponthetable.tools;

import java.awt.Color;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import my.sheeponthetable.gui.SheepPanel;

/**
 * Cell Renderer responsible for giving the cells in the Update List the correct
 * colours.
 * 
 * @author elias
 */
public class UpdateListCellRenderer extends DefaultListCellRenderer {
    
    SheepPanel sp;
    
    public UpdateListCellRenderer (SheepPanel sp) {
        super();
        this.sp = sp;
    }
    
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        Component c = super.getListCellRendererComponent(list,value,index,isSelected,cellHasFocus);
        SheepUpdate su = sp.getUpdateList().get(index);
        Color colour;
        // If this is a warning, make it red
        if (su.isAlarm()) {
            colour = Color.RED;
        }
        // If the sheep is sick, make it blue
        else if (su.getPulse() >  90 || su.getPulse() < 60 ||
                su.getTemp() < 35 || su.getTemp() > 45 ) {
            colour = Color.BLUE;
        }
        else {
            colour = Color.BLACK;
        }
	c.setForeground(colour);        
        return c;
     }
}