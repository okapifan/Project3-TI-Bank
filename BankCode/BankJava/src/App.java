
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
import javax.swing.*;

public class App {
	static CardLayout cl;
	static JPanel panelContainer;

	public int currentScreen = 1;

	public static void main(String[] args) {

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
		frame.setTitle("Bank");
		// frame.setSize(1920, 1080);
		frame.setLocationRelativeTo(null);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.pack();
		frame.setVisible(true);
		frame.add(panelContainer);

		cl.show(panelContainer, "Start");
	}


	public void switchToNotInUsePanel() {
		cl.show(panelContainer, "NotInUse");
		currentScreen = 0;
	}
	public void switchToStartPanel() {
		cl.show(panelContainer, "Start");
		currentScreen = 1;
	}
	public void switchToTypePinPanel() {
		cl.show(panelContainer, "TypePin");
		currentScreen = 2;
	}
	public void switchToCardBlockedPanel() {
		cl.show(panelContainer, "CardBlocked");
		currentScreen = 3;
	}
	public void switchToMenuPanel() {
		cl.show(panelContainer, "Menu");
		currentScreen = 4;
	}
	public void switchToBalancePanel() {
		cl.show(panelContainer, "Balance");
		currentScreen = 5;
	}
	public void switchToChooseAmountPanel() {
		cl.show(panelContainer, "ChooseAmount");
		currentScreen = 6;
	}
	public void switchToTypeAmountPanel() {
		cl.show(panelContainer, "TypeAmount");
		currentScreen = 7;
	}
	public void switchToNotEnoughPanel() {
		cl.show(panelContainer, "NotEnough");
		currentScreen = 8;
	}
	public void switchToChooseHowPanel() {
		cl.show(panelContainer, "ChooseHow");
		currentScreen = 9;
	}
	public void switchToReceiptPanel() {
		cl.show(panelContainer, "Receipt");
		currentScreen = 10;
	}
	public void switchToTakeCardPanel() {
		cl.show(panelContainer, "TakeCard");
		currentScreen = 11;
	}
	public void switchToPatiencePanel() {
		cl.show(panelContainer, "Patience");
		currentScreen = 12;
	}
	public void switchToGreetPanel() {
		cl.show(panelContainer, "Greet");
		currentScreen = 13;
	}
}
