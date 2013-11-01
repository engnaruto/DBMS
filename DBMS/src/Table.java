public interface Table {
	
	public String getTableName();
	
	public ColumnIdentifier[] getColIDs();

	public RecordSet select(String[] columnsNames, Condition condition);

	public void insert(Record newValues);

	public void delete(Condition condition);

	public void update(String[] columnsNames, Object[] values, Condition condition);

}