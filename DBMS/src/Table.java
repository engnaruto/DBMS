import java.util.List;

public interface Table {
	
	public String getTableName();

	public void setTableName(String tableName);

	public RecordSet select(String[] columnsNames, Condition condition);

	public void insert(Record newValues);

	public void delete(Condition condition);

	public void update(String[] columnsNames, Object[] values, Condition condition);

	public List<String> getColumnsNames();

	public void setColumnsNames(List<String> columnsNames);

	public XMLHandler getHandler();

	public void setHandler(XMLHandler handler);

	public List<Class<?>> getColumnsTypes();

	public void setColumnsTypes(List<Class<?>> columnsTypes);

}