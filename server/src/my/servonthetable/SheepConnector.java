package my.servonthetable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Date;


/**
 * The SheepConnector object takes care of the input from a sheep information
 * source. Implemented as a thread, it does not interfere with the server's
 * other operations.
 *
 * @author eliasaa
 */
public class SheepConnector {

    private Socket socket;
    private BufferedReader in;
    private MySqlHelper sqlHelper;
    private Date date;

    public SheepConnector(Socket socket, MySqlHelper sqlHelper) {
        this.socket = socket;
        this.sqlHelper = sqlHelper;
        date = new Date();
        establishConnection();
        accept();
    }
    
    /**
     * Creates input reader to communicate with the sheep.
     * Called on creation.
     *
     * @return boolean - building a reader is successful or not
     */
    private boolean establishConnection() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            return true;
        } catch (IOException e) {
            System.out.println("Could not get streams from " + socket.toString());
            return false;
        }
    }

    /**
     * Handles the input from a connected sheep.
     */
    public void accept() {
        try {
            String input = in.readLine().trim();
            if (input.indexOf("U") == 0) {
                SheepUpdate su = new SheepUpdate(input);
                if (su.isAlarm()) {
                    fireAlarm(su);
                }
                sqlHelper.addUpdate(su);
            } else {
                System.out.println("Recieved malformed input " + input + " from sheep.");
            }
        } catch (IOException e) {
            System.out.println("Could not get input from sheep!");
        } finally {
            purge();
        }
    }

    /**
     * Is called when an alarm-input is encountered. Finds the phone numbers
     * associated with the sheep and sends messages. Sets the alarm value of the
     * sheepUpdate to 2 if the message was succesfully sendt, so that the client
     * knows whether the farmer has been warned or not.
     * 
     * @param sheepUpdate
     */
    private void fireAlarm(SheepUpdate su) {
        boolean alarmFired = false;
        int number = sqlHelper.findPhoneNumber(su.getID());
        sqlHelper.killSheep(su.getID(),date.getTime());
        if (number>0) {
            String s = "Sheep under attack!";
            System.out.println(s);
            //smsSender.sendMessage(number,s);
            alarmFired = true;
        }
        if (alarmFired) {
            su.setAlarm(2);
        }
    }

    /**
     * Purges this user from connection.
     */
    public void purge() {
        try {
            socket.close();
        } catch (IOException e) {
            System.out.println("Could not purge " + socket + ".");
        }
    }
}
