/**
 *
 * @author Nexus
 */
package diagrep;

public class Mechanic
{
	private final String id;
	private final String mechFirstName;
	private final String mechLastName;
	private final int mechHourlyRate;
	
	public Mechanic(String id, String mechFirstName, String mechLastName, int mechHourlyRate)
	{
		this.id = id;
		this.mechFirstName = mechFirstName;
		this.mechLastName = mechLastName;
		this.mechHourlyRate = mechHourlyRate;
	}

	public String getId() {
		return id;
	}

	public String getMechFirstName() {
		return mechFirstName;
	}

	public String getMechLastName() {
		return mechLastName;
	}

	public int getMechHourlyRate() {
		return mechHourlyRate;
	}
	
}
