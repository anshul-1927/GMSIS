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
public  class Vehicle {
    
    private String regNum;
    private String model;
    private String make;
    private String engineSize;
    private String fuelType;
    private String colour;
    private String motDate;
    private String mileage;
    private String lastServiceDate;
    private String vehicleType;
    private VehicleWarrantyRole playerRole;
    
    
    public Vehicle(String type, String registrationNumber, String manufacturer, String model, String engineSize, String fuelType, String colour, String motDate, String mileage, String lastServiceDate, VehicleWarrantyRole role){
        this.vehicleType=type;
        this.regNum=registrationNumber;
        this.make=manufacturer;
        this.model=model;
        this.engineSize=engineSize;
        this.fuelType=fuelType;
        this.colour=colour;
        this.motDate=motDate;
        this.mileage=mileage;
        this.lastServiceDate=lastServiceDate;
        this.playerRole=role;
        
           
    }
    
    /***************************************************************************************
    * GETTERS AND SETTERS FOR VEHICLE INSTANCE VARIABLES
    *****************************************************************************************/
    
    public VehicleWarrantyRole getWarrantyRole(){
        return playerRole;
    }
    
    public void setVehicleType(String type){
        this.vehicleType=type;
    }
    public String getVehicleType(){
        return this.vehicleType;
    }
    
    public void setRegNum(String regnum){
        this.regNum=regnum;
    }
    public String getRegNum(){
        return this.regNum;
    }
    
    public void setModel(String model){
        this.model=model;
    }
    public String getModel(){
        return this.model;
    }
    
    public String getMileage(){
        return this.mileage;
    }
    public void setMilage(String milage){
        this.mileage=milage;
    }
    
    public void setColour(String col){
        this.colour=col;
    }
    public String getCol(){
        return this.colour;
    }
    
    public void setEngSize(String eng){
        this.engineSize=eng;
    }
    public String getEngSize(){
        return this.engineSize;
    }
    
    public void setFuelType(String fuel){
        this.fuelType=fuel;
    }
    public String getFuel(){
        return this.fuelType;
    }
    
    public void setMoT(String mot){
        this.motDate=mot;
    }
    public String getMoT(){
        return this.motDate;
    }
    
    public String getLastServiceDate(){
        return this.lastServiceDate;
    }
    public void setLastServiceDate(String last){
        this.lastServiceDate=last;
    }
    public void setMake(String make){
        this.make=make;
    }
    public String getMake(){
        return this.make;
    }
    public String toString(){
        return this.getMake()+" "+this.getModel();
    }
    
}
