/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my.servonthetable;

import java.io.*;
import java.net.*;


/**
 *
 * @author Filip
 */
public final class Server {

    ServerMySqlHelper sqlH;
    BufferedReader keyReader;
    BufferedReader clientReader;
    ServerSocket ss;
    Socket cs;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Server s = new Server();
    }

    public Server() {
        // Les inn fra config
        //sqlH = new ServerMySqlHelper();
        buildSockets();
        listenLoop();
    }

    public void inputLoop() {
        keyReader = new BufferedReader(new InputStreamReader(System.in));
        String input = "";
        try {
            while (!input.equals("exit")){
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
