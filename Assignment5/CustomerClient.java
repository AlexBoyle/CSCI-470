import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.*;

public class CustomerClient extends JFrame implements ActionListener {

	// GUI components
	private JButton connectButton = new JButton("Connect");
	private JButton getAllButton = new JButton("Get All");
	private JButton addButton = new JButton("Add");
	private JButton deleteButton = new JButton("Delete");
	private JButton updateButton = new JButton("Update Address");

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
		System.out.println("as");
		EventQueue.invokeLater(() -> {
			CustomerClient client = new CustomerClient();
			client.createAndShowGUI();
		});
	}

	private CustomerClient() {
		super("Customer Database");
	}

	private void createAndShowGUI() {
		JPanel panel0 = new JPanel();
    	JPanel panel1 = new JPanel();
    	JPanel panel2 = new JPanel();
    	JPanel panel3 = new JPanel();
    	
    	
    	setLayout(new BorderLayout ());
    	panel0.setLayout(new GridLayout(2,4,11,11));
    	panel2.setLayout(new FlowLayout());

    	
    	panel0.add(new JLabel("Name:"));
    	panel0.add(nameBox);
    	panel0.add(new JLabel("SSN:"));
    	panel0.add(ssnBox);
    	panel0.add(new JLabel("Address:"));
    	panel0.add(addrBox);
    	panel0.add(new JLabel("Zip Code:"));
    	panel0.add(zipBox);
    	
    	connectButton.addActionListener(this);
    	getAllButton.addActionListener(this);
    	addButton.addActionListener(this);
    	updateButton.addActionListener(this);
    	
    	panel2.add(connectButton);
    	panel2.add(getAllButton);
    	panel2.add(addButton);
    	panel2.add(deleteButton);
    	panel2.add(updateButton);
    	
    	panel3.setLayout(new GridLayout(3,1,0,0));
    	panel3.add(panel0);
    	panel3.add(panel1);
    	panel3.add(panel2);
    	add(panel3, BorderLayout.PAGE_START);
    	add(new JScrollPane(bigBox), BorderLayout.CENTER);
    	
    	//addActionListener(this);
    	setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    	setMinimumSize(new Dimension(600, 400));
        setLocationRelativeTo(null);
        setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		bigBox.setText("");
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

	private void connect() {
		try {
			// Replace 97xx with your port number
			//socket = new Socket("turing.cs.niu.edu", 9712);
			socket = new Socket("localhost", 9712);

			System.out.println("LOG: Socket opened");

			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());

			System.out.println("LOG: Streams opened");

			connectButton.setText("Disconnect");
			System.out.println("button");
			// Enable buttons
			

		} catch (UnknownHostException e) {
			System.err.println("Exception resolving host name: " + e);
		} catch (IOException e) {
			System.err.println("Exception establishing socket connection: " + e);
		}
	}

	private void disconnect() {
		connectButton.setText("Connect");
		
		// Disable buttons
		try {
			socket.close();
		} catch (IOException e) {
			System.err.println("Exception closing socket: " + e);
		}
	}

	private void handleGetAll() {
		try {
			out.writeObject("GET_ALL");
		} catch (IOException e) {
		}
	}

	private void handleAdd() {
		boolean valid = true;
		System.out.print(nameBox.getText());
		if(nameBox.getText() == "") {bigBox.setText(bigBox.getText() + "Name is not defined\n");valid = false;}
		if(ssnBox.getText() == "") {bigBox.setText(bigBox.getText() + "SSN is not defined\n");valid = false;}
		if(addrBox.getText() == "") {bigBox.setText(bigBox.getText() + "Address is not defined\n");valid = false;}
		if(zipBox.getText() == "") {bigBox.setText(bigBox.getText() + "Zip Code is not defined\n");valid = false;}
		if(valid) { 
			String send = 
					"ADD {name: '" + nameBox.getText() + 
					"', ssn: '" + ssnBox.getText() + 
					"', addr: '" + addrBox.getText() + 
					"', zip: '" + zipBox.getText() + "'}";
			try {
				out.writeObject(send);
			} catch (IOException e) {
			}
		}
	}

	private void handleDelete() {
		try {
			out.writeObject("DELETE ");
		} catch (IOException e) {
		}
	}

	private void handleUpdate() {
		try {
			out.writeObject("UPDATE ");
		} catch (IOException e) {
		}
	}
}