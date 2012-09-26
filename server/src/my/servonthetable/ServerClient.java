/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my.servonthetable;

/**
 *
 * @author Gruppe 7
 */
import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This object handles the execution for a single user.
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

    public void run() {
        // Open the InputStream
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            if (out != null) {
            }
            System.out.println("Could not get input stream from " + socket.toString());
            try {
                socket.close();
            } catch (IOException ee) {
                System.out.println("Could not close connection to" + socket.toString() + ". This might be a problem!");
            }
            return;
        }
        // Enter process loop
        try {
            while (connected && in != null) {
                // Announce
                System.out.println(socket + " has connected input.");
                try {
                    String[] input = in.readLine().trim().split(" ");
                    for (int i = 0; i < input.length; i++) {
                        System.out.println(input[i]);
                    }
                    switch (input[0]) {
                        case "LOGOUT":
                            System.out.println(socket.toString() + ": LOGGED OUT");
                            socket.close();
                            connected = false;
                            break;

                        case "LOGIN":
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
                            break;

                        case "GETSHEEPLIST":
                            if (loggedIn) {
                                // DISKUSJON: KVA GJER VI VED FLEIRE GARDAR
                                int farm_id = userID;
                                List<Sheep> sheepList = sqlHelper.getSheepList(farm_id);
                                for (Sheep s : sheepList) {
                                    out.println(s.toString(true));
                                }
                                out.println("SUCCESS");
                            } else {
                                out.println("ERROR Not logged in");
                            }
                            break;

                        case "EDITSHEEP":
                            if (loggedIn) {
                                out.println("WAITING");
                                try {
                                    Sheep editSheep = new Sheep(in.readLine());
                                    Boolean success = sqlHelper.updateSheep(editSheep);
                                    if (success) {
                                        out.println("SUCCESS");
                                    } else {
                                        out.println("ERROR Could not edit");
                                    }
                                } catch (IOException ex) {
                                    out.println("ERROR Could not get sheep");
                                }

                            } else {
                                out.println("ERROR Not logged in");
                            }
                            break;

                        case "GETUPDATES":
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
                            break;

                        case "NEWSHEEP":
                            if (loggedIn) {
                                // Make the rest of the input into a single string
                                String sheepParseString = "";
                                for (int i = 1; i < input.length; i++) {
                                    sheepParseString += input[i] + " ";
                                }
                                sheepParseString.trim();
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
                            break;

                        case "GETUSERID":
                            out.println(userID);
                            break;
                            
                        default:
                            out.println("ERROR Not a valid command");
                    }

                } catch (IOException ex) {
                    Logger.getLogger(ServerClient.class.getName()).log(Level.SEVERE, null, ex);
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
     */
    public ServerClient(Socket newSocket, MySqlHelper sqlHelper) {
        // Set properties
        this.sqlHelper = sqlHelper;
        socket = newSocket;
        connected = true;
        // Get input
        start();
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
    public String toString() {
        return socket.toString();
    }
}
