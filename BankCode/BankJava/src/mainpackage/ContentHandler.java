package mainpackage;

import java.awt.CardLayout;
import java.util.ArrayList;

import javax.swing.JPanel;

import mypackage.JPanel05;
import sun.jvm.hotspot.utilities.IntArray;

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
	private String pinValue = ""; //Get used for page 07: Type amount
	private int[][] pinValueChoices = new int[4][4];
	
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

				this.switchTo02TypePinPanel();
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
					this.switchTo05BalancePanel();
				}
			}
			break;
		
		case 4:
			if (dataSize == 1) { // Keypad input
				if (data.equals("1")) {
					this.switchTo05BalancePanel();
				} else if (data.equals("2")) {
					this.switchTo06ChooseAmountPanel();
				} else if (data.equals("3")) {
					this.switchTo09ChooseHowPanel(70);
				} else if (data.equals("A")) {
					this.switchTo13GreetPanel();
					//Todo Fix later the order (11 and reset)
				}
			}
			break;

		case 5:
			if (dataSize == 1) { // Keypad input
				if (data.equals("A")) {
					this.switchTo04MenuPanel();
				}
			}
			break;

		case 6:
			if (dataSize == 1) { // Keypad input
				if (data.equals("1")) {
					this.switchTo09ChooseHowPanel(20);
				} else if(data.equals("2")) {
					this.switchTo09ChooseHowPanel(50);
				} else if(data.equals("3")) {
					this.switchTo09ChooseHowPanel(70);
				} else if(data.equals("4")) {
					this.switchTo09ChooseHowPanel(100);
				} else if(data.equals("5")) {
					this.switchTo09ChooseHowPanel(150);
				} else if(data.equals("6")) {
					this.switchTo09ChooseHowPanel(200);
				} else if(data.equals("7")) {
					this.switchTo07TypeAmountPanel();;
				} else if(data.equals("A")) {
					this.switchTo04MenuPanel();
				}
			} 
			break;
		
		case 7:
			if (dataSize == 1) { // Keypad input
				if (data.equals("1") || data.equals("2") || data.equals("3") || data.equals("4") || data.equals("5") || 
					data.equals("6") || data.equals("7") || data.equals("8") || data.equals("9") || data.equals("0")) {
					pinValue += data;
				} else if (data.equals("#")) {
					int amount = Integer.parseInt(pinValue);
					pinValue = "";
					this.switchTo09ChooseHowPanel(amount);
				} else if (data.equals("*")) {
					pinValue = pinValue.substring(0, (pinValue.length() - 1));
				} else if (data.equals("A")) {
					this.switchTo04MenuPanel();
				}
			}
			break;
		
		case 8:
			if (dataSize == 1) { // Keypad input
				if (data.equals("A")) {
					this.switchTo04MenuPanel();
				}
			}
			break;

		case 9:
			if (dataSize == 1) { // Keypad input
				if (data.equals("A")) {
					this.switchTo04MenuPanel();
				}
			}
			break;

		case 10:
			if (dataSize == 1) { // Keypad input
				if (data.equals("A")) {
					this.switchTo04MenuPanel();
				}
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
	
	public void switchTo09ChooseHowPanel(int amount) {
		if(amount < this.balance) {
			switchTo08NotEnoughPanel();
			return;
		}
		if (amount % 5 == 0){
			//Amount is deelbaar door 5
			
			// //deel door 50 daarna 20 daarna 10 daarna 5
			// int tempAmount = amount;
			// this.pinValueChoices[0][0] = (int) Math.floor(tempAmount / 50);
			// tempAmount -= this.pinValueChoices[0][0];
			// this.pinValueChoices[0][1] = (int) Math.floor(tempAmount / 20);
			// tempAmount -= this.pinValueChoices[0][1];
			// this.pinValueChoices[0][2] = (int) Math.floor(tempAmount / 10);
			// tempAmount -= this.pinValueChoices[0][2];
			// this.pinValueChoices[0][3] = (int) Math.floor(tempAmount / 5);
			// tempAmount -= this.pinValueChoices[0][3];
			// //deel door 50 daarna 10 daarna 5
			// tempAmount = amount;
			// this.pinValueChoices[1][0] = (int) Math.floor(tempAmount / 50);
			// tempAmount -= this.pinValueChoices[1][0];
			// this.pinValueChoices[1][1] = 0;
			// this.pinValueChoices[1][2] = (int) Math.floor(tempAmount / 10);
			// tempAmount -= this.pinValueChoices[1][2];
			// this.pinValueChoices[1][3] = (int) Math.floor(tempAmount / 5);
			// tempAmount -= this.pinValueChoices[1][3];
			// //deel door 20 daarna 10 daarna 5
			// tempAmount = amount;
			// this.pinValueChoices[2][0] = 0;
			// this.pinValueChoices[2][1] = (int) Math.floor(tempAmount / 20);
			// tempAmount -= this.pinValueChoices[2][1];
			// this.pinValueChoices[2][2] = (int) Math.floor(tempAmount / 10);
			// tempAmount -= this.pinValueChoices[2][2];
			// this.pinValueChoices[2][3] = (int) Math.floor(tempAmount / 5);
			// tempAmount -= this.pinValueChoices[2][3];
			// //deel door 10 daarna 5
			// tempAmount = amount;
			// this.pinValueChoices[3][0] = 0;
			// this.pinValueChoices[3][1] = 0;
			// this.pinValueChoices[3][2] = (int) Math.floor(tempAmount / 10);
			// tempAmount -= this.pinValueChoices[3][2];
			// this.pinValueChoices[3][3] = (int) Math.floor(tempAmount / 5);
			// tempAmount -= this.pinValueChoices[3][3];

			this.cl.show(panelContainer, "09ChooseHow");
			this.currentScreen = 9;
		} else {
			//
		}
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
		this.resetInformation();
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

	public void fillPinOptions(int index, int amount, Boolean use50, Boolean use20, Boolean use10, Boolean use5){
		int tempAmount = amount;
		if(use50){
			this.pinValueChoices[index][0] = (int) Math.floor(tempAmount / 50);
			tempAmount -= this.pinValueChoices[index][0];
		}
		if(use20){
			this.pinValueChoices[index][1] = (int) Math.floor(tempAmount / 20);
			tempAmount -= this.pinValueChoices[index][1];
		}
		if(use10){
			this.pinValueChoices[index][2] = (int) Math.floor(tempAmount / 10);
			tempAmount -= this.pinValueChoices[index][2];
		}
		if(use5){
			this.pinValueChoices[index][3] = (int) Math.floor(tempAmount / 5);
			tempAmount -= this.pinValueChoices[index][3];
		}
		
	}
}
