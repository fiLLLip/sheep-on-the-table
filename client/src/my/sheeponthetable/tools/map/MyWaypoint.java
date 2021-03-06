package my.sheeponthetable.tools.map;

import java.awt.Color;
import org.jdesktop.swingx.mapviewer.DefaultWaypoint;
import org.jdesktop.swingx.mapviewer.GeoPosition;

/**
 * A waypoint that also has a color and a label.
 *
 * This class is based on the Waypoint implementation used in one of the
 * examples from JxMapViewer2.
 *
 * @author Gruppe7
 */
public class MyWaypoint extends DefaultWaypoint {

    private final String label;
    private final Color color;
    private final boolean sheep;
    private final int index;

    /**
     * Constructor for Waypoints
     *
     * @param label
     * @param color
     * @param coord
     * @param sheep
     * @param index
     */
    public MyWaypoint(String label, Color color, GeoPosition coord, int index, boolean sheep) {
        super(coord);
        this.index = index;
        this.label = label;
        this.color = color;
        this.sheep = sheep;
    }

    /**
     * Returns the label text
     *
     * @return
     */
    public String getLabel() {
        return label;
    }

    /**
     * Returns the color
     *
     * @return
     */
    public Color getColor() {
        return color;
    }

    /**
     * Returns true if this is a sheep waypoint, false if it is a update
     * waypoint
     *
     * @return
     */
    public boolean isSheepWaypoint() {
        return sheep;
    }

    /**
     * Returns the index of corresponding sheep or sheep update
     *
     * @return
     */
    public int getIndex() {
        return index;
    }
}