package mainpackage;

/*
 * Project 3/4
 * 
 * Daniël van der Drift
 * Robbin Koot
 * Timo van der Meer
 * Zoë Zegers
 */

import mypackage.*;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

import com.fazecast.jSerialComm.SerialPort;

public class App {
	static CardLayout cl;
	static JPanel panelContainer;
	static SerialPort comPort;
	public static ContentHandler contentHandler;
	public static JPanel00 panel00NotInUse = new JPanel00();
	public static JPanel01 panel01Start = new JPanel01();
	public static JPanel02 panel02TypePin = new JPanel02();
	public static JPanel03 panel03CardBlocked = new JPanel03();
	public static JPanel04 panel04Menu = new JPanel04();
	public static JPanel05 panel05Balance = new JPanel05();
	public static JPanel06 panel06ChooseAmount = new JPanel06();
	public static JPanel07 panel07TypeAmount = new JPanel07();
	public static JPanel08 panel08NotEnough = new JPanel08();
	public static JPanel09 panel09ChooseHow = new JPanel09();
	public static JPanel10 panel10Receipt = new JPanel10();
	public static JPanel11 panel11TakeCard = new JPanel11();
	public static JPanel12 panel12Patience = new JPanel12();
	public static JPanel13 panel13Greet = new JPanel13();
	public static JPanel14 panel14ReceiptShow = new JPanel14();

	public static void main(String[] args) {
		comPort = SerialPort.getCommPorts()[1];
		System.out.println(comPort.getPortDescription());
		comPort.openPort();
		
		cl = new CardLayout(5, 5);
		panelContainer = new JPanel(cl);
		contentHandler = new ContentHandler(cl, panelContainer);

		panelContainer.add(panel00NotInUse, "00NotInUse");
		panelContainer.add(panel01Start, "01Start");
		panelContainer.add(panel02TypePin, "02TypePin");
		panelContainer.add(panel03CardBlocked, "03CardBlocked");
		panelContainer.add(panel04Menu, "04Menu");
		panelContainer.add(panel05Balance, "05Balance");
		panelContainer.add(panel06ChooseAmount, "06ChooseAmount");
		panelContainer.add(panel07TypeAmount, "07TypeAmount");
		panelContainer.add(panel08NotEnough, "08NotEnough");
		panelContainer.add(panel09ChooseHow, "09ChooseHow");
		panelContainer.add(panel10Receipt, "10Receipt");
		panelContainer.add(panel11TakeCard, "11TakeCard");
		panelContainer.add(panel12Patience, "12Patience");
		panelContainer.add(panel13Greet, "13Greet");
		panelContainer.add(panel14ReceiptShow, "14ReceiptShow");

		JFrame frame = new JFrame();
		frame.setTitle("Timobank");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.pack();
		frame.setVisible(true);
		frame.add(panelContainer);


		frame.addWindowListener(new WindowAdapter() { // Exit button override
			@Override
			public void windowClosing(WindowEvent e) {
				System.out.println("Exit");
				comPort.closePort();
				contentHandler.database.CloseSocket(); // Close socket
				System.exit(0);
			}
		});
		
		contentHandler.switchTo01StartPanel();
		
		int delay = 50; // Milliseconds
		ActionListener taskPerformer = new KeypadActionListoner(comPort);
		new Timer(delay, taskPerformer).start();
	}

	public static void sendArduino(String data){
		// Send String
		System.out.println("Send data: " + data + " (" + data.length() + " bytes)");
		comPort.writeBytes(data.getBytes(), data.length());
	}
}
