package mainpackage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.fazecast.jSerialComm.SerialPort;

public class KeypadActionListoner implements ActionListener {
	private SerialPort comPort;

	public KeypadActionListoner(SerialPort comPort) {
		this.comPort = comPort;
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

				App.contentHandler.parseData(data, dataSize);
			}

			
			// Send String example
			//String data = "Test";
			//comPort.writeBytes(data.getBytes(), data.length());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
