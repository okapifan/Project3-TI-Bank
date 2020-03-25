import java.io.IOException;

import javax.swing.SwingUtilities;
import com.fazecast.jSerialComm.*;

public class App {

	public static MainScreen ms;
	
    public static void main(String[] args) throws IOException {
        
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				SerialPort comPort = SerialPort.getCommPorts()[0];
				comPort.openPort();
				ms = new MainScreen(comPort); //create Mainscreen
				
				//comPort.closePort();
			}
		});      
    }
}