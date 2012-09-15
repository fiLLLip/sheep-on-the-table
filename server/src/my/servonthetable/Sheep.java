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
    
    /**
     *
     * @param id
     * @param farm_id
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

    public int getID() {
        return id;
    }

    public int getFarmId() {
        return farmId;
    }

    public String getName() {
        return name;
    }

    public String getComment() {
        return comment;
    }

    public int getBorn() {
        return born;
    }
	
	public int getDeceased() {
        return born;
    }

    public List<SheepUpdate> getUpdates() {
        return updates;
    }
	
	public bool isAlive() {
		if(this.deceased == null)
			return true;
		return false;
	}
}
