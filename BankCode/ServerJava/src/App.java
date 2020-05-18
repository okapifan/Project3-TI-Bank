/*
 * Project 3/4
 * 
 * Daniël van der Drift
 * Robbin Koot
 * Timo van der Meer
 * Zoë Zegers
 */

// Database
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

// Json
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;

// Socket
import java.net.*;
import java.io.*;

public class App {
	static String localCountryName = "US";
	static String localBankCode = "TIMO";
	// Connection con; // Database connection

	static JSONObject databaseConfig;
	static String databaseUrl = "";
	static String databaseUser = ""; // Change user to an user with less permissions
	static String databasePassword = "";

	public static void main(String[] args) {
		JSONParser parser = new JSONParser();
		try {
			File file = new File("src/database.json");
			databaseConfig = (JSONObject) parser.parse(new FileReader(file));
			databaseUrl = "jdbc:mysql://" + databaseConfig.get("ip") + ":" + databaseConfig.get("port") + "/"
					+ databaseConfig.get("database");
			databaseUser = (String) databaseConfig.get("user");
			databasePassword = (String) databaseConfig.get("pass");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			ServerSocket ss = new ServerSocket(8000);
			Socket s = ss.accept();
			DataInputStream din = new DataInputStream(s.getInputStream());
			DataOutputStream dout = new DataOutputStream(s.getOutputStream());
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

			String str = "", str2 = "";
			while (!str.equals("stop")) {
				// Receive
				str = din.readUTF();
				System.out.println("client says: " + str);

				// Send
				str2 = br.readLine();
				dout.writeUTF(str2);
				dout.flush();
			}
			din.close();
			s.close();
			ss.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

}