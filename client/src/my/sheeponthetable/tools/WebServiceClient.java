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
import java.util.Date;
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
    public static String username;
    public static String password;
    public static String hash;
    public static String userid;
    public static String farmid;
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
    
    public static boolean isLoggedIn () {
        connect();
        // Construct new request
        String method = "sheepLogon";
        int requestID = 1;
        List<String> params=new ArrayList<String>();
        params.add(username);
        params.add(password);
        
        JSONRPC2Request request = new JSONRPC2Request(method, params, requestID);

        JSONRPC2Response response = null;
        
        // Send request and print response result / error
        boolean returnValue = false;
        try {
            response = mySession.send(request);
            if (response.indicatesSuccess()) {
                List<Object> values = (List<Object>)response.getResult();
                if (values.get(0).toString().length() == 40) {
                    System.out.println(values.toString());
                    hash = values.get(0).toString();
                    userid = values.get(1).toString();
                    farmid = values.get(2).toString();
                    returnValue = true;
                } else {
                    System.out.println("Error:" + response.getResult());
                }
            } else {
                System.err.println("Error:" + response.getError().getMessage());
            }
        } catch (JSONRPC2SessionException e) {
            System.err.println(e.getMessage());
        }catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            return returnValue;
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
        long startTime = new Date().getTime();
        connect();
        List<Sheep> sheeps = new ArrayList();
        
        // Construct new request
        String method = "getSheepList";
        int requestID = 1;
        List<String> params=new ArrayList<String>();
        params.add(farmid);
        params = hashParameters(params);
        System.out.println(params);
        //params.put("pass", this.password);
        JSONRPC2Request request = new JSONRPC2Request(method, params, requestID);

        JSONRPC2Response response = null;
        // Send request and print response result / error
        try {
            response = mySession.send(request);
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
                            WebServiceClient.getSheepUpdate(obj.get("id").toString(), "1"),
                            Double.parseDouble(obj.get("weight").toString())
                            );
                    sheeps.add(sheep);
                }
            } else {
                System.err.println(response.getError().getMessage());
            }
        } catch (JSONRPC2SessionException e) {
            System.err.println(e.getMessage());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            System.out.println("GetSheepList used: " + (new Date().getTime() - startTime) + "ms");
            return sheeps;
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
    public static List<SheepUpdate> getSheepUpdate (String sheepid, String limit) {
        connect();
        List<SheepUpdate> updates = new ArrayList();
        
        // Construct new request
        String method = "getSheepUpdates";
        int requestID = 1;
        List<String> params=new ArrayList<String>();
        params.add(sheepid);
        params.add(limit);
        params = hashParameters(params);
        System.out.println(params);
        //params.put("pass", this.password);
        JSONRPC2Request request = new JSONRPC2Request(method, params, requestID);

        JSONRPC2Response response = null;

        // Send request and print response result / error
        try {
            response = mySession.send(request);
            if (response.indicatesSuccess()) {
                //System.out.println(response.getResult().toString());
                // Gets a JSONArray within a JSONArray
                JSONArray sheeparr = getArrayOfJSONObjects(response.getResult());
                //System.out.println(sheeparr);
                for (int i = 0; i < sheeparr.size(); i++) {
                    JSONObject obj = (JSONObject)sheeparr.get(i);
                    System.out.println(obj);
                    SheepUpdate update = new SheepUpdate(
                            Integer.parseInt(obj.get("id").toString()),
                            Double.parseDouble(obj.get("pos_x").toString()),
                            Double.parseDouble(obj.get("pos_y").toString()),
                            Integer.parseInt(obj.get("pulse").toString()),
                            Double.parseDouble(obj.get("temp").toString()),
                            Integer.parseInt(obj.get("alarm").toString()),
                            Long.parseLong(obj.get("timestamp").toString())
                            );
                    updates.add(update);
                }
            } else {
                System.err.println(response.getError().getMessage());
            }
        } catch (JSONRPC2SessionException e) {
            System.err.println(e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            return updates;
        }
    }
}