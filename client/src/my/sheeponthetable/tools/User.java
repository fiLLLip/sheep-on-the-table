/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my.sheeponthetable.tools;

/**
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

    public boolean getSMSAlarmStationary() {
        return SMSAlarmStationary;
    }

    public void setSMSAlarmStationary(boolean SMSAlarmStationary) {
        this.SMSAlarmStationary = SMSAlarmStationary;
    }

    public boolean getSMSAlarmAttack() {
        return SMSAlarmAttack;
    }

    public void setSMSAlarmAttack(boolean SMSAlarmAttack) {
        this.SMSAlarmAttack = SMSAlarmAttack;
    }

    public boolean getSMSAlarmHealth() {
        return SMSAlarmHealth;
    }

    public void setSMSAlarmHealth(boolean SMSAlarmHealth) {
        this.SMSAlarmHealth = SMSAlarmHealth;
    }

    public boolean getEmailAlarmStationary() {
        return EmailAlarmStationary;
    }

    public void setEmailAlarmStationary(boolean EmailAlarmStationary) {
        this.EmailAlarmStationary = EmailAlarmStationary;
    }

    public boolean getEmailAlarmAttack() {
        return EmailAlarmAttack;
    }

    public void setEmailAlarmAttack(boolean EmailAlarmAttack) {
        this.EmailAlarmAttack = EmailAlarmAttack;
    }

    public boolean getEmailAlarmHealth() {
        return EmailAlarmHealth;
    }

    public void setEmailAlarmHealth(boolean EmailAlarmHealth) {
        this.EmailAlarmHealth = EmailAlarmHealth;
    }

    public User(int userId, String username, String name, String email, String telephone) {
        this.userId = userId;
        this.username = username;
        this.name = name;
        this.email = email;
        this.telephone = telephone;

    }

    public User(int userId,
            String username,
            String name,
            String email,
            String telephone,
            boolean SMSAlarmStationary,
            boolean SMSAlarmAttack,
            boolean SMSAlarmTemperature,
            boolean EmailAlarmStationary,
            boolean EmailAlarmAttack,
            boolean EmailAlarmTemperature) {

        this.userId = userId;
        this.username = username;
        this.name = name;
        this.email = email;
        this.telephone = telephone;

        this.SMSAlarmAttack = SMSAlarmAttack;
        this.SMSAlarmStationary = SMSAlarmStationary;
        this.SMSAlarmTemperature = SMSAlarmTemperature;
        this.EmailAlarmAttack = EmailAlarmAttack;
        this.EmailAlarmStationary = EmailAlarmStationary;
        this.EmailAlarmTemperature = EmailAlarmTemperature;

    }

    public int getClearance(int farmId) {
        return WebServiceClient.getUserLevel(farmId, this.userId);
    }

    public String getTelephone() {
        return this.telephone;
    }

    public String getEmail() {
        return this.email;
    }

    public String getName() {
        return this.name;
    }

    public String getUsername() {
        return this.username;
    }

    public int getUserId() {
        return this.userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String mail) {
        this.email = mail;
    }

    public void setTelephone(String number) {
        this.telephone = number;
    }
}
