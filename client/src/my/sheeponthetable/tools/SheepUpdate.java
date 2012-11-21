package my.sheeponthetable.tools;

/**
 * A SheepUpdate is a data structure storing the chunk of information that a
 * single real-world sheep would send at one time. That is the position of the
 * sheep and the physical conditions of the sheep at the time. Also is signalled
 * whether this update is an alarm or not.
 *
 * Finally, the server also attaches a timestamp to every update, so that the
 * client can know which updates is the newest, and use that information to plot
 * the sheep on the map.
 *
 * Also note that the SheepUpdate object does not contain any information about
 * which sheep it belongs to. The Sheep objects, on the other hand, contain
 * references to the SheepUpdates associated with it.
 *
 * @author Gruppe 7
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
     * Creates a new SheepUpdate by specifying all the information associated
     * with it.
     *
     * @param id
     * @param pos_x
     * @param alarm
     * @param pulse
     * @param pos_y
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

    /**
     * Gets the database ID of the SheepUpdate
     *
     * @return
     */
    public int getID() {
        return id;
    }

    /**
     * Gets the timestamp.
     *
     * @return
     */
    public Long getTimeStamp() {
        return timestamp;
    }

    /**
     * Gets the longitudinal position.
     *
     * @return
     */
    public double getX() {
        return pos_x;
    }

    /**
     * Gets the latitudinal position.
     *
     * @return
     */
    public double getY() {
        return pos_y;
    }

    /**
     * Gets the pulse value of the update.
     *
     * @return
     */
    public int getPulse() {
        return pulse;
    }

    /**
     * Gets the temperature value of the update.
     *
     * @return
     */
    public double getTemp() {
        return temperature;
    }

    /**
     * Returns true if this update was an alarm, false otherwise. For
     * information about which kind of alarm it is, use getAlarm().
     *
     * @return
     */
    public boolean isAlarm() {
        return (alarm > 0);
    }

    /**
     * Gets the alarm status of the update. The status is the sum of the
     * following flags:
     *
     * 1 - the update is an alarm, the sheep is dead 2 - the sheep has abnormal
     * physiological traits. It is most likely sick. 4 - the sheep has remained
     * stationary of at least 24 hours.
     *
     * Because of the binary encoding, it is possible to identify exactly which
     * kinds of alarms are triggered in the update.
     *
     * Example: Alarm 5 = 4 + 1, the sheep is thus dead and stationary.
     *
     * @return
     */
    public int getAlarm() {
        return alarm;
    }

    /**
     * Sets that alarm field of the update. 0 is no alarm, while different
     * positive integers correspond to different kinds of alarms.
     *
     * @param alarm
     */
    public void setAlarm(int alarm) {
        this.alarm = alarm;
    }
}
