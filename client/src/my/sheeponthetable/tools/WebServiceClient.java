package my.sheeponthetable.tools;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.thetransactioncompany.jsonrpc2.client.*;
import com.thetransactioncompany.jsonrpc2.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import net.minidev.json.*;
import net.minidev.json.parser.JSONParser;

/**
 * This object works as a backend, managing the communication with the server
 * on behalf of the client application.
 *
 * Upon creation, it tries to open a socket to the server. Using the socket, it
 * reads and writes information to and from the server when the appropriate
 * methods are called.
 *
 * @author Gruppe 7
 */
public class WebServiceClient {
    
    /**
     * Declaring private fields.
     */
    private static Config config = new Config();
    private static String url;
    private static String username;
    private static String password;
    private static String hash;
    private static String userid;
    private static String farmid;
    private static URL serverURL;
    private static JSONRPC2Session mySession;
    
    /**
     * Connects to the server.
     *
     * @return true if successfully connected, or false otherwise
     */
    private static boolean connect () {
        config.loadSettingsFile();
        url = config.getServerURL();
        username = config.getTempUser();
        password = config.getTempPass();
        userid = config.getTempUserID();
        hash = config.getTempHash();
        farmid = config.getTempFarmID();
        try {
            serverURL = new URL(url);
        } catch (MalformedURLException e) {
            System.err.println(e.getMessage());
            return false;
        }
        mySession = new JSONRPC2Session(serverURL);
        return true;
    }
    
    private static List<String> hashParameters(List<String> parameters) {
        parameters.add(0, userid);
        parameters.add(0, hash);
        return parameters;
    }
    
    private static JSONArray getArrayOfJSONObjects(Object json) {
        try {
            JSONArray arr1 = (JSONArray)json;
            JSONArray arr2 = (JSONArray)arr1.get(0);
            return arr2;
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            return null;
        }
    }
    
    public static List<Object> isLoggedIn () {
        connect();
        // Construct new request
        String method = "sheepLogon";
        int requestID = 1;
        List<String> params=new ArrayList<String>();
        params.add(username);
        params.add(password);
        //params.put("pass", this.password);
        JSONRPC2Request request = new JSONRPC2Request(method, params, requestID);

        // Send request
        JSONRPC2Response response = null;

        try {
            response = mySession.send(request);
        } catch (JSONRPC2SessionException e) {
            System.err.println(e.getMessage());
            return null;
        }

        // Print response result / error
        try {
            if (response.indicatesSuccess()) {
                List<Object> values = (List<Object>)response.getResult();
                if (values.get(0).toString().length() == 40) {
                    System.out.println(values.toString());
                    return values;
                } else {
                    System.out.println(response.getResult());
                    return null;
                }
            } else {
                System.out.println(response.getError().getMessage());
                return null;
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }

    }
    
    /**
     * Fetches the list of sheep from the server. 
     * 
     * Requires the connector to the logged in and connected.
     *
     * @return List<Sheep> or null, if not logged in, not connected or the user
     * doesn't have any sheep in the database.
     */
    public static List<Sheep> getSheepList () {
        connect();
        List<Sheep> sheeps = new ArrayList();
        
        // Construct new request
        String method = "getSheepList";
        int requestID = 1;
        List<String> params=new ArrayList<String>();
        params.add(farmid);
        params = hashParameters(params);
        //params.put("pass", this.password);
        JSONRPC2Request request = new JSONRPC2Request(method, params, requestID);

        // Send request
        JSONRPC2Response response = null;

        try {
            response = mySession.send(request);
        } catch (JSONRPC2SessionException e) {
            System.err.println(e.getMessage());
            return null;
        }

        // Print response result / error
        try {
            if (response.indicatesSuccess()) {
                //System.out.println(response.getResult().toString());
                // Gets a JSONArray within a JSONArray
                JSONArray sheeparr = getArrayOfJSONObjects(response.getResult());
                for (int i = 0; i < sheeparr.size(); i++) {
                    JSONObject obj = (JSONObject)sheeparr.get(i);
                    //System.out.println(obj);
                    Sheep sheep = new Sheep(
                            Integer.parseInt(obj.get("id").toString()),
                            Integer.parseInt(obj.get("farm_id").toString()),
                            obj.get("name").toString(),
                            Integer.parseInt(obj.get("born").toString()),
                            Integer.parseInt(obj.get("deceased").toString()),
                            obj.get("comment").toString(),
                            new ArrayList(),
                            Double.parseDouble(obj.get("weight").toString())
                            );
                    sheeps.add(sheep);
                }
                return sheeps;
            } else {
                System.err.println(response.getError().getMessage());
                return null;
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }
}