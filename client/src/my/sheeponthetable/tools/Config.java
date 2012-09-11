/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my.sheeponthetable.tools;

import java.io.IOException;
import java.util.Properties;
import javax.swing.JOptionPane;

/**
 *
 * @author gruppe 7
 */
public class Config {
    
    private String dbHost;
    private int dbPort = 3306; 
    private String dbName;
    private String dbUsername;
    private String dbPassword;
    
    /**
     *
     */
    public Config () {
        loadSettingsFile();
    }
    
    /**
     *
     * @return
     */
    public String getDBHost () {
        return this.dbHost;
    }
    
    /**
     *
     * @return
     */
    public int getDBPort () {
        return this.dbPort;
    }
    
    /**
     *
     * @return
     */
    public String getDBName () {
        return this.dbName;
    }
    
    /**
     *
     * @return
     */
    public String getDBUsername () {
        return this.dbUsername;
    }
    
    /**
     *
     * @return
     */
    public String getDBPassword () {
        return this.dbPassword;
    }
    
    private void loadSettingsFile () {
        
        Properties properties = new Properties();
        
        try {
            properties.load(getClass().getResourceAsStream("settings.properties"));
            
            /**
             * Load the following settings and assign them to class variables
             */
            
            this.dbHost = properties.getProperty("dbHost");
            this.dbPort = Integer.parseInt(properties.getProperty("dbPort"));
            this.dbUsername = properties.getProperty("dbUser");
            this.dbPassword = properties.getProperty("dbPass");
            this.dbName = properties.getProperty("dbName");
            
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Failure when reading settings: " + e.toString());
        }
    }
    
    
    
}
