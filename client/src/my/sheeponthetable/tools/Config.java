package my.sheeponthetable.tools;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;
import javax.swing.JOptionPane;
import my.sheeponthetable.SheepFinder;

/**
 * The Config class reads the config file, and stores the information found in
 * the file.
 *
 * @author gruppe 7
 */
public final class Config {

    private String serverURL = "";
    private String username = "";
    private String password = "";

    /**
     * Creates the object. This makes the object start reading the config file.
     */
    public Config() {
        loadSettingsFile();
    }

    /**
     * Gets the URL field.
     *
     * @return
     */
    public String getServerURL() {
        return serverURL;
    }

    /**
     * Gets the username field.
     *
     * @return
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the password field.
     *
     * @return
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the username, and which also stores this in the config file.
     *
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
        saveSettingsFile();
    }

    /**
     * Sets the password, and which also stores this in the config file.
     *
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
        saveSettingsFile();
    }

    /**
     * Reads the config file.
     */
    public void loadSettingsFile() {

        Properties properties = new Properties();

        try {
            File file = new File(
                    System.getProperty("user.dir") + File.separator
                    + "settings.properties");

            if (!file.exists()) {
                initLoadSettingsFile();
                file.createNewFile();
                saveSettingsFile();
            } else {
                InputStream reader = new FileInputStream(file);
                properties.load(reader);
                /**
                 * Load the following settings and assign them to class
                 * variables
                 */
                this.serverURL = properties.getProperty("serverURL");
                this.username = properties.getProperty("username");
                this.password = properties.getProperty("password");
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Failure when reading settings: " + e.toString());
        }
    }

    /**
     * Stores the current settings into the config file.
     */
    private void saveSettingsFile() {

        Properties properties = new Properties();

        try {
            File file = new File(
                    System.getProperty("user.dir") + File.separator
                    + "settings.properties");

            if (!file.exists()) {
                file.createNewFile();
                initLoadSettingsFile();
            }

            InputStream reader = new FileInputStream(file);
            properties.load(reader);

            /**
             * Load the following settings and assign them to class variables
             */
            properties.setProperty("serverURL", this.serverURL);
            properties.setProperty("username", this.username);
            properties.setProperty("password", this.password);

            //String filepath = new File(SheepFinder.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent();

            String comments = "# This is the config file where all settings should be.\n"
                    + "# Follow usual .properties-annotation\n"
                    + "# http://en.wikipedia.org/wiki/.properties\n"
                    + "# ";
            Writer writer = new FileWriter(file);
            properties.store(writer, comments);

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Failure when saving settings: " + e.toString());
        }
    }

    /**
     * Reads the initial config file.
     */
    private void initLoadSettingsFile() {

        Properties properties = new Properties();

        try {
            properties.load(getClass().getResourceAsStream("settings.properties"));

            /**
             * Load the following settings and assign them to class variables
             */
            this.serverURL = properties.getProperty("serverURL");
            this.username = properties.getProperty("username");
            this.password = properties.getProperty("password");

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Failure when reading settings: " + e.toString());
        }
    }
}
