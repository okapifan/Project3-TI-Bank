import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.fazecast.jSerialComm.SerialPort;

public class KeypadActionListoner implements ActionListener {
	private MainScreen ms;
	private SerialPort comPort;

	public KeypadActionListoner(MainScreen newMs, SerialPort newComPort) {
		this.ms = newMs;
		this.comPort = newComPort;
	}

	public void actionPerformed(ActionEvent evt) {
		try {
			//System.out.print("LoopTest");

			// Receive string
			if (comPort.bytesAvailable() > 0) {
				byte[] readBuffer = new byte[comPort.bytesAvailable()];
				// comPort.readBytes(readBuffer, readBuffer.length);
				int dataSize = comPort.readBytes(readBuffer, readBuffer.length);
				String data = new String(readBuffer);

				// Use data
				System.out.println("Read data: " + data + " (" + dataSize + " bytes)");

				parseData(data, dataSize);
			}

			
			// Send String example
			//String data = "Test";
			//comPort.writeBytes(data.getBytes(), data.length());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void parseData(String data, int dataSize) {

		switch (ms.currentScreen) {
			case 0:
				if (dataSize == 1) { // Keypad input
					if (data.equals("1")) {
						ms.switchToBalanceScreen();
					}
				} else { // RFID card UID
		
				}
				break;

			case 1:
				if (dataSize == 1) { // Keypad input
					if (data.equals("1")) {
						ms.switchToHomeScreen();
					}
				} else { // RFID card UID
		
				}
				break;
		
			default:
				System.out.println("CurrentScreen does not exist");
				break;
		}
		
		/*
		// Call function to parse the data
		if (ms.currentScreen == 0) { // homescreen
			if (dataSize == 1) { // Keypad input
				if (data.equals("1")) {
					ms.switchToBalanceScreen();
					ms.currentScreen = 1;
				}
			} else { // RFID card UID
	
			}
		} else if (ms.currentScreen == 1) { // balancescreen
			if (data.equals("1")) {
				ms.switchToHomeScreen();
				ms.currentScreen = 0;
			}
		} else {
			System.out.println("CurrentScreen does not exist");
		}
		*/
	}
}
