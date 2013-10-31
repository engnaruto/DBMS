import java.io.File;
import java.util.Hashtable;

public class StdDatabase implements Database{

//	private Hashtable<String, StdTable> tables;
	File dbFile;
	
	public StdDatabase(File dbFile) {
		this.dbFile = dbFile;
	}

	public String getName() {
		return dbFile.getName();
	}

	public void setName(String name) {
		this.name = name;
	}

	/*don't acctually need them now*/	
	public Hashtable<String, StdTable> getAllTables() {
		return null;
	}

	public void setAllTables(Hashtable<String, StdTable> tables) {
		//this.tables = tables;
	}
	
	public void addTable(String tableName) {
		tables.put(tableName, new StdTable(tableName));
	}

	public void removeTable(String tableName) {
		tables.remove(tableName);
	}

	//important for the modification in the table
	public StdTable getTable(String tableName) {
		return tables.get(tableName);
	}

}