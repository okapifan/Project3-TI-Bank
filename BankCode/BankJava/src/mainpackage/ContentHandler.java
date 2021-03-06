package mainpackage;

/*
 * Project 3/4
 * 
 * Daniël van der Drift
 * Robbin Koot
 * Timo van der Meer
 * Zoë Zegers
 */

import java.awt.CardLayout;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

import org.json.simple.parser.JSONParser;
import org.json.JSONObject;

public class ContentHandler {
	public DatabaseHandler database;

	private int currentScreen = 1;
	private CardLayout cl;
	private JPanel panelContainer;

	private TimerTask task;
	private Timer timer = new Timer();
	private int timeoutTime = 45000; // In milliseconds
	private int timeoutGreet = 3000; // In milliseconds
	private int cardblockedTime = 10000;

	// user information
	private double balance = 0;
	private int attempts = 0;

	private String bankName = "";
	private String accountnNr = "";
	private String country = "";
	private String pinCode = "";

	private String pinValue = ""; // Get used for page 07: Type amount
	private int[][] pinValueChoices = new int[4][4];
	private int pinValueChoice = 4;
	private Boolean wantsReceipt = false;
	private int withdrawValue = 0;
	private boolean processDone = false;

	public ContentHandler(CardLayout cl, JPanel panelContainer) {
		this.cl = cl;
		this.panelContainer = panelContainer;

		this.database = new DatabaseHandler("US", "TIMO");
	}

	// Receive:
	// R: RFID card (US-TIMO-01234567)
	// K: Keypad key (1,2,3,4,5,6,7,8,9,0,*,#,A,B,C,D)
	// C: Card in or out (in,out) in can be replaced with R
	// D: Dispence money Done ()

	// Send:
	// P: Print bon (Time-pinValue-accountnNr-balance)(Sat May 23 13:58:45 CEST
	// 2020-65-00001234-180)
	// D: Dispence money (amount $50 bills - amount $20 bills - amount $10 bills -
	// amount $5 bills)(1-0-2-0)

