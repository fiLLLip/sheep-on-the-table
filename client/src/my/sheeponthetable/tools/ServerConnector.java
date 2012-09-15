/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my.sheeponthetable.tools;

import java.io.*;
import java.net.*;

/**
 *
 * @author Gruppe 7
 */
public class ServerConnector {
    
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
    
    private Boolean connect () {
        try {
            this.socket = new Socket(this.host, this.port);
            return true;
        } catch (IOException e) {
            this.logger =  "Could not open connection to server.";
            return false;
        }
    }
    
}
