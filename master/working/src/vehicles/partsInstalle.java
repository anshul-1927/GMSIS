/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vehicles;

/**
 *
 * @author Mario_
 */
public class partsInstalle {
    private final int partID;
    private final String partName;
    private final String installDate;
    private final String warrantyDate;
    private final int custID;
    private final String custFName;
    private final String custSName;
    private final String vehicleRegNo;
    private final int bookingID;
    
    public partsInstalle(int partID,String partName, String installDate, String warrantyDate, int custID, String custFName, String custSName, String vehicleRegNo, int bookingID){
       this.partID = partID;
       this.partName = partName;
       this.installDate = installDate;
       this.warrantyDate = warrantyDate;
       this.custID = custID;
       this.custFName = custFName;
       this.custSName = custSName;
       this.vehicleRegNo = vehicleRegNo;
       this.bookingID = bookingID;
    }
    
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
    
    public int getCustID(){
        return this.custID;
    }
    
    public String getCustFName(){
        return this.custFName;
    }
    
    public String getCustSName(){
        return this.custSName;
    }
    
    public String
         getVehicleRegNo(){
        return this.vehicleRegNo;
    }
    
    public int getBookingID(){
        return this.bookingID;
    }
}
