/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customers;

public class Bill {
    final private int mechanicCost;
    final private int partsCost;
    final private int spcCost;
    final private int totalCost;
    final private boolean settled;

    public Bill(int mechanicCost, int partsCost, int spcCost, int totalCost, boolean settled) {
        this.mechanicCost=mechanicCost;
        this.partsCost=partsCost;
        this.spcCost=spcCost;
        this.totalCost=totalCost;
        this.settled=settled;
    }
    
    public int getMechanicCost() {
        return mechanicCost;
    }

    public int getPartsCost() {
        return partsCost;
    }

    public int getSpcCost() {
        return spcCost;
    }

    public boolean getSettled() {
        return settled;
    }
    
}
