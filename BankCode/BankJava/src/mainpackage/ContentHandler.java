package mainpackage;

import java.awt.CardLayout;
import java.util.ArrayList;

import javax.swing.JPanel;

import mypackage.JPanel05;
import mypackage.JPanel09;

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
	private String accountnNr = "1234";
	private String country = "US";
	private String pinCode = "1234";
	private String pinValue = ""; //Get used for page 07: Type amount
	private int[][] pinValueChoices = new int[4][4];
	private int pinValueChoice = 4;
	private Boolean wantsReceipt = false;
	
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
					if(pinValue.length() != 0){
						pinValue = pinValue.substring(0, (pinValue.length() - 1));
					}
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
				} else if(data.equals("1")) {
					this.switchTo11TakeCardPanel(true);
				} else if(data.equals("2")) {
					this.switchTo11TakeCardPanel(false);
				}
			}
			break;
		
		case 11:
			if (dataSize == 1) { // Keypad input
				if (data.equals("D")) {  //Has to be changed to a check if the RFID pass is removed
					this.switchTo12PatiencePanel();
				}
			}
			break;
		
		case 12:
			// if(arduinoDone){
			// 	this.switchTo13GreetPanel();
			// }

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
		this.balance = database.getBalance(this.country, this.bankName, this.pinCode, this.accountnNr); //Todo validate & if blocked, send to panel 3
		this.cl.show(panelContainer, "04Menu");
		this.currentScreen = 4;
	}
	
	public void switchTo05BalancePanel() {
		JPanel balancePanel = panelList.get(5);
		if(balancePanel instanceof JPanel05) {
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
		System.out.println(amount);
		System.out.println((int) this.balance);
		if(amount > ((int) this.balance)) {
			switchTo08NotEnoughPanel();
			return;
		}
		if (amount % 5 == 0){
			//Amount is deelbaar door 5
			//deel door 50 daarna 20 daarna 10 daarna 5
			fillPinOptions(0, amount, true, true, true, true);
			//deel door 50 daarna 10 daarna 5
			fillPinOptions(1, amount, true, false, true, true);
			//deel door 20 daarna 10 daarna 5
			fillPinOptions(2, amount, false, true, true, true);
			//deel door 10 daarna 5
			fillPinOptions(3, amount, false, false, true, true);
			JPanel panel = panelList.get(9);
			if(panel instanceof JPanel09) {
				JPanel09 panel2 = (JPanel09) panel;
				panel2.updateLabelOfButtons(this.pinValueChoices, amount);
			}
			this.cl.show(panelContainer, "09ChooseHow");
			this.currentScreen = 9;
		} else {
			//
		}
	}
	
	public void switchTo10ReceiptPanel(int choiceId) {
		this.pinValueChoice = choiceId;
		this.cl.show(panelContainer, "10Receipt");
		this.currentScreen = 10;
	}
	
	public void switchTo11TakeCardPanel(Boolean wantsReceipt) {
		this.wantsReceipt = wantsReceipt;
		this.cl.show(panelContainer, "11TakeCard");
		this.currentScreen = 11;
	}
	
	public void switchTo12PatiencePanel() {
		this.cl.show(panelContainer, "12Patience");
		this.currentScreen = 12;
		this.processMoney(); //Todo get this method working
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
		this.pinValueChoices = new int[4][4];
		this.pinValueChoice = 4;
		this.wantsReceipt = false;
	}

	public void fillPinOptions(int index, int amount, Boolean use50, Boolean use20, Boolean use10, Boolean use5){
		int tempAmount = amount;
		if(use50){
			this.pinValueChoices[index][0] = (int) (tempAmount / 50);
			tempAmount -= this.pinValueChoices[index][0] * 50;
		}
		if(use20){
			this.pinValueChoices[index][1] = (int) (tempAmount / 20);
			tempAmount -= this.pinValueChoices[index][1] * 20;
		}
		if(use10){
			this.pinValueChoices[index][2] = (int) (tempAmount / 10);
			tempAmount -= this.pinValueChoices[index][2] * 10;
		}
		if(use5){
			this.pinValueChoices[index][3] = (int) (tempAmount / 5);
			//tempAmount -= this.pinValueChoices[index][3] * 5;
		}
	}

	public void processMoney(){
		//use wantsReceipt to print bon
		//use pinchoice to print the money from choice x

		
	}
}
