package mainpackage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;

public class DatabaseHandler {
	String localCountryName;
	String localBankName;
	// Connection con; // Database connection

	private JSONObject databaseConfig;
	private String databaseUrl = "jdbc:mysql://localhost:3306/bank";
	private String databaseUser = ""; // Change user to an user with less permissions
	private String databasePassword = "";

	public DatabaseHandler(String countryName, String bankName) {
		JSONParser parser = new JSONParser();
		try {
			File file = new File("src/database.json");
			this.databaseConfig = (JSONObject) parser.parse(new FileReader(file));
			System.out.println( this.databaseConfig.get("pass"));
			this.databaseUrl = "jdbc:mysql://"+databaseConfig.get("ip")+":"+databaseConfig.get("port")+"/"+databaseConfig.get("database");
			this.databaseUser = (String) databaseConfig.get("user");
			this.databasePassword = (String) databaseConfig.get("pass");
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
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
			ResultSet rs1 = stmt1.executeQuery("SELECT pincode FROM accounts WHERE accountId = " + account); // SQL injection posible
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
			ResultSet rs1 = stmt1.executeQuery("SELECT pincode FROM accounts WHERE accountId = " + account); // SQL injection posible
			rs1.next();
			String pinCode = rs1.getString(1);
			if (pin.equals(pinCode)) {

				// Get balance
				Statement stmt2 = con.createStatement();
				ResultSet rs2 = stmt2.executeQuery("SELECT balance FROM accounts WHERE accountId = " + account);
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
			ResultSet rs1 = stmt1.executeQuery("SELECT pincode FROM accounts WHERE accountId = " + account); // SQL injection posible
			rs1.next();
			String pinCode = rs1.getString(1);
			if (pin.equals(pinCode)) {

				// Get balance
				Statement stmt2 = con.createStatement();
				ResultSet rs2 = stmt2.executeQuery("SELECT balance FROM accounts WHERE accountId = " + account);
				rs2.next();
				float balance = rs2.getFloat(1);
				if (amount >= balance) {

					float newBalance = balance - amount;
					Statement stmt3 = con.createStatement();
					stmt3.executeQuery("UPDATE accounts SET balance = " + newBalance + " WHERE accountId = " + account);

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