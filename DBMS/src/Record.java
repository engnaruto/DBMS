public class Record {
	
	private Object[] container;
	
	public Record(int size){
		container = new Object[size];
	}

	public Object getCell(int index) {
		return container[index];
	}
	
	public void setCell(int index, Object value) {
		container[index] = value;
	}

	public Object[] getContainer() {
		return container;
	}

	public void setContainer(Object[] container) {
		this.container = container;
	}
	
	
}