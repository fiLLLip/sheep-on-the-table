/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my.sheeponthetable.tools;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Gruppe 7
 */
public class ServerConnector {
    
    /**
     * Declaring private variables for functions.
     */
    private String host;
    private int port;
    private String username;
    private String password;
    private Socket socket;
    private String logger;
    private PrintWriter out;
    private BufferedReader in;
    private boolean connected = false;
    private int userID;
    private int farmID;
    private String farmName;

    /**
     *
     * @param host
     * @param port
     * @param username
     * @param password
     */
    public ServerConnector (String host, int port, String username, String password) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
    }
    
    /**
     * Connects to the server.
     * @return true or false
     */
    public Boolean connect () {
        try {
            this.socket = new Socket(this.host, this.port);
            this.logger = "Connection established";
            System.out.println("Connection established");
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.connected = true;
            return true;
        } catch (IOException e) {
            this.logger =  "Could not open connection to server.";
            return false;
        }
    }
    
    /**
     * Logs on to the server (checks username and password).
     * @return true or false
     */
    public Boolean login () {
        try {
            out.println("LOGIN " + this.username + " " + this.password);
            String[] ir = in.readLine().trim().split("@");
            System.out.println(ir);
            if (!ir[0].trim().equals("SUCCESS")) {
                return false;
            }
            this.userID = Integer.parseInt(ir[1].trim());
            this.farmID = Integer.parseInt(ir[2].trim());
            this.farmName = ir[3].trim();
            return true;
        } catch (IOException e) {
            this.logger = "Could not open connection to server.";
            return false;
        }
    } 
    
    /**
     * Fetches the list of sheeps from the server.
     * @return List<Sheep> or null
     */
    public List<Sheep> getSheepList () {
        List<Sheep> sheeps = new ArrayList<>();
        if (connected) {
            try {
                out.println("GETSHEEPLIST");
                System.out.println("GetSheepList");
                String inline = in.readLine();
                Sheep currentSheep = null;
                while (inline != null && !inline.equals("SUCCESS")) {
                    System.out.println(inline);
                    // This line contains a sheep
                    if (inline.indexOf("S") == 0) {
                        if (currentSheep != null) {
                            sheeps.add(currentSheep);
                        }
                        System.out.println("Made a sheep!");
                        currentSheep = new Sheep(inline);
                    }
                    // This line contains a sheep update
                    else if (inline.indexOf("U") == 0) {
                        System.out.println("Made a sheep update!");
                        SheepUpdate su = new SheepUpdate(inline);
                        currentSheep.addUpdate(su);
                    }
                    inline = in.readLine();
                }
                sheeps.add(currentSheep);
            } catch (IOException e) {
                System.out.println("IOEX!");
                this.logger =  "Could not fetch sheeps from server.";
                return null;
            }
        }
        
        return sheeps; //Returns null if no sheeps could be fetched from server
    }
    
    /**
     * Fetches the list of sheeps from the server.
     * @return List<SheepUpdate> or null
     */
    public List<SheepUpdate> getSheepUpdates (Integer sheepID, Integer numUpdates) {
        List<SheepUpdate> updates = new ArrayList<>();
        if (connected) {
            try {
                out.println("GETUPDATES" + sheepID + " " + numUpdates);
                String inline = in.readLine();
                while (inline != null) {
                    updates.add(new SheepUpdate(inline));
                    inline = in.readLine();
                }
            } catch (IOException e) {
                this.logger =  "Could not fetch sheepupdates from server.";
                return null;
            }
        }
        return updates; //Returns null if no updates could be fetched from server
    }
    
    /**
     * Sends an updates sheep object to server.
     * @return true or false
     */
    public Boolean editSheep (Sheep sheep) {
        if (connected) {
            try {
                out.println("EDITSHEEP");
                out.println(sheep.toString(false));
                if (!in.readLine().trim().equals("SUCCESS")) {
                    return false;
                }
                return true;
            } catch (IOException e) {
                this.logger =  "Could not fetch sheepupdates from server.";
                return null;
            }
        }
        return false;
    }
    
    /**
     * Stores a sheep object in the database.
     * @return true or false
     */
    public Boolean newSheep (Sheep sheep) {
        if (connected) {
            try {
                out.println("NEWSHEEP " + sheep.toString(false));
                System.out.println(sheep.toString(false));
                if (!in.readLine().trim().equals("SUCCESS")) {
                    return false;
                }
                return true;
            } catch (IOException e) {
                this.logger =  "Could not fetch sheepupdates from server.";
                return false;
            }
        }
        return false;
    }

    /**
     * Asks the server for current user's ID
     * @return int
     */
    public int getUserId() {
        if (connected) {
            try {
                out.println("GETUSERID");
                return Integer.parseInt(in.readLine().trim());
            } catch (IOException e) {
                return -1;
            }
        }
        return -1;
    }
    
    public String getUsername() {
        return this.username;
    }
    
    public String getFarmName() {
        return this.farmName;
    }

    /**
     * Gets the reason why something has failed.
     * @return string
     */
    public String getLogger () {
        return this.logger;
    }
    
    protected void finalize() throws Throwable
    {
      socket.close();
      super.finalize();
    } 
}