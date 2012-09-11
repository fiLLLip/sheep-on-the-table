/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my.servonthetable;

import java.sql.*;
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
     * @param s
     * @return
     */
    public boolean storeNewSheep() {
        try {
            stmt.executeQuery("SQLQUERY");
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
