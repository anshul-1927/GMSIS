/**
 *
 * @author Nexus
 */
package diagrep;

public class Part
{
	private final int id;
	private final String name;
	private final String des;
	private final String date;
	private final String cost;
	
	public Part(int id, String name, String des, String date, String cost)
	{
		this.id = id;
		this.name = name;
		this.des = des;
		this.date = date;
		this.cost = cost;
	}
	
	public String getName() {
		return name;
	}

	public String getDes() {
		return des;
	}

	public String getDate() {
		return date;
	}

	public int getId() {
		return id;
	}

	public String getCost() {
		return cost;
	}
}
