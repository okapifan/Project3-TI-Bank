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
				if (data.substring(0,1).equals("R")) { // Is RFID card

					//Substring starting from index 0 and ending at 1
					System.out.println(data.substring(0,1));
					// Je leest nu welk type bericht het is
					//Substring starting from index 1 and ending at 3
					System.out.println(data.substring(1, 3));
					// Je leest nu het 2 letterige Land info
					//Substring starting from index 3 and ending at 7
					System.out.println(data.substring(3, 7));
					// Je leest nu het 4 cijferige account nummer
					//Substring starting from index 7
					System.out.println(data.substring(7));
					// Je leest nu de bank naam uit



					// Parse data:
					// R+Land, Banknaam, Account nummer

					this.switchTo02TypePinPanel();
				}

				break;

			case 2:
				if (data.substring(0,1).equals("K")) { // Keypad input
					if (data.equals("K1")) {
						pinCode += "1";
					} else if (data.equals("K2")) {
						pinCode += "2";
					} else if (data.equals("K3")) {
						pinCode += "3";
					} else if (data.equals("K4")) {
						pinCode += "4";
					} else if (data.equals("K5")) {
						pinCode += "5";
					} else if (data.equals("K6")) {
						pinCode += "6";
					} else if (data.equals("K7")) {
						pinCode += "7";
					} else if (data.equals("K8")) {
						pinCode += "8";
					} else if (data.equals("K9")) {
						pinCode += "9";
					} else if (data.equals("K0")) {
						pinCode += "0";
					} else if (data.equals("K#")) {
						pinCode = "";
						//pinCode clearen;
						// laatse character eraf
					}

					if (pinCode.length() == 4) {
						this.switchTo05BalancePanel();
					} else { // RFID card UID
						this.switchTo13GreetPanel();
					}
				}
				break;

			case 4:
				if (dataSize == 1) { // Keypad input
					if (data.equals("1")) {
						this.switchTo05BalancePanel();
					}
				} else { // RFID card UID

				}
				break;

			case 5:
				if (dataSize == 1) { // Keypad input
					if (data.equals("A")) {
						this.switchTo04MenuPanel();
					}
				} else { // RFID card UID
				}
			break;
		

		case 6:
			if (dataSize == 1) { // Keypad input
				if (data.equals("1")) {
					if(this.balance >= 20) {
						this.switchTo09ChooseHowPanel();
					} else {
						this.switchTo08NotEnoughPanel();
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
	
	public void switchTo00NotInUsePanel() {
		this.cl.show(panelContainer, "00NotInUse");
		this.currentScreen = 0;
	}
	
	public void switchTo01StartPanel() {
		this.cl.show(panelContainer, "01Start");
		this.currentScreen = 1;
	}
	
	public void switchTo02TypePinPanel() {
		this.cl.show(panelContainer, "02TypePin");
		this.currentScreen = 2;
	}
	
	public void switchTo03CardBlockedPanel() {
		this.cl.show(panelContainer, "03CardBlocked");
		this.currentScreen = 3;
	}
	
	public void switchTo04MenuPanel() {
		this.cl.show(panelContainer, "04Menu");
		this.currentScreen = 4;
	}
	
	public void switchTo05BalancePanel() {
		JPanel balancePanel = panelList.get(5);
		if(balancePanel instanceof JPanel05) {
			this.balance = database.getBalance(this.country, this.bankName, this.pinCode, this.accountnNr);
			
			JPanel05 balancePanel2 = (JPanel05) balancePanel;
			balancePanel2.changeBalanceLabel(this.balance);
		} else {
			this.switchTo04MenuPanel();
		}
		this.cl.show(panelContainer, "05Balance");
		this.currentScreen = 5;
	}
	
	public void switchTo06ChooseAmountPanel() {
		this.cl.show(panelContainer, "06ChooseAmount");
		this.currentScreen = 6;
	}
	
	public void switchTo07TypeAmountPanel() {
		this.cl.show(panelContainer, "07TypeAmount");
		this.currentScreen = 7;
	}
	
	public void switchTo08NotEnoughPanel() {
		this.cl.show(panelContainer, "08NotEnough");
		this.currentScreen = 8;
	}
	
	public void switchTo09ChooseHowPanel() {
		this.cl.show(panelContainer, "09ChooseHow");
		this.currentScreen = 9;
	}
	
	public void switchTo10ReceiptPanel() {
		this.cl.show(panelContainer, "10Receipt");
		this.currentScreen = 10;
	}
	
	public void switchTo11TakeCardPanel() {
		this.cl.show(panelContainer, "11TakeCard");
		this.currentScreen = 11;
	}
	
	public void switchTo12PatiencePanel() {
		this.cl.show(panelContainer, "12Patience");
		this.currentScreen = 12;
	}
	
	public void switchTo13GreetPanel() {
		this.cl.show(panelContainer, "13Greet");
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