	void parseData(String data, int dataSize) {
		switch (this.currentScreen) {
			case 1:
				if (data.substring(0, 1).equals("R")) { // RFID card

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
				if (data.substring(0, 1).equals("K")) { // Keypad input
					String key = data.substring(1, 2);
					if (pinCode.length() < 4) {
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
						}
					}
					if (key.equals("#")) {
						pinCode = ""; // pinCode clearen
					}

					App.panel02TypePin.updateTextfield(pinCode);
					if (pinCode.length() == 4) {
						this.switchTo04MenuPanel();
					}
				} else if (data.substring(0, 1).equals("C")) { // Card in or out
					String key = data.substring(1);
					if (key.equals("out")) {
						this.switchTo13GreetPanel();
					}
				}
				break;

			case 3:
				if (data.substring(0, 1).equals("C")) { // Card in or out
					String key = data.substring(1);
					if (key.equals("out")) {
						this.switchTo13GreetPanel();
					}
				}
				break;

			case 4:
				if (data.substring(0, 1).equals("K")) { // Keypad input
					String key = data.substring(1, 2);
					if (key.equals("1")) {
						this.switchTo05BalancePanel();
					} else if (key.equals("2")) {
						this.switchTo06ChooseAmountPanel();
					} else if (key.equals("3")) {
						this.switchTo11TakeCardPanelFor70();
					} else if (key.equals("B")) {
						this.switchTo11TakeCardPanel(false, false);
					}
				} else if (data.substring(0, 1).equals("C")) { // Card in or out
					String key = data.substring(1);
					if (key.equals("out")) {
						this.switchTo13GreetPanel();
					}
				}
				break;

			case 5:
				if (data.substring(0, 1).equals("K")) { // Keypad input
					String key = data.substring(1, 2);
					if (key.equals("A")) {
						this.switchTo04MenuPanel();
					} else if (key.equals("B")) {
						this.switchTo11TakeCardPanel(false, false);
					}
				} else if (data.substring(0, 1).equals("C")) { // Card in or out
					String key = data.substring(1);
					if (key.equals("out")) {
						this.switchTo13GreetPanel();
					}
				}
				break;

			case 6:
				if (data.substring(0, 1).equals("K")) { // Keypad input
					String key = data.substring(1, 2);
					if (key.equals("1")) {
						this.switchTo09ChooseHowPanel(20);
					} else if (key.equals("2")) {
						this.switchTo09ChooseHowPanel(50);
					} else if (key.equals("3")) {
						this.switchTo09ChooseHowPanel(70);
					} else if (key.equals("4")) {
						this.switchTo09ChooseHowPanel(100);
					} else if (key.equals("5")) {
						this.switchTo09ChooseHowPanel(150);
					} else if (key.equals("6")) {
						this.switchTo09ChooseHowPanel(200);
					} else if (key.equals("7")) {
						this.switchTo07TypeAmountPanel();
						;
					} else if (key.equals("A")) {
						this.switchTo04MenuPanel();
					} else if (key.equals("B")) {
						this.switchTo11TakeCardPanel(false, false);
					}
				} else if (data.substring(0, 1).equals("C")) { // Card in or out
					String key = data.substring(1);
					if (key.equals("out")) {
						this.switchTo13GreetPanel();
					}
				}
				break;

			case 7:
				if (data.substring(0, 1).equals("K")) { // Keypad input
					String key = data.substring(1, 2);
					if (key.equals("1") || key.equals("2") || key.equals("3") || key.equals("4") || key.equals("5")
							|| key.equals("6") || key.equals("7") || key.equals("8") || key.equals("9")
							|| key.equals("0")) {
						pinValue += key;
					} else if (key.equals("#")) {
						int amount = Integer.parseInt(pinValue);
						pinValue = "";
						this.switchTo09ChooseHowPanel(amount);
					} else if (key.equals("*")) {
						if (pinValue.length() != 0) {
							pinValue = pinValue.substring(0, (pinValue.length() - 1));
						}
					} else if (key.equals("A")) {
						this.switchTo04MenuPanel();
					} else if (key.equals("B")) {
						this.switchTo11TakeCardPanel(false, false);
					}
					App.panel07TypeAmount.updateTextfield(pinValue);
				} else if (data.substring(0, 1).equals("C")) { // Card in or out
					String key = data.substring(1);
					if (key.equals("out")) {
						this.switchTo13GreetPanel();
					}
				}
				break;

			case 8:
				if (data.substring(0, 1).equals("K")) { // Keypad input
					String key = data.substring(1, 2);
					if (key.equals("A")) {
						this.switchTo04MenuPanel();
					} else if (key.equals("B")) {
						this.switchTo11TakeCardPanel(false, false);
					}
				} else if (data.substring(0, 1).equals("C")) { // Card in or out
					String key = data.substring(1);
					if (key.equals("out")) {
						this.switchTo13GreetPanel();
					}
				}
				break;

			case 9:
				if (data.substring(0, 1).equals("K")) { // Keypad input
					String key = data.substring(1, 2);
					if (key.equals("1")) {
						if(App.panel09ChooseHow.checkEnabled(1)){
							this.switchTo10ReceiptPanel(0);
						}
					} else if (key.equals("2")) {
						if(App.panel09ChooseHow.checkEnabled(2)){
							this.switchTo10ReceiptPanel(1);
						}
					} else if (key.equals("3")) {
						if(App.panel09ChooseHow.checkEnabled(3)){
							this.switchTo10ReceiptPanel(2);
						}
					} else if (key.equals("4")) {
						if(App.panel09ChooseHow.checkEnabled(4)){
							this.switchTo10ReceiptPanel(3);
						}
					} else if (key.equals("A")) {
						this.switchTo04MenuPanel();
					} else if (key.equals("B")) {
						this.switchTo11TakeCardPanel(false, false);
					}
				} else if (data.substring(0, 1).equals("C")) { // Card in or out
					String key = data.substring(1);
					if (key.equals("out")) {
						this.switchTo13GreetPanel();
					}
				}
				break;

			case 10:
				if (data.substring(0, 1).equals("K")) { // Keypad input
					String key = data.substring(1, 2);
					if (key.equals("1")) {
						this.switchTo11TakeCardPanel(true, true);
					} else if (key.equals("2")) {
						this.switchTo11TakeCardPanel(false, true);
					} else if (key.equals("A")) {
						this.switchTo04MenuPanel();
					} else if (key.equals("B")) {
						this.switchTo11TakeCardPanel(false, false);
					}
				} else if (data.substring(0, 1).equals("C")) { // Card in or out
					String key = data.substring(1);
					if (key.equals("out")) {
						this.switchTo13GreetPanel();
					}
				}
				break;

			case 11:
				if (data.substring(0, 1).equals("C")) { // Card in or out
					String key = data.substring(1);
					if (key.equals("out")) {
						this.switchTo12PatiencePanel();
					}
				}
				break;

			case 12:
				if (data.substring(0, 1).equals("D")) { // Dispence money Done
					this.switchTo13GreetPanel();
				}
				break;

			case 13:
				break;

			case 14:
				if (data.substring(0, 1).equals("K")) { // Keypad input
					String key = data.substring(1, 2);
					if (key.equals("B")) {
						this.switchTo13GreetPanel();
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
		JSONObject json = getAvailableMoneyJson();
		App.panel01Start.changeAvailableBillPanels((json.getInt("5") > 0),
												   (json.getInt("10") > 0),
												   (json.getInt("20") > 0),
												   (json.getInt("50") > 0));
		this.cl.show(panelContainer, "01Start");
		this.currentScreen = 1;
	}

	public void switchTo02TypePinPanel() {
		this.startTimer(timeoutTime, false);
		resetPanel2();
		this.cl.show(panelContainer, "02TypePin");
		this.currentScreen = 2;
	}

	public void switchTo03CardBlockedPanel() {
		this.startTimer(cardblockedTime, false);
		this.cl.show(panelContainer, "03CardBlocked");
		this.currentScreen = 3;
	}

	public void switchTo04MenuPanel() {
		this.startTimer(timeoutTime, false);

		this.checkAvailability();
		int statusCode = database.getBalance(this.country, this.bankName, this.pinCode, this.accountnNr);
		if (statusCode == 200) {
			this.cl.show(panelContainer, "04Menu");
			this.currentScreen = 4;
		} else if (statusCode == 401) {
			App.panel02TypePin.changeErrorLabel("Error: Foute pincode. Nog " + (3 - attempts) + " pogingen!");
			this.pinCode = ""; // Reset pincode
			App.panel02TypePin.updateTextfield(this.pinCode);
			return;
		} else if (statusCode == 403) {
			switchTo03CardBlockedPanel();
		} else if (statusCode == 404) {
			App.panel02TypePin.changeErrorLabel("Error: Onbekende bron van pas!");
			switchTo02TypePinPanel();
		} else {
			App.panel02TypePin.changeErrorLabel("Error: Er ging iets mis!");
			switchTo02TypePinPanel();
		}
	}

	public void switchTo05BalancePanel() {
		this.startTimer(timeoutTime, false);
		App.panel05Balance.changeBalanceLabel((float) this.balance);
		this.cl.show(panelContainer, "05Balance");
		this.currentScreen = 5;
	}

	public void switchTo06ChooseAmountPanel() {
		this.startTimer(timeoutTime, false);
		this.cl.show(panelContainer, "06ChooseAmount");
		this.currentScreen = 6;
	}

	public void switchTo07TypeAmountPanel() {
		this.startTimer(timeoutTime, false);
		this.resetPanel7();
		JSONObject json = getAvailableMoneyJson();
		App.panel07TypeAmount.changeAvailableBillPanels((json.getInt("5") > 0),
												   (json.getInt("10") > 0),
												   (json.getInt("20") > 0),
												   (json.getInt("50") > 0));
		this.cl.show(panelContainer, "07TypeAmount");
		this.currentScreen = 7;
	}

	public void switchTo08NotEnoughPanel() {
		this.startTimer(timeoutTime, false);
		this.cl.show(panelContainer, "08NotEnough");
		this.currentScreen = 8;
	}

	public void switchTo09ChooseHowPanel(int amount) {
		this.startTimer(timeoutTime, false);
		System.out.println(amount + " > " + (int) this.balance);
		if (amount > ((int) this.balance)) {
			switchTo08NotEnoughPanel();
			return;
		}
		if (amount <= 0) {
			App.panel07TypeAmount.changeErrorLabel("Error: Je kunt geen bedrag van 0 of lager invoeren!");
			return;
		}
		if (amount % 5 == 0) {
			this.withdrawValue = amount;
			// Amount is deelbaar door 5
			// Deel door 50 daarna 20 daarna 10 daarna 5
			fillPinOptions(0, amount, true, true, true, true);
			// Deel door 50 daarna 10 daarna 5
			fillPinOptions(1, amount, true, false, true, true);
			// Deel door 20 daarna 10 daarna 5
			fillPinOptions(2, amount, false, true, true, true);
			// Deel door 10 daarna 5
			fillPinOptions(3, amount, false, false, true, true);
			App.panel09ChooseHow.updateLabelOfButtons(this.pinValueChoices, amount);
			this.cl.show(panelContainer, "09ChooseHow");
			this.currentScreen = 9;
		} else {
			App.panel07TypeAmount.changeErrorLabel("Bedrag moet kunnen bestaan uit de aanwezige biljetten");
		}
	}

	public void switchTo10ReceiptPanel(int choiceId) {
		this.startTimer(timeoutTime, false);
		this.pinValueChoice = choiceId;
		this.cl.show(panelContainer, "10Receipt");
		this.currentScreen = 10;
	}

	public void switchTo11TakeCardPanelFor70(){
		if (70 > ((int) this.balance)) {
			switchTo08NotEnoughPanel();
			return;
		}
		fillPinOptions(0, 70, true, true, true, true);
		fillPinOptions(1, 70, true, false, true, true);
		fillPinOptions(2, 70, false, true, true, true);
		fillPinOptions(3, 70, false, false, true, true);
		
		this.withdrawValue = 70;
		System.out.println(70 + " > " + (int) this.balance);
		this.pinValueChoice = 0;

		this.switchTo11TakeCardPanel(true, true);
	}
	
	public void checkAvailability() {
		JSONObject json = this.getAvailableMoneyJson();
        int available5 = json.getInt("5");
		int available10 = json.getInt("10");
		int available20 = json.getInt("20");
        int available50 = json.getInt("50");
        
        if(available50 >= 1 && available20 >= 1) {
            App.panel04Menu.block70Button(false);
            this.pinValueChoice = 0;
        } else if (available50 >= 1 && available10 >= 2) {
        	App.panel04Menu.block70Button(false);
            this.pinValueChoice = 1;
        } else if (available20 >= 3 && available10 >= 1) {
        	App.panel04Menu.block70Button(false);
            this.pinValueChoice = 2;
        } else if (available10 >= 7) {
        	App.panel04Menu.block70Button(false);
            this.pinValueChoice = 3;
        } else {
        	App.panel04Menu.block70Button(true);
        }
	}

	public void switchTo11TakeCardPanel(Boolean wantsReceipt, boolean endOfProcess) {
		if(endOfProcess){
			this.wantsReceipt = wantsReceipt;
			this.processDone = true;
		}
		this.cl.show(panelContainer, "11TakeCard");
		this.currentScreen = 11;
	}

	public void switchTo12PatiencePanel() {
		stopTimer();
		if (this.processDone) { // Skip to 13 if not pinning money
			this.cl.show(panelContainer, "12Patience");
			this.currentScreen = 12;
			this.processMoney();
		} else {
			switchTo13GreetPanel();
		}
	}

	public void switchTo13GreetPanel() {
		this.startTimer(timeoutGreet, true);
		this.resetInformation();
		this.cl.show(panelContainer, "13Greet");
		this.currentScreen = 13;
	}

	public void switchTo14ReceiptShow() {
		this.startTimer(timeoutTime, true);
		this.cl.show(panelContainer, "14ReceiptShow");
		this.currentScreen = 14;
	}

	public void resetInformation() {
		this.balance = 0;
		this.attempts = 0;
		this.country = "";
		this.bankName = "";
		this.accountnNr = "";
		this.pinCode = "";
		this.pinValueChoices = new int[4][4];
		this.pinValueChoice = 4;
		this.pinValue = "";
		this.withdrawValue = 0;
		this.wantsReceipt = false;
		this.processDone = false;
	}

	public void resetPanel7() {
		App.panel07TypeAmount.changeErrorLabel("");
		App.panel07TypeAmount.updateTextfield("");
		this.pinValue = "";
	}

	public void resetPanel2() {
		App.panel02TypePin.changeErrorLabel("");
		App.panel02TypePin.updateTextfield("");
		this.pinCode = "";
	}

	public void fillPinOptions(int index, int amount, Boolean use50, Boolean use20, Boolean use10, Boolean use5) {
		int tempAmount = amount;
		if (use50) {
			this.pinValueChoices[index][0] = (int) (tempAmount / 50);
			tempAmount -= this.pinValueChoices[index][0] * 50;
		}
		if (use20) {
			this.pinValueChoices[index][1] = (int) (tempAmount / 20);
			tempAmount -= this.pinValueChoices[index][1] * 20;
		}
		if (use10) {
			this.pinValueChoices[index][2] = (int) (tempAmount / 10);
			tempAmount -= this.pinValueChoices[index][2] * 10;
		}
		if (use5) {
			this.pinValueChoices[index][3] = (int) (tempAmount / 5);
			// tempAmount -= this.pinValueChoices[index][3] * 5;
		}
	}

	public void startTimer(int miliseconds, boolean ignoreTakeOut) {
		stopTimer();
		task = new TimerTask() {
			public void run() {
				if (ignoreTakeOut) {
					switchTo01StartPanel();
					resetInformation();
					this.cancel();
				} else {
					switchTo11TakeCardPanel(false, false);
					this.cancel();
				}
			}
		};
		timer.schedule(task, miliseconds);
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public void setAttempts(int attempts) {
		this.attempts = attempts;
	}

	public void stopTimer() {
		if (task != null) {
			task.cancel();
		}
	}

	public JSONObject getAvailableMoneyJson() {
		try {
			Object obj = new JSONParser().parse(new FileReader("src/money.json"));
			JSONObject json = new JSONObject(obj.toString());
			return json;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void updateAvailableMoneyJson(int dollar5,int dollar10,int dollar20,int dollar50){
		try {
			Object obj = new JSONParser().parse(new FileReader("src/money.json"));
			JSONObject json = new JSONObject(obj.toString());
			json.put("5", dollar5);
			json.put("10", dollar10);
			json.put("20", dollar20);
			json.put("50", dollar50);
			FileWriter file = new FileWriter("src/money.json");
	        file.write(json.toString());
	        file.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void processMoney(){
		int statusCode = database.withdraw(this.country, this.bankName, this.pinCode, this.accountnNr, this.withdrawValue);
		if(statusCode == 200){
			// Send String example

			if(wantsReceipt){
				// P: Print bon (pinValue-accountnNr-date)(65-00001234-Sat May 23 13:58:45 CEST 2020)
				Date date = new Date();
				//String receiptString = "P" + pinValue + "-" + accountnNr + "-" + date;
				//App.sendArduino(receiptString);
				
				this.switchTo14ReceiptShow();
				App.panel14ReceiptShow.updateTextfield("" + withdrawValue, accountnNr, date.toString());
			}

			// TODO: Check if delay is needed

			// D: Dispence money (amount $50 bills, amount $20 bills, amount $10 bills, amount $5 bills)(1-0-2-0)
			String moneyString = "D" + pinValueChoices[pinValueChoice][0] + "-" + pinValueChoices[pinValueChoice][1] + "-" + pinValueChoices[pinValueChoice][2] + "-" + pinValueChoices[pinValueChoice][3];
			App.sendArduino(moneyString);
			JSONObject json = getAvailableMoneyJson();
			this.updateAvailableMoneyJson(json.getInt("5") - pinValueChoices[pinValueChoice][3], 
										  json.getInt("10") - pinValueChoices[pinValueChoice][2], 
										  json.getInt("20") - pinValueChoices[pinValueChoice][1], 
										  json.getInt("50") - pinValueChoices[pinValueChoice][0]);
			// Switch to done panal, in arduino serial message (D)
		}
		else {
			//TODO: show in GUI

			System.out.print("Error: Er ging iets mis!");
		}
	}
}
