import java.io.File;
import java.util.List;

public class StdTable implements Table{

	private String tableName;
	private List<String> columnsNames;
	private List<Class<?>> columnsTypes;
	private XMLHandler handler;
	File tableFile;

	public StdTable(String tableName) {
		this.tableName = tableName;
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

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public List<String> getColumnsNames() {
		return columnsNames;
	}

	public void setColumnsNames(List<String> columnsNames) {
		this.columnsNames = columnsNames;
	}

	public XMLHandler getHandler() {
		return handler;
	}

	public void setHandler(XMLHandler handler) {
		this.handler = handler;
	}

	public List<Class<?>> getColumnsTypes() {
		return columnsTypes;
	}

	public void setColumnsTypes(List<Class<?>> columnsTypes) {
		this.columnsTypes = columnsTypes;
	}

}
