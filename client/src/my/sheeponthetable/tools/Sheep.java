package my.sheeponthetable.tools;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The Sheep class implements the program-internal representation of a sheep.
 *
 * @author Filip
 */
public class Sheep {

    private int id;
    private int farmId;
    private String name;
    private String comment;
    private long born;
    private long deceased;
    private double weight;
    private List<SheepUpdate> updates;

    /**
     * Creates a new instance of the class sheep by specifying the information
     * about the sheep.
     *
     * @param id
     * @param born
     * @param farmId
     * @param updates
     * @param name
     * @param comment
     * @param deceased
     * @param weight
     */
    public Sheep(int id, int farmId, String name, long born, long deceased, String comment, List<SheepUpdate> updates, double weight) {
        this.id = id;
        this.farmId = farmId;
        this.name = name;
        this.comment = comment;
        this.born = born;
        this.deceased = deceased;
        this.weight = weight;
        this.updates = updates;
    }

    /**
     * Get the ID field.
     *
     * @return
     */
    public int getID() {
        return id;
    }

    /**
     * Gets the FarmID field.
     *
     * @return
     */
    public int getFarmId() {
        return farmId;
    }

    /**
     * Gets the name of the sheep.
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the comment field.
     *
     * @return
     */
    public String getComment() {
        return comment;
    }

    /**
     * Gets the unix timestamp of when sheep was born.
     *
     * @return
     */
    public Date getBorn() {
        Date formattedBorn = new Date(born);
        return formattedBorn;
    }

    /**
     * Gets the weight field.
     *
     * @return
     */
    public double getWeight() {
        return weight;
    }

    /**
     * Gets the unix timestamp of when sheep was deceased.
     *
     * @return
     */
    public Date getDeceased() {
        Date formattedDeceased = new Date(deceased);
        return formattedDeceased;
    }

    /**
     * Gets the list of updates associated with this sheep.
     *
     * @return
     */
    public List<SheepUpdate> getUpdates() {
        return updates;
    }

    /**
     * Sets the id field.
     *
     * @param id
     */
    public void setID(int id) {
        this.id = id;
    }

    /**
     * Sets the farm ID field
     *
     * @param id
     */
    public void setFarmID(int id) {
        farmId = id;
    }

    /**
     * Sets the name field.
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the comment field.
     *
     * @param comment
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * Sets the born date of the sheep. Make sure that the sheep is not given a
     * value that is older than it's deceased field, otherwise it will be
     * registered as dead.
     *
     * @param date
     */
    public void setBorn(Date date) {
        born = date.getTime();
    }

    /**
     * Sets the weight of the sheep
     *
     * @param w
     */
    public void setWeight(double w) {
        this.weight = w;
    }

    /**
     * Set the deceased value of the sheep. If the sheep is to be registered as
     * alive, set the deceased value to "1st Jan 1970 00:00:00"
     *
     * @param date
     */
    public void setDeceased(Date date) {
        deceased = date.getTime();
    }

    /**
     * Sets the SheepUpdate list in the sheep object. The program depends in
     * many places on the fact that this list is sorted by timestamp, with the
     * newest update having the lowest index. Therefore: Make sure that the list
     * is sorted before calling this method.
     *
     * @param updates
     */
    public void setUpdates(List<SheepUpdate> updates) {
        this.updates = updates;
    }

    /**
     * Store an additional sheep update in the sheep's update list. This method
     * should only be called if it can be guaranteed that the new update has a
     * newer timestamp than the previous newest update, thus making sure that
     * the update list remains sorted.
     *
     * If this cannot be guaranteed, use getUpdates(), add the update to the
     * list, sort the list, and then use setUpdates()
     *
     * @param su
     */
    public void addUpdate(SheepUpdate su) {
        if (updates == null) {
            updates = new ArrayList<>();
        }
        updates.add(su);
    }

    /**
     * Returns true if the sheep is dead. In terms of data specifications, this
     * is when the deceased time is set to be after the born time. Living sheep
     * should normally have their deceased field set to "1st Jan 1970 00:00:00"
     *
     * @return
     */
    public Boolean isAlive() {
        if (deceased <= born) {
            return true;
        }
        return false;
    }

    /**
     * Get status of the sheep. If the sheeps has any updates, it returns the
     * status of the newest update. If the sheep doesn't have any updates, it
     * returns -1.
     *
     * @return
     */
    public int getStatus() {
        if (getUpdates().isEmpty()) {
            return -1;
        } else {
            return getUpdates().get(0).getAlarm();
        }
    }
}
