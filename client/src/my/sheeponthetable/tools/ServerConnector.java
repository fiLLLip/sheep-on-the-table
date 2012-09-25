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
            return true;
        } catch (IOException e) {
            System.out.println("Could not open connection to server.");
            this.logger =  "Could not open connection to server.";
            return false;
        }
    }
    
    /**
     * Logs on to the server (checks username and password).
     * @return true or false
     */
    public Boolean login () {
        if (connect()) {
            try {
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out.println("LOGIN " + this.username + " " + this.password);
                String ir = in.readLine().trim();
                System.out.println(ir);
                if (!ir.equals("SUCCESS")) {
                    return false;
                }
                return true;
            } catch (IOException e) {
                this.logger =  "Could not open connection to server.";
                return false;
            }
        }
        return false;
    } 
    
    /**
     * Fetches the list of sheeps from the server.
     * @return List<Sheep> or null
     */
    public List<Sheep> getSheepList () {
        List<Sheep> sheeps = null;
        if (connect()) {
            try {
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                out.println("GETSHEEPLIST");
                ObjectInputStream ois = new ObjectInputStream(this.socket.getInputStream());
                sheeps = (List<Sheep>)ois.readObject();
            } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ServerConnector.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException e) {
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
        List<SheepUpdate> updates = null;
        if (connect()) {
            try {
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                out.println("GETUPDATES" + sheepID + " " + numUpdates);
                ObjectInputStream ois = new ObjectInputStream(this.socket.getInputStream());
                updates = (List<SheepUpdate>)ois.readObject();
            } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ServerConnector.class.getName()).log(Level.SEVERE, null, ex);
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
        if (connect()) {
            try {
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                out.println("EDITSHEEP");
                out.flush();
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                oos.writeObject(sheep);
                oos.flush();
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
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
     * Sends a new sheep object with id = null and updates = null.
     * @return true or false
     */
    public Boolean newSheep (Sheep sheep) {
        if (connect()) {
            try {
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                out.println("NEWSHEEP");
                out.flush();
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                oos.writeObject(sheep);
                oos.flush();
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
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
