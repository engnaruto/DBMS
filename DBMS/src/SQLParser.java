import java.util.StringTokenizer;

public class SQLParser {

	/**
	 * 1- can contain letters(capital and small) or numbers ONLY
	 * 
	 * 2- must start with a letter
	 */
	private static String nameReq = "[A-Za-z][A-Za-z0-9]*";

	private DBMS dbms;

	public SQLParser(DBMS dbms) {
		this.dbms = dbms;
	}

	/**
	 * The main method to parse the input string
	 * 
	 * @param toParse
	 */
	public void parseSQL(String toParse) {

		StringTokenizer tokens = new StringTokenizer(toParse, " ");

		if (tokens.hasMoreTokens()) {

			String operation = tokens.nextToken();

			if (operation.equalsIgnoreCase("create")) {
				// could be create statement
				handleCreateStatement(tokens);
			} else if (operation.equalsIgnoreCase("insert")) {
				// could be insert statement
				handleInsertStatement(tokens);
			} else if (operation.equalsIgnoreCase("delete")) {
				// could be delete statement
				handleDeleteStatement(tokens);
			} else if (operation.equalsIgnoreCase("select")) {
				// could be select statement
				handleSelectStatement(tokens);
			} else if (operation.equalsIgnoreCase("update")) {
				// could be update statement
				handleUpdateStatement(tokens);
			} else if (operation.equalsIgnoreCase("use")) {
				// could be use statement
				handleUseStatement(tokens);
			} else {
				System.out.println("ERROR: \"" + operation
						+ "\" IS NOT RECOGNIZED AS SQL OPERATION");
			}
		} else {
			System.out.println("ERROR: Wrong SQL Statemnet");
		}
	}

	/**
	 * when called, checks for validity of the Use statement and calls the
	 * method for the specified database if exists
	 * 
	 * @param tokens
	 */
	private void handleUseStatement(StringTokenizer tokens) {
		if (tokens.hasMoreTokens()) {
			String dbName = tokens.nextToken();
			try {
				dbms.setUsedDB(dbName);
			} catch (Exception e) {
				System.out.println("ERROR: WRONG DATABASE NAME");
			}
		} else {
			System.out.println("ERROR: WRONG INPUT");
		}
	}

	/**
	 * when called, checks for validity of the handle statement and calls the
	 * method for the specified database if exists
	 * 
	 * @param tokens
	 */

	private void handleUpdateStatement(StringTokenizer tokens) {

		if (tokens.hasMoreTokens()) {

			String tableName = tokens.nextToken();

			if (verifyName(tableName)) {

				if (tokens.hasMoreTokens()) {

					String isSetToken = tokens.nextToken();

					if (isSetToken.equalsIgnoreCase("set")) {
						invokeUpdateTable(tableName, tokens);
					} else {
						System.out.println("ERROR: Wrong SQL Statement");
					}
				}
			}
		}
	}

	private void invokeUpdateTable(String tableName, StringTokenizer tokens) {

		Database db = dbms.getUsedDB();
		if (db != null) {
			Table tb = db.getTable(tableName);

			if (tb != null) {
				createRecordToUpdate(tokens, tb);
			} else {
				System.out.println("ERROR: \"" + tableName
						+ "\" IS NOT DEFINED AS TABLE IN THIS DATABASE");
			}
		} else {
			System.out.println("ERROR: YOU MUST CHOOSE SOME DATABASE");
		}

	}

	private void createRecordToUpdate(StringTokenizer tokens, Table tb) {

		if (tokens.hasMoreTokens()) {

			String toReturn = "";

			while (true) {
				String piece = "";
				try {
					piece = tokens.nextToken();
				} catch (Exception e) {
					System.out.println("ERROR: WRONG INPUT");
					return;
				}

				if (piece.contains("where"))
					break;
				else
					toReturn += piece + " ";
			}

			StringTokenizer tt = new StringTokenizer(toReturn, ",");

			String[] columnsNames = new String[tt.countTokens()];
			String[] values = new String[tt.countTokens()];
			int i = 0;
			while (tt.hasMoreTokens()) {
				StringTokenizer ff = new StringTokenizer(tt.nextToken(), "=");
				if (ff.countTokens() == 2) {
					String name = ff.nextToken();
					String value = ff.nextToken();
					if (verifyName(name)) {
						columnsNames[i] = name.trim();
						values[i] = value.trim();
						i++;
					} else {
						System.out.println("ERROR: WRONG INPUT");
						return;
					}

				} else {
					System.out.println("ERROR: WRONG INPUT, (NAME TYPE)");
					return;
				}
			}

			Condition condition = extractCondition(tokens, tb);
			try {
				tb.update(columnsNames, values, condition);
			} catch (Exception e) {
				System.out.println("ERROR: UPDATING FAILED ba2a");
			}

		} else {
			return;
		}
	}

