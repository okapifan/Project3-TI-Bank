import java.awt.CardLayout;
import javax.swing.*;

import java.awt.*;

public class MainScreen{
	static JPanel panelContainer;
	static JPanel homePanel;
	static JPanel balancePanel;
	static CardLayout cl;
	
	public MainScreen() {
		//Create Frame
		JFrame frame = new JFrame();	
		frame.setTitle("Bank");
		
		//Buttons for all screens
	    JButton balanceBtn = new JButton("Check Balance");
	    JButton backToHomeBtn = new JButton("Home");

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
}
