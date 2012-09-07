/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my.sheeponthetable.tools;

/**
 *
 * @author Filip
 */
public class sheep {
    int id;
    int eier_id;
    String navn;
    String kommentar;
    int born_year;
    sheepUpdate[] updates;
    
    /**
     *
     * @param id
     * @param eier_id
     * @param navn
     * @param kommentar
     * @param born_year
     * @param updates
     */
    public sheep (int id, int eier_id, String navn, String kommentar, int born_year, sheepUpdate[] updates) {
        this.id = id;
        this.eier_id = eier_id;
        this.navn = navn;
        this.kommentar = kommentar;
        this.born_year = born_year;
        this.updates = updates;
    }
    
}
