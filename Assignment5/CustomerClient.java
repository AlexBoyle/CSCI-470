import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.*;

/**
 * This is the main driver class behind the client
 *
 */
public class CustomerClient extends JFrame implements ActionListener {

	// GUI components
	private JButton connectButton = new JButton("Connect");
	private JButton getAllButton = new JButton("Get All");
	private JButton addButton = new JButton("Add");
	private JButton deleteButton = new JButton("Delete");
	private JButton updateButton = new JButton("Update Address");
	
	private JLabel notification = new JLabel("Client Started");
	private JTextArea addrBox = new JTextArea();
	private JTextArea zipBox = new JTextArea();
	private JTextArea ssnBox = new JTextArea();
	private JTextArea nameBox = new JTextArea();
	private JTextArea bigBox = new JTextArea(8,40);
	private Socket socket;
	private ObjectInputStream in;
	private ObjectOutputStream out;

	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			CustomerClient client = new CustomerClient();
			client.createAndShowGUI();
		});
	}

	/**
	 * Called when this object is created
	 */
	private CustomerClient() {
		super("Customer Database");
	}

	/**
	 * This sets up all GUI elements
	 */
	private void createAndShowGUI() {
		// Panel set up
		JPanel panel0 = new JPanel();
    	JPanel panel1 = new JPanel();
    	JPanel panel2 = new JPanel();
    	JPanel panel3 = new JPanel();
    	JPanel panel4 = new JPanel();
    	
    	
    	setLayout(new BorderLayout ());
    	panel0.setLayout(new GridLayout(2,4,11,11));
    	panel2.setLayout(new FlowLayout());
    	
    	// Input setup
    	panel0.add(new JLabel("Name:"));
    	panel0.add(nameBox);
    	panel0.add(new JLabel("SSN:"));
    	panel0.add(ssnBox);
    	panel0.add(new JLabel("Address:"));
    	panel0.add(addrBox);
    	panel0.add(new JLabel("Zip Code:"));
    	panel0.add(zipBox);
    	
    	// Add listeners
    	connectButton.addActionListener(this);
    	getAllButton.addActionListener(this);
    	addButton.addActionListener(this);
    	updateButton.addActionListener(this);
    	deleteButton.addActionListener(this);
    	
    	//Set default button states
    	connectButton.setEnabled(true);
    	getAllButton.setEnabled(false);
    	addButton.setEnabled(false);
    	updateButton.setEnabled(false);
    	deleteButton.setEnabled(false);
    	
    	// Add buttons
    	panel2.add(connectButton);
    	panel2.add(getAllButton);
    	panel2.add(addButton);
    	panel2.add(deleteButton);
    	panel2.add(updateButton);
    	
    	// Put visuals together
    	panel3.setLayout(new BorderLayout());
    	panel4.setLayout(new BorderLayout());
    	panel3.add(panel0, BorderLayout.PAGE_START);
    	panel3.add(panel1, BorderLayout.CENTER);
    	panel3.add(panel2, BorderLayout.PAGE_END);
    	add(notification);
    	panel4.add(panel3, BorderLayout.CENTER);
    	panel4.add(notification,BorderLayout.PAGE_END);
    	add(panel4,BorderLayout.PAGE_START);
    	add(new JScrollPane(bigBox), BorderLayout.CENTER);
    	
    	setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    	setMinimumSize(new Dimension(600, 400));
        setLocationRelativeTo(null);
        setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//Clear text
		bigBox.setText("");
		notification.setText("");
		
		// send correct msg to server
		if (e.getActionCommand().equals("Connect")) {
			connect();
		} else if (e.getActionCommand().equals("Disconnect")) {
			disconnect();
		} else if (e.getSource() == getAllButton) {
			handleGetAll();
		} else if (e.getSource() == addButton) {
			handleAdd();
		} else if (e.getSource() == updateButton) {
			handleUpdate();
		} else if (e.getSource() == deleteButton) {
			handleDelete();
		}
	}

	/**
	 * Connect to server + connection setup
	 */
	private void connect() {
		try {
			// Replace 97xx with your port number
			socket = new Socket("hopper.cs.niu.edu", 9712);

			System.out.println("LOG: Socket opened");

			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());

			System.out.println("LOG: Streams opened");

			connectButton.setText("Disconnect");
			// Enable buttons
			getAllButton.setEnabled(true);
	    	addButton.setEnabled(true);
	    	updateButton.setEnabled(true);
	    	deleteButton.setEnabled(true);

		} catch (UnknownHostException e) {
			System.err.println("Exception resolving host name: " + e);
		} catch (IOException e) {
			System.err.println("Exception establishing socket connection: " + e);
		}
	}

	/**
	 * Disconnect from server
	 */
	private void disconnect() {
		connectButton.setText("Connect");
		
		// Disable buttons
		getAllButton.setEnabled(false);
    	addButton.setEnabled(false);
    	updateButton.setEnabled(false);
    	deleteButton.setEnabled(false);
		try {
			socket.close();
		} catch (IOException e) {
			System.err.println("Exception closing socket: " + e);
		}
	}

	/**
	 * send GET ALL command
	 */
	private void handleGetAll() {
		try {
			out.writeObject("GETALL");
			while(true) {
				String output = (String)in.readObject();
				if (output != null) {
					int count = 0;
					for (int i = 0; i < output.length(); i++) {
					    if (output.charAt(i) == '\n') {
					        count++;
					    }
					}
					notification.setText(count + " Items gotten");
					bigBox.setText(output);
					break;
				}
			}
		} catch (IOException e) {
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Send ADD command
	 */
	private void handleAdd() {
		// Data validation
		if(nameBox.getText().equals("")) {notification.setText("Name is not defined");return;}
		if(ssnBox.getText().equals("")) {notification.setText("SSN is not defined");return;}
		if(addrBox.getText().equals("")) {notification.setText("Address is not defined");;return;}
		if(zipBox.getText().equals("")) {notification.setText("Zip Code is not defined");return;}
		if(!ssnBox.getText().matches("\\d{3}-\\d{2}-\\d{4}")){notification.setText("SSN does not follow the format 'xxx-xx-xxxx'");}
		if(nameBox.getText().length() > 20) {notification.setText("Name is to long");return;}
		if(addrBox.getText().length() > 40) {notification.setText("Address is to long");return;}
		if(zipBox.getText().length() > 5) {notification.setText("Zip Code is to long");return;}
		Pattern p = Pattern.compile("^\\d{3}-\\d{2}-\\d{4}$");
		Matcher m = p.matcher(ssnBox.getText());
		if(!m.matches()){notification.setText("SSN is invalid");return;}
		String send = 
				"ADD {name: '" + nameBox.getText() + 
				"', ssn: '" + ssnBox.getText() + 
				"', addr: '" + addrBox.getText() + 
				"', zip: '" + zipBox.getText() + "'}";
		try {
			//send command
			out.writeObject(send);
			// wait for response
			while(true) {
				String output = (String)in.readObject();
				if (output != null) {
					notification.setText(output);
					break;
				}
			}
		} catch (IOException | ClassNotFoundException e) {
		}
	}

	/**
	 * send Delete command
	 */
	private void handleDelete() {
		//validation
		if(ssnBox.getText().equals("")) {notification.setText("SSN is not defined");return;}
		Pattern p = Pattern.compile("^\\d{3}-\\d{2}-\\d{4}$");
		Matcher m = p.matcher(ssnBox.getText());
		if(!m.matches()){notification.setText("SSN is invalid");return;}
		try {
			//Send command
			out.writeObject("DELETE {ssn: '" + ssnBox.getText() + "'}");
			//wait for resonse
			while(true) {
				String output = (String)in.readObject();
				if (output != null) {
					notification.setText(output);
					break;
				}
			}
		} catch (IOException e) {
		} catch (ClassNotFoundException e) {
		}
	}
	
	/**
	 * send update command
	 */
	private void handleUpdate() {
		//validation
		if(ssnBox.getText().equals("")) {notification.setText("SSN is not defined");return;}
		if(addrBox.getText().equals("")) {notification.setText("Address is not defined");;return;}
		if(addrBox.getText().length() > 40) {notification.setText("Address is to long");return;}
		Pattern p = Pattern.compile("^\\d{3}-\\d{2}-\\d{4}$");
		Matcher m = p.matcher(ssnBox.getText());
		if(!m.matches()){notification.setText("SSN is invalid");return;}
		try {
			// Send command
			out.writeObject("UPDATE {ssn: '" + ssnBox.getText() + "', addr: '" + addrBox.getText() + "'}");
			//Wait for response
			while(true) {
				String output = (String)in.readObject();
				if (output != null) {
					notification.setText(output);
					System.out.println(output + "a");
					break;
				}
			}
		} catch (IOException e) {
		} catch (ClassNotFoundException e) {
		}
	}
}