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
public class UnderWarranty implements VehicleWarrantyRole{
    private String company;
    private String expDate;
    private String address;
    
    public UnderWarranty(String company, String expDate, String address){
        this.address=address;
        this.company=company;
        this.expDate=expDate;
    }
    public String getAddress(){
        return this.address;
    }
    public String getCompany(){
        return this.company;
        
    }
    public String getExpDate(){
        return this.expDate;
    }
    
}
