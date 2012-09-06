/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my.sheeponthetable;

/**
 *
 * @author eliasaa
 */
public class Sheep {

    private int ID;
    private String kallenavn;
    private double[] gps_koordinat;
    private boolean pulsavvik;
    private boolean tempavvik;
    private int fodselsaar;
    private String kommentar;

    public Sheep(int ID, String kallenavn, double[] gps_koordinat,
                boolean pulssavvik, boolean tempavvik, int fodelsaar,
                String kommentar) {
        this.ID = ID;
        this.kallenavn = kallenavn;
        this.gps_koordinat = gps_koordinat;
        this.pulsavvik = pulsavvik;
        this.tempavvik = tempavvik;
        this.fodselsaar = fodselsaar;
        this.kommentar = kommentar;
    }

    public int get_ID() {
        return ID;
    }

    public String get_kallenavn() {
        return kallenavn;
    }

    public double[] get_posisjon() {
        return gps_koordinat;
    }

    public boolean get_puls() {
        return pulsavvik;
    }

    public boolean get_temperatur() {
        return tempavvik;
    }

    public int get_fodselsaar() {
        return fodselsaar;
    }

    public String get_kommentar() {
        return kommentar;
    }

    public void set_kommentar(String kommentar) {
	this.kommentar = kommentar;
    }

    public void set_kallenavn(String kallenavn) {
        this.kallenavn = kallenavn;
       }
}
