/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my.servonthetable;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 *
 * @author Gruppe 7
 */
public class Server extends Thread {

    private static final int CLIENT_PORT = 30480;
    private static final int WAITING_TIME = 200;
    private static final int TIMEOUT = 5000;
    private ServerSocket serverSocket;
    private InetAddress hostAddress;
    private Socket socket;
    private ArrayList<ServerClient> clients = new ArrayList<>();
    private MySqlHelper sqlHelper;
    private Config config;
    private SmsSender sendSms;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Server s = new Server();
    }

    /**
     * Creates a new TCP/IP Socket for clients to connect to.
     */
    public Server() {
        // Attempt to get the host address
        try {
            byte[] addr = new byte[4];
            addr[0] = (byte) 0;
            addr[1] = (byte) 0;
            addr[2] = (byte) 0;
            addr[3] = (byte) 0;
            hostAddress = InetAddress.getByAddress(addr);
            //hostAddress = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            System.out.println("Could not get the host address.");
            return;
        }
        // Announce the host address
        System.out.println("Server host address is: " + hostAddress);
        // Attempt to create server socket
        try {
            serverSocket = new ServerSocket(CLIENT_PORT, 0, hostAddress);
        } catch (IOException e) {
            System.out.println("Could not open server socket.");
            return;
        }
        // Announce the socket creation
        System.out.println("Socket " + serverSocket + " created.");
        config = new Config();
        sqlHelper = new MySqlHelper(config.getDBHost(),
                                    config.getDBPort(),
                                    config.getDBUsername(),
                                    config.getDBPassword(),
                                    config.getDBName());
        if (!sqlHelper.connect()) {
            System.out.println("Could not connect to MySQL");
            System.exit(1);
        }
        
        sendSms = new SmsSender(config.getApiKey());
        
        start();
    }

    /**
     * Starts the client accepting process.
     */
    public void run() {
        // Announce the starting of the process
        System.out.println("Server has been started.");
        // set timeout
        // Enter the main loop
        boolean run = true;
        while (run) {
            checkForDisconnects(clients);
            listenForConnectingClients();
            sleep();
        }
    }

    /**
     * Check if someone has disconnected and close the link.
     */
    private void checkForDisconnects(ArrayList<ServerClient> cs) {
        // Remove all disconnected clients
        for (int i = 0; i < cs.size(); i++) {
            // Check connection, remove on dead
            if (!cs.get(i).isConnected()) {
                System.out.println(cs.get(i) + " removed due to lack of connection.");
                cs.remove(i);
            } else {
                System.out.println(cs.get(i) + " is still connected.");
            }
        }
    }

    /**
     * Listen on specified port for any new TCP/IP Socket connections.
     * When someone connects, add the user to list for maintenance.
     */
    private void listenForConnectingClients() {
        // Get a client trying to connect
        try {
            System.out.print("Listening for new client: ");
            serverSocket.setSoTimeout(TIMEOUT);
            socket = serverSocket.accept();
            // Client has connected
            System.out.print(socket + " has connected.");
            // Add user to list
            clients.add(new ServerClient(socket,sqlHelper));
        } 
        catch (SocketTimeoutException e) {
            System.out.print("listen timeout.");
        }
        catch (IOException e) {
            System.out.print("could not connect.");
        }
        finally {
            System.out.print("\r");
        }
    }

    /**
     * Method to make the Thread sleep for some time before continuing to prevent overflow.
     */
    private void sleep() {
        try {
            Thread.sleep(WAITING_TIME);
        } catch (InterruptedException e) {
            System.out.println("Server thread has been interrupted.");
        }
    }
}
