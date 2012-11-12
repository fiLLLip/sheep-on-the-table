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
     * Creates a new SheepUpdate by specifying all the information associated 
     * with it.
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
     */
    public int getID() {
        return id;
    }

    /**
     * Gets the timestamp. 
     */
    public Long getTimeStamp() {
        return timestamp;
    }

    /**
     * Gets the longitudinal position.
     */
    public double getX() {
        return pos_x;
    }

    /**
     * Gets the latitudinal position.
     */
    public double getY() {
        return pos_y;
    }

    /**
     * Gets the pulse value of the update.
     */
    public int getPulse() {
        return pulse;
    }

    /**
     * Gets the temperature value of the update.
     */
    public double getTemp() {
        return temperature;
    }

    /**
     * Returns true if this update was an alarm, false otherwise. For information
     * about which kind of alarm it is, use getAlarm().
     */
    public boolean isAlarm() {
        return (alarm > 0);
    }

    /**
     * Returns the alarm value of the update. This is more fine-grained than
     * isAlarm() as it states the alarm type. To only check whether the update
     * is an alarm or not, use isAlarm().
     */
    public int getAlarm() {
        return alarm;
    }

    /**
     * Sets that alarm field of the update. 0 is no alarm, while different 
     * positive integers correspond to different kinds of alarms.
     */
    public void setAlarm(int alarm) {
        this.alarm = alarm;
    }
    
        
    /**
     * Gets the status of the update. The status is found accordingly:
     * 
     *  0 - this update is an alarm, and the sheep is therefore dead
     *  1 - the sheep is alive and healthy in this update
     *  2 - the sheep is alive, but sick (abnormal pulse or temperature)
     */
    public int getStatus() {
        // First check for death
        if (isAlarm()) {
            return 0;
        }
        // Then check for disease
        else if (getPulse() > 90 || getPulse() < 60
                || getTemp() < 35 || getTemp() > 45) {
            return 2;
        }
        // Otherwise, the sheep is safe and sound. 
        else {
            return 1;
        }
    }
}
