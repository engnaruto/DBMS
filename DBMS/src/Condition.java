import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Condition {

	String op1AttrName, op2AttrName;
	String operator;
	String condition;

	public String toString() {
		return condition;
				//op1AttrName + " " + operator + " " + op2AttrName;
	}

	public Condition(String cond, Table table) {
		String op1 = cond.substring(0, cond.indexOf(' '));
		String op2;

		if (cond.contains("\""))
			op2 = cond
					.substring(cond.indexOf('\"') + 1, cond.lastIndexOf('\"'));
		else
			op2 = cond.substring(cond.lastIndexOf(' ') + 1, cond.length());

		List<String> attrNames = new ArrayList<String>();
		ColumnIdentifier[] colIDs = table.getColIDs();
		for (int i = 0; i < colIDs.length; i++)
			attrNames.add(colIDs[i].getColumnName());

		condition = cond;
		op1AttrName = null;
		op2AttrName = null;

		for (String s : attrNames) {
			if (op1.compareTo(s) == 0)
				op1AttrName = new String(s);
			if (op2.compareTo(s) == 0)
				op2AttrName = new String(s);
		}
		operator = cond.substring(
				cond.indexOf(' ') + 1,
				(cond.contains("\"")) ? cond.indexOf('\"') : cond
						.lastIndexOf(' '));
	}

	public boolean meetsCondition(Record rec) {
		Object LHS = getLHSValue(rec);
		Object RHS = getRHSValue(rec);

		if (LHS == null || RHS == null || !(LHS instanceof Comparable)
				|| !(RHS instanceof Comparable)
				|| !(LHS.getClass().equals(RHS.getClass())))
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
		if (op1AttrName != null)
			return rec.getValue(op1AttrName);
		else
			return null;
	}

	private Object getRHSValue(Record rec) {
		if (op2AttrName != null)
			return rec.getValue(op2AttrName);
		else
			return RHSConstValue(getLHSValue(rec));
	}

	private Object RHSConstValue(Object LHS) { // parse to same type as LHS
		String op2;
		if (condition.contains("\""))
			op2 = condition.substring(condition.indexOf('\"') + 1,
					condition.lastIndexOf('\"'));
		else
			op2 = condition.substring(condition.lastIndexOf(' ') + 1,
					condition.length());

		if (LHS == null)
			return null;

		try {
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
		} catch (Exception e) {
			return null;
		}
		return null;
	}
}