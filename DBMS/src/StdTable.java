import java.io.File;

public class StdTable implements Table {

	private String tableName;
	File tableFile;
	File tmpFile;
	ColumnIdentifier[] columnsId;

	public StdTable(String tableName, File tableFile,
			ColumnIdentifier[] columnsId) {
		this.tableName = tableName;
		this.tableFile = tableFile;
		this.columnsId = columnsId;
		this.tmpFile = new File(tableFile.getParentFile().getAbsolutePath()
				+ File.separatorChar + tableName + ".tmp");
	}

	public String getTableName() {
		return tableName;
	}

	public ColumnIdentifier[] getColIDs() {
		return columnsId;
	}

	public void insert(Record newValues) throws Exception {
		// rename table file to a "tableName.tmp":
		tableFile.renameTo(tmpFile);

		// open tmpFile for read
		XMLHandler tempFileXMLHandler = new XMLHandler(tmpFile, columnsId,
				false);
		// open tableFile for write
		XMLHandler tableFileXMLHandler = new XMLHandler(tableFile, columnsId,
				true);
		// read all elements of tmpFile and write them into tableFile
		Record readRecord;
		while ((readRecord = tempFileXMLHandler.readNextRecord()) != null) {
			tableFileXMLHandler.writeNextRecord(readRecord);
		}
		// write the new record inside tableFile
		tableFileXMLHandler.writeNextRecord(newValues);
		// close tmpFile
		tempFileXMLHandler.close();
		// close tableFile
		tableFileXMLHandler.close();
		// delete tmpFile
		tmpFile.delete();
	}

	public RecordSet select(String[] columnsNames, Condition condition) {
		return null;
	}

	public void delete(Condition condition) throws Exception {
		// rename table file to a "tableName.tmp":
		tableFile.renameTo(tmpFile);

		// open tmpFile for read
		XMLHandler tempFileXMLHandler = new XMLHandler(tmpFile, columnsId,
				false);
		// open tableFile for write
		XMLHandler tableFileXMLHandler = new XMLHandler(tableFile, columnsId,
				true);
		// read all elements of tmpFile and write them into tableFile while
		// !meet condition
		Record readRecord;
		while ((readRecord = tempFileXMLHandler.readNextRecord()) != null) {
			if (!condition.meetsCondition(readRecord)) {
				tableFileXMLHandler.writeNextRecord(readRecord);
			}
		}
		// close tmpFile
		tempFileXMLHandler.close();
		// close tableFile
		tableFileXMLHandler.close();
		// delete tmpFile
		tmpFile.delete();
	}

	public void update(String[] columnsNames, Object[] values,
			Condition condition) throws Exception {

		// rename table file to a "tableName.tmp":
		tableFile.renameTo(tmpFile);

		// open tmpFile for read
		XMLHandler tempFileXMLHandler = new XMLHandler(tmpFile, columnsId,
				false);
		// open tableFile for write
		XMLHandler tableFileXMLHandler = new XMLHandler(tableFile, columnsId,
				true);
		// read all elements of tmpFile and write them into tableFile and update
		// records if it meet the condition
		Record readRecord;
		while ((readRecord = tempFileXMLHandler.readNextRecord()) != null) {
			if (condition.meetsCondition(readRecord)) {
				for (int i = 0; i < columnsNames.length; i++) {
					for (int j = 0; j < readRecord.countColumns(); j++) {
						if (columnsNames[i].equalsIgnoreCase(readRecord
								.getColumnName(j))) {
							readRecord.setCell(j, values[i]);
						}
					}
				}
			}
			tableFileXMLHandler.writeNextRecord(readRecord);
		}
		// close tmpFile
		tempFileXMLHandler.close();
		// close tableFile
		tableFileXMLHandler.close();
		// delete tmpFile
		tmpFile.delete();

	}

}
