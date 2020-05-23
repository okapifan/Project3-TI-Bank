/*
 * Project 3/4
 * 
 * Daniël van der Drift
 * Robbin Koot
 * Timo van der Meer
 * Zoë Zegers
 */

// Database
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;

// Socket
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;

// Json
import org.json.*;

public class App {
	static String localCountryCode = "US";
	static String localBankCode = "TIMO";
	// Connection con; // Database connection

	//static JSONParser parser; 
	static JSONObject databaseConfig;
	static String databaseUrl = "";
	static String databaseUser = ""; // Change user to an user with less permissions
	static String databasePassword = "";

	static int landNodePort = 666;
	static int timobankPort = 8000;

	public static void main(String[] args) {
		try {
			File file = new File("src/database.json");
			databaseConfig = new JSONObject(new FileReader(file));
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
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

			String str = "", str2 = "";
			while (!str.equals("stop")) {
				// Receive
				//str = din.readUTF();
				str = "{\"body\":{\"pin\":\"1234\",\"account\":\"00001234\"},\"header\":{\"originCountry\":\"US\",\"originBank\":\"TIMO\",\"receiveCountry\":\"US\",\"receiveBank\":\"TIMO\"}}";
				JSONObject jsonMessage = new JSONObject(str);

				String account = jsonMessage.getJSONObject("body").getString("account");
				String pin = jsonMessage.getJSONObject("body").getString("pin");
				String originCountry = jsonMessage.getJSONObject("header").getString("originCountry");
				String originBank = jsonMessage.getJSONObject("header").getString("originBank");
				String receiveCountry = jsonMessage.getJSONObject("header").getString("receiveCountry");
				String receiveBank = jsonMessage.getJSONObject("header").getString("receiveBank");

				String jsonSendMessage = "";
				if(receiveCountry.equals(localCountryCode) && receiveBank.equals(localBankCode)){
					try {
						Class.forName("com.mysql.jdbc.Driver");
						Connection con = DriverManager.getConnection(databaseUrl, databaseUser, databasePassword);
			
						// Check pin
						Statement stmt1 = con.createStatement();
						ResultSet rs1 = stmt1.executeQuery("SELECT pincode FROM accounts WHERE accountId = " + account); // SQL injection posible
						rs1.next();
						String pinCode = rs1.getString(1);
						if (pin.equals(pinCode)) {
							// Update attempts to 0
							Statement stmt3 = con.createStatement();
							stmt3.executeQuery("UPDATE accounts SET failedAttempts = "+0+"WHERE accountId="+account);

							// Get balance
							Statement stmt2 = con.createStatement();
							ResultSet rs2 = stmt2.executeQuery("SELECT balance FROM accounts WHERE accountId = " + account);
							rs2.next();
							double balance = rs2.getDouble(1);
			
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
								stmt3.executeQuery("UPDATE accounts SET failedAttempts = "+(failedAttempts+1)+"WHERE accountId="+account);
							} else {
								stmt3.executeQuery("UPDATE accounts SET failedAttempts = "+(failedAttempts+1)+",isBlocked = true WHERE accountId="+account);
							}

							System.out.println("Pin incorrect");
							con.close();
						}
					} catch (Exception e) {
						System.out.println(e);
					}
					//Todo return statuscode && Todo check if pass is blocked
				} else {
					//dump at landnode
				}
				System.out.println("client says: " + account);

				// Send
				str2 = br.readLine();
				dout.writeUTF(str2);
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


	//Ontvang jsonbericht
	//Verwerk jsonbericht 
	//Stuur jsonresponse terug
}