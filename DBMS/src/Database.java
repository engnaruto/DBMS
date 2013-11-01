import java.util.Hashtable;

public interface Database {

	public String getName();
	
	public Table getTable(String tableName);
	
	public Hashtable<String, Table> getAllTables();

	public void addTable(String tableName, ColumnIdentifier[] columnsId) throws Exception;

	public void removeTable(String tableName) throws Exception;

}
