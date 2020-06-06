package mainpackage;

// Socket
import java.net.*;
import java.io.*;

// Json
import org.json.*;

public class DatabaseHandler {
	String localCountryCode;
	String localBankCode;

	Socket s;
	DataInputStream din;
	DataOutputStream dout;

	public DatabaseHandler(String countryName, String bankName) {
		localCountryCode = countryName;
		localBankCode = bankName;

		OpenSocket();
	}

	private void OpenSocket() {
		try {
			s = new Socket("145.24.222.211", 8000);
			din = new DataInputStream(s.getInputStream());
			dout = new DataOutputStream(s.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void CloseSocket(){
		// Send
		try {
			dout.writeUTF("stop");
			dout.flush();
			dout.close();
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int getBalance(String countryCode, String bankCode, String pin, String account) {
		try {
			// Send
			String str = "{\"header\":{\"originCountry\":\"" + localCountryCode + "\",\"originBank\":\"" + localBankCode + "\",\"receiveCountry\":\"" + countryCode + "\",\"receiveBank\":\"" + bankCode + "\",\"action\":\"balance\"},\"body\":{\"pin\":\"" + pin + "\",\"account\":\"" + account + "\"}}";
			dout.writeUTF(str);
			dout.flush();

			// Receive
			while(din.available() > 0){}
			String str2 = din.readUTF();
			//String str2_example = "{\"header\":{\"originCountry\":\"NL\",\"originBank\":\"INGB\",\"receiveCountry\":\"US\",\"receiveBank\":\"TIMO\",\"action\":\"balance\"},\"body\":{\"code\":200,\"message\":\"Success\",\"balance\":999.99}}";

			JSONObject obj = new JSONObject(str2);
			int statusCode = obj.getJSONObject("body").getInt("code");
			String statusMessage = obj.getJSONObject("body").getString("message");
			System.out.println("" + statusCode + ": " + statusMessage);
			
			if (statusCode == 200) {
				double balance = obj.getJSONObject("body").getDouble("balance");
				App.contentHandler.setBalance(balance);
			}
			else if (statusCode == 401) {
				int attempts = obj.getJSONObject("body").getInt("attempts");
				App.contentHandler.setAttempts(attempts);
			}

			return statusCode;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//Example json
		//String getBalanceBody = "{\"header\":{\"originCountry\":\"DE\",\"originBank\":\"DEBA\",\"receiveCountry\":\"NL\",\"receiveBank\":\"INGB\",\"action\":\"balance\"},\"body\":{\"pin\":\"1234\",\"account\":\"123456\"}}";
		//String receivedString = "{\"header\":{\"originCountry\":\"NL\",\"originBank\":\"INGB\",\"receiveCountry\":\"DE\",\"receiveBank\":\"DEBA\",\"action\":\"balance\"},\"body\":{\"code\":200,\"message\":\"Success\",\"balance\":9999.99}}";
		 
		return 0;
	}

	public int withdraw(String countryName, String bankName, String pin, String account, int amount) {
		try {
			
			// Send
			String str = "{\"header\":{\"originCountry\":\"" + localCountryCode + "\",\"originBank\":\"" + localBankCode + "\",\"receiveCountry\":\"" + countryName + "\",\"receiveBank\":\"" + bankName + "\",\"action\":\"withdraw\"},\"body\":{\"pin\":\"" + pin + "\",\"account\":\"" + account + "\",\"amount\":" + amount + "}}";
			dout.writeUTF(str);
			dout.flush();

			// Receive
			while(din.available() > 0) {}
			String str2 = din.readUTF();
			//String str2_example = "{\"header\":{\"originCountry\":\"NL\",\"originBank\":\"INGB\",\"receiveCountry\":\"DE\",\"receiveBank\":\"DEBA\",\"action\":\"withdraw\"},\"body\":{\"code\":200,\"message\":\"Success\"}}";

			JSONObject obj = new JSONObject(str2);
			int statusCode = obj.getJSONObject("body").getInt("code");
			String statusMessage = obj.getJSONObject("body").getString("message");
			System.out.println("" + statusCode + ": " + statusMessage);
			
			// if (statusCode == 200){
			// 	//	
			// }

			return statusCode;
		} catch (IOException e) {
			e.printStackTrace();
		}

		//Example json
		//String withdrawBody = "{\"header\":{\"originCountry\":\"NL\",\"originBank\":\"INGB\",\"receiveCountry\":\"DE\",\"receiveBank\":\"DEBA\",\"action\":\"withdraw\"},\"body\":{\"pin\":\"1234\",\"account\":\"123456\",\"amount\":123.45}}";
		//String receivedString = "{\"header\":{\"originCountry\":\"NL\",\"originBank\":\"INGB\",\"receiveCountry\":\"DE\",\"receiveBank\":\"DEBA\",\"action\":\"withdraw\"},\"body\":{\"code\":200,\"message\":\"Success\"}}";

		return 0;
	}
}