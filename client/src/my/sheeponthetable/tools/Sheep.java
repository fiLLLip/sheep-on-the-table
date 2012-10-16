/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my.sheeponthetable.tools;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Filip
 */
public class Sheep implements Serializable {
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
     */
    
    public Sheep() {
        this.id = -1;
        this.farmId = -1;
        this.name = "n0ll";
        this.comment = "n0ll";
        this.born = -1;
	this.deceased = -1;
        this.updates = null;
        this.weight = -1;
    }

    /**
     * Parses the parameter string and initializes Sheep.
     * 
     * @param String string: Expects @-splittet string
     */
    public Sheep(String string) {
        String[] parseString = string.split("@");
        try {
            id = Integer.parseInt(parseString[1]);
            farmId = Integer.parseInt(parseString[2]);
            name = parseString[3];
            comment = parseString[4];
            born = Integer.parseInt(parseString[5]);
            deceased = Integer.parseInt(parseString[6]);
            weight = Double.parseDouble(parseString[7]);
            updates = new ArrayList();
        } catch (Exception e) {
            System.err.println("Could not convert this string to a sheep object!");
            e.printStackTrace();
        }
    }
    
    /**
     * Initializes class Sheep
     * 
     * @param int id
     * @param int farmId
     * @param String name
     * @param int born
     * @param int deceased
     * @param String comment
     * @param List<SheepUpdate> updates
     * @param double weight
     */
    public Sheep (int id, int farmId, String name, int born, int deceased, String comment, List<SheepUpdate> updates, double weight) {
        this.id = id;
        this.farmId = farmId;
        this.name = name;
        this.comment = comment;
        this.born = born;
	this.deceased = deceased;
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
     * @return the unix timestamp of when sheep was born
     */
    public double getWeight() {
        return weight;
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
    
    /**
     *
     * @param d
     */
    public void setID(int d) {
        this.id = d;
    }

    /**
     *
     * @param e
     */
    public void setEierID(int e) {
        this.farmId = e;
    }

    /**
     *
     * @param n
     */
    public void setNavn(String n) {
        this.name = n;
    }

    /**
     *
     * @param k
     */
    public void setKommentar(String k) {
         this.comment = k;
    }

    /**
     *
     * @param i
     */
    public void setBornYear(int i) {
        this.born = i;
    }
     /**
     *
     * @param w
     */
    public void setWeight(double w) {
        this.weight = w;
    }
    
    /**
    *
    * @param d
    */
    public void setDeceaced(int d) {
        this.deceased = d;
    }

    /**
     *
     * @param updates
     */
    public void setUpdates (List<SheepUpdate> updates) {
        this.updates = updates;
    }

    /**
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
     *
     * @return a Boolean parsed from 
     */
    public Boolean isAlive() {
        if(this.deceased == 0)
                return true;
        return false;
    }

    /**
     *
     * @param includeUpdates
     * @return
     */
    public String toString(boolean includeUpdates) {
        String s = "S@" + id + "@" + farmId + "@" + name + "@" + comment + "@" + born + "@" + deceased + "@" + weight;
        if (includeUpdates) {
            for (SheepUpdate su : updates) {
                s += "\n" + su.toString();
            }
        }
        return s;
    }
}
