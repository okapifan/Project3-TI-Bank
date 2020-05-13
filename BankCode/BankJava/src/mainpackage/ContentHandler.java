package mainpackage;

import java.awt.CardLayout;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

import mypackage.*;

public class ContentHandler {
	private DatabaseHandler database;
	
	private int currentScreen = 5;
	private CardLayout cl;
	private JPanel panelContainer;

	private TimerTask task;
	private Timer timer = new Timer();
	private int timeoutTime = 45000; //In miliseconds
	private int timeoutGreet = 3000; //In miliseconds

	//user information
	private float balance = 0;
	// private String bankName = "";
	// private String accountnNr = "";
	// private String country = "";
	// private String pinCode = "";

	private String bankName = "TIMO";
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

		this.database = new DatabaseHandler("US", "TIMO");

		// JPanel balancePanel = panelList.get(0);
		// JPanel05 balancePanel2 = (JPanel05) balancePanel;
		// balancePanel2.get()
		// //witdrawMoneyBtn.addActionListener(e -> switchToWitdrawScreen());
		// balancePanel2.changeBalanceLabel(this.balance);
	}
	
// R: RFID card (RUS-TIMO-01234567)
// K: Keaypad key (1,2,3,4,5,6,7,8,9,0,*,#,A,B,C,D)
// C: Card in or out (in,out) in can be replaced with R
// D: Dispence money Done ()



	void parseData(String data, int dataSize) {
		switch (this.currentScreen) {
			case 1:
				if (data.substring(0,1).equals("R")) { // RFID card

					// Parse data:
					// R, Land, Bankcode, Account nummer
					// VB: RUS-TIMO-01234567

					// Je leest nu het 2 letterige Land info
					country = data.substring(1, 3);
					System.out.println(country);

					// Je leest nu de bank naam uit
					bankName = data.substring(4, 8);
					System.out.println(bankName);

					// Je leest nu het 4 cijferige account nummer
					accountnNr = data.substring(9);
					System.out.println(accountnNr);


					this.switchTo02TypePinPanel();
				}
				break;

			case 2:
				if (data.substring(0,1).equals("K")) { // Keypad input
					String key = data.substring(1,2);
					if (key.equals("1")) {
						pinCode += "1";
					} else if (key.equals("2")) {
						pinCode += "2";
					} else if (key.equals("3")) {
						pinCode += "3";
					} else if (key.equals("4")) {
						pinCode += "4";
					} else if (key.equals("5")) {
						pinCode += "5";
					} else if (key.equals("6")) {
						pinCode += "6";
					} else if (key.equals("7")) {
						pinCode += "7";
					} else if (key.equals("8")) {
						pinCode += "8";
					} else if (key.equals("9")) {
						pinCode += "9";
					} else if (key.equals("0")) {
						pinCode += "0";
					} else if (key.equals("#")) {
						pinCode = "";
						// pinCode clearen;
						// Laatse character eraf
					}

					if (pinCode.length() == 4) {
						this.switchTo04MenuPanel(); // Test pin in switchTo04MenuPanel()
					}
				}
				break;
		
			case 4:
				if (data.substring(0,1).equals("K")) { // Keypad input
					String key = data.substring(1,2);
					if (key.equals("1")) {
						this.switchTo05BalancePanel();
					} else if (key.equals("2")) {
						this.switchTo06ChooseAmountPanel();
					} else if (key.equals("3")) {
						this.switchTo09ChooseHowPanel(70);
					} else if (key.equals("B")) {
						this.switchTo11TakeCardPanel(false);
						//Todo Fix later the order (skip 12)
					}
				}
				break;

			case 5:
				if (data.substring(0,1).equals("K")) { // Keypad input
					String key = data.substring(1,2);
					if (key.equals("A")) {
						this.switchTo04MenuPanel();
					} else if (key.equals("B")) {
						this.switchTo11TakeCardPanel(false);
					}
				}
				break;

			case 6:
				if (data.substring(0,1).equals("K")) { // Keypad input
					String key = data.substring(1,2);
					if (key.equals("1")) {
						this.switchTo09ChooseHowPanel(20);
					} else if(key.equals("2")) {
						this.switchTo09ChooseHowPanel(50);
					} else if(key.equals("3")) {
						this.switchTo09ChooseHowPanel(70);
					} else if(key.equals("4")) {
						this.switchTo09ChooseHowPanel(100);
					} else if(key.equals("5")) {
						this.switchTo09ChooseHowPanel(150);
					} else if(key.equals("6")) {
						this.switchTo09ChooseHowPanel(200);
					} else if(key.equals("7")) {
						this.switchTo07TypeAmountPanel();;
					} else if(key.equals("A")) {
						this.switchTo04MenuPanel();
					} else if (key.equals("B")) {
						this.switchTo11TakeCardPanel(false);
					}
				} 
				break;
			
			case 7:
				if (data.substring(0,1).equals("K")) { // Keypad input
					String key = data.substring(1,2);
					if (key.equals("1") || key.equals("2") || key.equals("3") || key.equals("4") || key.equals("5") || 
						key.equals("6") || key.equals("7") || key.equals("8") || key.equals("9") || key.equals("0")) {
						pinValue += key;
					} else if (key.equals("#")) {
						int amount = Integer.parseInt(pinValue);
						pinValue = "";
						this.switchTo09ChooseHowPanel(amount);
					} else if (key.equals("*")) {
						if(pinValue.length() != 0){
							pinValue = pinValue.substring(0, (pinValue.length() - 1));
						}
					} else if (key.equals("A")) {
						this.switchTo04MenuPanel();
					} else if (key.equals("B")) {
						this.switchTo11TakeCardPanel(false);
					}
					App.panel07TypeAmount.updateTextfield(pinValue);
				}
				break;
			
			case 8:
				if (data.substring(0,1).equals("K")) { // Keypad input
					String key = data.substring(1,2);
					if (key.equals("A")) {
						this.switchTo04MenuPanel();
					} else if (key.equals("B")) {
						this.switchTo11TakeCardPanel(false);
					}
				}
				break;

			case 9:
				if (data.substring(0,1).equals("K")) { // Keypad input
					String key = data.substring(1,2);
					if (key.equals("1")) {
						this.switchTo10ReceiptPanel(1);
					} else if(key.equals("2")) {
						this.switchTo10ReceiptPanel(2);
					} else if(key.equals("3")) {
						this.switchTo10ReceiptPanel(3);
					} else if(key.equals("4")) {
						this.switchTo10ReceiptPanel(4);
					} else if(key.equals("A")) {
						this.switchTo04MenuPanel();
					} else if (key.equals("B")) {
						this.switchTo11TakeCardPanel(false);
					}
				}
				break;

			case 10:
				if (data.substring(0,1).equals("K")) { // Keypad input
					String key = data.substring(1,2);
					if(data.equals("1")) {
						this.switchTo11TakeCardPanel(true);
					} else if(data.equals("2")) {
						this.switchTo11TakeCardPanel(false);
					} else if (key.equals("A")) {
						this.switchTo04MenuPanel();
					} else if (key.equals("B")) {
						this.switchTo11TakeCardPanel(false);
					}
				}
				break;
			
			case 11:
				if (data.substring(0,1).equals("K")) { // Keypad input
					String key = data.substring(1,2);
					if (key.equals("D")) { // Temporary: Has to be changed to a check if the RFID pass is removed
						this.switchTo12PatiencePanel();
					}
				} else if (data.substring(0,1).equals("C")) { // Card in or out (Later: in every screen)
					String key = data.substring(1,2);
					if (key.equals("out")) {
						this.switchTo12PatiencePanel();
					}
				}
				break;
			
			case 12:
				if (data.substring(0,1).equals("D")) { // Dispence money Done
					this.switchTo13GreetPanel();
				}

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
		//App.panel01Start.changeAvailableBillPanels(available5, available10, available20, available50);
		this.cl.show(panelContainer, "01Start");
		this.currentScreen = 1;
	}
	
	public void switchTo02TypePinPanel() {
		this.startTimer(timeoutTime);
		this.cl.show(panelContainer, "02TypePin");
		this.currentScreen = 2;
	}
	
	public void switchTo03CardBlockedPanel() {
		this.startTimer(timeoutTime);
		this.cl.show(panelContainer, "03CardBlocked");
		this.currentScreen = 3;
	}
	
	public void switchTo04MenuPanel() {
		this.startTimer(timeoutTime);
		this.balance = database.getBalance(this.country, this.bankName, this.pinCode, this.accountnNr); //Todo validate & if blocked, send to panel 3
		this.cl.show(panelContainer, "04Menu");
		this.currentScreen = 4;
	}
	
	public void switchTo05BalancePanel() {
		this.startTimer(timeoutTime);
		App.panel05Balance.changeBalanceLabel(this.balance);
		this.cl.show(panelContainer, "05Balance");
		this.currentScreen = 5;
	}
	
	public void switchTo06ChooseAmountPanel() {
		this.startTimer(timeoutTime);
		this.cl.show(panelContainer, "06ChooseAmount");
		this.currentScreen = 6;
	}
	
	public void switchTo07TypeAmountPanel() {
		this.startTimer(timeoutTime);
		this.resetPanel7();
		//App.panel07TypeAmount.changeAvailableBillPanels(available5, available10, available20, available50);
		this.cl.show(panelContainer, "07TypeAmount");
		this.currentScreen = 7;
	}
	
	public void switchTo08NotEnoughPanel() {
		this.startTimer(timeoutTime);
		this.cl.show(panelContainer, "08NotEnough");
		this.currentScreen = 8;
	}
	
	public void switchTo09ChooseHowPanel(int amount) {
		this.startTimer(timeoutTime);
		System.out.println(amount + " > " + (int) this.balance);
		if(amount > ((int) this.balance)) {
			switchTo08NotEnoughPanel();
			return;
		}
		if(amount <= 0){
			App.panel07TypeAmount.changeErrorLabel("Error: Je kunt geen bedrag van 0 of lager invoeren!");
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
			App.panel09ChooseHow.updateLabelOfButtons(this.pinValueChoices, amount);
			this.cl.show(panelContainer, "09ChooseHow");
			this.currentScreen = 9;
		} else {
			App.panel07TypeAmount.changeErrorLabel("Bedrag moet kunnen bestaan uit de aanwezige biljetten"); //Todo geef feedback wat incorrect is
		}
	}
	
	public void switchTo10ReceiptPanel(int choiceId) {
		this.startTimer(timeoutTime);
		this.pinValueChoice = choiceId;
		this.cl.show(panelContainer, "10Receipt");
		this.currentScreen = 10;
	}
	
	public void switchTo11TakeCardPanel(Boolean wantsReceipt) {
		startTimer(2000); //Todo Remove this later
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
		this.startTimer(timeoutGreet);	
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
		this.pinValue = "";
		this.wantsReceipt = false;
	}

	public void resetPanel7(){
		App.panel07TypeAmount.changeErrorLabel("");
		App.panel07TypeAmount.updateTextfield("");
		this.pinValue = "";
	}

	public void resetPanel2(){
		App.panel02TypePin.changeErrorLabel("");
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

	public void startTimer(int miliseconds){
		stopTimer();
		task = new TimerTask() {
			public void run(){
				switchTo01StartPanel();
				resetInformation();
				this.cancel();
			}
		};
		timer.schedule(task, miliseconds);
	}

	public void stopTimer(){
		if(task != null){
			task.cancel();
		}
	}

	public void processMoney(){
		//use wantsReceipt to print bon
		//use pinchoice to print the money from choice x

		
	}
}
