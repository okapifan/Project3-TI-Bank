import java.awt.CardLayout;

import javax.swing.JPanel;

public class ContentHandler {
	
	private int currentScreen = 4;
	private CardLayout cl;
	private JPanel panelContainer;
	
	public ContentHandler(CardLayout cl, JPanel panelContainer) {
		this.cl = cl;
		this.panelContainer = panelContainer;
	}
	
	void parseData(String data, int dataSize) {
		switch (this.currentScreen) {
		case 4:
			if (dataSize == 1) { // Keypad input
				if (data.equals("1")) {
					this.switchToBalancePanel();
				}
			} else { // RFID card UID
	
			}
			break;

		case 1:
			if (dataSize == 1) { // Keypad input
				if (data.equals("A")) {
					this.switchToMenuPanel();
				}
			} else { // RFID card UID
	
			}
			break;
	
		default:
			System.out.println("CurrentScreen does not exist");
			break;
		}
	}
	
	public void switchToNotInUsePanel() {
		this.cl.show(panelContainer, "NotInUse");
		this.currentScreen = 0;
	}
	
	public void switchToStartPanel() {
		this.cl.show(panelContainer, "Start");
		this.currentScreen = 1;
	}
	
	public void switchToTypePinPanel() {
		this.cl.show(panelContainer, "TypePin");
		this.currentScreen = 2;
	}
	
	public void switchToCardBlockedPanel() {
		this.cl.show(panelContainer, "CardBlocked");
		this.currentScreen = 3;
	}
	
	public void switchToMenuPanel() {
		this.cl.show(panelContainer, "Menu");
		this.currentScreen = 4;
	}
	
	public void switchToBalancePanel() {
		this.cl.show(panelContainer, "Balance");
		this.currentScreen = 5;
	}
	
	public void switchToChooseAmountPanel() {
		this.cl.show(panelContainer, "ChooseAmount");
		this.currentScreen = 6;
	}
	
	public void switchToTypeAmountPanel() {
		this.cl.show(panelContainer, "TypeAmount");
		this.currentScreen = 7;
	}
	
	public void switchToNotEnoughPanel() {
		this.cl.show(panelContainer, "NotEnough");
		this.currentScreen = 8;
	}
	
	public void switchToChooseHowPanel() {
		this.cl.show(panelContainer, "ChooseHow");
		this.currentScreen = 9;
	}
	
	public void switchToReceiptPanel() {
		this.cl.show(panelContainer, "Receipt");
		this.currentScreen = 10;
	}
	
	public void switchToTakeCardPanel() {
		this.cl.show(panelContainer, "TakeCard");
		this.currentScreen = 11;
	}
	
	public void switchToPatiencePanel() {
		this.cl.show(panelContainer, "Patience");
		this.currentScreen = 12;
	}
	
	public void switchToGreetPanel() {
		this.cl.show(panelContainer, "Greet");
		this.currentScreen = 13;
	}
}
