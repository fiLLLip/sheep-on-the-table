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
    private int eierId;
    private String navn;
    private String kommentar;
    private int bornYear;
    private List<sheepUpdate> updates;
    
    /**
     *
     * @param id
     * @param eier_id
     * @param navn
     * @param kommentar
     * @param born_year
     * @param updates
     */
    public Sheep (int id, int eierId, String navn, String kommentar, int bornYear, List<sheepUpdate> updates) {
        this.id = id;
        this.eierId = eierId;
        this.navn = navn;
        this.kommentar = kommentar;
        this.bornYear = bornYear;
        this.updates = updates;
    }

    public int getID() {
        return id;
    }

    public int getEierID() {
        return eierId;
    }

    public String getNavn() {
        return navn;
    }

    public String getKommentar() {
        return kommentar;
    }

    public int getBornYear() {
        return bornYear;
    }

    public List<sheepUpdate> getUpdates() {
        return updates;
    }
}
