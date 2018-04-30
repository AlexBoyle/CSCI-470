import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class XMLDownloader extends JFrame implements ActionListener {
	private static final long serialVersionUID = 2L;
	// Main panel for this app
	private XMLDownloadPanel panel = new XMLDownloadPanel();
	
	
	/**
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		// Start the application
        EventQueue.invokeLater(() -> {
        	new XMLDownloader();
        });
	}
	
	/**
	 * Main driver for the Mileage Redemption App
	 * @param filePath path to file you want to use
	 */
	public XMLDownloader() {
		super("ITunes Album Getter");
		// Set up the window.
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());
        // Set the layout.
        add(panel, BorderLayout.CENTER);
        add(new BouncingBallPanel(), BorderLayout.LINE_END);
        panel.button.addActionListener(this);
        //Display the window.
        setMinimumSize(new Dimension(1024, 650));
        setLocationRelativeTo(null);
        setVisible(true);
	}
    /**
     * This action listener looks at the redeem button
     * When the button is clicked, the redeemMiles function is called
     * and an appropriate output is generated 
     */
    public void actionPerformed(ActionEvent e) {
    	XMLDownloadTask service = new XMLDownloadTask();
    	panel.textarea.setText("");
    	service.setUrl(panel.type.getSelectedItem().toString().toLowerCase(), panel.limit.getSelectedItem().toString().toLowerCase(), panel.explicit.getSelectedItem().toString() == "Yes" ? "explicit" : "non-explicit");
    	service.execute();
    }
}
