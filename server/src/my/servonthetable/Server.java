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

    ServerMySqlHelper sqlH;
    BufferedReader keyReader;
    BufferedReader clientReader;
    ServerSocket ss;
    Socket cs;
    
    private static final int UMBRA_PORT = 30480;
    private static final int ROOM_THROTTLE = 200;
    private ServerSocket serverSocket;
    private InetAddress hostAddress;
    private Socket socket;
    private ArrayList<ServerClient> users = new ArrayList<ServerClient>();

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
                    hostAddress = InetAddress.getLocalHost();
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
                    serverSocket = new ServerSocket(UMBRA_PORT,0,hostAddress);
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
        while (true) {
            // Remove all disconnected clients
            for (int i = 0;i < users.size();i++) {
                // Check connection, remove on dead
                if(!users.get(i).isConnected()) {
                    System.out.println(users.get(i)+" removed due to lack of connection.");
                    users.remove(i);
                }
                else {
                    System.out.println(users.get(i)+" is still connected.");
                }
            }
            // Get a client trying to connect
            try {
                serverSocket.setSoTimeout(5000);
                socket = serverSocket.accept();
            }
            catch (SocketException e) {
                System.out.println("Client listen timeout.");
            }
            catch (IOException e) {
                System.out.println("Could not get a client.");
            }
            // Client has connected
            System.out.println("Client "+socket+" has connected.");
            // Add user to list
            users.add(new ServerClient(socket));
            // Sleep
            try
            {
                Thread.sleep(ROOM_THROTTLE);
            }
            catch(InterruptedException e)
            {
                System.out.println("Room has been interrupted.");
            }
        }
    }

    /* OLD
     * public Server() {
        // Les inn fra config
        //sqlH = new ServerMySqlHelper();
        //buildSockets();
        inputLoop();
        //listenLoop();
    }*/

    public void inputLoop() {
        keyReader = new BufferedReader(new InputStreamReader(System.in));
        String input = "";
        try {
            while (!input.trim().toLowerCase().contains("exit")){
                input = keyReader.readLine();
            }
        }
        catch (IOException e) {
            System.err.println("Feil i inputlesing. Input: " + input);
        }
    }

    private void buildSockets() {
        // Establish the server socket
        try {
            ss = new ServerSocket(8888);
        } catch (IOException e) {
            System.err.println("Feil ved lytting på port 8888");
            System.exit(1);
        }
        // Wait for a client application
        try {
            cs = ss.accept();
            clientReader = new BufferedReader(new InputStreamReader(cs.getInputStream()));
        } catch (IOException e) {
            System.err.println("Kunne ikke finne en klient");
            System.exit(1);
        }
    }

    private void listenLoop() {
        String inputLine;
        try {
            while((inputLine = clientReader.readLine()) != null) {
                System.out.println(inputLine);
                if (inputLine.toLowerCase().trim().contains("exit")){
                    System.out.println("Exiting!");
                    System.exit(0);
                }
            }
        } catch (IOException e) {
            System.err.println("Feil i lesing av input fra server.");
        }
    }
}
