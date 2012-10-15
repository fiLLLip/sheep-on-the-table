package my.servonthetable;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.*;

/**
 * The MySqlHelper handles mySql-queries from the server to the database.
 *
 * @author Filip
 */
public class MySqlHelper {

    /*
     *      Declare the local fields
     */
    private String dbHost;
    private String dbUser;
    private String dbPass;
    private String dbName;
    private int port = 3306;
    private Connection con;
    private Statement stmt;
    private Server server;

    /**
     * Constructor method. All the information required to connect to the
     * database can be found and read from a config-file.
     *
     * @param dbHost - the IP adress of the database
     * @param dbUser - the username used to connect to the database
     * @param dbPass - the password used to connect to the database
     * @param dbName - the name of the database
     * @param port   - the port that you use to acess the database
     */
    public MySqlHelper(String dbHost, int port, String dbUser, String dbPass, String dbName, Server server) {
        this.dbHost = dbHost;
        this.dbName = dbName;
        this.dbPass = dbPass;
        this.dbUser = dbUser;
        this.port = port;
        this.server = server;
    }

    /**
     * Initiate a connection to the database.
     *
     * @return true is successful, false otherwise.
     */
    public boolean connect() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = urlGenerator(this.dbHost, this.port, this.dbName);
            con = DriverManager.getConnection(url, dbUser, dbPass);
            stmt = con.createStatement();
            return true;
        } catch (Exception ex) {
            Logger.getLogger(MySqlHelper.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    /**
     * Fetch the list of sheep belonging to the given farm in the database.
     *
     * @param farm_id - the ID of the farm
     *
     * @return List<sheep>
     */
    public List<Sheep> getSheepList(int farm_id) {
        List<Sheep> sheeps = new ArrayList<>();
        ResultSet results;

        try {
            // First the sheep without the assosiated sheepUpdates
            results = stmt.executeQuery("SELECT id, farm_id, name, UNIX_TIMESTAMP(born) as born, UNIX_TIMESTAMP(deceased) as deceased, comment, weight FROM sheep_sheep WHERE farm_id = '" + farm_id + "'");
            System.out.println(results.toString());
            while (results.next()) {
                sheeps.add(new Sheep(results.getInt("id"),
                        results.getInt("farm_id"),
                        results.getString("name"),
                        results.getInt("born"),
                        results.getInt("deceased"),
                        results.getString("comment"),
                        null,
                        results.getDouble("weight")));
            }
            results.close();

            // Then fetch the list of sheepUpdates for every sheep
            for (Sheep sheep : sheeps) {
                List<SheepUpdate> updates = getSheepUpdates(sheep.getID(), 1);
                sheep.setUpdates(updates);
            }

            return sheeps;

        } catch (SQLException ex) {
            System.out.println(ex.toString());
            Logger.getLogger(MySqlHelper.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Ask for the set of updates for a given sheep.
     *
     * @param id - the ID of the given sheep
     * @param numUpdates - the max number of updates wanted. If you want all availible, 
     * use a negative number.
     *
     * @return List<SheepUpdates>
     */
    public List<SheepUpdate> getSheepUpdates(int id, int numUpdates) {

        List<SheepUpdate> updates = new ArrayList<>();
        ResultSet results;
        try {
            String query = "SELECT id, sheep_id, UNIX_TIMESTAMP(timestamp) as timestamp, pos_x, pos_y, pulse, temp, alarm FROM sheep_updates WHERE sheep_id = '" + id + "' ORDER BY id DESC";
            System.out.println(query);
            results = stmt.executeQuery(query);
            while (results.next() && updates.size() != numUpdates) {
                updates.add(new SheepUpdate(results.getInt("id"),
                        results.getDouble("pos_x"),
                        results.getDouble("pos_y"),
                        results.getInt("pulse"),
                        results.getDouble("temp"),
                        0,
                        results.getInt("timestamp")));
            }
            results.close();
            return updates;
        } catch (SQLException ex) {
            Logger.getLogger(MySqlHelper.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Store another sheep in the database.
     *
     * @param s - the given Sheep
     *
     * @return boolean - success or failure
     */
    public boolean storeNewSheep(Sheep s) {
        try {
            String sq = "INSERT INTO sheep_sheep (farm_id, name, born, deceased, comment) VALUES ('" + s.getFarmId() + "', '" + s.getName() + "', '" + s.getBorn() + "', '" + s.getDeceased() + "', '" + s.getComment() + "')";
            System.out.println(sq);
            stmt.executeUpdate(sq);
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(MySqlHelper.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    /**
     * Store a new update in the database
     * 
     * @param sheepUpdate
     * 
     * @return boolean - success or failure
     */
    public boolean addUpdate(SheepUpdate su) {
        try {
            String sq = "INSERT INTO sheep_updates (sheep_id, timestamp, pos_x, pos_y, pulse, temp, alarm) VALUES ('" + su.getID() + "', from_unixtime('" + su.getTimeStamp() + "'), '" + su.getX() + "', '" + su.getY() + "', '" + su.getPulse() + "', '" + su.getTemp() + "', '" + su.getAlarm() + "')";
            System.out.println(sq);
            stmt.executeUpdate(sq);
            server.setLastDBUpdate(su.getTimeStamp());
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(MySqlHelper.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    /**
     * Update the information in the database concerning a single sheep.
     *
     * @param s - sheep object containing all the up-to-date information
     *
     * @return boolean - success or failure
     */
    public boolean updateSheep(Sheep s) {
        try {
            stmt.executeQuery("UPDATE sheep_sheep SET farm_id='" + s.getFarmId() + "', name='" + s.getName() + "', born='" + s.getBorn() + "', deceased='" + s.getDeceased() + "', comment='" + s.getComment() + "' WHERE id='" + s.getID() + "'");
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(MySqlHelper.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    /**
     * Delete a sheep and all its updates from the database.
     *
     * @param sheep - the unfortunate sheep
     *
     * @return boolean - success or failure
     */
    public boolean removeSheep(Sheep sheep) {
        try {
            stmt.execute("DELETE FROM sheep_sheep WHERE id = '" + sheep.getID() + "'");
            
        } catch (SQLException ex) {
            Logger.getLogger(MySqlHelper.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

        try {
            stmt.execute("DELETE FROM sheep_updates WHERE sheep_id = '" + sheep.getID() + "'");
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(MySqlHelper.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    /**
     *  Set the deceased parameter to the current time, thus stating that the
     *  sheep is dead.
     *
     * @param sheep - the unfortunate sheep
     *
     * @return boolean - success or failure
     */
    public boolean killSheep(int id, Long time) {
        try {
            time /= 1000;
            stmt.executeUpdate("UPDATE sheep_sheep SET deceased = '" + time + "'  WHERE id = '" + id + "'");
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(MySqlHelper.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    /**
     * Find the user ID of a user with the given username or password.
     *
     * @param userName
     * @param password
     *
     * @return int - user ID if password and username is correct, -1 otherwise
     * or in case of an error.
     */
    public int findUser(String userName, String password) {
        try {
            String q = "SELECT id FROM sheep_user WHERE un = '" + userName + "' AND pw = '" + password + "' LIMIT 1";
            System.out.println(q);
            ResultSet results = stmt.executeQuery(q);

            if (results.next()) {
                return results.getInt("id");
            } else {
                return -1;
            }
        } catch (SQLException s) {
            Logger.getLogger(MySqlHelper.class.getName()).log(Level.SEVERE, null, s);
            return -1;
        }
    }
    
    /**
     * Returns the farm ID of the user's farm.
     *
     * @param userID
     *
     * @return farm ID or -1 is non-existant or errror occured.
     */
    public int findFarm (int userID) {
        try {
            String q = "SELECT farm_id FROM sheep_user WHERE id = '" + userID + "' LIMIT 1";
            System.out.println(q);
            ResultSet results = stmt.executeQuery(q);

            if (results.next()) {
                return results.getInt("farm_id");
            } else {
                return -1;
            }
        } catch (SQLException s) {
            Logger.getLogger(MySqlHelper.class.getName()).log(Level.SEVERE, null, s);
            return -1;
        }
    }
    
    /**
     * Find farm name based on farm ID
     *
     * @param farmID
     *
     * @return farm name or null if a problem occurred.
     */
    public String findFarmName (int farmID) {
        try {
            String q = "SELECT name FROM sheep_farm WHERE id = '" + farmID + "' LIMIT 1";
            System.out.println(q);
            ResultSet results = stmt.executeQuery(q);

            if (results.next()) {
                return results.getString("name");
            } else {
                return null;
            }
        } catch (SQLException s) {
            Logger.getLogger(MySqlHelper.class.getName()).log(Level.SEVERE, null, s);
            return null;
        }
    }

    /**
     * Find phone number based on sheep ID
     *
     * @param int sheepID
     *
     * @return int phoen number
     */
    public int findPhoneNumber (int sheepID) {
        try {
            String q = "SELECT u.phone "
                + "FROM sheep_user u, sheep_updates up, sheep_sheep s, sheep_farm f "
                + "WHERE up.sheep_id = s.id "
                + "AND s.farm_id = f.id "
                + "AND u.farm_id = f.id "
                + "AND s.id =  '" + sheepID + "' "
                + "LIMIT 1";
            ResultSet results = stmt.executeQuery(q);

            if (results.next()) {
                return results.getInt("phone");
            } else {
                return -1;
            }
        } catch (SQLException s) {
            Logger.getLogger(MySqlHelper.class.getName()).log(Level.SEVERE, null, s);
            return -1;
        }
    }

    /**
     * Makes a url-string from host name, port and database name.
     *
     * @param String host
     * @param int port
     * @param String database
     *
     * @return url string
     */
    private String urlGenerator(String host, int port, String database) {
        String url;
        url = "jdbc:mysql://" + host + ":" + Integer.toString(port) + "/" + database;
        return url;
    }

    /**
     * Terminate the connection.
     *
     * @throws Throwable
     */
    protected void finalize() throws Throwable {
        con.close();
    }

    /**
     * Find the timestap of the most recent update.
     * 
     * @return The timestap as a long, corresponding to the number of milliseconds
     * since the first of january 1970.
     */
    public Long getLastUpdateTime() {
        ResultSet results;
        Long lastUpdate = new Long("0");
        try {
            String query = "SELECT UNIX_TIMESTAMP(timestamp) as timestamp FROM sheep_updates ORDER BY timestamp DESC";
            results = stmt.executeQuery(query);
            results.next();
            lastUpdate = results.getLong("timestamp");
        } catch (SQLException ex) {
            Logger.getLogger(MySqlHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lastUpdate;
    }
}
