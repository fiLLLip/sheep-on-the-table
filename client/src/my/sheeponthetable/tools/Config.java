/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my.sheeponthetable.tools;

import java.io.*;
import java.net.URL;
import java.util.Properties;
import javax.swing.JOptionPane;

/**
 *
 * @author gruppe 7
 */
public class Config {
    
    private String serverURL = null;
    private String username = null;
    private String password = null;
    
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
    public String getServerURL () {
        return this.serverURL;
    }
    
    public String getUsername () {
        return this.username;
    }
    
    public String getPassword () {
        return this.password;
    }
    
    public void setUsername (String username) {
        this.username = username;
        this.saveSettingsFile();
    }
    
    public void setPassword (String password) {
        this.password = password;
        this.saveSettingsFile();
    }
    
    public void loadSettingsFile () {
        
        Properties properties = new Properties();
        
        try {
            properties.load(getClass().getResourceAsStream("settings.properties"));
            
            /**
             * Load the following settings and assign them to class variables
             */
            
            this.serverURL = properties.getProperty("serverURL");
            this.username = properties.getProperty("username");
            this.password = properties.getProperty("password");
            
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Failure when reading settings: " + e.toString());
        }
    }
    
    private void saveSettingsFile () {
        
        Properties properties = new Properties();
        
        try {
            properties.load(getClass().getResourceAsStream("settings.properties"));
            
            /**
             * Load the following settings and assign them to class variables
             */
            
            properties.setProperty("serverURL", this.serverURL);
            properties.setProperty("username", this.username);
            properties.setProperty("password", this.password);
            
            URL url = getClass().getResource("settings.properties");  
            String path = url.getPath(); 
            String comments = "# This is the config file where all settings should be.\n" +
            "# Follow usual .properties-annotation\n" +
            "# http://en.wikipedia.org/wiki/.properties\n" +
            "# ";
            Writer writer = new FileWriter(path);  
            properties.store(writer, comments);
            
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Failure when saving settings: " + e.toString());
        }
    }
    
    
    
}
