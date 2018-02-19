import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Collections;
import java.util.Scanner;

/**
 * This class creates, keeps track of, and calculate mile redemption
 * @author MLB
 *
 */
public class MilesRedeemer {
	List<Destination> destinations;
	int miles;
	
	/**
	 * Constructor that is called on object creation
	 */
	public MilesRedeemer () {
		this.destinations = new ArrayList<Destination>();
	};
	
	/**
	 * Populate this service with a file scanner
	 * @param fileScanner the scanner that is reading the file with relevant information in it
	 */
	public void readDestinations(Scanner fileScanner) {
		// while there are lines to read, read them
		while (fileScanner.hasNextLine()) {
			// create a new destination object
			this.destinations.add(new Destination(fileScanner.nextLine()));
		}
		Collections.sort(this.destinations, new MileageComparator());
	}
	/**
	 * Get city names
	 * @return Array of city names in alphabetical order
	 */
	public String[] getCityNames() {
		//create output array
		String[] cityList = new String[this.destinations.size()];
		// Get all city names
		for(int i = 0; i < this.destinations.size(); i ++) {
			cityList[i] = this.destinations.get(i).getCity();
		}
		// Sort names alphabetically
		Arrays.sort(cityList);
		// Return array
		return cityList;
	}
	
	/**
	 * @param miles Miles to be redeemed
	 * @param month Month of departure
	 * @return ArrayList<String> of ticket information
	 */
	public ArrayList<String> redeemMiles(int miles, int month) {
		// List of final output
		ArrayList<String> output = new ArrayList<String>();
		// Temporary list for moving data
		ArrayList<Destination> temp = new ArrayList<Destination>();
		// Get all ticket possible starting with farthest
		for(int i = 0; i < this.destinations.size(); i ++) {
			Destination d  =this.destinations.get(i);
			int cost = d.getNormalMileage() ;
			// If leaving in a month where supersaver applies, apply discount
			if(month >= d.getStartMonth() && month <= d.getEndMonth()) {
				cost = d.getSupersaverMileage();
			}
			// Add flight to temp if miles apply
			if(miles > cost) {
				miles -= cost;
				temp.add(d);
			}
		}
		// Check for upgrades and format output
		for(int i = 0; i < temp.size(); i++) {
			// If can upgrade, do it
			if(miles > temp.get(i).getAdditionalMileage()) {
				miles -= temp.get(i).getAdditionalMileage();
				System.out.println(miles);
				output.add("* A trip to " + temp.get(i).getCity() + ", first class");
			}
			// Else here is a normal economy ticket
			else {
				output.add("* A trip to " + temp.get(i).getCity() + ", economy class");
			}
		}
		// Set internal miles left
		this.miles = miles;
		// Return
		return output;
	}
	
	/**
	 * @return Miles left after redemption 
	 */
	public int getRemainingMiles() {return miles;}
}
