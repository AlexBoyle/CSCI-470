import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CustomerServer extends Thread {
	private ServerSocket listenSocket;
	
	/**
	 * Launch the server
	 * @param args
	 */
	public static void main(String args[]) {
		new CustomerServer();
	}
	/**
	 * Initalize server
	 */
	private CustomerServer() {
		// Replace 97xx with your port number
		int port = 9712;
		try {
			listenSocket = new ServerSocket(port);
		} catch (IOException e) {
			System.err.println("Exception creating server socket: " + e);
			System.exit(1);
		}

		System.out.println("LOG: Server listening on port " + port);
		this.start();
	}

	/**
	 * run()
	 * The body of the server thread. Loops forever, listening for and
	 * accepting connections from clients. For each connection, create a
	 * new Conversation object to handle the communication through the
	 * new Socket.
	 */

	public void run() {
		try {
			while (true) {
				Socket clientSocket = listenSocket.accept();

				System.out.println("LOG: Client connected");

				// Create a Conversation object to handle this client and pass
				// it the Socket to use.  If needed, we could save the Conversation
				// object reference in an ArrayList. In this way we could later iterate
				// through this list looking for "dead" connections and reclaim
				// any resources.
				new Conversation(clientSocket);
			}
		} catch (IOException e) {
			System.err.println("Exception listening for connections: " + e);
		}
	}
}

/**
 * The Conversation class handles all communication with a client.
 */
class Conversation extends Thread {

	private Socket clientSocket;
	private ObjectInputStream in;
	private ObjectOutputStream out;

	// Where JavaCustXX is your database name
	private static final String URL = "jdbc:mysql://courses:3306/JavaCust12";

	private PreparedStatement getAllStatement = null;
	private PreparedStatement addStatement = null;
	private PreparedStatement deleteStatement = null;
	private PreparedStatement updateStatement = null;

	/**
	 * Constructor
	 *
	 * Initialize the streams and start the thread.
	 */
	Conversation(Socket socket) {
		clientSocket = socket;

		try {
			out = new ObjectOutputStream(clientSocket.getOutputStream());
			in = new ObjectInputStream(clientSocket.getInputStream());
			System.out.println("LOG: Streams opened");
		} catch (IOException e) {
			try {
				clientSocket.close();
			} catch (IOException e2) {
				System.err.println("Exception closing client socket: " + e2);
			}

			System.err.println("Exception getting socket streams: " + e);
			return;
		}

		try {
			System.out.println("LOG: Trying to create database connection");
			
			
			Connection connection = DriverManager.getConnection(URL);

			// Create your Statements and PreparedStatements here
			this.getAllStatement = connection.prepareStatement("SELECT * FROM customer");
			this.addStatement = connection.prepareStatement("INSERT INTO customer values(?,?,?,?)");
			this.deleteStatement = connection.prepareStatement("DELETE FROM customer WHERE ssn=?");
			this.updateStatement = connection.prepareStatement("UPDATE customer SET address=? WHERE ssn=?");
			System.out.println("LOG: Connected to database");

		} catch (SQLException e) {
			System.err.println("Exception connecting to database manager: " + e);
			return;
		}
		// Start the run loop.
		System.out.println("LOG: Connection achieved, starting run loop");
		this.start();
	}

	/**
	 * run()
	 *
	 * Reads and processes input from the client until the client disconnects.
	 */
	public void run() {
		System.out.println("LOG: Thread running");

		try {
			while (true) {
				// Read and process input from the client.
				String output = (String)in.readObject();
				if(output != null){
					System.out.println(output);
					MessageObject obj = new MessageObject(output);
					switch(obj.command){
						case "GETALL":
							handleGetAll(obj);
							break;
						case "ADD":
							handleAdd(obj);
							break;
						case "DELETE":
							handleDelete(obj);
							break;
						case "UPDATE":
							handleUpdate(obj);
							break;
						default:
							
					}
				}
			}
		} catch (IOException e) {
			System.err.println("IOException: " + e);
			System.out.println("LOG: Client disconnected");
		}
		catch( ClassNotFoundException e){}

		 finally {
			try {
				clientSocket.close();
			} catch (IOException e) {
				System.err.println("Exception closing client socket: " + e);
			}
		}
	}

	/**
	 * handles the GET ALL commads
	 * @param clientMsg
	 */
	private void handleGetAll(MessageObject clientMsg) {
		try {
			String outt = "";
			ResultSet output = this.getAllStatement.executeQuery();
			while(output.next()) {
				outt += output.getString(1) + " | ";
				outt += output.getString(2) + " | ";
				outt += output.getString(3) + " | ";
				outt += output.getString(4) + " | ";
				outt += "\n";
			}
			out.writeObject(outt);
		} catch (SQLException e) {
			try {
				System.out.println(e);
				out.writeObject("Failed to execute bd query");
			} catch (IOException e1) {
				System.out.println("Failed to write to stream");
			}
		} catch (IOException e) {
			System.out.println("Failed to write to stream");
		}
	}
	
	/**
	 * Handle ADD command
	 * @param clientMsg message sent by the client
	 */
	private void handleAdd(MessageObject clientMsg){
		try {
			this.addStatement.setString(1, clientMsg.name);
			this.addStatement.setString(2, clientMsg.ssn);
			this.addStatement.setString(3, clientMsg.addr);
			this.addStatement.setString(4, clientMsg.zip);
			int output = (int)this.addStatement.executeUpdate();
			if(output == 1) {
				out.writeObject("Query Succsess");
			}
			else {
				out.writeObject("Add failed");
			}
		} catch (SQLException e) {
			try {
				out.writeObject(e.getMessage());
				System.out.println(e);
			} catch (IOException e1) {
				System.out.println("Failed to write to stream");
			}
		} catch (IOException e) {
			System.out.println("Failed to write to stream");
		}
	}

	/**
	 * Handle DELETE command
	 * @param clientMsg message sent by the client
	 */
	private void handleDelete(MessageObject clientMsg) {
		try {
			this.deleteStatement.setString(1, clientMsg.ssn);
			int output = (int)this.deleteStatement.executeUpdate();
			if(output == 1) {
				out.writeObject("Query Succsess");
			}
			else {
				out.writeObject("Nothing to remove");
			}
		} catch (SQLException e) {
			try {
				out.writeObject("Failed to execute bd query");
				System.out.println(e);
			} catch (IOException e1) {
				System.out.println("Failed to write to stream");
			}
		} catch (IOException e) {
			System.out.println("Failed to write to stream");
		}
	}

	/**
	 * Handle UPDATE command
	 * @param clientMsg message sent by the client
	 */
	private void handleUpdate(MessageObject clientMsg) {
		try {
			this.updateStatement.setString(1, clientMsg.addr);
			this.updateStatement.setString(2, clientMsg.ssn);
			int output = (int)this.updateStatement.executeUpdate();
			if(output == 1) {
				out.writeObject("Query Succsess");
			}
			else {
				out.writeObject("Update Failed");
			}
		} catch (SQLException e) {
			System.out.println(e);
			try {
				out.writeObject("Failed to execute bd query");
			} catch (IOException e1) {
				System.out.println("Failed to write to stream");
			}
		} catch (IOException e) {
			System.out.println("Failed to write to stream");
		}
		
	}
}