/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customers;

import diagrep.Diagrep;

public class BillWrapper {

    final private Bill bill;
    final private Diagrep booking;

    public BillWrapper(Bill bill, Diagrep booking) {
        this.bill = bill;
        this.booking = booking;
    }

    public Bill getBill() {
        return bill;
    }

    public Diagrep getBooking() {
        return booking;
    }

    /**
     * ************************
     */
    public int getMechanicCost() {
        return bill.getMechanicCost();
    }

    public int getPartsCost() {
        return bill.getPartsCost();
    }

    public int getSpcCost() {
        return bill.getSpcCost();
    }

    public int getTotalCost() {
        return (bill.getMechanicCost()+bill.getPartsCost()+bill.getSpcCost());
                
    }

    public String getSettled() {
        boolean b = bill.getSettled();
        if (b) {
            return "Settled";
        } else {
            return "Not Settled";
        }
    }

    /**
     * ****************************
     */
    public String getType() {
        return booking.getType();
    }

    public String getDate() {
        return booking.getDate();
    }

    public String getVehReg() {
        return booking.getVehReg();
    }
}
