import java.util.Hashtable;


public interface DBMS {
	
	public void createDB(String DBName) throws Exception;
	
	public void removeDB(String DBName);
	
	public StdDatabase getUsedDB();
	
	public void setUsedDB(String dbName) throws Exception;
	
	//public void createTable(String dbName, String tableName, String[] columnsNames, Class<?>[] columnsTypes) throws Exception;
	
	//public RecordSet selectFromTable(String dbName, String tableName, String[] columnsNames, Condition condition);
	
	//public void insertIntoTable(String dbName, String tableName, Record newValues);
	
	//public void deleteFromTable(String dbName, String tableName, Condition condition);
	
	//public void updateTable(String dbName, String tableName, String[] columnsNames, Object[] values, Condition condition);
	
	// added this method
	
	//public Hashtable<String, Database> getDBContainer();
	
	 
}