import java.io.File;

public class StdTable implements Table {

	private String tableName;
	File tableFile;
	ColumnIdentifier[] columnsId;

	public StdTable(String tableName, File tableFile, ColumnIdentifier[] columnsId) {
		this.tableName = tableName;
		this.tableFile = tableFile;
		this.columnsId = columnsId;
	}
	
	public String getTableName() {
		return tableName;
	}

	public ColumnIdentifier[] getColIDs() {
		return columnsId;
	}

	public RecordSet select(String[] columnsNames, Condition condition) {
		return null;
	}

	public void insert(Record newValues) {

	}

	public void delete(Condition condition) {

	}

	public void update(String[] columnsNames, Object[] values, Condition condition) {

	}

}
