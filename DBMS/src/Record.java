public class Record {
	
	private String[] columnsNames;
	private Object[] values;
	
	public Record(String[] columnsNames, Object[] values){
		this.columnsNames = columnsNames;
		this.values = values;
	}

	public Object getCell(int index) {
		return index;
	}
	
	public void setCell(int index, Object value) {
	}

	public Object[] getContainer() {
		return columnsNames;
	}

	public void setContainer(Object[] container) {
	}
}