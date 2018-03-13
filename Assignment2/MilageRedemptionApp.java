import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MilageRedemptionApp extends JFrame implements ActionListener, ListSelectionListener {
	//Eclipse wants this here. Sure, why not
	private static final long serialVersionUID = 1L;
	
	// Class Vars
	private String[] cities;
	private ArrayList<JTextArea> textAreaPool = new ArrayList<JTextArea>();
	private MilesRedeemer service = new MilesRedeemer();
	private int lastIndex = -1;
	private JSpinner spinner;
	
	// Set month array
	private final String[] MONTHS = new String[] {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
	
	/**
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		// Start the application
        EventQueue.invokeLater(() -> {
        	new MilageRedemptionApp("./src/temp.txt");
        });
	}
	
	/**
	 * Main driver for the Mileage Redemption App
	 * @param filePath path to file you want to use
	 */
	public MilageRedemptionApp(String filePath) {
		super("Ticket Redeemer");
		File file = new File(filePath);
		Scanner scanner = null;
		try {
			scanner = new Scanner(file);
		} catch (FileNotFoundException e) {}
		this.service.readDestinations(scanner);
		
		// Set up the window.
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());
        // Set the layout.
        setupLayout();

        //Display the window.
        setMinimumSize(new Dimension(950, 350));
        setLocationRelativeTo(null);
        setVisible(true);
	}
	

    private void setupLayout() {
    	// Initialization
    	JPanel content = new JPanel();
    	JPanel panel1 = new JPanel();
    	JPanel panel2 = new JPanel();
    	JPanel panel3 = new JPanel();
    	JPanel panel4 = new JPanel();
    	JPanel panel5 = new JPanel();
    	JPanel panel6 = new JPanel();
    	JPanel panel7 = new JPanel();
    	JPanel panel8 = new JPanel();
    	panel1.setBackground(new Color(176,224,230));
    	panel2.setBackground(Color.PINK);
    	panel3.setBackground(new Color(176,224,230));
    	panel4.setBackground(Color.PINK);
    	panel5.setBackground(Color.PINK);
    	panel6.setBackground(Color.PINK);
    	panel7.setBackground(Color.PINK);
    	panel8.setBackground(Color.PINK);
    	
    	// Layouts
    	content.setLayout(new BorderLayout());
    	panel1.setLayout(new GridLayout(2,1,11,11));
        panel2.setLayout(new BoxLayout(panel2, BoxLayout.PAGE_AXIS));
        panel3.setLayout(new GridLayout(4,2,10,7));
    	panel4.setLayout(new GridLayout(3,1));
        panel5.setLayout(new FlowLayout());
        panel6.setLayout(new FlowLayout());
        panel7.setLayout(new FlowLayout());
    	
    	// Other Setup
    	cities = this.service.getCityNames();
        JList<String> list = new JList<String>(cities);
        list.addListSelectionListener(this);
        list.setFixedCellWidth(300);
        list.setVisibleRowCount(9);
        String[] MONTHStrings = MONTHS; //get month names
        SpinnerListModel monthModel = new SpinnerListModel(MONTHStrings);
        spinner = new JSpinner(monthModel);
        JButton button = new JButton("Redeem tickets >>>");
        button.addActionListener(this);

        //TextPool Setup
        textAreaPool.add(new JTextArea());
        textAreaPool.add(new JTextArea());
        textAreaPool.add(new JTextArea());
        textAreaPool.add(new JTextArea());
        textAreaPool.add(new JTextArea(1,8));
        textAreaPool.add(new JTextArea(1,8));
        textAreaPool.add(new JTextArea(8,40));
        textAreaPool.get(0).setEditable(false);
        textAreaPool.get(1).setEditable(false);
        textAreaPool.get(2).setEditable(false);
        textAreaPool.get(3).setEditable(false);
        textAreaPool.get(5).setEditable(false);
        textAreaPool.get(6).setEditable(false);
        
        // Border Setup
        panel1.setBorder(BorderFactory.createTitledBorder("List of destination cities"));
        panel2.setBorder(BorderFactory.createTitledBorder("Redeem Tickets"));
        panel3.setBorder(BorderFactory.createEmptyBorder(10, 10, 8, 8));
        
        // Connect content through adds
        panel3.add(new JLabel("Required Miles"));
        panel3.add(textAreaPool.get(0));
        panel3.add(new JLabel("Miles For Upgrading"));
        panel3.add(textAreaPool.get(1));
        panel3.add(new JLabel("Miles For SuperSaver"));
        panel3.add(textAreaPool.get(2));
        panel3.add(new JLabel("MONTHS For SuperSaver"));
        panel3.add(textAreaPool.get(3));
        panel5.add(new JLabel("Your Accumulated Miles: "));
        panel5.add(textAreaPool.get(4));
        panel6.add(new JLabel("Month Of Depature: "));
        panel8.add(new JLabel("Your Remaining Miles: "));
        panel8.add(textAreaPool.get(5));
        panel6.add(spinner);
        panel7.add(button);
        panel2.add(panel4,BorderLayout.PAGE_START);
        panel1.add(list, BorderLayout.PAGE_START);
        panel1.add(panel3,BorderLayout.CENTER);
        panel2.add(panel5);
        panel2.add(panel6);
        panel2.add(panel7);
        panel2.add(textAreaPool.get(6), BorderLayout.CENTER);
        panel2.add(panel8, BorderLayout.PAGE_END);
        content.add(panel1, BorderLayout.LINE_START);
        content.add(panel2, BorderLayout.CENTER);
        
        add(content);
    }
    /**
     * This action listener looks at the redeem button
     * When the button is clicked, the redeemMiles function is called
     * and an appropriate output is generated 
     */
    public void actionPerformed(ActionEvent e) {
    	int amount = 0;
    	// Check if an amount was given, if no valid amount was givin, input = 0
    	try {
    		amount = Integer.parseInt(this.textAreaPool.get(4).getText());
    	}
    	catch (Exception e1) {}
    	// redeem miles
    	ArrayList<String> temp= service.redeemMiles(amount,java.util.Arrays.asList(MONTHS).indexOf((String)spinner.getValue()) + 1);
    	String out = "\nYour Accumulated miles can be usedto redeem the following air tickets: \n\n";
    	for(int i = 0; i < temp.size(); i ++) {
    		out += temp.get(i) + "\n";
    	}
    	textAreaPool.get(6).setText(out);
    	textAreaPool.get(5).setText(service.getRemainingMiles() + "");
    }

	/**
	 * This value change listener looks at the citie list
	 * when a city is selected, its information is displayed below
	 */
	public void valueChanged(ListSelectionEvent e) {
		// Check for new value
		if(e.getValueIsAdjusting()) {
			// Use correct index
			int index = e.getLastIndex();
			if(lastIndex == index) {
				index = e.getFirstIndex();
			}
			lastIndex = index;
			
			//get and display city information
			Destination d = service.getDestinationByName(cities[index]);
			textAreaPool.get(0).setText(String.valueOf(d.getNormalMileage()));
			textAreaPool.get(1).setText(String.valueOf(d.getAdditionalMileage()));
			textAreaPool.get(2).setText(String.valueOf(d.getSupersaverMileage()));
			textAreaPool.get(3).setText(MONTHS[d.getStartMonth() -1 ] + " to " + MONTHS[d.getEndMonth() - 1]);
			
		}
	}

}
