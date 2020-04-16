package mainpackage;

import java.sql.*;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;

public class DatabaseHandler {
	String localCountryName;
	String localBankName;
	// Connection con; // Database connection

	private String databaseUrl = "jdbc:mysql://localhost:3306/bank";
	private String databaseUser = "root"; // Change user to an user with less permissions
	private String databasePassword = "";

	public DatabaseHandler(String countryName, String bankName) {
		this.localCountryName = countryName;
		this.localBankName = bankName;

		
		// try {
		// 	Class.forName("com.mysql.jdbc.Driver");
		// 	con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank", "root", "");
		// }
		// catch (Exception e) { System.out.println(e); }
	}

	public String Test(String countryName, String bankName, String pin, String account) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(databaseUrl, databaseUser, databasePassword);
			
			// Check pin
			Statement stmt1 = con.createStatement();
			ResultSet rs1 = stmt1.executeQuery("SELECT pincode FROM pinpassen WHERE persoonsnr = " + account); // SQL injection posible
			rs1.next();
			String pinCode = rs1.getString(1);

			System.out.println("Test successfull");
			con.close();
			return pinCode;
		} catch (Exception e) {
			System.out.println(e);
		}
		return "-";
	}

	public float getBalance(String countryName, String bankName, String pin, String account) {
		if (localCountryName == countryName && localBankName == bankName) {
			System.out.println("GetBalance from local Database");
			float balance = localGetBalance(pin, account);
			return balance;
		} else {
			System.out.println("GetBalance from NOOB");
			float balance = noobGetBalance(countryName, bankName, pin, account);
			return balance;
		}
	}

	public void withdraw(String countryName, String bankName, String pin, String account, Float amount) {
		if (localCountryName == countryName && localBankName == bankName) {
			System.out.println("Withdraw from local Database");
			localWithdraw(pin, account, amount);
		} else {
			System.out.println("Withdraw from NOOB");
			noobWithdraw(countryName, bankName, pin, account, amount);
		}
	}

	private float localGetBalance(String pin, String account) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(databaseUrl, databaseUser, databasePassword);

			// Check pin
			Statement stmt1 = con.createStatement();
			ResultSet rs1 = stmt1.executeQuery("SELECT pincode FROM pinpassen WHERE persoonsnr = " + account); // SQL injection posible
			rs1.next();
			String pinCode = rs1.getString(1);
			if (pin.equals(pinCode)) {

				// Get balance
				Statement stmt2 = con.createStatement();
				ResultSet rs2 = stmt2.executeQuery("SELECT bedrag FROM accounts WHERE persoonsnr = " + account);
				rs2.next();
				float balance = rs2.getFloat(1);

				System.out.println("GetBalance successfull");
				con.close();
				return balance;
			} else {
				System.out.println("Pin incorrect");
				con.close();
				return (float)0; //return null; // Need a way to return success status
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return (float)0; //return null;
	}

	private boolean localWithdraw(String pin, String account, Float amount) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(databaseUrl, databaseUser, databasePassword);

			// Check pin
			Statement stmt1 = con.createStatement();
			ResultSet rs1 = stmt1.executeQuery("SELECT pincode FROM pinpassen WHERE persoonsnr = " + account); // SQL injection posible
			rs1.next();
			String pinCode = rs1.getString(1);
			if (pin.equals(pinCode)) {

				// Get balance
				Statement stmt2 = con.createStatement();
				ResultSet rs2 = stmt2.executeQuery("SELECT bedrag FROM accounts WHERE persoonsnr = " + account);
				rs2.next();
				float balance = rs2.getFloat(1);
				if (amount >= balance) {

					float newBalance = balance - amount;
					Statement stmt3 = con.createStatement();
					stmt3.executeQuery("UPDATE accounts SET bedrag = " + newBalance + " WHERE persoonsnr = " + account);

					System.out.println("Withfraw successfull");
					con.close();
					return true;
				} else {
					System.out.println("Not enough money");
					con.close();
					return false;
				}
			} else {
				System.out.println("Pin incorrect");
				con.close();
				return false;
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return false;
	}

	private float noobGetBalance(String countryName, String bankName, String pin, String account) {
		/*
		 * 
		 * //Example json
		 * 
		 * //Check pin String getBalanceBody =
		 * "{\"body\":{\"pin\":\"1234\",\"account\":\"123456\"},\"header\":{\"originCountry\":\"DE\",\"originBank\":\"DEBA\",\"receiveCountry\":\"NL\",\"receiveBank\":\"INGB\"}}";
		 * io.emit("balance", getBalanceBody)
		 * 
		 * String receivedString =
		 * "{\"body\":{\"code\":200,\"message\":\"Success\",\"balance\":9999.99},\"header\":{\"originCountry\":\"NL\",\"originBank\":\"INGB\",\"receiveCountry\":\"DE\",\"receiveBank\":\"DEBA\",\"action\":\"balance\"}}";
		 * JSONParser parser = new JSONParser();
		 */

		// Check balance
		float balance = 0;

		// Return balance
		return balance;
	}

	private boolean noobWithdraw(String countryName, String bankName, String pin, String account, Float amount) {
		
		return false;
	}
}