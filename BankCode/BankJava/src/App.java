
/*
 * Project 3/4
 * 
 * Daniël van der Drift
 * Robbin Koot
 * Timo van der Meer
 * Zoë Zegers
 */

import mypackage.*;
import mainpackage.*;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.*;

import com.fazecast.jSerialComm.SerialPort;

public class App {
	static CardLayout cl;
	static JPanel panelContainer;
	//public static ContentHandler contentHandler;

	public static void main(String[] args) {
		SerialPort comPort = SerialPort.getCommPorts()[0];
		comPort.openPort();
		
		cl = new CardLayout(5, 5);
		panelContainer = new JPanel(cl);

		ArrayList<JPanel> panelList = new ArrayList<>();

		ContentHandler contentHandler = new ContentHandler(cl, panelContainer);

		JPanel00 notInUsePanel = new JPanel00(contentHandler);
		JPanel01 startPanel = new JPanel01(contentHandler);
		JPanel02 typePinPanel = new JPanel02(contentHandler);
		JPanel03 cardBlockedPanel = new JPanel03(contentHandler);
		JPanel04 menuPanel = new JPanel04(contentHandler);
		JPanel05 balancePanel = new JPanel05(contentHandler);
		JPanel06 chooseAmountPanel = new JPanel06(contentHandler);
		JPanel07 typeAmountPanel = new JPanel07(contentHandler);
		JPanel08 notEnoughPanel = new JPanel08(contentHandler);
		JPanel09 chooseHowPanel = new JPanel09(contentHandler);
		JPanel10 receiptPanel = new JPanel10(contentHandler);
		JPanel11 takeCardPanel = new JPanel11(contentHandler);
		JPanel12 patiencePanel = new JPanel12(contentHandler);
		JPanel13 greetPanel = new JPanel13(contentHandler);
		panelContainer.add(notInUsePanel, "00NotInUse");
		panelContainer.add(startPanel, "01Start");
		panelContainer.add(typePinPanel, "02TypePin");
		panelContainer.add(cardBlockedPanel, "03CardBlocked");
		panelContainer.add(menuPanel, "04Menu");
		panelContainer.add(balancePanel, "05Balance");
		panelContainer.add(chooseAmountPanel, "06ChooseAmount");
		panelContainer.add(typeAmountPanel, "07TypeAmount");
		panelContainer.add(notEnoughPanel, "08NotEnough");
		panelContainer.add(chooseHowPanel, "09ChooseHow");
		panelContainer.add(receiptPanel, "10Receipt");
		panelContainer.add(takeCardPanel, "11TakeCard");
		panelContainer.add(patiencePanel, "12Patience");
		panelContainer.add(greetPanel, "13Greet");
		panelList.add(notInUsePanel);
		panelList.add(startPanel);
		panelList.add(typePinPanel);
		panelList.add(cardBlockedPanel);
		panelList.add(menuPanel);
		panelList.add(balancePanel);
		panelList.add(chooseAmountPanel);
		panelList.add(typeAmountPanel);
		panelList.add(notEnoughPanel);
		panelList.add(chooseHowPanel);
		panelList.add(receiptPanel);
		panelList.add(takeCardPanel);
		panelList.add(patiencePanel);
		panelList.add(greetPanel);
		contentHandler.setPanelList(panelList);

		JFrame frame = new JFrame();
		frame.setTitle("Timobank");
		// frame.setSize(1920, 1080);
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
				comPort.closePort(); // Unnecessary?
				System.exit(0);
			}
		});
		

		contentHandler.switchTo01StartPanel();
		//contentHandler.switchTo04MenuPanel();

		
		int delay = 50; // Milliseconds
		ActionListener taskPerformer = new KeypadActionListoner(comPort, contentHandler);
		new Timer(delay, taskPerformer).start();
	}
}
