import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class StdTable implements Table {

	private String tableName;
	File tableFile;
	File tmpFile;
	File dtdFile;
	ColumnIdentifier[] columnsId;

	public StdTable(String tableName, File tableFile,
			ColumnIdentifier[] columnsId) {
		this.tableName = tableName;
		this.tableFile = tableFile;
		this.columnsId = columnsId;
		this.tmpFile = new File(tableFile.getParentFile().getAbsolutePath()
				+ File.separatorChar + tableName + ".tmp");
		this.dtdFile = new File(tableFile.getParentFile().getAbsolutePath()
				+ File.separatorChar + tableName + ".dtd");
	}

	public String getTableName() {
		return tableName;
	}

	public ColumnIdentifier[] getColIDs() {
		return columnsId;
	}

	public void insert(Record newValues) throws Exception {

		// rename table file to a "tableName.tmp":
		try {
			Files.move(tableFile.toPath(), tmpFile.toPath());
		} catch (IOException e1) {
			System.out.println("can't move to tmp file! " + e1.getMessage());
			throw e1;
		}
		
		System.out.println("old file: " + tableFile.getAbsolutePath());
		System.out.println("tmp file: " + tmpFile.getAbsolutePath());

		// open tmpFile for read
		XMLHandler tempFileXMLHandler = new XMLHandler(tmpFile, columnsId,
				this, false);
		
		System.out.println("hello!");
		
		// open tableFile for write
		XMLHandler tableFileXMLHandler = new XMLHandler(tableFile, columnsId,
				this, true);
		// read all elements of tmpFile and write them into tableFile
		Record readRecord;
		try {
			while ((readRecord = tempFileXMLHandler.readNextRecord()) != null) {
				tableFileXMLHandler.writeNextRecord(readRecord);
			}
		} catch (Exception e) {
			// do nothing.
		}
		
		// write the new record inside tableFile
		tableFileXMLHandler.writeNextRecord(newValues);
		// close tmpFile
		tempFileXMLHandler.close();
		// close tableFile
		tableFileXMLHandler.close();
		// delete tmpFile
		tmpFile.delete();

		DTDGenerator.main(new String[] { tableFile.getAbsolutePath(),
				dtdFile.getAbsolutePath() });
	}

	public RecordSet select(String[] columnsNames, Condition condition) {

		// make new instance
		RecordSet ret = new RecordSet(columnsNames);

		// open tableFile for read
		XMLHandler hndl = new XMLHandler(tableFile, columnsId, this, false);

		// loop
		Record readRecord;
		try {
			while ((readRecord = hndl.readNextRecord()) != null) {
				if (condition.meetsCondition(readRecord)) {
					Record r = new Record(this);
					for (String col : columnsNames)
						r.setCell(col, readRecord.getValue(col));
					ret.add(r);
				}
			}
		} catch (Exception e) {
			// do nothing.
		}

		// close hndl:
		hndl.close();

		// return the record set
		return ret;
	}

	public void delete(Condition condition) throws Exception {
		// rename table file to a "tableName.tmp":
		tableFile.renameTo(tmpFile);

		// open tmpFile for read
		XMLHandler tempFileXMLHandler = new XMLHandler(tmpFile, columnsId,
				this, false);
		// open tableFile for write
		XMLHandler tableFileXMLHandler = new XMLHandler(tableFile, columnsId,
				this, true);
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
				this, false);
		// open tableFile for write
		XMLHandler tableFileXMLHandler = new XMLHandler(tableFile, columnsId,
				this, true);
		// read all elements of tmpFile and write them into tableFile and update
		// records if it meet the condition
		Record readRecord;
		while ((readRecord = tempFileXMLHandler.readNextRecord()) != null) {
			if (condition.meetsCondition(readRecord)) {
				for (int i = 0; i < columnsNames.length; i++) {
					readRecord.setCell(columnsNames[i], values[i]);
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
