package my.sheeponthetable.tools;

import java.awt.Color;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import my.sheeponthetable.gui.SheepPanel;

/**
 * Cell Renderer responsible for giving the cells in the Sheep List the correct
 * colours.
 *
 * @author elias
 */
public class SheepListCellRenderer extends DefaultListCellRenderer {

    SheepPanel sp;

    /**
     * Creates a new SheepListCellRenderer
     * 
     * @param sp 
     */
    public SheepListCellRenderer(SheepPanel sp) {
        super();
        this.sp = sp;
    }

    @Override
    /**
     * Called when displaying the elements in the list. Basically it does what
     * a generic listCellRendererComponent would do, but adds some colour 
     * depending on the status of the sheep.
     */
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        Sheep s = sp.getSheepList().get(index);
        int status = s.getStatus();
        Color colour;
        // If there are no updates, make it grey
        if (status == -1) {
            colour = Color.GRAY;
        } // If the sheep is normal, make it black
        else if (status == 0) {
            colour = Color.BLACK;
        } // If the sheep is dead, make it red
        else if (status % 2 == 1) {
            colour = Color.RED;
        } // Otherwise, the sheep has a "minor" problem (sick or stationary)
        else {
            colour = Color.BLUE;
        }
        c.setForeground(colour);
        return c;
    }
}
