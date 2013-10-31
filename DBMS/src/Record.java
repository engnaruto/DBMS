public class Record {
	
	private String[] columnsNames;
	private Object[] values;
	
	public Record(String[] columnsNames, Object[] values){
		this.columnsNames = columnsNames;
		this.values = values;
	}

	public Object getCell(int index) {
		return values[index];
	}
	
	public void setCell(int index, Object value) {
		values[index] = value;
	}

	public String[] getColNames() {
		return columnsNames;
	}

}