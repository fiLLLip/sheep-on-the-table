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
    private String input = "";

    private int userID = -1;
    private boolean loggedIn = false;

    public void run() {
        // Open the InputStream
        try {

            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            System.out.println("Could not get input stream from " + socket.toString());
            return;
        }
        // Announce
        System.out.println(socket + " has connected input.");
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            String[] input = in.readLine().trim().split(" ");
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
                        // TODO
                    }
                    break;
                case "GETSHEEPLIST":
                    if (loggedIn) {
                        // DISKUSJON: KVA GJER VI VED FLEIRE GARDAR
                        int farm_id = 666;
                        List<Sheep> sheepList = sqlHelper.getSheepList(farm_id);
                        oos.writeObject(sheepList);
                    } else {
                        out.println("ERROR Not logged in");
                    }
                    break;
                case "EDITSHEEP":
                    if (loggedIn) {
                        out.println("WAITING");
                        try {
                            Sheep editSheep = (Sheep) ois.readObject();
                            Boolean success = sqlHelper.updateSheep(editSheep);
                            if (success) {
                                out.println("SUCCESS");
                            } else {
                                out.println("ERROR Could not edit");
                            }
                        } catch (ClassNotFoundException e) {
                            out.println("ERROR Could not cast to sheep");
                        }
                    } else {
                        out.println("ERROR Not logged in");
                    }
                    break;
                case "GETUPDATES":
                    if (loggedIn) {
                        try {
                            int sheepID = Integer.parseInt(input[1]);
                            int updates = Integer.parseInt(input[2]);
                            sqlHelper.getSheepUpdates(sheepID, updates);
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
                        out.println("WAITING");
                        try {
                            Sheep newSheep = (Sheep) ois.readObject();
                            boolean success = sqlHelper.storeNewSheep(newSheep);
                            if (success) {
                                out.println("SUCCESS");
                            } else {
                                out.println("ERROR Could not store sheep");
                            }
                        } catch (ClassNotFoundException ex) {
                            out.println("ERROR Could not cast to sheep");
                        }
                    } else {
                        out.println("ERROR Not logged in");
                    }
                    break;
            }

        } catch (IOException ex) {
            Logger.getLogger(ServerClient.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Enter process loop
        try {
            while ((input = in.readLine()) != null) {
                System.out.println(socket.toString() + ": " + input);
                Thread.sleep(USER_THROTTLE);
            }
            connected = false;
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