	private void handleSelectStatement(StringTokenizer tokens) {

		if (tokens.hasMoreTokens()) {
			String columnsNames = getBetweenBraces(tokens);
			if (tokens.hasMoreTokens()) {
				String isFrom = tokens.nextToken();
				if (isFrom.equalsIgnoreCase("from")) {
					if (tokens.hasMoreTokens()) {
						String tableName = tokens.nextToken();
						if (verifyName(tableName)) {

							Database db = dbms.getUsedDB();

							if (db != null) {
								Table tb = db.getTable(tableName);

								Condition cond = extractCondition(tokens, tb);

								if (tb != null) {
									String[] arr = columnsNames.split(",");
									String[] toBeTrimmed = new String[arr.length];
									for (int i = 0; i < toBeTrimmed.length; i++) {
										toBeTrimmed[i] = arr[i].trim();
									}
									RecordSet selected = tb.select(toBeTrimmed,
											cond);
									for (Record r : selected) {
										System.out.println(r.toString());
									}
								}
							}
						}
					}
				}
			}
		}
	}

	private void handleDeleteStatement(StringTokenizer tokens) {

		if (tokens.hasMoreTokens()) {

			String isFrom = tokens.nextToken();

			if (isFrom.equalsIgnoreCase("from")) {
				if (tokens.hasMoreTokens()) {

					Database db = dbms.getUsedDB();

					if (db != null) {
						String tableName = tokens.nextToken();
						Table tb = db.getTable(tableName);

						Condition cond = extractCondition(tokens, tb);

						if (tb != null) {
							try {
								tb.delete(cond);
							} catch (Exception e) {
								System.out.println("ERROR: DELETION FAILED");
							}
						}
					}
				}
			} else {
				System.out
						.println("ERROR: WRONG SQL STATEMENT, MISSING \"FROM\" ");
			}
		}
	}

	private void handleInsertStatement(StringTokenizer tokens) {

		if (tokens.hasMoreTokens()) {

			String isInto = tokens.nextToken();
			if (isInto.equalsIgnoreCase("into")) {

				Database db = dbms.getUsedDB();

				if (db != null) {
					String tableName = tokens.nextToken();

					if (verifyName(tableName)) {
						Table tb = db.getTable(tableName);

						if (tb != null) {
							invokeInsertInto(tokens, tb);
						} else {
							System.out.println("ERROR: CANNOT FIND TABLE");
						}
					}
				} else {
					System.out.println("ERROR: CANNOT FIND DATABASE");
				}
			} else {
				System.out.println("ERROR: Wrong SQL Statemnet");
				return;
			}
		}
	}

	private void invokeInsertInto(StringTokenizer tokens, Table tb) {
		String columnsNames = getBetweenBraces(tokens);
		if (tokens.hasMoreTokens()) {
			String isValues = tokens.nextToken();
			if (isValues.equalsIgnoreCase("values")) {
				String columnsValues = getBetweenBraces(tokens);
				if (columnsNames != null && columnsValues != null) {
					String[] colNames = columnsNames.split(",");
					String[] colVals = columnsValues.split(",");

					String[] trimmedCN = new String[colNames.length];
					String[] trimmedCV = new String[colNames.length];

					for (int i = 0; i < colNames.length; i++) {
						trimmedCN[i] = colNames[i].trim();
						trimmedCV[i] = colVals[i].trim();
					}

					Record newValues = null;
					try {
						newValues = new Record(trimmedCN, trimmedCV, tb);

						try {
							tb.insert(newValues);
						} catch (Exception e) {
							System.out.println("ERROR: INSERTION FAILED");
						}

					} catch (Exception e1) {
						System.out.println("ERROR : WRONG INPUT");
					}

				}
			}
		}
	}

	private void handleCreateStatement(StringTokenizer tokens) {

		if (tokens.hasMoreTokens()) {

			String toBeCreated = tokens.nextToken();

			if (toBeCreated.equalsIgnoreCase("table")) {
				invokeCreateTable(tokens);
			} else if (toBeCreated.equalsIgnoreCase("database")) {
				invokeCreateDatabase(tokens);
			} else {
				System.out.println("ERROR: Wrong SQL Statemnet \""
						+ toBeCreated + "\" CANNOT BE CREATED");
			}
		} else {
			System.out
					.println("ERROR: Wrong SQL Statemnet, CANNOT CREATE VOID");
		}
	}

	private void invokeCreateTable(StringTokenizer tokens) {

		if (tokens.hasMoreTokens()) {
			String tableName = tokens.nextToken();
			if (verifyName(tableName)) {
				Database db = dbms.getUsedDB();
				ColumnIdentifier[] columnIdentifiers = extractColumnIdentifiers(tokens);
				if (db != null && columnIdentifiers != null) {
					try {
						db.addTable(tableName, columnIdentifiers);
					} catch (Exception e) {
						System.out.println("ERROR: CREATING TABLE FAILED");
					}
				} else {
					System.out.println("ERROR: CREATING TABLE FAILED");
				}
			}
		} else {
			System.out.println("ERROR: CANNOT CREATE TABLE NAME VOID");
		}
	}

