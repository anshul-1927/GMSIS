/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customers;

public class Customer {

    final private int id;
    final private String firstName;
    final private String lastName;
    final private String type;
    final private String address;
    final private String postcode;
    final private String phoneNumber;
    final private String emailAddress;

    public Customer(int idd, String firstName, String lastName, String ttpe, String address, String postcode, String phoneNumber, String emailAddress) {
        this.id = idd;
        this.firstName = firstName;
        this.lastName = lastName;
        this.type = ttpe;
        this.address = address;
        this.postcode = postcode;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
    }

    public int getId() {
        return this.id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getType() {
        return type;
    }

    public String getAddress() {
        return this.address;
    }

    public String getPostcode() {
        return this.postcode;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public String getEmailAddress() {
        return this.emailAddress;
    }
    public String toString(){
        return this.getFirstName()+" "+this.getLastName();
    }
}
