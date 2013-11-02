public interface Table {

	public String getTableName();

	public ColumnIdentifier[] getColIDs();

	public RecordSet select(String[] columnsNames, Condition condition);

	public void insert(Record newValues) throws Exception;

	public void delete(Condition condition) throws Exception;

	public void update(String[] columnsNames, Object[] values,
			Condition condition) throws Exception;
	
	public void update(String[] columnsNames, String[] values,
			Condition condition) throws Exception;

}