	private void invokeCreateDatabase(StringTokenizer tokens) {

		if (tokens.hasMoreTokens()) {

			String dbName = tokens.nextToken();

			if (verifyName(dbName)) {
				try {
					dbms.createDB(dbName);
				} catch (Exception e) {
					System.out.println("ERROR: CANNOT CREATE DATABASE");
					return;
				}
			}
		} else {
			System.out
					.println("ERROR: Wrong SQL Statemnet, CANNOT CREATE DATABASE NAME VOID");
		}
	}

	private Condition extractCondition(StringTokenizer tokens, Table tb) {

		if (tokens.hasMoreTokens()) {
			String isWhere = tokens.nextToken();

			if (isWhere.equalsIgnoreCase("where")) {
				String all = depleteTokens(tokens);
				if (validateCondition(all)) {
					// TODO :try to separate if not separated
					return new Condition(all, tb);
				} else {
					System.out.println("ERROR: INVALID CONDITION FORMAT");
				}

			} else {

				String all = isWhere + " " + depleteTokens(tokens);
				if (validateCondition(all)) {
					// TODO :try to separate if not separated
					Condition cond = new Condition(all, tb);
					return cond;
				} else {
					System.out.println("ERROR: INVALID CONDITION FORMAT");
				}
				System.out
						.println("ERROR: WRONG SQL STATEMENT, MISSING \"WHERE\"");
			}
		} else {
			System.out.println("ERROR: WRONG SQL STATEMENT");
		}

		return null;
	}

	private String depleteTokens(StringTokenizer tokens) {

		String all = "";
		int i = 0;
		while (tokens.hasMoreTokens()) {
			all += tokens.nextToken() + " ";
			i++;
		}

		if (i == 0)
			return null;
		else
			return all;
	}

	private boolean validateCondition(String all) {

		if (all == null)
			return false;

		String operatorPat = "(\\*|\\<\\=|\\>\\=|\\=|\\<|\\>)";

		String intPat = "(-){0,1}[0-9]+";
		String RealPat = "(-){0,1}[0-9]+(\\.){0,1}[0-9]+";
		String stringPat = ".*";
		String datePat = "[1-2]([0-9]){3}-((0[1-9])|1[0-2])-([0-2][1-9]|3[0-1])T([0-2][0-3])\\:([0-5][0-9])\\:([0-5][0-9])";
		String booleanPat = "(((?i)true)|((?i)false))";

		String parPatCV = intPat + "|" + RealPat + "|" + booleanPat + "|"
				+ stringPat + "|" + datePat;

		String conditionPat = nameReq + "\\s*" + operatorPat + "\\s*" + "("
				+ nameReq + "|" + parPatCV + ")";

		return all.matches(conditionPat);
	}

	private String getBetweenBraces(StringTokenizer tokens) {

		if (tokens.hasMoreTokens()) {

			String toReturn = "";

			while (true) {
				String piece = "";
				try {
					piece = tokens.nextToken();
				} catch (Exception e) {
					System.out.println("ERROR: WRONG INPUT");
					return null;
				}

				toReturn += piece;

				if (piece.endsWith(")"))
					break;
				else
					toReturn += " ";
			}
			if (toReturn.startsWith("(") && toReturn.endsWith(")")) {
				return toReturn.substring(1, toReturn.length() - 1);
			} else {
				return null;
			}
		}
		return null;
	}

	private ColumnIdentifier[] extractColumnIdentifiers(StringTokenizer tokens) {

		String toSplit = getBetweenBraces(tokens);

		if (toSplit == null) {
			System.out.println("ERROR: CANNOT EXTRACT COLUMNS IDENTIFIERS");
			return null;
		} else {

			StringTokenizer tt = new StringTokenizer(toSplit, ",");

			ColumnIdentifier[] ids = new ColumnIdentifier[tt.countTokens()];
			int i = 0;
			while (tt.hasMoreTokens()) {
				StringTokenizer ff = new StringTokenizer(tt.nextToken(), " ");
				if (ff.countTokens() == 2) {
					String name = ff.nextToken();
					String type = ff.nextToken();
					if (verifyName(name)) {
						String[] del = { name.trim(), type.trim() };
						ids[i++] = new ColumnIdentifier(del);
					} else {
						System.out.println("ERROR: WRONG INPUT");
						return null;
					}
				} else {
					System.out.println("ERROR: WRONG INPUT, (NAME TYPE)");
					return null;
				}
			}
			return ids;
		}
	}

	public DBMS getDbms() {
		return dbms;
	}

	public void setDbms(DBMS dbms) {
		this.dbms = dbms;
	}

	private boolean verifyName(String objectName) {
		return objectName.trim().matches(nameReq);
	}

}