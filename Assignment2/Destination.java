import java.util.Comparator;

/**
 * This class is a wrapper for flight destinations
 * @author MLB
 *
 */
public class Destination {
	// Class vars
	private String city;
	private int normalMileage;
	private int supersaverMileage;
	private int additionalMileage;
	private int startMonth;
	private int endMonth;
	
	/**
	 * Blank constructor
	 */
	public Destination () {
		this("", 0, 0, 0, 0, 0);
	}
	
	/**
	 * Copy constructor
	 * @param dest 
	 */
	public Destination(Destination dest) {
		this.setCity(dest.getCity());
		this.setNormalMileage(dest.getNormalMileage());
		this.setSupersaverMileage(dest.getSupersaverMileage());
		this.setAdditionalMileage(dest.getAdditionalMileage());
		this.setStartMonth(dest.getStartMonth());
		this.setEndMonth(dest.getEndMonth());
	}
	
	/**
	 * Parameterized constructor
	 * @param city 
	 * @param normalMileage
	 * @param supersaverMileage
	 * @param additionalMileage
	 * @param startMonth
	 * @param endMonth
	 */
	public Destination (String city, int normalMileage, int supersaverMileage, int additionalMileage, int startMonth, int endMonth) {
		this.setCity(city);
		this.setNormalMileage(normalMileage);
		this.setSupersaverMileage(supersaverMileage);
		this.setAdditionalMileage(additionalMileage);
		this.setStartMonth(startMonth);
		this.setEndMonth(endMonth);
	}
	
	/**
	 * Generate constructor
	 * @param str Given a descriptive string, this object will be populated with equivelent information
	 */
	public Destination (String str) {
		// Split up the relevent data
		String[] splitData = str.split(";");
		// Split up the relevent date ranges
		String[] splitDate = splitData[4].split("-");
		// Set relevent data
		this.setCity(splitData[0]);
		this.setNormalMileage(Integer.parseInt(splitData[1]));
		this.setSupersaverMileage(Integer.parseInt(splitData[2]));
		this.setAdditionalMileage(Integer.parseInt(splitData[3]));
		this.setStartMonth(Integer.parseInt(splitDate[0]));
		this.setEndMonth(Integer.parseInt(splitDate[1]));
		
	}
	
	
	/**
	 * @return this.city
	 */
	public String getCity() {return this.city;}
	/**
	 * @return this.normalMileage
	 */
	public int getNormalMileage() {return this.normalMileage;}
	/**
	 * @return this.supersaverMileage
	 */
	public int getSupersaverMileage() {return this.supersaverMileage;}
	/**
	 * @return this.additionalMileage
	 */
	public int getAdditionalMileage() {return this.additionalMileage;}
	/**
	 * @return this.startMonth
	 */
	public int getStartMonth() {return this.startMonth;}
	/**
	 * @return this.endMonth
	 */
	public int getEndMonth() {return this.endMonth;}
	/**
	 * @param city sets this.city
	 */
	public void setCity(String city) {this.city = city;}
	/**
	 * @param normalMileage sets this.normalMileage
	 */
	public void setNormalMileage(int normalMileage) {this.normalMileage = normalMileage;}
	/**
	 * @param supersaverMileage sets this.supersaverMileage
	 */
	public void setSupersaverMileage(int supersaverMileage) {this.supersaverMileage = supersaverMileage;}
	/**
	 * @param additionalMileage sets this.additionalMileage
	 */
	public void setAdditionalMileage(int additionalMileage) {this.additionalMileage = additionalMileage;}
	/**
	 * @param startMonth sets this.startMonth
	 */
	public void setStartMonth(int startMonth) {this.startMonth = startMonth;}
	/**
	 * @param endMonth sets this.endMonth
	 */
	public void setEndMonth(int endMonth) {this.endMonth = endMonth;}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {return this.city + ";" + this.normalMileage + ";" + this.supersaverMileage + ";" + this.additionalMileage + ";" + this.startMonth + "-" + this.endMonth;}
}

/**
 * This class is used to sort the destination class by miles
 * @author MLB
 *
 */
class MileageComparator implements Comparator<Destination> {
	 public int compare(Destination d1, Destination d2) {
	 return (d2.getNormalMileage() - d1.getNormalMileage());
	 }
	}

