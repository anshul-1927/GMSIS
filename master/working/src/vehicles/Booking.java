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
public class Booking {
        private  int id;
	private  String type;
	private  String date;
	private  String duration;
	private  int mechID;
	private  String mechDuration;
        private  int bill;

	public Booking (int id, String type, String date, String duration, int mechID, String mechDuration, int bill){
		this.id = id;
		this.type = type;
		this.date = date;
		this.duration = duration;
		this.mechID = mechID;
		this.mechDuration = mechDuration;
                this.bill=bill;
	}
	public int getBill(){
            return this.bill;
        }
	public int getID(){
		return this.id;
	}
        public void setID(int id){
            this.id=id;
        }
	public String getType(){
		return this.type;
	}
        
        public void setType(String tp){
            this.type=tp;
        }
	public String getDate(){
		return this.date;
	}
        public void setDate(String date){
            this.date=date;
        }
	public String getDuration(){
		return this.duration;
	}
	
	public int getMechID(){
		return this.mechID;
	}
	public String getMechDuration(){
		return this.mechDuration;
	}
}
