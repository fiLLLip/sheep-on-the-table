package my.servonthetable;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Each ServerClient object handles communication with a single client.
 * Upon creation it establishes a socket to read and write input to the client.
 * To handle each client in parallel, each ServerClient is implemented as a
 * separate thread.
 *
 * @author Gruppe 7
 */
public class ServerClient extends Thread {

    private MySqlHelper sqlHelper;
    private static final int USER_THROTTLE = 200;
    private Socket socket;
    private boolean connected;
    private BufferedReader in;
    private PrintWriter out;
    private int userID = -1;
    private boolean loggedIn = false;

    /*
     * Handles the main loop of the ServerClient. Runs a continous loop
     * listening to input from the connected client until a break order is
     * called.
     *
     */
    @Override
    public void run() {
        try {
            while (connected) {
                // Announce for debug
                System.out.println(socket + " has connected input.");
                try {
                    String[] input = in.readLine().trim().split(" ");
                    // Print for debug??
                    for (int i = 0; i < input.length; i++) {
                        System.out.println(input[i]);
                    }
                    switch (input[0]) {
                        case "LOGOUT":
                            logout();
                            break;
                        
                        case "PING":
                            out.println("PONG");
                            break;

                        case "LOGIN":
                            login(input);
                            break;

                        case "GETSHEEPLIST":
                            getSheepList();
                            break;

                        case "EDITSHEEP":
                            editSheep(input);
                            break;

                        case "GETUPDATES":
                            getUpdates(input);
                            break;

                        case "NEWSHEEP":
                            newSheep(input);
                            break;

                        case "GETUSERID":
                            out.println(userID);
                            break;
                            
                        default:
                            out.println("ERROR Not a valid command");
                    }

                } catch (IOException ex) {
                    Logger.getLogger(ServerClient.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println(socket.toString() + " has connection reset.");
                    purge();
                }
                // Sleep as to avoid overflow
                Thread.sleep(USER_THROTTLE);
            }
        } catch (Exception e) {
            System.out.println(socket.toString() + " has input interrupted.");
        }
    }

    /**
     * Creates a new Sheep Client User with the socket from the newly connected client.
     *
     * @param newSocket  The socket from the connected client.
     * @param sqlHelper  
     */
    public ServerClient(Socket newSocket, MySqlHelper sqlHelper) {
        // Set properties
        this.sqlHelper = sqlHelper;
        socket = newSocket;
        connected = true;
        establishConnection();
        // Get input
        start();
    }

    /**
     * Creates input and output readers to communicate with the client.
     * Called on creation.
     *
     * @return boolean - connection is successful or not
     */
    private boolean establishConnection() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            return true;
        } catch (IOException e) {
            System.out.println("Could not get streams from " + socket.toString());
            return false;
        }
    }

    /**
     * Gets the connection status of this user.
     *
     * @return  If this user is still connected.
     */
    public boolean isConnected() {
        return connected;
    }

    /**
     * Purges this user from connection.
     */
    public void purge() {
        // Close everything
        try {
            connected = false;
            socket.close();
        } catch (IOException e) {
            System.out.println("Could not purge " + socket + ".");
        }
    }

    /**
     * Returns the String representation of this user.
     *
     * @return  A string representation.
     */
    @Override
    public String toString() {
        return socket.toString();
    }

    /**
     *  Called by run() to handle the LOGOUT order from a client.
     */
    private void logout() throws IOException {
        // Debug print
        System.out.println(socket.toString() + ": LOGGED OUT");
        socket.close();
        connected = false;
    }

    /**
     *  Called by run() to handle the LOGIN order from a client.
     */
    private void login(String[] input) {
        if (loggedIn) {
            out.println("ERROR Already logged in");
        } else {
            try {
                String userName = input[1];
                String password = input[2];
                userID = sqlHelper.findUser(userName, password);
                System.out.println("USERID: " + userID);
                if (userID >= 0) {
                    loggedIn = true;
                    int farmID = sqlHelper.findFarm(userID);
                    String farmName = sqlHelper.findFarmName(farmID);
                    out.println("SUCCESS@" + userID + "@" + farmID + "@" + farmName);
                } else {
                    out.println("ERROR username or password not correct");
                }

            } catch (ArrayIndexOutOfBoundsException e) {
                out.println("ERROR LOGIN requires a username and a password");
            }
        }

    }

    /**
     *  Called by run() to handle the GETSHEEPLIST order from a client.
     */
    private void getSheepList() {
        if (loggedIn) {
            int farm_id = userID;
            List<Sheep> sheepList = sqlHelper.getSheepList(farm_id);
            for (Sheep s : sheepList) {
                out.println(s.toString(true));
            }
            out.println("SUCCESS");
        } else {
            out.println("ERROR Not logged in");
        }
    }

    /**
     *  Called by run() to handle the GETSHEEPLIST order from a client.
     */
    private void editSheep(String[] input) {
        String sheepParseString = buildParameterString(input);
        if (loggedIn) {
            Sheep editSheep = new Sheep(sheepParseString);
            Boolean success = sqlHelper.updateSheep(editSheep);
            if (success) {
                out.println("SUCCESS");
            } else {
                out.println("ERROR Could not edit");
            }

        } else {
            out.println("ERROR Not logged in");
        }
    }

    /**
     *  Called by run() to handle the GETUPDATES order from a client.
     */
    private void getUpdates(String[] input) {
        if (loggedIn) {
            try {
                int sheepID = Integer.parseInt(input[1]);
                int numUpdates = Integer.parseInt(input[2]);
                List<SheepUpdate> sheepUpdateList = sqlHelper.getSheepUpdates(sheepID, numUpdates);

                for (SheepUpdate su : sheepUpdateList) {
                    out.println(su.toString());
                }
            } catch (NumberFormatException e) {
                out.print("ERROR Input parameters must be numbers");
            } catch (ArrayIndexOutOfBoundsException e) {
                out.print("ERROR GETUPDATES must specify two parameters");
            }
        } else {
            out.println("ERROR Not logged in");
        }

    }

    /**
     *  Called by run() to handle the NEWSHEEP order from a client.
     */
    private void newSheep(String[] input) {
        if (loggedIn) {
            String sheepParseString = buildParameterString(input);
            // The make a sheep for string and store in DB
            System.out.println(sheepParseString);
            Sheep newSheep = new Sheep(sheepParseString);
            boolean success = sqlHelper.storeNewSheep(newSheep);
            if (success) {
                out.println("SUCCESS");
            } else {
                out.println("ERROR Could not store sheep");
            }

        } else {
            out.println("ERROR Not logged in");
        }

    }

    /**
     * Transforms the input string array into a single string. This is used to
     * build a complete string to parse into a sheep or sheepUpdate. 
     *
     * @param String[] input
     * @return String input combined into a single string
     */

    private String buildParameterString(String[] input) {
        String parameterString = "";
        for (int i = 1; i < input.length; i++) {
            parameterString += input[i] + " ";
        }
        return parameterString.trim();
    }
}
