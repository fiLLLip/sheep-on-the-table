/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my.sheeponthetable.tools;

/**
 *
 * @author Filip
 */
public class sheepUpdate {
    private int id;
    private float position_x;
    private float position_y;
    private int pulse;
    private int temperature;
    private int datetime;
    
    /**
     *
     */
    public sheepUpdate () {
        this.id = -1;
        this.position_x = -1;
        this.position_y = -1;
        this.pulse = -1;
        this.temperature = -1;
        this.datetime = -1;
    }
    
    /**
     *
     * @param id 
     * @param position_x
     * @param position_y
     * @param pulse
     * @param temperature
     * @param datetime
     */
    public sheepUpdate (int id, float position_x, float position_y, int pulse, int temperature, int datetime) {
        this.id = id;
        this.position_x = position_x;
        this.position_y = position_y;
        this.pulse = pulse;
        this.temperature = temperature;
        this.datetime = datetime;
    }

    public int getID() {
        return id;
    }

    public int getTimeStamp() {
        return datetime;
    }

    public double getX() {
        return position_x;
    }

    public double getY() {
        return position_y;
    }

    public int getPuls() {
        return pulse;
    }

    public int getTemp() {
        return temperature;
    }

}
