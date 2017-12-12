/**
 * @author Lloyd
 */

package specialist;


public class SPC {
    
    private final String id;
    private String name;
    private String address;
    private String phoneNo;
    private String email;
    private int inUse;
    
    public SPC(String id, String name, String address, String phone, String email, int inUse){
        this.id = id;
        this.name = name;
        this.address = address;
        this.phoneNo = phone;
        this.email = email;
        this.inUse = inUse;
    }
    
    public int getInUse(){
        return this.inUse;
    }
    
    public String getId(){
        return this.id;
    }
    
    public String getName(){
        return this.name;
    }
    
    public String getAddress(){
        return this.address;
    }
    
    public String getPhoneNo(){
        return this.phoneNo;
    }
    
    public String getEmail(){
        return this.email;
    }
    
}
