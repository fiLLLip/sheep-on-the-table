/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my.sheeponthetable.tools;

import java.util.List;

/**
 *
 * @author Filip
 */
public class Sheep {
    private int id;
    private int farmId;
    private String name;
    private String comment;
    private int born;
    private int deceased;
    private List<SheepUpdate> updates;
    
    /**
     *
     * @param id
     * @param farmId 
     * @param name
     * @param comment
     * @param born
     * @param deceased
     * @param updates
     */
    public Sheep (int id, int farmId, String name, int born, int deceased, String comment, List<SheepUpdate> updates) {
        this.id = id;
        this.farmId = farmId;
        this.name = name;
        this.comment = comment;
        this.born = born;
	this.born = deceased;
        this.updates = updates;
    }

    /**
     *
     * @return
     */
    public int getID() {
        return id;
    }

    /**
     *
     * @return
     */
    public int getFarmId() {
        return farmId;
    }

    /**
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return
     */
    public String getComment() {
        return comment;
    }

    /**
     *
     * @return
     */
    public int getBorn() {
        return born;
    }
	
	/**
     *
     * @return
     */
    public int getDeceased() {
        return born;
    }

    /**
     *
     * @return
     */
    public List<SheepUpdate> getUpdates() {
        return updates;
    }
	
    /**
     *
     * @return
     */
    public Boolean isAlive() {
        if(this.deceased == 0)
                return true;
        return false;
    }
}
