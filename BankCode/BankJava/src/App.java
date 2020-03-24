import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.swing.SwingUtilities;
import com.fazecast.jSerialComm.*;

public class App {

	public static MainScreen ms;
	
    public static void main(String[] args) throws IOException {
        
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				SerialPort comPort = SerialPort.getCommPorts()[0];
				comPort.openPort();
				ms = new MainScreen(); //create Mainscreen
				
				try {
					//while (true) // Loop through te code
					//{
						// Receive string
						if (comPort.bytesAvailable() > 0) {
							byte[] readBuffer = new byte[comPort.bytesAvailable()];
							//comPort.readBytes(readBuffer, readBuffer.length);
							int dataSize = comPort.readBytes(readBuffer, readBuffer.length);
							String data = new String(readBuffer);

							// Use data
							System.out.println("Read data: " + data + " (" + dataSize + " bytes)");

							// Call function to parse the data
							if(ms.currentScreen == 0) { //homescreen
								if(data.equals("1")) {
									ms.switchToBalanceScreen();
									ms.currentScreen = 1;
								}
							} else { //balancescreen
								if(data.equals("1")) {
									ms.switchToHomeScreen();
									ms.currentScreen = 0;
								}
							}
						}
						
						// Simulate event
						if (System.in.available() > 0){
							while(System.in.available() > 0){
								System.in.read();
							}
							
							// Send String
							String data = "Test";
							comPort.writeBytes(data.getBytes(), data.length());
						}
						//TimeUnit.SECONDS.sleep(60);
					//}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				comPort.closePort();
			}
		});      
    }
}