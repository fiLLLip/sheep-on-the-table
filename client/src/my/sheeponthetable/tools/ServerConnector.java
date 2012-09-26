package my.sheeponthetable.tools;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This object works as a backend, managing the communication with the server
 * on behalf of the client application.
 *
 * Upon creation, it tries to open a socket to the server. Using the socket, it
 * reads and writes information to and from the server when the appropriate
 * methods are called.
 *
 * @author Gruppe 7
 */
public class ServerConnector {
    
    /**
     * Declaring private fields.
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
     * @param String host - the IP address of the server application
     * @param int port - the port that the server uses for sockets
     * @param String username
     * @param String password
     */
    public ServerConnector (String host, int port,
                            String username, String password) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
    }
    
    /**
     * Connects to the server.
     *
     * @return true if successfully connected, or false otherwise
     */
    public Boolean connect () {
        try {
            socket = new Socket(host, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            InputStreamReader isr = 
                    new InputStreamReader(socket.getInputStream());
            in = new BufferedReader(isr);
            if (isConnected()) {
                this.logger = "Connection established";
                return true;
            }
            else {
                this.logger =  "Could not open connection to server.";
                return false;
            }
        } catch (IOException e) {
            this.logger =  "Could not open connection to server.";
            return false;
        }
    }
    
    /**
     * Logs on to the server (checks username and password).
     * Simultaniously retrieves information about the farm and user from the
     * server.
     *
     * @return true if login is successful, or false otherwise
     */
    public Boolean login () {
        try {
            out.println("LOGIN " + username + " " + password);
            String[] ir = in.readLine().split("@");
            if (!ir[0].trim().equals("SUCCESS")) {
                return false;
            }
            userID = Integer.parseInt(ir[1]);
            farmID = Integer.parseInt(ir[2]);
            farmName = ir[3];
            return true;
        } catch (IOException e) {
            this.logger = "Could not open connection to server.";
            return false;
        }
    } 
    
    /**
     * Fetches the list of sheep from the server. 
     * 
     * Requires the connector to the logged in and connected.
     *
     * @return List<Sheep> or null, if not logged in, not connected or the user
     * doesn't have any sheep in the database.
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
                // Do a loop over the list of input strings.
                // Each line can either contain a sheep or sheep update.
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
                // Store the final sheep
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
        // Only return a list if there are sheep in it
        if (!sheeps.isEmpty()) {
            return sheeps;
        }
        else {
            System.out.println("No sheeps");
            return null;
        }
    }
    
    /**
     * Fetches the list of updates that a given sheep has stored in the database
     * from the server. 
     * 
     * Requires the connector to be logged in and connected.
     * .
     * @param sheepID - the ID of the given sheep
     * @param numUpdates - the max number of updates desired. Give a negative
     * number to get all availible updates.
     * @return List<SheepUpdate> or null if no updates could be fetched from
     * the server.
     */
    public List<SheepUpdate> getSheepUpdates (int sheepID, int numUpdates) {
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
        return updates;
    }
    
    /**
     * Asks the server to update the information contained in the database
     * about a given sheep.
     *
     * @param sheep 
     * @return true if successful or false if an error happened.
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
                this.logger =  "Could not edit sheep" + sheep.toString(false);
                return null;
            }
        }
        return false;
    }
    
    /**
     * Asks the server to store a new sheep in the database.
     *
     * @param sheep 
     * @return true if successful or false if an error happened.
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
                this.logger =  "Could not store new sheep."
                        + sheep.toString(false);
                return false;
            }
        }
        return false;
    }

    /**
     * Asks the server for current user's ID.
     *
     * @return int - user ID if logged in and no errors occur, or -1 if not
     * logged in or in case an error occured.
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
     * Gets the username stored in the server connector.
     *
     * @return String username
     */
    public String getUsername () {
        return username;
    }
    
    /**
     * Gets the farmname stored in the server connector.
     *
     * @return String farm name
     */
    public String getFarmName () {
        return this.farmName;
    }

    /**
     * Gets the reason why something has failed.
     *
     * @return string
     */
    public String getLogger () {
        return this.logger;
    }
    
    /**
     * Asks whether the connector is connected to the server or not.
     *
     * @return boolean
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
            String getThisClassName = ServerConnector.class.getName();
            Logger.getLogger(getThisClassName).log(Level.SEVERE, null, ex);
            return false;
        }
    }


    /**
     * Closes the socket, hence terminating the connection to the server.
     *
     */
    protected void finalize () throws Throwable
    {
      socket.close();
    }
}