/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my.servonthetable;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 *
 * @author Filip
 */
public class SmsSender {
    String logger;
    String apiKey;
    
    /**
     * Needs apiKey to work
     * @param apiKey
     */
    public SmsSender (String apiKey) {
        this.apiKey = apiKey;
    }
    
    /**
     * Tries to send an SMS-message to the recipient
     * @param phoneNumber to send to
     * @param message you should send
     * @return boolean of success
     */
    public boolean sendMessage (int phoneNumber, String message) {
        String recipient = Integer.toString(phoneNumber);
        if (recipient.length() == 8 && !this.apiKey.equals(null)) {
            boolean success = false;
            try {
                // Set message so recipient can recognize it
                message = "SheepServer: " + message;
                
                // Construct data
                String data = URLEncoder.encode("recipient", "UTF-8") + "=" + URLEncoder.encode(recipient, "UTF-8");
                data += "&" + URLEncoder.encode("message", "UTF-8") + "=" + URLEncoder.encode(message, "UTF-8");
                data += "&" + URLEncoder.encode("apikey", "UTF-8") + "=" + URLEncoder.encode(this.apiKey, "UTF-8");

                // Send data
                URL url = new URL("http://www.vestnesconsulting.no:80/smsgateway/smssheep.php");
                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write(data);
                wr.flush();

                // Get the response
                BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                while ((line = rd.readLine()) != null) {
                    if (line.equals("true")) {
                        success = true;
                    }
                }
                wr.close();
                rd.close();
                return success;
            } catch (Exception e) {
                this.logger = "Connection with the SMS Gateway failed in some way";
                return success;
            }            
        }
        this.logger = "Phonenumber is not 8 digits";
        return false;
    }
    
    /**
     * Last message that was logged
     * @return the log
     */
    public String getLogger () {
        return this.logger;
    }
}
