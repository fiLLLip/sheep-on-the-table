/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my.sheeponthetable.tools;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Filip
 */
public class mysqlHelper {
    
    String dbHost;
    String dbUser;
    String dbPass;
    String dbName;
    int port = 3306;
    Connection con;
    Statement stmt;
    
    /**
     *
     * @param dbHost
     * @param dbUser
     * @param dbPass
     * @param dbName
     * @param port
     */
    public mysqlHelper(String dbHost, int port, String dbUser, String dbPass, String dbName){
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
            Logger.getLogger(mysqlHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public List<sheep> getSheepList() {
        List<sheep> sheeps = new ArrayList<sheep>();
        try{
            ResultSet results = null;
            results = stmt.executeQuery("SELECT id, eier_id, navn, kommentar, fodt_ar FROM sau WHERE eier_id = 1");
            while(results.next()){
                List<sheepUpdate> updates = new ArrayList<sheepUpdate>();
                sheeps.add(new sheep(Integer.parseInt(results.getString(1)), Integer.parseInt(results.getString(2)), results.getString(3), results.getString(4), Integer.parseInt(results.getString(5)), updates));
            }
            return sheeps;
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return sheeps;
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
