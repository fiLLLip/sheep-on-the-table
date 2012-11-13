package my.sheeponthetable.tools;

import com.thetransactioncompany.jsonrpc2.*;
import com.thetransactioncompany.jsonrpc2.client.*;
import java.awt.Window;
import java.net.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import my.sheeponthetable.gui.PasswordScreen;
import net.minidev.json.*;

/**
 * This class works as a backend, managing the communication with the server on
 * behalf of the client application.
 *
 * Upon creation, it tries to open a socket to the server. Using the socket, it
 * reads and writes information to and from the server when the appropriate
 * methods are called.
 *
 * Also, note that this class is a static class, not an object class. This is 
 * because the entire application only needs a single WebServiceClient, so there
 * is no benefit to forcing the programmer to create a object that would work
 * exactly like a static class.
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
    private static String name;
    private static String password;
    private static String hash;
    private static String userid;
    private static String farmid;
    private static int accessLevel = 0;
    private static ArrayList<Map> farmids = new ArrayList<>();
    private static URL serverURL;
    private static JSONRPC2Session mySession;
    public static String errorMessage;

    /**
     * Connects to the server.
     *
     * @return true if successfully connected, or false otherwise
     */
    private static boolean connect() {
        
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

    private static JSONArray getArrayOfJSONObjects(Object json) {
        try {
            JSONArray arr1 = (JSONArray) json;
            JSONArray arr2 = (JSONArray) arr1.get(0);
            return arr2;
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            return null;
        }
    }

    /**
     * Does a request to the webservice.
     *
     * @param String which method to request from webservice
     * @param List<String> parameters to send to webservice
     * @param Boolean - does this method require authentication
     * @return Object
     */
    private static Object doRequest(String method, List<String> parameters, Boolean reqAuth) {
        long startTime = new Date().getTime();
        connect();
        // Construct new request
        int requestID = 1;
        if (reqAuth) {
            parameters.add(0, userid);
            parameters.add(0, hash);
        }

        JSONRPC2Request request = new JSONRPC2Request(method, parameters, requestID);

        JSONRPC2Response response = null;
        // Send request and print response result / error
        Object returnValue = null;
        try {
            response = mySession.send(request);
            if (response.indicatesSuccess()) {
                returnValue = response.getResult();
                if (returnValue.toString().equals("sessionTimeout")) {
                    returnValue = null;
                    JOptionPane.showMessageDialog(null, "Session timed out, please log in again.");
                    for (Window window : java.awt.Window.getWindows()) {
                        window.dispose();
                    }
                    new PasswordScreen().setVisible(true);
                }
            } else {
                System.err.println(response.getError().getMessage());
                setErrorMessage(response.getError().getMessage());
            }
        } catch (JSONRPC2SessionException e) {
            System.err.println(e.getMessage());
            setErrorMessage(e.getMessage());
        } catch (Exception e) {
            System.err.println(e.getMessage());
            setErrorMessage(e.getMessage());
        } finally {
            System.out.println(method + " used: " + (new Date().getTime() - startTime) + "ms");
            return returnValue;
        }
    }

    /**
     * Logs into the webservice and fetches variables to use further on.
     * Required to run before anything else
     *
     * @return boolean
     */
    public static boolean isLoggedIn() {
        String method = "sheepLogon";
        List<String> params = new ArrayList<>();
        params.add(username);
        params.add(password);

        Object response = doRequest(method, params, false);

        Boolean returnValue = false;

        if (response != null) {
            List<Object> values = (List<Object>) response;
            if (values.get(0).toString().length() == 40) {
                System.out.println(values.toString());
                hash = values.get(0).toString();
                userid = values.get(1).toString();
                name = values.get(2).toString();
                farmids.clear();
                JSONArray JSONfarmid = (JSONArray) values.get(3);
                for (int i = 0; i < JSONfarmid.size(); i++) {
                    JSONObject obj = (JSONObject) JSONfarmid.get(i);
                    Map<String, String> map = new HashMap<>();
                    map.put("id", obj.get("id").toString());
                    map.put("name", obj.get("name").toString());
                    map.put("address", obj.get("address").toString());
                    map.put("level", obj.get("level").toString());
                    farmids.add(map);
                }
                returnValue = true;
            } else {
                setErrorMessage("Something went wrong.");
            }
        }

        return returnValue;
    }

    /**
     * Fetches the list of sheep from the server.
     *
     * Requires the connector to the logged in and connected.
     *
     * @return List<Sheep> or null, if not logged in, not connected or the user
     * doesn't have any sheep in the database.
     */
    public static List<Sheep> getSheepList() {
        List<Sheep> sheeps = new ArrayList();
        String method = "getSheepList";
        List<String> params = new ArrayList<>();
        params.add(farmid);

        Object response = doRequest(method, params, true);

        if (response != null) {
            // Gets a JSONArray within a JSONArray
            JSONArray sheeparr = getArrayOfJSONObjects(response);
            for (int i = 0; i < sheeparr.size(); i++) {
                JSONObject obj = (JSONObject) sheeparr.get(i);
                List<SheepUpdate> updates = new ArrayList();
                if (obj.get("updateid") != null) {
                    SheepUpdate update = new SheepUpdate(Integer.parseInt(obj.get("updateid").toString()),
                            Double.parseDouble(obj.get("updateposx").toString()),
                            Double.parseDouble(obj.get("updateposy").toString()),
                            Integer.parseInt(obj.get("updatepulse").toString()),
                            Double.parseDouble(obj.get("updatetemp").toString()),
                            Integer.parseInt(obj.get("updatealarm").toString()),
                            Long.parseLong(obj.get("updatetimestamp").toString()) * 1000);
                    updates.add(update);
                }
                Sheep sheep = new Sheep(
                        Integer.parseInt(obj.get("id").toString()),
                        Integer.parseInt(obj.get("farm_id").toString()),
                        obj.get("name").toString(),
                        Long.parseLong(obj.get("born").toString()) * 1000,
                        Long.parseLong(obj.get("deceased").toString()) * 1000,
                        obj.get("comment").toString(),
                        updates,
                        Double.parseDouble(obj.get("weight").toString()));
                sheeps.add(sheep);
            }
        }
        return sheeps;
    }

    /**
     * Fetches the list of sheep from the server.
     *
     * Requires the connector to the logged in and connected.
     *
     * @return List<Sheep> or null, if not logged in, not connected or the user
     * doesn't have any sheep in the database.
     */
    public static List<SheepUpdate> getSheepUpdate(String sheepid, String limit) {
        List<SheepUpdate> updates = new ArrayList();

        // Construct new request
        String method = "getSheepUpdates";
        List<String> params = new ArrayList<>();
        params.add(sheepid);
        params.add(limit);

        Object response = doRequest(method, params, true);

        if (response != null) {
            // Gets a JSONArray within a JSONArray
            JSONArray sheeparr = getArrayOfJSONObjects(response);
            //System.out.println(sheeparr);
            for (int i = 0; i < sheeparr.size(); i++) {
                JSONObject obj = (JSONObject) sheeparr.get(i);
                //System.out.println(obj);
                SheepUpdate update = new SheepUpdate(
                        Integer.parseInt(obj.get("id").toString()),
                        Double.parseDouble(obj.get("pos_x").toString()),
                        Double.parseDouble(obj.get("pos_y").toString()),
                        Integer.parseInt(obj.get("pulse").toString()),
                        Double.parseDouble(obj.get("temp").toString()),
                        Integer.parseInt(obj.get("alarm").toString()),
                        Long.parseLong(obj.get("timestamp").toString()) * 1000);
                updates.add(update);
            }
        }
        return updates;
    }

    /**
     * Edits a specific sheep in the database
     *
     * @param sheep
     * @return true if successful or false if an error happened.
     */
    public static Boolean editSheep(Sheep sheep) {
        boolean returnValue = false;
        // Construct new request
        String method = "editSheep";
        List<String> params = new ArrayList<>();
        params.add(Integer.toString(sheep.getID()));
        params.add(sheep.getName());
        params.add(Long.toString(sheep.getBorn().getTime() / 1000));
        params.add(Long.toString(sheep.getDeceased().getTime() / 1000));
        params.add(sheep.getComment());
        params.add(Double.toString(sheep.getWeight()));

        Object response = doRequest(method, params, true);

        // Uses >= 0 because you don't want the error if they clicked save and 
        // nothing is updated
        if (response != null && Integer.parseInt(response.toString()) >= 0) {
            returnValue = true;
        }
        return returnValue;
    }

    /**
     * Registers new password supplied from GUI
     *
     * @param String Old Password
     * @param String New Password
     * @return true if successful or false if an error happened.
     */
    public static Boolean newPassword(String oldPassword, String newPassword, String newConfirmedPassword) {
        boolean returnValue = false;
        // Construct new request
        String method = "setUserNewPassword";
        List<String> params = new ArrayList<>();
        params.add(oldPassword);
        params.add(newPassword);
        params.add(newConfirmedPassword);

        Object response = doRequest(method, params, true);

        if (response != null && Integer.parseInt(response.toString()) >= 1) {
            returnValue = true;
        }
        return returnValue;
    }

    /**
     * Creates a new sheep in the database
     *
     * @param sheep
     * @return true if successful or false if an error happened.
     */
    public static Boolean newSheep(Sheep sheep) {
        boolean returnValue = false;
        // Construct new request
        String method = "newSheep";
        List<String> params = new ArrayList<>();
        params.add(farmid);
        params.add(sheep.getName());
        params.add(Long.toString(sheep.getBorn().getTime() / 1000));
        params.add(Long.toString(sheep.getDeceased().getTime() / 1000));
        params.add(sheep.getComment());
        params.add(Double.toString(sheep.getWeight()));

        Object response = doRequest(method, params, true);

        if (response != null && Integer.parseInt(response.toString()) >= 1) {
            returnValue = true;
        }

        return returnValue;
    }

    /**
     * Adds permission for a user to current farm.
     * Sets default level to 0 (read only)
     *
     * @param String
     * @return true if successful or false if an error happened.
     */
    public static Boolean addNewUserToFarm(String username) {
        boolean returnValue = false;
        // Construct new request
        String method = "addNewUserToFarm";
        List<String> params = new ArrayList<>();
        params.add(farmid);
        params.add(username);

        Object response = doRequest(method, params, true);

        if (response != null && Integer.parseInt(response.toString()) >= 1) {
            returnValue = true;
        }

        return returnValue;
    }

    /**
     * Removes permission for a user from current farm.
     *
     * @param User
     * @return true if successful or false if an error happened.
     */
    public static Boolean removeUserFromFarm(User user) {
        boolean returnValue = false;
        // Construct new request
        String method = "removeUserFromFarm";
        List<String> params = new ArrayList<>();
        params.add(farmid);
        params.add(Integer.toString(user.getUserId()));
                
        Object response = doRequest(method, params, true);

        if (response != null && Integer.parseInt(response.toString()) >= 1) {
            returnValue = true;
        }

        return returnValue;
    }

    /**
     * Edits a specific sheep in the database
     *
     * @param sheep
     * @return true if successful or false if an error happened.
     */
    public static Boolean removeSheep(Sheep sheep) {
        boolean returnValue = false;
        // Construct new request
        String method = "removeSheep";
        List<String> params = new ArrayList<>();
        params.add(Integer.toString(sheep.getID()));
        Object response = doRequest(method, params, true);

        if (response != null && Integer.parseInt(response.toString()) >= 1) {
            returnValue = true;
        }
        return returnValue;
    }

    /**
     * Returns the users connected to a specific farm
     *
     * @param farm_id
     * @return ArrayList<String> with usernames
     */
    public static ArrayList<User> getUsersForFarm(int farm_id) {

        ArrayList<User> returnArray = new ArrayList();

        ArrayList<String> params = new ArrayList<>();
        params.add(Integer.toString(farm_id));

        Object response = doRequest("getUsersForFarm", params, true);
        System.out.println(response);
        if (response != null) {

            JSONArray res = getArrayOfJSONObjects(response);
            for (int i = 0; i < res.size(); i++) {
                JSONObject obj = (JSONObject) res.get(i);
                returnArray.add(new User(Integer.parseInt(obj.get("user_id").toString()),
                        obj.get("un").toString(),
                        obj.get("name").toString(),
                        obj.get("email").toString(),
                        obj.get("phone").toString(),
                        obj.get("SMSAlarmStationary").toString().equals("1"),
                        obj.get("SMSAlarmAttack").toString().equals("1"),
                        obj.get("SMSAlarmHealth").toString().equals("1"),
                        obj.get("EmailAlarmStationary").toString().equals("1"),
                        obj.get("EmailAlarmAttack").toString().equals("1"),
                        obj.get("EmailAlarmHealth").toString().equals("1")));
            }
            return returnArray;
        } else {
            return null;
        }
    }

    /**
     * Returns the users connected to a specific farm
     *
     * @param farm_id
     * @return ArrayList<String> with usernames
     */
    public static boolean setUserOptions(User user) {

        ArrayList<User> returnArray = new ArrayList();

        ArrayList<String> params = new ArrayList<>();
        params.add(farmid);
        params.add(Integer.toString(user.getUserId()));
        params.add(getStringFromBoolean(user.getSMSAlarmAttack()));
        params.add(getStringFromBoolean(user.getSMSAlarmHealth()));
        params.add(getStringFromBoolean(user.getSMSAlarmStationary()));
        params.add(getStringFromBoolean(user.getEmailAlarmAttack()));
        params.add(getStringFromBoolean(user.getEmailAlarmHealth()));
        params.add(getStringFromBoolean(user.getEmailAlarmStationary()));

        Object response = doRequest("setUserSettings", params, true);
        System.out.println(response);
        if (response != null && Integer.parseInt(response.toString()) >= 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns the User object of the user logged in
     *
     * @param farm_id
     * @return User on success, null if else
     */
    public static User getUserDetails() {

        User returnUser = null;

        ArrayList<String> params = new ArrayList<>();


        Object response = doRequest("getUserDetails", params, true);

        if (response != null) {

            JSONObject obj = (JSONObject) response;
            returnUser = new User(Integer.parseInt(obj.get("id").toString()),
                    obj.get("un").toString(),
                    obj.get("name").toString(),
                    obj.get("email").toString(),
                    obj.get("phone").toString());

            return returnUser;
        } else {
            return null;
        }
    }

    /**
     * Updates the user details in the database
     *
     * @param user
     * @return true if success else false
     */
    public static boolean setUserDetails(User user) {


        ArrayList<String> params = new ArrayList<>();
        params.add(user.getName());
        params.add(user.getEmail());
        params.add(user.getTelephone());

        Object response = doRequest("setUserDetails", params, true);

        if (response != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns the user level of a specific user to a farm
     *
     * @param farm_id
     * @param user_id
     * @return integer level: -1: error, 0: view only, 1: Admin, 2: Owner
     */
    public static int getUserLevel(int farm_id, int user_id) {
        List<String> params = new ArrayList<String>();
        params.add(Integer.toString(farm_id));
        params.add(Integer.toString(user_id));

        Object response = doRequest("getUserPermission", params, true);

        if (response != null) {
            return Integer.parseInt(response.toString());
        } else {
            return -1;
        }
    }

    /**
     * Returns the user level of a specific user to a farm
     *
     * @param farm_id
     * @param user_id
     * @return integer level: -1: error, 0: view only, 1: Admin, 2: Owner
     */
    public static boolean setUserPermission(int user_id, int level) {
        List<String> params = new ArrayList<String>();
        params.add(farmid);
        params.add(Integer.toString(user_id));
        params.add(Integer.toString(level));

        Object response = doRequest("setUserPermission", params, true);

        if (response != null && Integer.parseInt(response.toString()) >= 0) {
            return true;
        } else {
            return false;
        }
    }

    public static String getUsername() {
        return username;
    }

    public static String getName() {
        return name;
    }

    public static String getUserID() {
        return userid;
    }

    public static ArrayList<Map> getFarmIds() {
        return farmids;
    }

    public static void setFarmId(String fid) {
        farmid = fid;
        for (int i = 0;i<farmids.size();i++) {
            if(farmids.get(i).get("id").equals(fid)) {
                accessLevel = Integer.parseInt(farmids.get(i).get("level").toString());
                i = farmids.size();
            }
        }
    }

    public static void setUsername(String un) {
        username = un;
    }

    public static void setPassword(String pw) {
        password = pw;
    }

    public static String getFarmId() {
        return farmid;
    }

    public static void setErrorMessage(String message) {
        errorMessage = message;
    }

    public static String getErrorMessage() {
        String temp = errorMessage;
        setErrorMessage("");
        return temp;
    }

    public static boolean isErrorMessage() {
        if (errorMessage != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns the name of the selected farm
     *
     * @return String with farm name - empty string if it fails to find the
     * selected farm
     */
    public static String getFarmName() {

        for (Map farm : WebServiceClient.farmids) {
            if (farm.get("id").equals(WebServiceClient.farmid)) {
                return (String) farm.get("name");
            }
        }

        return "";
    }

    private static String getStringFromBoolean(boolean bool) {
        if (bool) {
            return "1";
        } else {
            return "0";
        }
    }
}