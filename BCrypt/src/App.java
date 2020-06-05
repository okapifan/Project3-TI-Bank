/*
 * Project 3/4
 * 
 * Daniël van der Drift
 * Robbin Koot
 * Timo van der Meer
 * Zoë Zegers
 */

import org.springframework.security.crypto.bcrypt.BCrypt;

public class App {
	
	public static void main(String[] args) {
		String password = "password";
		System.out.println(password);
		//String password_hash = BCrypt.hashpw(password, ""); // Does not work without salt
		String password_hash = BCrypt.hashpw(password, BCrypt.gensalt());
		System.out.println(password_hash);
	}
}
