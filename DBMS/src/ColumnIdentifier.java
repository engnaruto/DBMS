public class ColumnIdentifier {

	private String columnName;
	private Class<?> columnType;

	public ColumnIdentifier() {

	}

	public ColumnIdentifier(String name, Class<?> type) {
		columnName = name;
		columnType = type;
	}
	
	public ColumnIdentifier(String strColID) {
		String strs[] = strColID.split(":");
		try {
			columnName = strs[0];
			columnType = this.getClass().getClassLoader().loadClass(strs[1]);
		} catch (ClassNotFoundException e) {
			columnType = String.class;
		}
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
