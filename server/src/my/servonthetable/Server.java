/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my.servonthetable;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 *
 * @author Filip
 */
public class Server extends Thread {
    
    private static final int CLIENT_PORT = 30480;
    private static final int WAITING_TIME = 200;
    private ServerSocket serverSocket;
    private InetAddress hostAddress;
    private Socket socket;
    private ArrayList<ServerClient> clients = new ArrayList<>();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Server s = new Server();
    }
    
    /**
     * Creates a new Umbra room for clients to connect to.
     */
    public Server(){
    // Attempt to get the host address
            try
            {
                byte[] addr = new byte[4];
                addr[0] = (byte)127;
                addr[1] = (byte)0;
                addr[2] = (byte)0;
                addr[3] = (byte)1;
                hostAddress = InetAddress.getByAddress(addr);
                //hostAddress = InetAddress.getLocalHost();
            }
            catch(UnknownHostException e)
            {
                    System.out.println("Could not get the host address.");
                    return;
            }
            // Announce the host address
            System.out.println("Server host address is: "+hostAddress);
            // Attempt to create server socket
            try
            {
                    serverSocket = new ServerSocket(CLIENT_PORT,0,hostAddress);
            }
            catch(IOException e)
            {
                    System.out.println("Could not open server socket.");
                    return;
            }
            // Announce the socket creation
            System.out.println("Socket "+serverSocket+" created.");
            run();
    }
    /**
     * Starts the client accepting process.
     */
    public void run () {
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

    private void checkForDisconnects(ArrayList<ServerClient> cs) {
        // Remove all disconnected clients
        for (int i = 0;i < cs.size();i++) {
            // Check connection, remove on dead
            if(!cs.get(i).isConnected()) {
                System.out.println(cs.get(i)+" removed due to lack of connection.");
                cs.remove(i);
            }
            else {
            System.out.println(cs.get(i)+" is still connected.");
            }
        }
    }

    private void listenForConnectingClients() {
        // Get a client trying to connect
        try {
            serverSocket.setSoTimeout(5000);
            socket = serverSocket.accept();
           // Client has connected
           System.out.println("Client "+socket+" has connected.");
           // Add user to list
           clients.add(new ServerClient(socket));
       }
       catch (SocketException e) {
           System.out.println("Client listen timeout.");
       }
       // Bør ikkje dette vera ein TimeOutException eller noko?
       catch (IOException e) {
           System.out.println("Could not get a client.");
       }
    }

    private void sleep() {
        try
        {
            Thread.sleep(WAITING_TIME);
        }
        catch(InterruptedException e)
        {
            System.out.println("Room has been interrupted.");
        }
    }
}
