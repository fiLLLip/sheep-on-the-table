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

    public User(int userId, String username, String name, String email, String telephone) {
        this.userId = userId;
        this.username = username;
        this.name = name;
        this.email = email;
        this.telephone = telephone;
        
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
}
