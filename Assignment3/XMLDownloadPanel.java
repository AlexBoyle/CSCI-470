import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class XMLDownloadPanel extends JPanel {
	// Class Vars
	private static final long serialVersionUID = 1L;
	public JComboBox<String> type;
	public JComboBox<String> limit;
	public JComboBox<String> explicit;
	public JButton button = new JButton("Get Albums");
	public static JTextArea textarea = new JTextArea(8,40);
	
	// Setup this class as a panel
	public XMLDownloadPanel() {
		JPanel panel0 = new JPanel();
    	JPanel panel1 = new JPanel();
    	JPanel panel2 = new JPanel();
    	JPanel panel3 = new JPanel();
    	
    	
    	setLayout(new BorderLayout ());
    	panel0.setLayout(new GridLayout(1,3,11,11));
    	panel1.setLayout(new GridLayout(1,3,11,11));
    	panel2.setLayout(new FlowLayout());
    	
    	type = new JComboBox<String>(new String[] {"New Music", "Recent Releases", "Top Albums"});
    	limit = new JComboBox<String>(new String[] {"10", "25", "50", "100"});
    	explicit = new JComboBox<String>(new String[] {"Yes", "No"});
    	
    	panel0.add(new JLabel("Type:"));
    	panel1.add(type);
    	panel0.add(new JLabel("Limit:"));
    	panel1.add(limit);
    	panel0.add(new JLabel("Explicit:"));
    	panel1.add(explicit);
    	
    	panel2.add(button, BorderLayout.CENTER);
    	panel3.setLayout(new GridLayout(3,1,0,0));
    	panel3.add(panel0);
    	panel3.add(panel1);
    	panel3.add(panel2);
    	add(panel3, BorderLayout.PAGE_START);
    	add(new JScrollPane(textarea), BorderLayout.CENTER);
	}

}
