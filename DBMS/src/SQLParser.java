import java.util.StringTokenizer;

public class SQLParser {

	private DBMS dbms;

	public SQLParser(DBMS dbms) {
		this.dbms = dbms;
	}

	public void parseSQL(String toParse) {

		
		StringTokenizer tokens = new StringTokenizer(toParse, " ");

		if (tokens.hasMoreTokens()) {

			String operation = tokens.nextToken();

			if (operation.equalsIgnoreCase("create")) {
				handleCreateStatement(tokens);
			} else if (operation.equalsIgnoreCase("insert")) {
				if (tokens.nextToken().equalsIgnoreCase("into")) {
					handleInsertStatement(tokens);
				} else {
					System.out.println("ERROR: Wrong SQL Statemnet");
				}
			} else if (operation.equalsIgnoreCase("delete")) {
				handleDeleteStatement(tokens);
			} else if (operation.equalsIgnoreCase("select")) {
				handleSelectStatement(tokens);
			} else if (operation.equalsIgnoreCase("update")) {
				handleUpdateStatement(tokens);
			} else if (operation.equalsIgnoreCase("use")) {
				handleUseStatement(tokens);
			} else {
				System.out.println("ERROR: \"" + operation
						+ "\" is not recognized as an operation");
			}
		} else {
			System.out.println("ERROR: Wrong SQL Statemnet");
		}
	}

	private void handleUseStatement(StringTokenizer tokens) {
		if (tokens.hasMoreTokens()) {
			String dbName = tokens.nextToken();
			try {
				dbms.setUsedDB(dbName);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void handleUpdateStatement(StringTokenizer tokens) {

		if (tokens.hasMoreTokens()) {

			String tableName = tokens.nextToken();

			if (verifyName(tableName)) {
				String isSetToken = tokens.nextToken();
				if (isSetToken.equalsIgnoreCase(isSetToken)) {
					// dbms.updateTable(dbms.getUsedDB().getName(), tableName,
					// null, extractCondition(), extarctValues());
				} else {
					System.out.println("ERROR: Wrong SQL Statement");
				}
			}
		}

	}

	private Record extarctValues() {
		return null;
	}

	private Condition extractCondition() {
		return null;
	}

	private void handleSelectStatement(StringTokenizer tokens) {

	}

	private void handleDeleteStatement(StringTokenizer tokens) {

	}

	private void handleInsertStatement(StringTokenizer tokens) {

	}

	private void handleCreateStatement(StringTokenizer tokens) {

		if (tokens.hasMoreTokens()) {

			String toBeCreated = tokens.nextToken();

			if (toBeCreated.equalsIgnoreCase("table")) {
				invokeCreateTable(tokens);

			} else if (toBeCreated.equalsIgnoreCase("database")) {
				invokeCreateDatabase(tokens);
			}
		} else {
			System.out.println("ERROR: Wrong SQL Statemnet");
		}
	}

	private void invokeCreateTable(StringTokenizer tokens) {

		if (tokens.hasMoreTokens()) {
			String tableName = tokens.nextToken();

			if (verifyName(tableName)) {
				try {
					ColumnIdentifier[] columnIdentifiers = extractIdentifiers(tokens);
					//dbms.createTable(dbms.getUsedDB().getName(), tableName, columnIdentifiers);
				} catch (Exception e) {
					System.out.println("Creating Table failed");
				}
			}
		} else {
			System.out.println("ERROR: Wrong SQL Statemnet");
		}

	}

	private ColumnIdentifier[] extractIdentifiers(StringTokenizer tokens) {
		
		return null;
	}

	private void invokeCreateDatabase(StringTokenizer tokens) {

		if (tokens.hasMoreTokens()) {

			String dbName = tokens.nextToken();

			if (verifyName(dbName)) {
				try {
					dbms.createDB(dbName);
				} catch (Exception e) {
					System.out.println("Creating Database Failed");
				}
			} else {
				System.out.println("Illegal Name for a database");
			}
		} else {
			System.out.println("ERROR: Wrong SQL Statemnet");
		}
	}

	private Class<?>[] extractColumnsTypes() {

		return null;
	}

	private String[] extractColumnsNames() {

		return null;
	}

	private boolean verifyName(String objectName) {
		// only letters and numbers, and must start with a letter
		return false;
	}

	public DBMS getDbms() {
		return dbms;
	}

	public void setDbms(DBMS dbms) {
		this.dbms = dbms;
	}

}