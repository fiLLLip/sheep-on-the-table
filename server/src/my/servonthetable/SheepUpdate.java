/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my.servonthetable;

/**
 *
 * @author Filip
 */
public class SheepUpdate {
    private int id;
	private int sheep_id;
    private float pos_x;
    private float pos_y;
    private int pulse;
    private double temperature;
    private int timestamp;
	private int alarm;
    
    /**
     *
     */
    public SheepUpdate () {
        this.id = -1;
        this.pos_x = -1;
        this.pos_y = -1;
        this.pulse = -1;
        this.temperature = -1.0;
        this.timestamp = -1;
    }
    
    /**
     *
     * @param id 
     * @param pos_x
     * @param pos_y
     * @param pulse
     * @param temperature
     * @param timestamp
     */
    public SheepUpdate (int id, float pos_x, float pos_y, int pulse, double temperature, int timestamp) {
        this.id = id;
        this.pos_x = pos_x;
        this.pos_y = pos_y;
        this.pulse = pulse;
        this.temperature = temperature;
        this.timestamp = timestamp;
    }

    public int getID() {
        return id;
    }

    public int getTimeStamp() {
        return timestamp;
    }

    public double getX() {
        return pos_x;
    }

    public double getY() {
        return pos_y;
    }

    public int getPulse() {
        return pulse;
    }

    public int getTemp() {
        return temperature;
    }

}
