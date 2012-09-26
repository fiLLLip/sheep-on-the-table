/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my.sheeponthetable.tools;

import java.io.Serializable;

/**
 *
 * @author Filip
 */
public class SheepUpdate implements Serializable {
    private int id;
    private double pos_x;
    private double pos_y;
    private int pulse;
    private double temperature;
    private int timestamp;
    private boolean alarm;

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

    public SheepUpdate(String string) {
        System.out.println(string);
        String[] parseString = string.split("@");
        try {
            id = Integer.parseInt(parseString[1]);
            pos_x = Double.parseDouble(parseString[2]);
            pos_y = Double.parseDouble(parseString[3]);
            pulse = Integer.parseInt(parseString[4]);
            temperature = Double.parseDouble(parseString[5]);
            alarm = Boolean.getBoolean(parseString[6]);
            timestamp = Integer.parseInt(parseString[7]);
        } catch (Exception e) {
            System.err.println("Could not convert string to SheepUpdate object!");
            e.printStackTrace();
        }
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
    public SheepUpdate (int id, double pos_x, double pos_y, int pulse, double temperature, boolean alarm, int timestamp) {
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

    public double getTemp() {
        return temperature;
    }

    public boolean isAlarm() {
        return alarm;
    }

    public String toString(int sheepId) {
        return "SU@" + sheepId +"@" + id + "@" + pos_x + "@" + pos_y + "@" + pulse + "@" + temperature + "@" + alarm + "@"+ timestamp;
    }

}
