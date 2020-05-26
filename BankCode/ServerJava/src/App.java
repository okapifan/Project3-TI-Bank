/*
 * Project 3/4
 * 
 * Daniël van der Drift
 * Robbin Koot
 * Timo van der Meer
 * Zoë Zegers
 */

// Database
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

	static int timobankPort = 8000;
	static int landNodePort = 666;
	static String landNodeIP = "";

	public static void main(String[] args) {
		try {
			Object obj = new JSONParser().parse(new FileReader("src/database.json"));
			databaseConfig = new JSONObject(obj.toString());
			//System.out.println(databaseConfig.toString());
			databaseUrl = "jdbc:mysql://" + databaseConfig.get("ip") + ":" + databaseConfig.get("port") + "/"
					+ databaseConfig.get("database");
			databaseUser = (String) databaseConfig.get("user");
			databasePassword = (String) databaseConfig.get("pass");
		} catch (Exception e) {
			e.printStackTrace();
		}

		makeSoccetConnection();
	}

	public static void makeSoccetConnection() {
		try {
			ServerSocket ss = new ServerSocket(timobankPort);
			Socket s = ss.accept();
			DataInputStream din = new DataInputStream(s.getInputStream());
			DataOutputStream dout = new DataOutputStream(s.getOutputStream());
			
			String str = "";
			while (!str.equals("stop")) {
				// Receive
				str = din.readUTF();
				//str = "{\"body\":{\"pin\":\"1234\",\"account\":\"00001234\"},\"header\":{\"originCountry\":\"US\",\"originBank\":\"TIMO\",\"receiveCountry\":\"US\",\"receiveBank\":\"TIMO\"}}";
				
				String jsonResponse = GetBalance(str);

				// Send
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

	public static String GetBalance(String jsonMessageString){
		JSONObject jsonMessage = new JSONObject(jsonMessageString);
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
			//dump at landnode

			try {
				Socket s2;
				DataInputStream din2;
				DataOutputStream dout2;
				s2 = new Socket(landNodeIP, landNodePort);
				din2 = new DataInputStream(s2.getInputStream());
				dout2 = new DataOutputStream(s2.getOutputStream());
	
	
	
				// Send
				dout2.writeUTF(jsonMessageString);
				dout2.flush();
	
				// Receive
				while(din2.available() > 0){ // Wait
					
				}
				String str2 = din2.readUTF();
				//String str2_example = "{\"body\":{\"code\":200,\"message\":\"Success\",\"balance\":999.99},\"header\":{\"originCountry\":\"NL\",\"originBank\":\"INGB\",\"receiveCountry\":\"US\",\"receiveBank\":\"TIMO\",\"action\":\"balance\"}}";
				//{"body":{"code":200,"message":"Success","balance":999.99},"header":{"originCountry":"NL","originBank":"INGB","receiveCountry":"US","receiveBank":"TIMO","action":"balance"}}
	
				
				// Close connection
				dout2.writeUTF("stop");
				dout2.flush();
				dout2.close();
				s2.close();

				return str2;
			} catch (Exception e) {
				e.printStackTrace();
			}

			return "";
		}
	}
	//Ontvang jsonbericht
	//Verwerk jsonbericht 
	//Stuur jsonresponse terug
}