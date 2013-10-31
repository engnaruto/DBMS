//import java.util.Scanner;

public class Main {
	
	public static void main(String[] args) {
		//Scanner scan = new Scanner(System.in);
		DBMS mainDBSM = new StdDBMS();
		//SQLParser parser = new SQLParser(mainDBSM);
		System.out.println("Omario Database Management System 1.0");
		System.out.println("Loading the interactive shell...");
		try {
			mainDBSM.createDB("tarek");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("tarek database already exists!");
		}
		
		try {
			mainDBSM.setUsedDB("tarek");
		} catch (Exception e) {
			System.out.println("DB not found!");
		}
		
		Database tarekDB = mainDBSM.getUsedDB();
		try {
			tarekDB.addTable("users", null);
		} catch (Exception e) {
			System.out.println("table users exist!");
		}
		
		/*
		while(true) {
			System.out.print("% ");
			String cmd = scan.nextLine();
			if (cmd.equalsIgnoreCase("exit") || cmd.equalsIgnoreCase("quit")) {
				scan.close();
				return;
			}
			parser.parseSQL(cmd);
		}*/
	}
	
}
