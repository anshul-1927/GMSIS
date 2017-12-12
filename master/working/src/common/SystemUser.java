/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

/**
 *
 * @author ec305
 */
public class SystemUser {
    private String name;
    private String surname;
    private String ID;
    private String password;
    private boolean isAdmin;
         
    
    public SystemUser(String name, String surname, String ID, String password, boolean admin){
        this.name=name;
        this.surname=surname;
        this.ID=ID;
        this.password=password;
        this.isAdmin=admin;
    }
    
    //Getters and setters for instance variables
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name=name;
    }
    
    public String getSurname(){
        return this.surname;
    }
    public void setSurname(){
        this.surname=surname;
    }
    
    public String getID(){
        return this.ID;
    }
    public void setID(String ID){
        this.ID=ID;
    }
    
    public String getPassword(){
        return this.password;
    }
    public void setPassword(String password){
        this.password=password;
    }
    public String getIsAdmin(){
        return isAdmin==false ? "Standard" : "Admin";
    }
}
