/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vehicles;

/**
 *
 * @author EfthymiosChatziathanasiadis
 */
public class VehicleWrapper {
    private Customer cust;
    private Vehicle veh;
    private Booking bk;
    
    public VehicleWrapper(Customer cust, Vehicle veh, Booking bk){
        this.cust=cust;
        this.veh=veh;
        this.bk=bk;
    }
    
    public Customer getCustomer(){
        return cust;
    }
    public Vehicle getVehicle(){
        return veh;
    }
    public Booking getBooking(){
        return bk;
    }
    
    /*********************************
    *       CUSTOMER GETTERS
    **********************************/
    public int getId() {
        return cust.getId();
    }

    public String getFirstName() {
        return cust.getFirstName();
    }

    public String getLastName() {
        return cust.getLastName();
    }

    
    public String getAddress() {
        return cust.getAddress();
    }

    public String getPostcode() {
        return cust.getPostcode();
    }

    public String getPhoneNumber() {
        return cust.getPhoneNumber();
    }

    public String getEmailAddress() {
        return cust.getEmailAddress();
    }
    /*********************************
    *       VEHICLE GETTERS
    **********************************/
    public VehicleWarrantyRole getWarrantyRole(){
        return veh.getWarrantyRole();
    }
    public String getVehicleType(){
        return veh.getVehicleType();
    }
    
    public String getRegNum(){
        return veh.getRegNum();
    }
    
    public String getModel(){
        return veh.getModel();
    }
    
    public String getMileage(){
        return veh.getMileage();
    }
    
    
    public String getCol(){
        return veh.getCol();
    }
    
    public String getEngSize(){
        return veh.getEngSize();
    }
   
    public String getFuel(){
        return veh.getFuel();
    }
    
    public String getMoT(){
        return veh.getMoT();
    }
    
    public String getLastServiceDate(){
        return veh.getLastServiceDate();
    }
   
    
    public String getMake(){
        return veh.getMake();
    }
    
    /*********************************
    *       BOOKING GETTERS
    **********************************/
    
        public int getID(){
		return bk.getID();
	}
	public String getType(){
		return bk.getType();
	}
	public String getDate(){
		return bk.getDate();
	}
	public String getDuration(){
		return bk.getDuration();
	}
	
	public int getMechID(){
		return bk.getMechID();
	}
	public String getMechDuration(){
		return bk.getMechDuration();
	}
        
}
