/**
 * @author Lloyd
 */

package specialist;

import java.time.LocalDate;

public class Specialist{

    private final String id, partId, spcId, bookId, customerId;
    private String customerFName, customerLName;
    private String vehicleRegNo, vehicleModel, vehicleMake;
    private String partName;
    private String bookingDate;
    private LocalDate returnDate;
    private LocalDate deliveryDate;
    private double cost;
    private String spcName;
    
    public Specialist(String id, String pid, String sid, String bid, String cid, String cFName, String cLName, String vRegNo, String vModel, String vMake, String partName, String bDate, LocalDate rDate, LocalDate dDate, double c, String spcName){
        this.id = id;
        this.partId = pid;
        this.spcId = sid;
        this.bookId = bid;
        this.customerId = cid;
        this.customerFName = cFName;
        this.customerLName = cLName;
        this.vehicleRegNo = vRegNo;
        this.vehicleModel = vModel;
        this.vehicleMake = vMake;
        this.partName = partName;
        this.bookingDate = bDate;
        this.returnDate = rDate;
        this.deliveryDate = dDate;
        this.cost = c;
        this.spcName = spcName;
    }
    
    public double getCost(){
        return this.cost;
    }
    
    public String getBookingDate(){
        return this.bookingDate;
    }
    
    public String getCustomerId(){
        return customerId;
    }
    
    public String getBookId(){
        return bookId;
    }
    
    public String getPartId() {
        return partId;
    }

    public String getSpcId() {
        return spcId;
    }
    
    public String getId(){
        return id;
    }

    public String getCustomerFName() {
        return customerFName;
    }

    public String getCustomerLName() {
        return customerLName;
    }

    public String getVehicleRegNo() {
        return vehicleRegNo;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public String getVehicleMake() {
        return vehicleMake;
    }

    public String getPartName() {
        return partName;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }

    public String getSpcName() {
        return spcName;
    }
 
}