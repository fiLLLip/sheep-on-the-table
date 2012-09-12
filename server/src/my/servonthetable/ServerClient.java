/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my.servonthetable;

/**
 *
 * @author Filip
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
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println("Welcome! Press enter two times to continue!");
            in.readLine();
            in.readLine();
            out.println("Please enter username:");
            input = in.readLine();
            System.out.println(socket.toString() + ": Username: " + input);
            out.println("Please enter password:");
            input = in.readLine();
            System.out.println(socket.toString() + ": Password: " + input);
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
     * Creates a new Umbra Client User with the socket from the newly connected client.
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
