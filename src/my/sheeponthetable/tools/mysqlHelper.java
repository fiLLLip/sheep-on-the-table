/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my.sheeponthetable.tools;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Filip
 */
public class mysqlHelper {
    
    String dbHost =  "mysql.stud.ntnu.no";
    String dbUser = "knutela_sheep";
    String dbPass = "pwonthetable";
    String dbName = "knutela_sheep";
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
