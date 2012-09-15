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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This object handles the execution for a single user.
 */
public class ServerClient extends Thread {

    private static final int USER_THROTTLE = 200;
    private Socket socket;
    private boolean connected;
    private BufferedReader in;
    private String input = "";

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
            //TODO: Alt her er kun for testing. Implementer funksjoner som er passende her
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            if (in.readLine().trim().equals("HELO")) {
                out.println("EHLO");
                System.out.println(socket.toString() + ": Initialized");
                out.println("USERNAME");
                String username = in.readLine();
                out.println("OK");
                out.println("PASSWORD");
                String password = in.readLine();
                if (password != null) {
                    out.println("SUCCESS");
                }
                System.out.println(socket.toString() + ": USER: " + username + " AND PASS: " + password);
            }
            else {
                System.out.println(socket.toString() + ": FAILED TO INIT");
                socket.close();
                connected = false;
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
    public ServerClient(Socket newSocket) {
        // Set properties
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
        return new String(socket.toString());
    }
}
