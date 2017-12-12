/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parts;

/**
 *
 * @author Mario
 */
public class BookingView {
    private final String bookingDate;
    private final String bookingType;
    
    public BookingView(String bDate, String bType){
        this.bookingDate = bDate;
        this.bookingType = bType;
    }
    
    
    public String getBookingDate(){
        return this.bookingDate;
    }
    
    public String getBookingType(){
        return this.bookingType;
    }
    
}
