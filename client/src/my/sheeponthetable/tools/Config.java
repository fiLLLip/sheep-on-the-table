/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my.sheeponthetable.tools;

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
        //TODO: read file and assign 
        readConfigFile();
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
    
    private void readConfigFile () {
        
    }
    
}
