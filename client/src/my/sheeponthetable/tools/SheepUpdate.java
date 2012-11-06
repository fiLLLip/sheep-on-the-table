/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my.sheeponthetable.tools;

/**
 *
 * @author Filip
 */
public class SheepUpdate {

    private int id;
    private double pos_x;
    private double pos_y;
    private int pulse;
    private double temperature;
    private long timestamp;
    private int alarm;

    /**
     *
     */
    public SheepUpdate() {
        this.id = -1;
        this.pos_x = -1;
        this.pos_y = -1;
        this.pulse = -1;
        this.temperature = -1.0;
        this.timestamp = 0;
        this.alarm = -1;
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
    public SheepUpdate(int id, double pos_x, double pos_y, int pulse, double temperature, int alarm, long timestamp) {
        this.id = id;
        this.pos_x = pos_x;
        this.pos_y = pos_y;
        this.pulse = pulse;
        this.temperature = temperature;
        this.timestamp = timestamp;
        this.alarm = alarm;
    }

    public int getID() {
        return id;
    }

    public Long getTimeStamp() {
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

    public double getTemp() {
        return temperature;
    }

    public boolean isAlarm() {
        return (alarm > 0);
    }

    public int getAlarm() {
        return alarm;
    }

    public void setAlarm(int alarm) {
        this.alarm = alarm;
    }

    @Override
    public String toString() {
        return "U@" + id + "@" + pos_x + "@" + pos_y + "@" + pulse + "@" + temperature + "@" + alarm + "@" + timestamp;
    }
}
