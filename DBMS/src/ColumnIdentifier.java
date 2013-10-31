public class ColumnIdentifier {

	private String columnName;
	private Class<?> columnType;

	public ColumnIdentifier() {

	}

	public ColumnIdentifier(String name, Class<?> type) {
		columnName = name;
		columnType = type;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public Class<?> getColumnType() {
		return columnType;
	}

	public void setColumnType(Class<?> columnType) {
		this.columnType = columnType;
	}

}
