/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my.servonthetable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 *
 * @author Filip
 */
public class Server {

    ServerMySqlHelper sqlH;
    BufferedReader br;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Server s = new Server();
    }

    public Server() {
        // Les inn fra config
        //sqlH = new ServerMySqlHelper();
        mainLoop();
    }

    public void mainLoop() {
        br = new BufferedReader(new InputStreamReader(System.in));
        String input = "";
        try {
            while (!input.equals("exit")){
                input = br.readLine();
            }
        }
        catch (IOException e) {
            System.out.println("Feil i inputlesing. Input: " + input);
            e.printStackTrace();
        }
    }
}
