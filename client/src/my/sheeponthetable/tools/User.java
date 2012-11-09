package my.sheeponthetable.tools;

/**
 * This is basically a data structure storeing information about the active user.
 * 
 * The information stored is the username in the database, the real name, the
 * telephone number and email (used in case of alarm), and information about
 * which kinds of events the user wants to trigger which alarms.
 * 
 * @author Alex
 */
public class User {

    private String username, name, email, telephone;
    private int userId;
    private boolean SMSAlarmStationary,
            SMSAlarmAttack,
            SMSAlarmHealth,
            EmailAlarmStationary,
            EmailAlarmAttack,
            EmailAlarmHealth;

    /**
     * Creates a new user without specifying information about which alarm the
     * user wants.
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
     */ 
    public boolean getSMSAlarmStationary() {
        return SMSAlarmStationary;
    }

    /**
     * Sets the SMSAlarmStationary field
     */ 
    public void setSMSAlarmStationary(boolean SMSAlarmStationary) {
        this.SMSAlarmStationary = SMSAlarmStationary;
    }
    
    /**
     * Gets the SMSAlarmAttack field
     */ 
    public boolean getSMSAlarmAttack() {
        return SMSAlarmAttack;
    }

    /**
     * Sets the SMSAlarmAttack field
     */ 
    public void setSMSAlarmAttack(boolean SMSAlarmAttack) {
        this.SMSAlarmAttack = SMSAlarmAttack;
    }

    /**
     * Gets the SMSAlarmHealth field
     */ 
    public boolean getSMSAlarmHealth() {
        return SMSAlarmHealth;
    }

    /**
     * Sets the SMSAlarmHealth field
     */ 
    public void setSMSAlarmHealth(boolean SMSAlarmHealth) {
        this.SMSAlarmHealth = SMSAlarmHealth;
    }

    /**
     * Gets the EmailAlarmStationary field
     */ 
    public boolean getEmailAlarmStationary() {
        return EmailAlarmStationary;
    }
    
    /**
     * Sets the EmailAlarmStationary field
     */ 
    public void setEmailAlarmStationary(boolean EmailAlarmStationary) {
        this.EmailAlarmStationary = EmailAlarmStationary;
    }

    /**
     * Gets the EmailAlarmAttack field
     */ 
    public boolean getEmailAlarmAttack() {
        return EmailAlarmAttack;
    }

    /**
     * Sets the EmailAlarmAttack field
     */ 
    public void setEmailAlarmAttack(boolean EmailAlarmAttack) {
        this.EmailAlarmAttack = EmailAlarmAttack;
    }

    /**
     * Gets the EmailAlarmField field
     */ 
    public boolean getEmailAlarmHealth() {
        return EmailAlarmHealth;
    }

    
    /**
     * Sets the EmailAlarmHealth field
     */ 
    public void setEmailAlarmHealth(boolean EmailAlarmHealth) {
        this.EmailAlarmHealth = EmailAlarmHealth;
    }

    /**
     * Gets the level of clerance the user has on this farm, by asking the 
     * server to provide this information.
     */
    public int getClearance(int farmId) {
        return WebServiceClient.getUserLevel(farmId, this.userId);
    }

    /**
     * Gets the telephone field.
     */
    public String getTelephone() {
        return telephone;
    }
    
    /**
     * Gets the email field.
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * Gets the name field.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gets the username field.
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Gets the user id field.
     */
    public int getUserId() {
        return this.userId;
    }

    /**
     * Sets the name field
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the email field
     */
    public void setEmail(String mail) {
        this.email = mail;
    }

    /**
     * Sets the telephone field
     */
    public void setTelephone(String number) {
        this.telephone = number;
    }
}
