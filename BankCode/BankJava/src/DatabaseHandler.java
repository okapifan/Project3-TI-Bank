import java.sql.*;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;


public class DatabaseHandler {
	String localCountryName;
	String localBankName;
	//Connection con; // Database connection

	private String databaseUrl = "jdbc:mysql://localhost:3306/bank";
	private String databaseUser = "root";
	private String databasePassword = "";
	

	public DatabaseHandler(String countryName, String bankName) {
		this.localCountryName = countryName;
		this.localBankName = bankName;

		/*
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank", "root", "");
		} catch (Exception e) {
			System.out.println(e);
		}
		*/
	}

	public float getBalance(String countryName, String bankName, String pin, String account) {
		if (localCountryName == countryName && localBankName == bankName) {
			float balance = localGetBalance(pin, account);
			return balance;
		}
		else {
			float balance = noobGetBalance(countryName, bankName, pin, account);
			return balance;
		}
	}

	public void withdraw(String countryName, String bankName, String pin, String account, Float amount) {
		if (localCountryName == countryName && localBankName == bankName) {
			localWithdraw(pin, account, amount);
		}
		else {
			noobWithdraw(countryName, bankName, pin, account, amount);
		}
	}

	private float localGetBalance(String pin, String account) {
		//Check pin
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(databaseUrl, databaseUser, databasePassword);
			
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select * from accounts");
			
			//while (rs.next())
			//	System.out.println(rs.getInt(1) + "  " + rs.getString(2) + "  " + rs.getString(3) + "  " + rs.getString(4));
			float balance = rs.getFloat(1);
			con.close();
			return balance;
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}

	private void localWithdraw(String pin, String account, Float amount) {

	}

	private float noobGetBalance(String countryName, String bankName, String pin, String account) {
		/*
		
		//Example json
		
		//Check pin
		String getBalanceBody = "{\"body\":{\"pin\":\"1234\",\"account\":\"123456\"},\"header\":{\"originCountry\":\"DE\",\"originBank\":\"DEBA\",\"receiveCountry\":\"NL\",\"receiveBank\":\"INGB\"}}";
		io.emit("balance", getBalanceBody)

		String receivedString = "{\"body\":{\"code\":200,\"message\":\"Success\",\"balance\":9999.99},\"header\":{\"originCountry\":\"NL\",\"originBank\":\"INGB\",\"receiveCountry\":\"DE\",\"receiveBank\":\"DEBA\",\"action\":\"balance\"}}";
		JSONParser parser = new JSONParser();
		*/

		//Check balance
		float balance = 0;

		//Return balance
		return balance;
	}

	private void noobWithdraw(String countryName, String bankName, String pin, String account, Float amount) {

	}
}