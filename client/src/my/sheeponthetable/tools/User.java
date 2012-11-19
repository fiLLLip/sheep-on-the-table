package my.sheeponthetable.tools;

/**
 * This is basically a data structure storeing information about a single user.
 * This might be the active user, or it might be any of the users associated with
 * the active farm, such as when looking through permissions in FarmTools.
 * 
 * The information stored is the username in the database, the real name, the
 * telephone number and email (used in case of alarm), and information about
 * which kinds of events the user wants to trigger which alarms.
 * 
 * @author Alex
 */
public class User {

    private static String username, name, email, telephone;
    private static int userId;
    private static boolean SMSAlarmStationary,
            SMSAlarmAttack,
            SMSAlarmHealth,
            EmailAlarmStationary,
            EmailAlarmAttack,
            EmailAlarmHealth;

    /**
     * Creates a new user without specifying information about which alarm the
     * user wants.
     * @param userId 
     * @param username 
     * @param name 
     * @param telephone
     * @param email  
     */
    public User(int userId, String username, String name, String email, String telephone) {
        this.userId = userId;
        this.username = username;
        this.name = name;
        this.email = email;
        this.telephone = telephone;
    }
    
    /**
     * Creates a new user object by stating all the associated information.
     * @param userId 
     * @param username 
     * @param EmailAlarmHealth
     * @param name
     * @param telephone 
     * @param SMSAlarmStationary 
     * @param email
     * @param SMSAlarmAttack
     * @param EmailAlarmStationary 
     * @param SMSAlarmHealth 
     * @param EmailAlarmAttack  
     */
    public User(int userId,
            String username,
            String name,
            String email,
            String telephone,
            boolean SMSAlarmStationary,
            boolean SMSAlarmAttack,
            boolean SMSAlarmHealth,
            boolean EmailAlarmStationary,
            boolean EmailAlarmAttack,
            boolean EmailAlarmHealth) {

        this.userId = userId;
        this.username = username;
        this.name = name;
        this.email = email;
        this.telephone = telephone;

        this.SMSAlarmAttack = SMSAlarmAttack;
        this.SMSAlarmStationary = SMSAlarmStationary;
        this.SMSAlarmHealth = SMSAlarmHealth;
        this.EmailAlarmAttack = EmailAlarmAttack;
        this.EmailAlarmStationary = EmailAlarmStationary;
        this.EmailAlarmHealth = EmailAlarmHealth;
    }

    /**
     * Gets the SMSAlarmStationary field
     * @return 
     */ 
    public boolean getSMSAlarmStationary() {
        return SMSAlarmStationary;
    }

    /**
     * Sets the SMSAlarmStationary field
     * @param SMSAlarmStationary 
     */ 
    public void setSMSAlarmStationary(boolean SMSAlarmStationary) {
        this.SMSAlarmStationary = SMSAlarmStationary;
    }
    
    /**
     * Gets the SMSAlarmAttack field
     * @return 
     */ 
    public boolean getSMSAlarmAttack() {
        return SMSAlarmAttack;
    }

    /**
     * Sets the SMSAlarmAttack field
     * @param SMSAlarmAttack 
     */ 
    public void setSMSAlarmAttack(boolean SMSAlarmAttack) {
        this.SMSAlarmAttack = SMSAlarmAttack;
    }

    /**
     * Gets the SMSAlarmHealth field
     * @return 
     */ 
    public boolean getSMSAlarmHealth() {
        return SMSAlarmHealth;
    }

    /**
     * Sets the SMSAlarmHealth field
     * @param SMSAlarmHealth 
     */ 
    public void setSMSAlarmHealth(boolean SMSAlarmHealth) {
        this.SMSAlarmHealth = SMSAlarmHealth;
    }

    /**
     * Gets the EmailAlarmStationary field
     * @return 
     */ 
    public boolean getEmailAlarmStationary() {
        return EmailAlarmStationary;
    }
    
    /**
     * Sets the EmailAlarmStationary field
     * @param EmailAlarmStationary 
     */ 
    public void setEmailAlarmStationary(boolean EmailAlarmStationary) {
        this.EmailAlarmStationary = EmailAlarmStationary;
    }

    /**
     * Gets the EmailAlarmAttack field
     * @return 
     */ 
    public boolean getEmailAlarmAttack() {
        return EmailAlarmAttack;
    }

    /**
     * Sets the EmailAlarmAttack field
     * @param EmailAlarmAttack 
     */ 
    public void setEmailAlarmAttack(boolean EmailAlarmAttack) {
        this.EmailAlarmAttack = EmailAlarmAttack;
    }

    /**
     * Gets the EmailAlarmField field
     * @return 
     */ 
    public boolean getEmailAlarmHealth() {
        return EmailAlarmHealth;
    }

    
    /**
     * Sets the EmailAlarmHealth field
     * @param EmailAlarmHealth 
     */ 
    public void setEmailAlarmHealth(boolean EmailAlarmHealth) {
        this.EmailAlarmHealth = EmailAlarmHealth;
    }

    /**
     * Gets the level of clerance the user has on this farm, by asking the 
     * server to provide this information.
     * @param farmId 
     * @return 
     */
    public int getClearance(int farmId) {
        return WebServiceClient.getUserLevel(farmId, this.userId);
    }

    /**
     * Gets the telephone field.
     * @return 
     */
    public String getTelephone() {
        return telephone;
    }
    
    /**
     * Gets the email field.
     * @return 
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * Gets the name field.
     * @return 
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gets the username field.
     * @return 
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Gets the user id field.
     * @return 
     */
    public int getUserId() {
        return this.userId;
    }

    /**
     * Sets the name field
     * @param name 
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the email field
     * @param mail 
     */
    public void setEmail(String mail) {
        this.email = mail;
    }

    /**
     * Sets the telephone field
     * @param number 
     */
    public void setTelephone(String number) {
        this.telephone = number;
    }
}
