
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

import javax.swing.*;

import com.fazecast.jSerialComm.SerialPort;

public class App {
	static CardLayout cl;
	static JPanel panelContainer;

	public static void main(String[] args) {
		SerialPort comPort = SerialPort.getCommPorts()[0];
		comPort.openPort();
		
		cl = new CardLayout(5, 5);
		panelContainer = new JPanel(cl);

		JPanel00 notInUsePanel = new JPanel00();
		JPanel01 startPanel = new JPanel01();
		JPanel02 typePinPanel = new JPanel02();
		JPanel03 cardBlockedPanel = new JPanel03();
		JPanel04 menuPanel = new JPanel04();
		JPanel05 balancePanel = new JPanel05();
		JPanel06 chooseAmountPanel = new JPanel06();
		JPanel07 typeAmountPanel = new JPanel07();
		JPanel08 notEnoughPanel = new JPanel08();
		JPanel09 chooseHowPanel = new JPanel09();
		JPanel10 receiptPanel = new JPanel10();
		JPanel11 takeCardPanel = new JPanel11();
		JPanel12 patiencePanel = new JPanel12();
		JPanel13 greetPanel = new JPanel13();
		panelContainer.add(notInUsePanel, "NotInUse");
		panelContainer.add(startPanel, "Start");
		panelContainer.add(typePinPanel, "TypePin");
		panelContainer.add(cardBlockedPanel, "CardBlocked");
		panelContainer.add(menuPanel, "Menu");
		panelContainer.add(balancePanel, "Balance");
		panelContainer.add(chooseAmountPanel, "ChooseAmount");
		panelContainer.add(typeAmountPanel, "TypeAmount");
		panelContainer.add(notEnoughPanel, "NotEnough");
		panelContainer.add(chooseHowPanel, "ChooseHow");
		panelContainer.add(receiptPanel, "Receipt");
		panelContainer.add(takeCardPanel, "TakeCard");
		panelContainer.add(patiencePanel, "Patience");
		panelContainer.add(greetPanel, "Greet");

		JFrame frame = new JFrame();
		frame.setTitle("Timo-Bank");
		// frame.setSize(1920, 1080);
		frame.setLocationRelativeTo(null);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.pack();
		frame.setVisible(true);
		frame.add(panelContainer);
		
		cl.show(panelContainer, "Menu");
		
		int delay = 50; // Milliseconds
		ActionListener taskPerformer = new KeypadActionListoner(comPort, cl, panelContainer);
		new Timer(delay, taskPerformer).start();
	}
}
