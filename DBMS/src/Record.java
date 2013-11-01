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
	@Override
	public String toString(){
		String s = "{";
		for(int i  =0 ; i < values.length ; i++){
			s+= columnsNames[i]+ " = " + values[i];
			if(i !=values.length-1)s+=", ";
		}
		s+="}";
		return s;
		
	}

}