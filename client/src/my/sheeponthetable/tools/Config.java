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
    private String tempuser = null;
    private String temppass = null;
    private String temphash = null;
    private String tempuserid = null;
    private String tempfarmid = null;
    
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
    
    public String getTempUser () {
        return this.tempuser;
    }
    
    public String getTempPass () {
        return this.temppass;
    }
    
    public String getTempHash () {
        return this.temphash;
    }
    
    public String getTempUserID () {
        return this.tempuserid;
    }
    
    public String getTempFarmID () {
        return this.tempfarmid;
    }
    
    public void setUsername (String username) {
        this.username = username;
        this.saveSettingsFile();
    }
    
    public void setPassword (String password) {
        this.password = password;
        this.saveSettingsFile();
    }
    
    public void setTempUser (String username) {
        this.tempuser = username;
        this.saveSettingsFile();
    }
    
    public void setTempPass (String password) {
        this.temppass = password;
        this.saveSettingsFile();
    }
    
    public void setTempHash (String hash) {
        this.temphash = hash;
        this.saveSettingsFile();
    }
    
    public void setTempUserID (String userid) {
        this.tempuserid = userid;
        this.saveSettingsFile();
    }
    
    public void setTempFarmID (String farmid) {
        this.tempfarmid = farmid;
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
            this.tempuser = properties.getProperty("tempname");
            this.temppass = properties.getProperty("temppass");
            this.temphash = properties.getProperty("temphash");
            this.tempuserid = properties.getProperty("tempuserid");
            this.tempfarmid = properties.getProperty("tempfarmid");
            
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
            properties.setProperty("tempname", this.tempuser);
            properties.setProperty("temppass", this.temppass);
            properties.setProperty("temphash", this.temphash);
            properties.setProperty("tempuserid", this.tempuserid);
            properties.setProperty("tempfarmid", this.tempfarmid);
            
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
