import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Scanner;

public class StdDatabase implements Database {

	// disk files
	private File dbFile;
	private File schemaFile;

	// tables:
	Hashtable<String, Table> tables;

	private void readSchema() {
		try {
			Scanner scan = new Scanner(schemaFile);
			while (scan.hasNext()) {
				// expected to read [table name]:
				String tableName = scan.nextLine();
				tableName = tableName.substring(1, tableName.length() - 1);

				// now read count of colIDs:
				int count = Integer.parseInt(scan.nextLine());
				ColumnIdentifier colIDs[] = new ColumnIdentifier[count];

				// read colIDs:
				for (int i = 0; i < count; i++) {
					String strColID = scan.nextLine();
					colIDs[i] = new ColumnIdentifier(strColID);
				}

				// read --DONE--
				scan.nextLine();

				// create File structure of XML:
				File xmlFile = new File(dbFile.getAbsolutePath()
						+ File.separatorChar + tableName + ".xml");

				// create table structure:
				Table table = new StdTable(tableName, xmlFile, colIDs);

				// add table to the hash table.
				tables.put(tableName, table);
			}
			scan.close();
		} catch (FileNotFoundException e) {
			// file doesn't exist.. no problem at all.
		}
	}

	private void writeSchema() {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(schemaFile));

			// Write the schema:
			Enumeration<String> enumKey = tables.keys();
			while (enumKey.hasMoreElements()) {
				String tableName = enumKey.nextElement();
				out.write("[" + tableName + "]\n");
				Table table = tables.get(tableName);
				out.write(table.getColIDs().length + "\n");
				for (ColumnIdentifier colId : table.getColIDs()) {
					out.write(colId.getColumnName() + ":"
							+ colId.getColumnType().getName() + "\n");
				}
				out.write("--DONE--\n");
			}

			// Close the output stream
			out.close();
		} catch (Exception e) {

		}
	}

	public StdDatabase(File dbFile) {
		this.dbFile = dbFile;
		tables = new Hashtable<String, Table>();
		schemaFile = new File(dbFile.getAbsolutePath() + File.separatorChar
				+ "schema.db");
		readSchema();
	}

	public String getName() {
		return dbFile.getName();
	}

	public Table getTable(String tableName) {
		return tables.get(tableName);
	}

	public Hashtable<String, Table> getAllTables() {
		return tables;
	}

	public void addTable(String tableName, ColumnIdentifier[] columnsId)
			throws Exception {
		// first, make sure table doesn't exist:
		if (getTable(tableName) != null) {
			throw new Exception("Table exists!");
		}

		// columnsId shouldn't be null:
		if (columnsId == null) {
			throw new Exception("Columns Identifiers are invalid");
		}

		// Make xmlFile for the table:
		File xmlFile = new File(dbFile.getAbsolutePath() + File.separatorChar
				+ tableName + ".xml");
		
		// Make Table object in memory:
		Table tbl = new StdTable(tableName, xmlFile, columnsId);
		
		// write empty XML file:
		XMLHandler hndl = new XMLHandler(xmlFile, columnsId, tbl, true);
		hndl.close();

		// Add to the hash table:
		tables.put(tableName, tbl);

		// update schema file:
		writeSchema();
	}

	public void removeTable(String tableName) throws Exception {
		// first, make sure table exists:
		if (getTable(tableName) == null) {
			throw new Exception("Table doesn't exist!");
		} else {
			File xmlFile = new File(dbFile.getAbsolutePath()
					+ File.separatorChar + tableName + ".xml");
			xmlFile.deleteOnExit();
			tables.remove(tableName);
			writeSchema();
		}

	}

}