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
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.logger = "Connection established";
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
        List<Sheep> sheeps = new ArrayList();
        if (isConnected()) {
            try {
                out.println("GETSHEEPLIST");
                String inline = in.readLine();
                if(inline == null) {
                    this.logger = "Not connected";
                    System.out.println(logger);
                    return null;
                }
                Sheep currentSheep = null;
                while (inline != null && !inline.equals("SUCCESS")) {
                    // This line contains a sheep
                    if (inline.indexOf("S") == 0) {
                        if (currentSheep != null) {
                            sheeps.add(currentSheep);
                        }
                        currentSheep = new Sheep(inline);
                    }
                    // This line contains a sheep update
                    else if (inline.indexOf("U") == 0) {
                        SheepUpdate su = new SheepUpdate(inline);
                        currentSheep.addUpdate(su);
                    }
                    inline = in.readLine();
                }
                if (currentSheep != null) {
                    sheeps.add(currentSheep);
                }
            } catch (IOException e) {
                this.logger =  "Could not fetch sheeps from server.";
                return null;
            }
        }
        else{
            System.out.println("Is not connected");
            return null;
        }
        if (!sheeps.isEmpty()) {
            return sheeps;
        }
        else {
            System.out.println("No sheeps");
            return null;//Returns null if no sheeps could be fetched from server
        }
    }
    
    /**
     * Fetches the list of sheeps from the server.
     * @param sheepID 
     * @param numUpdates 
     * @return List<SheepUpdate> or null
     */
    public List<SheepUpdate> getSheepUpdates (Integer sheepID, Integer numUpdates) {
        List<SheepUpdate> updates = new ArrayList<>();
        if (isConnected()) {
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
     * @param sheep 
     * @return true or false
     */
    public Boolean editSheep (Sheep sheep) {
        if (isConnected()) {
            try {
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
     * @param sheep 
     * @return true or false
     */
    public Boolean newSheep (Sheep sheep) {
        if (isConnected()) {
            try {
                out.println("NEWSHEEP " + sheep.toString(false));
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
    public int getUserId () {
        if (isConnected()) {
            try {
                out.println("GETUSERID");
                return Integer.parseInt(in.readLine().trim());
            } catch (IOException e) {
                return -1;
            }
        }
        return -1;
    }
    
    /**
     *
     * @return
     */
    public String getUsername () {
        return this.username;
    }
    
    /**
     *
     * @return
     */
    public String getFarmName () {
        return this.farmName;
    }

    /**
     * Gets the reason why something has failed.
     * @return string
     */
    public String getLogger () {
        return this.logger;
    }
    
    /**
     *
     * @return
     */
    public boolean isConnected () {
        try {
            out.println("PING");
            String input = in.readLine();
            if (input != null) {
                return true;
            } 
            else {
                this.socket.close();
                return false;
            }
        } catch (IOException ex) {
            Logger.getLogger(ServerConnector.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    protected void finalize () throws Throwable
    {
      socket.close();
      super.finalize();
    }
}