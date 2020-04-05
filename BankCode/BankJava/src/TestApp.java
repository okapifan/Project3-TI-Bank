
public class TestApp {
	public static void main(String[] args) {
		TestDatabaseHandler();
	}

	private static void TestDatabaseHandler() {
		DatabaseHandler databaseHandler = new DatabaseHandler("US", "Timobank");

		String pinCode = databaseHandler.Test("US", "Timobank", "1234", "1");
		System.out.println(pinCode);
	}
}