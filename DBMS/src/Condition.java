import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Condition {

	int op1AttrNum, op2AttrNum;

	String operator;
	String condition;

	public Condition(String cond, Table table) {
		String op1 = cond.substring(0, cond.indexOf(' '));
		String op2;
		
		if(cond.contains("\""))
			op2 = cond.substring(cond.indexOf('\"')+1, cond.lastIndexOf('\"'));
		else
			op2 = cond.substring(cond.lastIndexOf(' ')+1, cond.length());
		
		List<String> attrNames = new ArrayList<String>();
		ColumnIdentifier[] colIDs = table.getColIDs();
		for (int i = 0; i < colIDs.length; i++)
			attrNames.add(colIDs[i].getColumnName());
		
		condition = cond;
		op1AttrNum = -1;
		op2AttrNum = -1;

		int i = 0;
		for (String s : attrNames) {
			if (op1.compareTo(s) == 0)
				op1AttrNum = i;
			if (op2.compareTo(s) == 0)
				op2AttrNum = i;
			i++;
		}
		operator = cond.substring(cond.indexOf(' ')+1, (cond.contains("\""))?
				cond.indexOf('\"') : cond.lastIndexOf(' '));
	}

	public Condition() {
		new Condition("202122 = 202122", new StdTable("TempTable", null, null));
	}

	public boolean meetsCondition(Record rec) {
		Object LHS = getLHSValue(rec);
		Object RHS = getRHSValue(rec);

		if (LHS == null || RHS == null || !(LHS instanceof Comparable)
				|| !(RHS instanceof Comparable) || !(LHS.getClass().equals(RHS.getClass())))
			return true;

		@SuppressWarnings({ "unchecked", "rawtypes" })
		int comparisonResult = ((Comparable) LHS).compareTo((Comparable) RHS);

		if (operator.contains("<="))
			return (comparisonResult == -1 || comparisonResult == 0);
		else if (operator.contains(">="))
			return (comparisonResult == 1 || comparisonResult == 0);
		else if (operator.contains("<"))
			return (comparisonResult == -1);
		else if (operator.contains(">"))
			return (comparisonResult == 1);
		else if (operator.contains("="))
			return (comparisonResult == 0);
		return true;
	}

	private Object getLHSValue(Record rec) {
		if (op1AttrNum != -1)
			return rec.getCell(op1AttrNum);
		else
			return null;

	}

	private Object getRHSValue(Record rec) {
		if (op2AttrNum != -1)
			return rec.getCell(op2AttrNum);
		else
			return RHSConstValue(getLHSValue(rec));
	}

	
	private Object RHSConstValue(Object LHS) { // parse to same type as LHS
		String op2;
		if(condition.contains("\""))
			op2 = condition.substring(condition.indexOf('\"')+1, condition.lastIndexOf('\"'));
		else
			op2 = condition.substring(condition.lastIndexOf(' ')+1, condition.length());

		if (LHS == null)
			return null;

		try{
			if (LHS instanceof Integer)
				return Integer.valueOf(op2);
			if (LHS instanceof Double)
				return Double.valueOf(op2);
			if (LHS instanceof Float)
				return Float.valueOf(op2);
			if (LHS instanceof Long)
				return Long.valueOf(op2);
			if (LHS instanceof String)
				return String.valueOf(op2);
			if (LHS instanceof Date)
				return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(op2);
			if (LHS instanceof Boolean)
				return Boolean.parseBoolean(op2);
		}
		catch(Exception e){
			return null;
		}
		return null;
	}
}