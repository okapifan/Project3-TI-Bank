import mypackage.*;

import mainpackage.*;

import java.awt.*;

import javax.swing.*;

public class TestApp {
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
		CardLayout cl = new CardLayout(5, 5);
		JPanel panelContainer = new JPanel(cl);
		ContentHandler contentHandler = new ContentHandler(cl, panelContainer);


		JPanel testPanel = new JPanel07(contentHandler);


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