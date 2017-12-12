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
public class StockView {    
    private int partID;
    
    private String deliveryDate;
    private int deliveryQuantity;
    
    private String withdrawDate;
    private int withdrawQuantity;
    
    
    
    public StockView(int id, String date, int quantity, boolean b){
        this.partID = id;
        
        if(b){
            this.deliveryDate = date;
            this.deliveryQuantity = quantity;    
        }
                  
        this.withdrawDate = date;
        this.withdrawQuantity = quantity;
    }
/*    
    public static StockView delivery(String date, int quantity){
        return new StockView(date, quantity);
    }
    
    public static StockView withdraw(String date, int quantity){
        return new StockView(date, quantity);
    }
*/   
    
    public int getPartID(){
        return this.partID;
    }
    
    public String getDeliveryDate(){
        return this.deliveryDate;
    }
    
    public int getDeliveryQuantity(){
        return this.deliveryQuantity;
    }
    
    public String getWithdrawDate(){
        return this.withdrawDate;
    }
    
    public int getWithdrawQuantity(){
        return this.withdrawQuantity;
    }
    
}
