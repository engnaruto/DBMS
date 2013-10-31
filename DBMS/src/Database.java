import java.util.Hashtable;

public interface Database {

	public String getName();

	public Hashtable<String, StdTable> getAllTables();

	public void setAllTables(Hashtable<String, StdTable> tables);

	public void addTable(String tableName, ColumnIdentifier[] columnsId);

	public void removeTable(String tableName);

	public Table getTable(String tableName);

}
