import java.util.Scanner;

public class Main {
	
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		DBMS mainDBSM = new StdDBMS();
		SQLParser parser = new SQLParser(mainDBSM);
		System.out.println("Omario Database Management System 1.0");
		System.out.println("Loading the interactive shell...");
		while(true) {
			System.out.print("% ");
			String cmd = scan.nextLine();
			if (cmd.equalsIgnoreCase("exit") || cmd.equalsIgnoreCase("quit")) {
				scan.close();
				return;
			}
			parser.parseSQL(cmd);
		}
	}
	
}
