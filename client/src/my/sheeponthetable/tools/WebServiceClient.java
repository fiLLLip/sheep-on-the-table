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
    public static ArrayList<Map> farmids = new ArrayList();
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
        mySession.getOptions().trustAllCerts(true);
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
                    farmids.clear();
                    JSONArray JSONfarmid = (JSONArray)values.get(2);
                    for (int i = 0; i < JSONfarmid.size(); i++) {
                        JSONObject obj = (JSONObject)JSONfarmid.get(i);
                        Map<String,String> map = new HashMap<String,String>();
                        map.put("id", obj.get("id").toString());
                        map.put("name", obj.get("name").toString());
                        map.put("address", obj.get("address").toString());
                        farmids.add(map);
                    }
                    returnValue = true;
                } else {
                    System.out.println("Error:" + response.getResult());
                }
            } else {
                System.err.println("Error:" + response.getError().getMessage());
            }
        } catch (JSONRPC2SessionException e) {
            System.err.println("Error: " + e.getMessage());
        }catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
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
                    //System.out.println(obj);
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
    
    /**
     * Edits a specific sheep in the database
     *
     * @param sheep 
     * @return true if successful or false if an error happened.
     */
    public static Boolean editSheep (Sheep sheep) {        
        connect();
        boolean returnValue = false;
        // Construct new request
        String method = "newSheep";
        int requestID = 1;
        List<String> params=new ArrayList<String>();
        params.add(Integer.toString(sheep.getID()));
        params.add(sheep.getName());
        params.add(Integer.toString(sheep.getBorn()));
        params.add(Integer.toString(sheep.getDeceased()));
        params.add(sheep.getComment());
        params.add(Double.toString(sheep.getWeight()));
        params = hashParameters(params);
        System.out.println(params);
        
        JSONRPC2Request request = new JSONRPC2Request(method, params, requestID);

        JSONRPC2Response response = null;

        // Send request and print response result / error
        try {
            response = mySession.send(request);
            if (response.indicatesSuccess()) {
                if(response.getResult() != null) {
                    returnValue = true;
                }
                // Gets a JSONArray within a JSONArray
                //JSONArray sheeparr = getArrayOfJSONObjects(response.getResult());
                //System.out.println(sheeparr);
            } else {
                System.err.println(response.getError().getMessage());
            }
        } catch (JSONRPC2SessionException e) {
            System.err.println(e.getMessage());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            return returnValue;
        }
    }
    
    /**
     * Creates a new sheep in the database
     *
     * @param sheep 
     * @return true if successful or false if an error happened.
     */
    public static Boolean newSheep (Sheep sheep) {        
        connect();
        boolean returnValue = false;
        // Construct new request
        String method = "newSheep";
        int requestID = 1;
        List<String> params=new ArrayList<String>();
        params.add(farmid);
        params.add(sheep.getName());
        params.add(Integer.toString(sheep.getBorn()));
        params.add(Integer.toString(sheep.getDeceased()));
        params.add(sheep.getComment());
        params.add(Double.toString(sheep.getWeight()));
        params = hashParameters(params);
        System.out.println(params);
        
        JSONRPC2Request request = new JSONRPC2Request(method, params, requestID);

        JSONRPC2Response response = null;

        // Send request and print response result / error
        try {
            response = mySession.send(request);
            if (response.indicatesSuccess()) {
                if(response.getResult() != null) {
                    returnValue = true;
                }
                // Gets a JSONArray within a JSONArray
                //JSONArray sheeparr = getArrayOfJSONObjects(response.getResult());
                //System.out.println(sheeparr);
            } else {
                System.err.println(response.getError().getMessage());
            }
        } catch (JSONRPC2SessionException e) {
            System.err.println(e.getMessage());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            return returnValue;
        }
    }
    
    /**
     * Edits a specific sheep in the database
     *
     * @param sheep 
     * @return true if successful or false if an error happened.
     */
    public static Boolean removeSheep (Sheep sheep) {        
        connect();
        boolean returnValue = false;
        // Construct new request
        String method = "removeSheep";
        int requestID = 1;
        List<String> params=new ArrayList<String>();
        params.add(Integer.toString(sheep.getID()));
        params = hashParameters(params);
        System.out.println(params);
        
        JSONRPC2Request request = new JSONRPC2Request(method, params, requestID);

        JSONRPC2Response response = null;

        // Send request and print response result / error
        try {
            response = mySession.send(request);
            if (response.indicatesSuccess()) {
                if(response.getResult() != null) {
                    returnValue = true;
                }
            } else {
                System.err.println(response.getError().getMessage());
            }
        } catch (JSONRPC2SessionException e) {
            System.err.println(e.getMessage());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            return returnValue;
        }
    }

    /**
     * Function to send request to the WebService
     * @param method
     * @param params
     * @return a JSONRPC2Response on success or null if else
     */
    
    public static JSONRPC2Response doRequest(String method, List<String> params) {
        connect();
        
        int requestID = 1;
        
        params = hashParameters(params);
        
        JSONRPC2Request request = new JSONRPC2Request(method, params, requestID);

        JSONRPC2Response response = null;
        // Send request and print response result / error
        try {
            response = mySession.send(request);
            if (response.indicatesSuccess()) {
                // Success
            } else {
                System.err.println(response.getError().getMessage());
            }
        } catch (JSONRPC2SessionException e) {
            System.err.println(e.getMessage());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            return response;
        }
    }

    /**
     * Returns the users connected to a specific farm
     * @param farm_id
     * @return ArrayList<String> with usernames
     */
    public static ArrayList<String> getUsersForFarm(int farm_id) {
        
        ArrayList<String> returnArray = new ArrayList();
        
        ArrayList<String> params = new ArrayList<>();
        params.add(Integer.toString(farm_id));
        
        JSONRPC2Response response = WebServiceClient.doRequest("getUsersForFarm", params);
        
        if (response != null) {
            
            JSONArray userArray = getArrayOfJSONObjects(response.getResult());
                for (int i = 0; i < userArray.size(); i++) {
                    JSONObject obj = (JSONObject)userArray.get(i);
                    
                    returnArray.add(obj.toString());
                }
                
                return returnArray;
        } else {
            return null;
        }
    }
    
    /**
     * Returns the user level of a specific user to a farm
     * @param farm_id
     * @param user_id
     * @return integer level: -1: error, 0: view only, 1: Admin, 2: Owner
     */
    
    public static int getUserLevel(int farm_id, int user_id) {
        List<String> params = new ArrayList<String>();
        params.add(Integer.toString(farm_id));
        params.add(Integer.toString(user_id));
        
        JSONRPC2Response response = WebServiceClient.doRequest("getUserPermission", params);
        
        if (response != null) {
            return Integer.parseInt(response.getResult().toString());
        } else {
            return -1;
        }
    }
    
    /**
     * Returns the usersettings for a user for a specific farm
     * @param farm_id
     * @param user_id
     * @return Array or Map.. whatever
     */
    
    public static Map getUserSettings(int farm_id, int user_id) {
        List<String> params = new ArrayList<String>();
        params.add(Integer.toString(farm_id));
        params.add(Integer.toString(user_id));
        
        JSONRPC2Response response = WebServiceClient.doRequest("getUserSettings", params);
        
        if (response != null) {
            System.out.println(response.getResult());
            return null;
        } else {
            return null;
        }
    }
    
    
}