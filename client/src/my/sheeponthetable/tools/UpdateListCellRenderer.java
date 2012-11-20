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

    /**
     * Creates a new UpdateListCellRenderer
     *
     * @param sp
     */
    public UpdateListCellRenderer(SheepPanel sp) {
        super();
        this.sp = sp;
    }

    @Override
    /**
     * Called when displaying the elements in the list. Basically it does what a
     * generic listCellRendererComponent would do, but adds some colour
     * depending on the status of the sheep in the given update.
     */
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        // Sometimes the program tries to draw a cell that has no corresponding
        // sheep update. Surround by a try{} block to avoid ArrayIndexOutOfBounds
        // exceptions. 
        try {
            SheepUpdate su = sp.getUpdateList().get(index);
            int status = su.getAlarm();
            Color colour;
            // If the sheep is dead, make it red
            if (status % 2 == 1) {
                colour = Color.RED;
            } // If the sheep is normal, make it black
            else if (status == 0) {
                colour = Color.BLACK;
            } // Otherwise, the sheep has a "minor" problem (sick or stationary)
            else {
                colour = Color.BLUE;
            }
            c.setForeground(colour);
        } finally {
            return c;
        }
    }
}