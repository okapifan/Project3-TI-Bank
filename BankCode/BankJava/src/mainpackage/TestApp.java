package mainpackage;

import mypackage.*;

import java.awt.*;
import javax.swing.*;

public class TestApp {
	static CardLayout cl = new CardLayout(5, 5);
	static JPanel panelContainer = new JPanel(cl);
	public static ContentHandler contentHandler = new ContentHandler(cl, panelContainer);

	public static void main(String[] args) {
		//TestDatabaseHandler();
		TestPage();
	}

	private static void TestDatabaseHandler() {
		DatabaseHandler databaseHandler = new DatabaseHandler("US", "Timobank");

		String pinCode = databaseHandler.Test("US", "Timobank", "1234", "1");
		System.out.println(pinCode);
	}

	private static void TestPage() {
		cl = new CardLayout(5, 5);
		panelContainer = new JPanel(cl);
		contentHandler = new ContentHandler(cl, panelContainer);
		


		JPanel07 testPanel = new JPanel07();
		testPanel.changeAvailableBillPanels(true, false, true, false);


		panelContainer.add(testPanel, "Panel");

		JFrame frame = new JFrame();
		frame.setLocationRelativeTo(null);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.pack();
		frame.setVisible(true);
		frame.add(panelContainer);
		
		cl.show(panelContainer, "Panel");
	}
}