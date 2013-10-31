import java.io.File;
import java.util.Hashtable;

public class StdDatabase implements Database{

	File dbFile;
	
	public StdDatabase(File dbFile) {
		this.dbFile = dbFile;
	}

	public String getName() {
		return dbFile.getName();
	}

	// we don't actually need this now	
	public Hashtable<String, StdTable> getAllTables() {
		return null;
	}

	// we don't actually need this now	
	public void setAllTables(Hashtable<String, StdTable> tables) {
		//this.tables = tables;
	}
	
	public void addTable(String tableName, ColumnIdentifier[] columnsId) throws Exception {
		File xmlFile = new File(dbFile.getAbsolutePath()
	                            + File.separatorChar + tableName + ".xml");
		System.out.println(xmlFile.getAbsolutePath());
		if (xmlFile.exists()) {
			throw new Exception("Table exists!");
		}
		XMLHandler hndl = new XMLHandler(xmlFile, columnsId, true);
		hndl.close();
	}

	public void removeTable(String tableName) {
//		tables.remove(tableName);
	}

	//important for the modification in the table
	public Table getTable(String tableName) {
//		return tables.get(tableName);
		return null;
	}

}