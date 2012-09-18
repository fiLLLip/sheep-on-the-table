/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my.servonthetable;

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
    private double weight;
    
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
    
    public Sheep() {
        this.id = -1;
        this.farmId = -1;
        this.name = "n0ll";
        this.comment = "n0ll";
        this.born = -1;
	this.born = -1;
        this.updates = null;
        this.weight = -1;
    }
    
    public Sheep (int id, int farmId, String name, int born, int deceased, String comment, List<SheepUpdate> updates, double weight) {
        this.id = id;
        this.farmId = farmId;
        this.name = name;
        this.comment = comment;
        this.born = born;
	this.born = deceased;
        this.updates = updates;
        this.weight = weight;
    }

    /**
     *
     * @return sheep id
     */
    public int getID() {
        return id;
    }

    /**
     *
     * @return the farm id where the sheep belongs
     */
    public int getFarmId() {
        return farmId;
    }

    /**
     *
     * @return the name of the sheep, if any
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return the comment (if it's pregnant, or hurt, or something)
     */
    public String getComment() {
        return comment;
    }

    /**
     *
     * @return the unix timestamp of when sheep was born
     */
    public int getBorn() {
        return born;
    }
	
	/**
     *
     * @return the unix timestamp of when sheep was deceased
     */
    public int getDeceased() {
        return deceased;
    }

    /**
     *
     * @return the list of updates for the sheep.
     */
    public List<SheepUpdate> getUpdates() {
        return updates;
    }
    
    public void setID(int d) {
        this.id = d;
    }

    public void setEierID(int e) {
        this.farmId = e;
    }

    public void setNavn(String n) {
        this.name = n;
    }

    public void setKommentar(String k) {
         this.comment = k;
    }

    public void setBornYear(int i) {
        this.born = i;
    }

    public void setUpdates (List<SheepUpdate> updates) {
        this.updates = updates;
    }
	
    /**
     *
     * @return a Boolean parsed from 
     */
    public Boolean isAlive() {
        if(this.deceased == 0)
                return true;
        return false;
    }

}
