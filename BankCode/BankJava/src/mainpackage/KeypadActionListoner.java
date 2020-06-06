package mainpackage;

/*
 * Project 3/4
 * 
 * Daniël van der Drift
 * Robbin Koot
 * Timo van der Meer
 * Zoë Zegers
 */

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
			// Receive string
			if (comPort.bytesAvailable() > 0) {
				byte[] readBuffer = new byte[comPort.bytesAvailable()];
				int dataSize = comPort.readBytes(readBuffer, readBuffer.length);
				String data = new String(readBuffer);

				System.out.println("Read data: " + data + " (" + dataSize + " bytes)");

				// Use data
				App.contentHandler.parseData(data, dataSize);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
