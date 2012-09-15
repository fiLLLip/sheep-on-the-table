/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my.servonthetable;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Filip
 */
public class ServerMySqlHelper {
    
    private String dbHost;
    private String dbUser;
    private String dbPass;
    private String dbName;
    private int port = 3306;
    private Connection con;
    private Statement stmt;
    
    /**
     *
     * @param dbHost
     * @param dbUser
     * @param dbPass
     * @param dbName
     * @param port
     */
    public ServerMySqlHelper(String dbHost, int port, String dbUser, String dbPass, String dbName){
        this.dbHost = dbHost;
        this.dbName = dbName;
        this.dbPass = dbPass;
        this.dbUser = dbUser;
        this.port = port;
    }
    
    void connect(){
        try {
            String url = urlGenerator(this.dbHost, this.port, this.dbName);
            con = DriverManager.getConnection(url, dbUser, dbPass);
            stmt = con.createStatement();
        } catch (Exception ex) {
            Logger.getLogger(ServerMySqlHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     *
     * @return
     */
    public List<Sheep> getSheepList(int farm_id) {
        
		List<Sheep> sheeps = new ArrayList<>();
        ResultSet results;
		
		try {
            
			results = stmt.executeQuery("SELECT id, farm_id, name, UNIX_TIMESTAMP(born) as born, UNIX_TIMESTAMP(deceased) as deceased, comment FROM sheep_sheep WHERE farm_id = '" + farm_id + "' AND deceased = NULL");
            
			while(results.next()){
                List<SheepUpdate> updates = getSheepUpdates(results.getInt("id"), 1);
                sheeps.add(new Sheep(	results.getInt("id"),
										results.getInt("farm_id"),
										results.getString("name"),
										results.getInt("born"),
										results.getInt("deceased"),
										results.getString("comment"),
										updates));
            }
			
            results.close();
            return sheeps;
			
        } catch (SQLException ex) {
            Logger.getLogger(ServerMySqlHelper.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public List<SheepUpdate> getSheepUpdates(int id, int numUpdates) {
	
		List<SheepUpdate> updates = new ArrayList<>();
        ResultSet results;
	
        try {
            results = stmt.executeQuery("SELECT id, sheep_id, UNIX_TIMESTAMP(timestamp) as timestamp, pos_x, pos_y, pulse, temp, alarm FROM oppdateringer WHERE sheep_id = " + Integer.toString(id) + " LIMIT " + Integer.toString(numUpdates) + " ORDER BY id DESC");
            while(results.next()){
                updates.add(new SheepUpdate(	results.getInt("id"),
												Float.valueOf(results.getString(2).trim()),
												Float.valueOf(results.getString(3).trim()),
												Integer.parseInt(results.getString(4)),
												Integer.parseInt(results.getString(5)),
												Integer.parseInt(results.getString(6))));
            }
            results.close();
            return updates;
        } catch (SQLException ex) {
            Logger.getLogger(ServerMySqlHelper.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     *
     * @param s
     * @return
     */
    public boolean storeNewSheep(Sheep s) {
        try {
            stmt.executeQuery("INSERT INTO sau (eier_id, navn, kommentar, fodt_ar) VALUES \'" + s.getEierID() + "\',\'" + s.getNavn() + "\',\'" + s.getKommentar() + "\',\'" +  s.getBornYear() + "\'");
            /* This should not try to store updates, since new sheep doesn't have any updates
            for (sheepUpdate su : s.getUpdates()) {
                stmt.executeQuery("INSERT INTO oppdateringer (id, sau_id, timestamp, posisjon_x, posisjon_y, puls, temperatur) VALUES \'" + su.getID() + "\',\'" + s.getID() + "\',\'" + su.getTimeStamp() + "\',\'" + su.getX() + "\',\'" + su.getY() + "\',\'" + su.getPuls() + "\',\'" + su.getTemp() + "\'");
            }
            */
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ServerMySqlHelper.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    /**
     *
     * @param s
     * @return
     */
    public boolean updateSheep(Sheep s) {
        try {
            stmt.executeQuery("UPDATE sau SET eier_id=\'" + s.getEierID() + "\', navn=\'" + s.getNavn() + "\', kommentar=\'" + s.getKommentar() + "\', fodt_ar=\'" +  s.getBornYear() + "\' WHERE id=\'" + s.getID() + "\'");
            /* This should not try to store updates, since new sheep doesn't have any updates
            for (sheepUpdate su : s.getUpdates()) {
                stmt.executeQuery("INSERT INTO oppdateringer (id, sau_id, timestamp, posisjon_x, posisjon_y, puls, temperatur) VALUES \'" + su.getID() + "\',\'" + s.getID() + "\',\'" + su.getTimeStamp() + "\',\'" + su.getX() + "\',\'" + su.getY() + "\',\'" + su.getPuls() + "\',\'" + su.getTemp() + "\'");
            }
            */
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ServerMySqlHelper.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    private String urlGenerator(String host, int port, String database){
        String url;
        url = "jdbc:mysql://" + host + ":" + Integer.toString(port) + "/" + database;
        return url;
    }
    
    protected void finalize() throws Throwable
    {
      con.close();
      super.finalize();
    } 
}
