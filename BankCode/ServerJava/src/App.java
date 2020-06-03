/*
 * Project 3/4
 * 
 * Daniël van der Drift
 * Robbin Koot
 * Timo van der Meer
 * Zoë Zegers
 */

// Database
import java.beans.VetoableChangeSupport;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileReader;

// Socket
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;

// Json
import org.json.*;
import org.json.simple.parser.JSONParser;

public class App {

	static String localCountryCode = "US";
	static String localBankCode = "TIMO";
	// Connection con; // Database connection

	//static JSONParser parser;
	static JSONObject databaseConfig;
	static String databaseUrl = "";
	static String databaseUser = ""; // Change user to an user with less permissions
	static String databasePassword = "";

	static int localPortATM = 8000;
	static int localPortLandNode = 665;
	static int landNodePort = 666;
	static String landNodeIP = "";

	// LandNode
	static ServerSocket server = null;
	static DataInputStream dIn;

	public static void main(String[] args) {
		try {
			Object obj = new JSONParser().parse(new FileReader("src/database.json"));
			databaseConfig = new JSONObject(obj.toString());
			//System.out.println(databaseConfig.toString());
			databaseUrl = "jdbc:mysql://" + databaseConfig.get("ip") + ":" + databaseConfig.get("port") + "/"
					+ databaseConfig.get("database");
			databaseUser = (String) databaseConfig.get("user");
			databasePassword = (String) databaseConfig.get("pass");

			try {
				server = new ServerSocket(localPortLandNode);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		while(true){
			makeSoccetConnectionATM();

			// Listen for request from LandNode
			listenForLandNodeRequest();
		}
	}

	public static void makeSoccetConnectionATM() {
		try {
			ServerSocket ss = new ServerSocket(localPortATM);
			Socket s = ss.accept();
			DataInputStream din = new DataInputStream(s.getInputStream());
			DataOutputStream dout = new DataOutputStream(s.getOutputStream());
			
			String str = "";
			while (!str.equals("stop")) {
				// Receive
				str = din.readUTF();
				System.out.println("Incoming message: " + str);
				//str = "{\"body\":{\"pin\":\"1234\",\"account\":\"00001234\"},\"header\":{\"originCountry\":\"US\",\"originBank\":\"TIMO\",\"receiveCountry\":\"US\",\"receiveBank\":\"TIMO\"}}";
				
				String jsonResponse;
				try {
					JSONObject jsonMessage = new JSONObject(str);
					String action = jsonMessage.getJSONObject("header").getString("action");
					if(action.equals("balance")){
						jsonResponse = getBalance(jsonMessage);
					}
					else if(action.equals("withdraw")){
						jsonResponse = "withdraw";//withdraw(jsonMessage);
					}
					else {
						jsonResponse = "";
					}
				} catch (Exception e) {
					jsonResponse = "";
				}

				// Send
				System.out.println("Outgoing message: " + jsonResponse);
				dout.writeUTF(jsonResponse);
				dout.flush();
			}
			din.close();
			s.close();
			ss.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void listenForLandNodeRequest(){
		try {
			// Receive
			Socket socket = server.accept();
			System.out.println("Received packet from LandNode");
			dIn = new DataInputStream(socket.getInputStream());
			String type = dIn.readUTF();
			JSONObject json = new JSONObject(dIn.readUTF());
			socket.close();

			// Process
			String response = "";
			if (type == "balance"){
				response = getBalance(json);
			}
			else if (type == "withdraw"){
				response = witdraw(json);
			}
			else {
				// Error
			}

			// Send
			Socket bankConnection = new Socket(landNodeIP, landNodePort);
			DataOutputStream dOut = new DataOutputStream(bankConnection.getOutputStream());
			dOut.writeUTF("response");
			dOut.writeUTF(response);
			System.out.println("Sent data back to LandNode");
			bankConnection.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getBalance(JSONObject jsonMessage){
		String account = jsonMessage.getJSONObject("body").getString("account");
		String pin = jsonMessage.getJSONObject("body").getString("pin");
		String originCountry = jsonMessage.getJSONObject("header").getString("originCountry");
		String originBank = jsonMessage.getJSONObject("header").getString("originBank");
		String receiveCountry = jsonMessage.getJSONObject("header").getString("receiveCountry");
		String receiveBank = jsonMessage.getJSONObject("header").getString("receiveBank");

		int statuscode = 0;
		String message = "";
		String addedJson = "";
		if(receiveCountry.equals(localCountryCode) && receiveBank.equals(localBankCode)){
			try {
				Class.forName("com.mysql.jdbc.Driver");
				Connection con = DriverManager.getConnection(databaseUrl, databaseUser, databasePassword);
	
				// Check pin
				Statement stmt1 = con.createStatement();
				ResultSet rs1 = stmt1.executeQuery("SELECT pincode,isBlocked FROM accounts WHERE accountId = " + account); // SQL injection posible
				rs1.next();
				String pinCode = rs1.getString(1);
				boolean isBlocked = rs1.getBoolean(2);
				if(pinCode.equals(null)){

					statuscode = 404;
					message = "Pinpas not found";
					System.out.println("Pinpas not found");
					con.close();
				} else if(!isBlocked){
					if (pin.equals(pinCode)) {
						// Update attempts to 0
						Statement stmt3 = con.createStatement();
						stmt3.executeUpdate("UPDATE accounts SET failedAttempts = 0 WHERE accountId = "+account);

						// Get balance
						Statement stmt2 = con.createStatement();
						ResultSet rs2 = stmt2.executeQuery("SELECT balance FROM accounts WHERE accountId = " + account);
						rs2.next();
						double balance = rs2.getDouble(1);
						
						statuscode = 200;
						message = "Success";
						addedJson = ",\"balance\":"+balance+"";
						System.out.println("GetBalance successfull");
						con.close();
					} else {
						Statement stmt4 = con.createStatement();
						ResultSet rs4 = stmt4.executeQuery("SELECT failedAttempts FROM accounts WHERE accountId = " + account);
						rs4.next();
						int failedAttempts = rs4.getInt(1);	
						
						// Update attempts
						Statement stmt3 = con.createStatement();
						if((failedAttempts+1) < 3){
							stmt3.executeUpdate("UPDATE accounts SET failedAttempts = "+(failedAttempts+1)+" WHERE accountId = "+account);
						} else {
							stmt3.executeUpdate("UPDATE accounts SET failedAttempts = "+(failedAttempts+1)+",isBlocked = true WHERE accountId = "+account);
						}
						
						statuscode = 401;
						message = "Pin incorrect";
						addedJson = ",\"attempts\":"+(failedAttempts+1)+"";
						System.out.println("Pin incorrect");
						con.close();
					}
				} else {

					statuscode = 403;
					message = "Pinpas blocked";
					System.out.println("Pin blocked");
					con.close();
				}
			} catch (Exception e) {
				System.out.println(e);
			}
			String jsonResponse = "{\"body\":{\"code\":"+statuscode+",\"message\":\"" + message + "\""+addedJson+"},\"header\":{\"originCountry\":\""+receiveCountry+"\",\"originBank\":\""+receiveBank+"\",\"receiveCountry\":\""+originCountry+"\",\"receiveBank\":\""+originBank+"\",\"action\":\"balance\"}}";
			return jsonResponse;
		} else {
			// Dump at landnode

			String jsonResponse = askLandNode("balance", jsonMessage);

			return jsonResponse;
		}
	}

	public static String witdraw(JSONObject jsonMessage){
		String account = jsonMessage.getJSONObject("body").getString("account");
		String pin = jsonMessage.getJSONObject("body").getString("pin");
		double amount = jsonMessage.getJSONObject("body").getDouble("amount");
		String originCountry = jsonMessage.getJSONObject("header").getString("originCountry");
		String originBank = jsonMessage.getJSONObject("header").getString("originBank");
		String receiveCountry = jsonMessage.getJSONObject("header").getString("receiveCountry");
		String receiveBank = jsonMessage.getJSONObject("header").getString("receiveBank");

		int statuscode = 0;
		String message = "";
		String addedJson = "";

		if(receiveCountry.equals(localCountryCode) && receiveBank.equals(localBankCode)){
			try {
				Class.forName("com.mysql.jdbc.Driver");
				Connection con = DriverManager.getConnection(databaseUrl, databaseUser, databasePassword);
	
				// Check pin
				Statement stmt1 = con.createStatement();
				ResultSet rs1 = stmt1.executeQuery("SELECT pincode,isBlocked FROM accounts WHERE accountId = " + account); // SQL injection posible
				rs1.next();
				String pinCode = rs1.getString(1);
				boolean isBlocked = rs1.getBoolean(2);
				if(pinCode.equals(null)){

					statuscode = 404;
					message = "Pinpas not found";
					System.out.println("Pinpas not found");
					con.close();
				} else if(!isBlocked){
					if (pin.equals(pinCode)) {
					
						// Get balance
						Statement stmt2 = con.createStatement();
						ResultSet rs2 = stmt2.executeQuery("SELECT balance FROM accounts WHERE accountId = " + account);
						rs2.next();
						double balance = rs2.getDouble(1);
						if((balance - amount) >= 0){
							Statement stmt3 = con.createStatement();
							stmt3.executeUpdate("UPDATE accounts SET balance = "+(balance - amount)+" WHERE accountId = " + account);
						
							statuscode = 200;
							message = "Success";
							System.out.println("witdraw successfull");
							con.close();
						} else {
							statuscode = 402;
							message = "Not enough money";
							System.out.println("Not enough money");
							con.close();
						}
					} else {
						Statement stmt4 = con.createStatement();
						ResultSet rs4 = stmt4.executeQuery("SELECT failedAttempts FROM accounts WHERE accountId = " + account);
						rs4.next();
						int failedAttempts = rs4.getInt(1);	
					
						// Update attempts
						Statement stmt3 = con.createStatement();
						if((failedAttempts+1) < 3){
							stmt3.executeUpdate("UPDATE accounts SET failedAttempts = "+(failedAttempts+1)+" WHERE accountId = "+account);
						} else {
							stmt3.executeUpdate("UPDATE accounts SET failedAttempts = "+(failedAttempts+1)+",isBlocked = true WHERE accountId = "+account);
						}
					
						statuscode = 401;
						addedJson = ",\"attempts\":"+(failedAttempts+1)+"";
						message = "Pin incorrect";
						System.out.println("Pin incorrect");
						con.close();
					}
				}else {
					
					statuscode = 403;
					message = "Pinpas blocked";
					System.out.println("Pin blocked");
					con.close();
				}
			} catch(Exception e){
				e.printStackTrace();
			}
			String jsonResponse = "{\"body\":{\"code\":"+statuscode+",\"message\":\"" + message + "\""+addedJson+"},\"header\":{\"originCountry\":\""+receiveCountry+"\",\"originBank\":\""+receiveBank+"\",\"receiveCountry\":\""+originCountry+"\",\"receiveBank\":\""+originBank+"\",\"action\":\"withdraw\"}}";
			return jsonResponse;
		} else {
			// Dump at landnode

			String jsonResponse = askLandNode("withdraw", jsonMessage);

			return jsonResponse;
		}
	}

	public static String askLandNode(String type, JSONObject jsonMessage){
		try{
			// Send
			Socket s1 = new Socket(landNodeIP, landNodePort);
			DataOutputStream dOut = new DataOutputStream(s1.getOutputStream());
			dOut.writeUTF(type);
			dOut.writeUTF(jsonMessage.toString());
			System.out.println("" + type + ": Sent");
			s1.close();
			//dOut.flush();
			
			

			// Receive
			Socket s2 = null;
			boolean check = false;
			while(check == false) // Wait
			{
				try {
					s2 = server.accept();
					check = true;
				} catch (Exception e) {
				}
			}
			//Socket s2 = server.accept();
			dIn = new DataInputStream(s2.getInputStream());
			String type2 = dIn.readUTF(); // Should be "response"
			JSONObject jsonResponse = new JSONObject(dIn.readUTF());
			System.out.println("" + type + ": Receive");
			s2.close();

			return jsonResponse.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
}