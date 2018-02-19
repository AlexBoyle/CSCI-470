import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class MilageRedemptionApp {
	// Is app running
	private boolean isRunning = true;
	
	/**
	 * Main driver for the Mileage Redemption App
	 * @param filePath path to file you want to use
	 */
	public MilageRedemptionApp(String filePath) {
		
		File file = new File(filePath);
		Scanner scanner = null;
		Scanner in = null;
		try {
			scanner = new Scanner(file);
			in = new Scanner(System.in);
		} catch (FileNotFoundException e) {}
		MilesRedeemer service = new MilesRedeemer();
		service.readDestinations(scanner);
		while(this.isRunning) {
			try {
				System.out.println("---------------------------------------------");
				System.out.println("List of destination cities you can travel to:");
				System.out.println("");
				String[] cities = service.getCityNames();
				for (int i = 0; i < cities.length; i ++) {
					System.out.println(cities[i]);
				}
				System.out.println("---------------------------------------------");
				System.out.print("Please input your total accumulated miles:");
				int totalMiles = Integer.parseInt(in.next());
				System.out.println("");
				System.out.print("Please input your month of departure (1-12):");
				int month = Integer.parseInt(in.next());
				ArrayList<String> desc = service.redeemMiles(totalMiles, month);
				for(String line : desc) {
					System.out.println(line);
				}
				System.out.println("");
				System.out.println("Your remaining miles: " + service.getRemainingMiles());
				System.out.println("");
				System.out.print("Do you want to continue (y/n)? ");
				char ans = in.next().charAt(0);
				if(ans == 'n') {
					this.isRunning = false;
				}
			} catch (Exception e) {
				this.isRunning = false;
				System.out.println(e);
			}
		}
	}
	
	/**
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		try {
		new MilageRedemptionApp(args[0]);
		} 
		catch(Exception e) {
			System.out.println("Program failed to Run");
		}
	}

}
