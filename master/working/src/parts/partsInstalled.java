/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parts;

/**
 *
 * @author Mario_
 */
public class partsInstalled {
    /*
    private final int partID;
    private final String partName;
    private final String installDate;
    private final String warrantyDate;
    */
    private final int custID;
    private final String custFName;
    private final String custSName;
    private final String vehicleRegNo;
    //private final String vehicleModel;
    //private final String vehicleMake;
    private final int bookingID;
    private final String bookingType;
    private final String bookingDate;
   /*
    public partsInstalled(int partID, String partName, String installDate, String warrantyDate, int custID, String custFName, String custSName, String vehicleRegNo, int bookingID, String bookingType, String bookingDate){
       this.partID = partID;
       this.partName = partName;
       this.installDate = installDate;
       this.warrantyDate = warrantyDate;
       this.custID = custID;
       this.custFName = custFName;
       this.custSName = custSName;
       this.vehicleRegNo = vehicleRegNo;
       this.bookingID = bookingID;
       this.bookingType = bookingType;
       this.bookingDate = bookingDate;
    }
    */
    public partsInstalled( int custID, String custFName, String custSName, String vehicleRegNo, int bookingID, String bookingType, String bookingDate){
       /*
        this.partID = partID;
       this.partName = partName;
       this.installDate = installDate;
       this.warrantyDate = warrantyDate;
       */
       this.custID = custID;
       this.custFName = custFName;
       this.custSName = custSName;
       this.vehicleRegNo = vehicleRegNo;
       //this.vehicleModel = vehicleModel;
       //this.vehicleMake = vehicleMake;
       this.bookingID = bookingID;
       this.bookingType = bookingType;
       this.bookingDate = bookingDate;
    }
    
 
 /*   
    public int getPartID(){
        return this.partID;
    }
    
    public String getPartName(){
        return this.partName;
    }
    
    public String getInstallDate(){
        return this.installDate;
    }
    
    public String getWarrantyDate(){
        return this.warrantyDate;
    }
 */   
    public int getCustID(){
        return this.custID;
    }
    
    public String getCustFName(){
        return this.custFName;
    }
    
    public String getCustSName(){
        return this.custSName;
    }
    
    public String getVehicleRegNo(){
        return this.vehicleRegNo;
    }
   /* 
    public String getVehicleModel(){
        return this.vehicleModel;
    }
    
    public String getVehicleMake(){
        return this.vehicleMake;
    }
   */ 
    public int getBookingID(){
        return this.bookingID;
    }
    
    public String getBookingType(){
        return this.bookingType;
    }
    
    public String getBookingDate(){
        return this.bookingDate;
    }
}
