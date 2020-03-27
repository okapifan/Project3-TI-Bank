import java.io.IOException;
//import com.mysql.jdbc.Driver;
import java.sql.*;

public class App {
	
	public static void main(String[] args) throws IOException {
		System.out.println("Java mySQL database");
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank", "root", "");
			
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select * from accounts");
			while (rs.next())
				System.out.println(rs.getInt(1) + "  " + rs.getString(2) + "  " + rs.getString(3) + "  " + rs.getString(4));
			con.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		
		
		// Important for security: Connect with a user with limited privilages
	}
}