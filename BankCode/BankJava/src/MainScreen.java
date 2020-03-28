import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import com.fazecast.jSerialComm.SerialPort;

import java.awt.*;

public class MainScreen {
	static JPanel panelContainer;
	static JPanel homePanel;
	static JPanel balancePanel;
	static CardLayout cl;
	JButton balanceBtn;
	JButton backToHomeBtn;
	public int currentScreen = 0;

	public MainScreen(SerialPort comPort) {

		// Buttons for all screens
		balanceBtn = new JButton("1. Check Balance");
		balanceBtn.addActionListener(e -> switchToBalanceScreen());

		backToHomeBtn = new JButton("0. Home");
		backToHomeBtn.addActionListener(e -> switchToHomeScreen());

		
		// Create CardLayout & the container
		cl = new CardLayout(5, 5);
		panelContainer = new JPanel(cl);
		panelContainer.setBackground(Color.pink);


		// Creates Screens
		homePanel = new JPanel();
		homePanel.setBackground(Color.red);
		homePanel.add(balanceBtn);
		panelContainer.add(homePanel, "Home");

		balancePanel = new JPanel();
		balancePanel.setBackground(Color.blue);
		balancePanel.add(backToHomeBtn);
		panelContainer.add(balancePanel, "Balance");


		// Create Frame
		JFrame frame = new JFrame();
		frame.setTitle("Bank");
		frame.setSize(1920, 1080);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.pack();
		frame.setVisible(true);
		frame.add(panelContainer);
		switchToHomeScreen(); // cl.show(panelContainer, "Home");


		int delay = 50; // Milliseconds
		ActionListener taskPerformer = new KeypadActionListoner(this, comPort);
		new Timer(delay, taskPerformer).start();
	}

	public void switchToBalanceScreen() {
		cl.show(panelContainer, "Balance");
		currentScreen = 1;
	}

	public void switchToHomeScreen() {
		cl.show(panelContainer, "Home");
		currentScreen = 0;
	}
}
