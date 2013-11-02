import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;

public class Record {

	Hashtable<String, Object> cells;
	Table table;

	private String getType(String columnName) {
		for(ColumnIdentifier colID : table.getColIDs()) {
			if (colID.getColumnName().equals(columnName)) {
				return colID.getColumnType().getSimpleName();
			}
		}
		return "String";
	}
	
	public Record(Table table) {
		cells = new Hashtable<String, Object>();
		this.table = table;
	}
	
	public Record(String[] columnsNames, Object[] values, Table table) {
		cells = new Hashtable<String, Object>();
		for (int i = 0; i < columnsNames.length; i++)
			cells.put(columnsNames[i], values[i]);
		this.table = table;
	}
	
	public Record(String[] columnsNames, String[] values, Table table) throws Exception {
		cells = new Hashtable<String, Object>();
		
		this.table = table;
		for(int i = 0; i < columnsNames.length; i++) {
			Object o;
			switch(getType(columnsNames[i])) {
				case "Integer":
					o = new Integer(values[i]);
					break;
				case "Double":
					o = new Double(values[i]);
					break;
				case "Float":
					o = new Float(values[i]);
					break;
				case "Long":
					o = new Long(values[i]);
					break;
				case "Date":
					final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
				    SimpleDateFormat dfIn = new SimpleDateFormat(DATE_FORMAT);
					try {
						o = dfIn.parse(values[i]);
					} catch (ParseException e) {
						o = new Date(0);
					}
					break;
				case "Boolean":
					o = new Boolean(values[i]);
					break;
				default:
					o = new String(values[i]);
			}
			cells.put(columnsNames[i], o);
		}
	}
	
	public Object getValue(String columnName) {
		Object ret = cells.get(columnName);
		if (ret == null || ret.equals("")) {
			switch (getType(columnName)) {
				case "Integer":
					ret = new Integer(0);
					break;
				case "Double":
					ret = new Double(0);
					break;
				case "Float":
					ret = new Float(0);
					break;
				case "Long":
					ret = new Long(0);
					break;
				case "Date":
					ret = new Date(0);
					break;
				case "Boolean":
					ret = new Boolean(false);
					break;
				default:
					ret = new String("-");
			}
		}
		return ret;
	}

	public void setCell(String columnName, Object value) {
		cells.put(columnName, value);
	}

	@Override
	public String toString() {
		String s = "{";
		
		/*for (int i = 0; i < table.getColIDs().length; i++) {
			String columnName = table.getColIDs()[i].getColumnName();
			Object value = getValue(columnName);
			s += columnName + " = " + value.toString();
			if (i < table.getColIDs().length - 1)
				s += ", ";
		}*/
		
		Enumeration<String> enumKey = cells.keys();
		while (enumKey.hasMoreElements()) {
			String columnName = enumKey.nextElement();
			Object value = getValue(columnName);
			s += columnName + " = " + value.toString();
			if (enumKey.hasMoreElements())
		          s += ", ";
		}
		s += "}";
		return s;

	}

}