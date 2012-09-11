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
    BufferedReader br;
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
        inputLoop();
    }

    public void inputLoop() {
        br = new BufferedReader(new InputStreamReader(System.in));
        String input = "";
        try {
            while (!input.equals("exit")){
                input = br.readLine();
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
        // Establish the client socket
        try {
            cs = ss.accept();
        } catch (IOException e) {
            System.err.println("Klientserveren kunne ikke etableres");
            System.exit(1);
        }
    }

    private void listenLoop() {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
