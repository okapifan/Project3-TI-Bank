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

				// Call function to parse the data
				if (ms.currentScreen == 0) { // homescreen
					if (data.equals("1")) {
						ms.switchToBalanceScreen();
						ms.currentScreen = 1;
					}
				} else { // balancescreen
					if (data.equals("1")) {
						ms.switchToHomeScreen();
						ms.currentScreen = 0;
					}
				}
			}

			// Simulate event
			if (System.in.available() > 0) {
				while (System.in.available() > 0) {
					System.in.read();
				}

				// Send String
				String data = "Test";
				comPort.writeBytes(data.getBytes(), data.length());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
