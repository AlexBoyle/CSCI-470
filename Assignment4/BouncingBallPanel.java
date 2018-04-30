import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 * This Class assembles the right panel
 *
 */
public class BouncingBallPanel extends JPanel implements ActionListener{
	// Class Vars
	private static final long serialVersionUID = 10L;
	public JButton button = new JButton("Start");
	public JButton button1 = new JButton("Stop");
	public static JTextArea textarea = new JTextArea(8,40);
	public AnimationPanel ani;
	
	// Setup this class as a panel
	public BouncingBallPanel() {
    	JPanel panel2 = new JPanel();
    	JPanel panel3 = new JPanel();
    	
    	// create animation panel
    	ani = new AnimationPanel();
    	// Start animation
    	ani.start();
    	setLayout(new BorderLayout ());
    	panel2.setLayout(new FlowLayout());
    	
    	// Setup listener for buttons
    	button.addActionListener(this);
    	button1.addActionListener(this);
    	
    	// Assemble panel
    	panel2.add(button);
    	panel2.add(button1);
    	panel2.setPreferredSize(new Dimension(350, 100));
    	panel3.add(panel2);
    	add(panel3, BorderLayout.PAGE_START);
    	add(ani, BorderLayout.CENTER);
	}

	/**
	 * Checks for pressed buttons
	 */
	public void actionPerformed(ActionEvent a) {
		// Check what button was pressed and execute the correct function
		switch(a.getActionCommand()) {
		case "Start":
			ani.start();
			break;
		case "Stop":
			ani.stop();
			break;
		}
	}
	

}
