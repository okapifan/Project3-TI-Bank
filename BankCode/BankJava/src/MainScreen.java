import java.awt.CardLayout;
import javax.swing.*;

import java.awt.*;

public class MainScreen{
	static JPanel panelContainer;
	static JPanel homePanel;
	static JPanel balancePanel;
	static CardLayout cl;
	JButton balanceBtn;
	JButton backToHomeBtn;
	public int currentScreen = 0;
	
	public MainScreen() {
		//Create Frame
		JFrame frame = new JFrame();	
		frame.setTitle("Bank");
		
		//Buttons for all screens
	    balanceBtn = new JButton("1. Check Balance");
	    backToHomeBtn = new JButton("1. Home");
	    
	    //Create CardLayout & the container
	    cl = new CardLayout(5, 5);
	    panelContainer = new JPanel(cl);
	    panelContainer.setBackground(Color.pink);

	    //creates mainscreen
	    homePanel = new JPanel();
	    homePanel.setBackground(Color.red);
	    homePanel.add(balanceBtn);   

	    panelContainer.add(homePanel, "Home");

	    balancePanel = new JPanel();
	    balancePanel.setBackground(Color.blue);
	    balancePanel.add(backToHomeBtn);

	    panelContainer.add(balancePanel, "Balance");

	    balanceBtn.addActionListener(e -> cl.show(panelContainer, "Balance"));
	    backToHomeBtn.addActionListener(e -> cl.show(panelContainer, "Home"));
	    
	    frame.add(panelContainer);
	    cl.show(panelContainer, "Home");

		frame.setSize(1920, 1080);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    frame.setLocationRelativeTo(null);
	    frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
	    frame.pack();
	    frame.setVisible(true);
	}
	
	public void switchToBalanceScreen() {
		balanceBtn.doClick();
	}
	
	public void switchToHomeScreen() {
		backToHomeBtn.doClick();
	}
}
