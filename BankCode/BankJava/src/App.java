import java.io.IOException;
import javax.swing.SwingUtilities;
import com.fazecast.jSerialComm.*;

public class App {

    public static void main(String[] args) throws IOException {
        SerialPort comPort = SerialPort.getCommPorts()[0];
		comPort.openPort();		
		SwingUtilities.invokeLater(MainScreen::new); //create Mainscreen

        try {
            while (true) // Loop through te code
            {
                // Receive string
                if (comPort.bytesAvailable() > 0) {
                    byte[] readBuffer = new byte[comPort.bytesAvailable()];
                    //comPort.readBytes(readBuffer, readBuffer.length);
                    int dataSize = comPort.readBytes(readBuffer, readBuffer.length);
                    String data = new String(readBuffer);

                    // Use data
                    System.out.println("Read data: " + data + " (" + dataSize + " bytes)");

                    // Call function to parse the data
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

                
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        comPort.closePort();
    }
}