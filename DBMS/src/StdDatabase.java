import java.util.Hashtable;

public class StdDatabase implements Database{

	private String name;
	private Hashtable<String, StdTable> tables;
	File dbFile;
	
	public StdDatabase(File dbFile) {
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

/*don't acctually need them now*/
	
	public Hashtable<String, StdTable> getAllTables() {
		return tables;
	}

	public void setAllTables(Hashtable<String, StdTable> tables) {
		this.tables = tables;
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