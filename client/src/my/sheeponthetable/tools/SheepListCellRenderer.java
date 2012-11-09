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
     * @param The SheepPanel on which the list we want to render is displayed.
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
        Color colour;
        // If there are no updates, make it grey
        if (s.getUpdates().isEmpty()) {
            colour = Color.GRAY;
        } // If the sheep is dead, make it red
        else if (!s.isAlive()) {
            colour = Color.RED;
        } // If the sheep is sick, make it blue
        else if (s.getUpdates().get(0).getPulse() > 90
                || s.getUpdates().get(0).getPulse() < 60
                || s.getUpdates().get(0).getTemp() < 35
                || s.getUpdates().get(0).getTemp() > 45) {
            colour = Color.BLUE;
        } // If nothing is weird about this sheep, make it normal, i.e. black.
        else {
            colour = Color.BLACK;
        }
        c.setForeground(colour);
        return c;
    }
}
