import java.awt.*;
import java.awt.CardLayout;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

import com.fazecast.jSerialComm.SerialPort;

public class MainScreen {
	static JPanel panelContainer;
	static JPanel homePanel;
	static JPanel balancePanel;
	static JPanel startPanel;
	static JPanel loginPanel;
	static JPanel witdrawPanel;
	static JPanel choosePanel;
	static CardLayout cl;
	JButton balanceBtn;
	JButton backToHomeBtn;
	JButton backToHomeBtn2;
	JButton backToHomeBtn3;
	JButton witdrawMoneyBtn;
	JButton witdraw20MoneyBtn;
	JButton witdraw50MoneyBtn;
	JButton witdraw70MoneyBtn;
	JButton witdraw70MoneyBtn2;
	JButton witdraw100MoneyBtn;
	JButton witdraw150MoneyBtn;
	JButton witdraw200MoneyBtn;
	JButton witdrawChoosenMoneyBtn;
	public int currentScreen = 0;

	public MainScreen(SerialPort comPort) {

		// Buttons for all screens
		balanceBtn = new JButton("1. Check Saldo");
		balanceBtn.addActionListener(e -> switchToBalanceScreen());

		backToHomeBtn = new JButton("A. Terug naar Menu");
		backToHomeBtn.addActionListener(e -> switchToHomeScreen());
		backToHomeBtn2 = new JButton("A. Terug naar Menu");
		backToHomeBtn2.addActionListener(e -> switchToHomeScreen());
		backToHomeBtn3 = new JButton("A. Terug naar Menu");
		backToHomeBtn3.addActionListener(e -> switchToHomeScreen());
		
		witdrawMoneyBtn = new JButton("2. Geldopnemen");
		witdrawMoneyBtn.addActionListener(e -> switchToWitdrawScreen());
		
		witdraw20MoneyBtn = new JButton("1. $20 Opnemen");
		witdraw50MoneyBtn = new JButton("2. $50 Opnemen");
		witdraw70MoneyBtn = new JButton("3. $70 Opnemen");
		witdraw70MoneyBtn2 = new JButton("3. $70 Opnemen");
		witdraw100MoneyBtn = new JButton("4. $100 Opnemen");
		witdraw150MoneyBtn = new JButton("5. $150 Opnemen");
		witdraw200MoneyBtn = new JButton("6. $200 Opnemen");
		
		witdrawChoosenMoneyBtn = new JButton("7. Kies Bedrag");
		witdrawChoosenMoneyBtn.addActionListener(e -> switchToChooseScreen());
		
		// Create CardLayout & the container
		cl = new CardLayout(5, 5);
		panelContainer = new JPanel(cl);
		panelContainer.setBackground(Color.pink);


		// Creates Screens
		startPanel = new JPanel();
		//Todo add Labels and logo
		startPanel.setBackground(Color.green);
		panelContainer.add(startPanel, "Start");
		
		loginPanel = new JPanel();
		JLabel fillInLabel = new JLabel();
		fillInLabel.setText("Voer uw pincode in");
		loginPanel.add(fillInLabel);
		JLabel pinCodeLabel = new JLabel();
		pinCodeLabel.setText("[****]");
		loginPanel.add(pinCodeLabel);
		JLabel wrongPinCodeLabel = new JLabel();
		wrongPinCodeLabel.setText("");
		loginPanel.add(wrongPinCodeLabel);
		loginPanel.setBackground(Color.orange);
		panelContainer.add(loginPanel, "Login");
		
		homePanel = new JPanel();
		homePanel.setBackground(Color.red);
		homePanel.add(balanceBtn);
		homePanel.add(witdrawMoneyBtn);
		homePanel.add(witdraw70MoneyBtn2);
		panelContainer.add(homePanel, "Home");

		balancePanel = new JPanel();
		balancePanel.setBackground(Color.blue);
		JLabel balanceLabel = new JLabel();
		balanceLabel.setText("Uw saldo is: $"+"Dummy-Text");
		balancePanel.add(balanceLabel);
		balancePanel.add(backToHomeBtn);
		panelContainer.add(balancePanel, "Balance");

		witdrawPanel = new JPanel();
		witdrawPanel.setBackground(Color.cyan);
		witdrawPanel.add(backToHomeBtn2);
		witdrawPanel.add(witdraw20MoneyBtn);
		witdrawPanel.add(witdraw50MoneyBtn);
		witdrawPanel.add(witdraw100MoneyBtn);
		witdrawPanel.add(witdraw150MoneyBtn);
		witdrawPanel.add(witdraw200MoneyBtn);
		witdrawPanel.add(witdrawChoosenMoneyBtn);
		panelContainer.add(witdrawPanel, "Witdraw");

		choosePanel = new JPanel();
		JLabel fillInAmountLabel = new JLabel();
		choosePanel.add(backToHomeBtn3);
		fillInAmountLabel.setText("Toets het gewenste bedrag in");
		choosePanel.add(fillInAmountLabel);
		JLabel amountLabel = new JLabel();
		amountLabel.setText("[$"+"Dummy-Text"+"] # ENTER");
		choosePanel.add(amountLabel);
		choosePanel.setBackground(Color.magenta);
		panelContainer.add(choosePanel, "Choose");
		
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
		switchToHomeScreen(); //cl.show(panelContainer, "Home");

		frame.addWindowListener(new WindowAdapter() { // Exit button override
			@Override
			public void windowClosing(WindowEvent e) {
				System.out.println("Exit");
				comPort.closePort(); // Unnecessary?
				System.exit(0);
			}
		});


		int delay = 50; // Milliseconds
		ActionListener taskPerformer = new KeypadActionListoner(this, comPort);
		new Timer(delay, taskPerformer).start();
	}

	public void switchToStartScreen() {
		cl.show(panelContainer, "Start");
		currentScreen = 1;
	}
	
	public void switchToLoginScreen() {
		cl.show(panelContainer, "Login");
		currentScreen = 2;
	}
	
	public void switchToBlockedScreen() {
		cl.show(panelContainer, "Blocked");
		currentScreen = 3;
	}
	
	public void switchToHomeScreen() {
		cl.show(panelContainer, "Home");
		currentScreen = 4;
	}
	
	public void switchToBalanceScreen() {
		cl.show(panelContainer, "Balance");
		currentScreen = 5;
	}

	public void switchToWitdrawScreen() {
		cl.show(panelContainer, "Witdraw");
		currentScreen = 6;
	}
	
	public void switchToChooseScreen() {
		cl.show(panelContainer, "Choose");
		currentScreen = 7;
	}
	
}
