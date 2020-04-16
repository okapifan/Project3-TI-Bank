package mainpackage;

import java.awt.CardLayout;
import java.util.ArrayList;

import javax.swing.JPanel;

import mypackage.JPanel05;

public class ContentHandler {
	private DatabaseHandler database;
	
	private int currentScreen = 5;
	private CardLayout cl;
	private JPanel panelContainer;
	private ArrayList<JPanel> panelList;

	//user information
	private float balance = 0;
	// private String bankName = "";
	// private String accountnNr = "";
	// private String country = "";
	// private String pinCode = "";

	private String bankName = "Timobank";
	private String accountnNr = "1";
	private String country = "US";
	private String pinCode = "1234";
	
	public ContentHandler(CardLayout cl, JPanel panelContainer) {
		this.cl = cl;
		this.panelContainer = panelContainer;

		this.database = new DatabaseHandler("US", "Timobank");


		// JPanel balancePanel = panelList.get(0);
		// JPanel05 balancePanel2 = (JPanel05) balancePanel;
		// balancePanel2.get()
		// //witdrawMoneyBtn.addActionListener(e -> switchToWitdrawScreen());
		// balancePanel2.changeBalanceLabel(this.balance);
	}

	public void setPanelList(ArrayList<JPanel> pl) {
		this.panelList = pl;
	}
	
	void parseData(String data, int dataSize) {
		switch (this.currentScreen) {
		case 1:
			if (true)  { // Is RFID card

				// Parse data:
				// Land, Banknaam, Account nummer

				this.switchToTypePinPanel();
			}
			break;
		
		case 2:
			if (dataSize == 2) { // Keypad input
				if (data.equals("k1")) {
					pinCode += "1";
				}
				else if (data.equals("k2")) {
					pinCode += "2";
				}
				else if (data.equals("k#")) {
					//pinCode ; // laatse character eraf
				}

				if (pinCode.length() == 4){
					this.switchToBalancePanel();
				}
			}
			break;
		
		case 4:
			if (dataSize == 1) { // Keypad input
				if (data.equals("1")) {
					this.switchToBalancePanel();
				}
			} else { // RFID card UID
	
			}
			break;

		case 5:
			if (dataSize == 1) { // Keypad input
				if (data.equals("A")) {
					this.switchToMenuPanel();
				}
			} else { // RFID card UID
	
			}
			break;
		

		case 6:
			if (dataSize == 1) { // Keypad input
				if (data.equals("1")) {
					if(this.balance >= 20) {
						this.switchToChooseHowPanel();
					} else {
						this.switchToNotEnoughPanel();
					}
				} 
				//Todo: Add for every value

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
		JPanel balancePanel = panelList.get(5);
		if(balancePanel instanceof JPanel05) {
			this.balance = database.getBalance(this.country, this.bankName, this.pinCode, this.accountnNr);
			
			JPanel05 balancePanel2 = (JPanel05) balancePanel;
			balancePanel2.changeBalanceLabel(this.balance);
		} else {
			this.switchToMenuPanel();
		}
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

	public void resetInformation() {
		this.balance = 0;
		this.country = "";
		this.bankName = "";
		this.accountnNr = "";
		this.pinCode = "";
	}
}
