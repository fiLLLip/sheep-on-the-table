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
    private String name, password, email;
    private int telephone, clearance;
    
    public User(String name,String password, String email, int telephone,int clearance){
        this.name = name;
        this.password = password;
        if(telephone == 12) {this.telephone = telephone;}
        if (clearance > -1 && clearance < 5) {this.clearance = clearance;}
        if(email.contains("@")){this.email = email;}
    }
    public User(String name,String password, int clearance){
    if (clearance > -1 && clearance < 5) {this.clearance = clearance;}
    this.name = name;
    this.password = password;
    }
    public int getClearance(){
    return this.clearance;}
    
    public int getTelephone(){
    return this.telephone;}
    
    public String getEmail(){
    return this.email;}
    
    public String getName(){
    return this.name;}
    
    public String getPassword(){
    return this.password;}
}
