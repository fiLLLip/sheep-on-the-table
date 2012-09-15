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
     * Connects to the server and checks username and password.
     * @return true or false
     */
    private Boolean connect () {
        try {
            this.socket = new Socket(this.host, this.port);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out.println("HELO");
            if (!in.readLine().trim().equals("EHLO")) {
                return false;
            }
            if (!in.readLine().trim().equals("USERNAME")) {
                return false;
            }
            out.println(this.username);
            if (!in.readLine().trim().equals("OK")) {
                return false;
            }
            if (!in.readLine().trim().equals("PASSWORD")) {
                return false;
            }
            out.println(this.password);
            if (!in.readLine().trim().equals("SUCCESS")) {
                this.logger = "Wrong username or password";
                return false;
            }
            return true;
        } catch (IOException e) {
            this.logger =  "Could not open connection to server.";
            return false;
        }
    }
    
    /**
     * Fetches the list of sheeps from the server.
     * @return
     */
    public List<Sheep> getSheepList () {
        List<Sheep> sheeps = null;
        if (connect()) {
            try {
                InputStream is = this.socket.getInputStream();
                ObjectInputStream ois = new ObjectInputStream(is);
                sheeps = (List<Sheep>)ois.readObject();
                is.close();
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
