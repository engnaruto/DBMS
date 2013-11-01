import static org.junit.Assert.*;
import java.util.Date;
import org.junit.Test;

public class StdDatabaseTest {

	private Database getFoeDB() {

		DBMS mainDBMS = new StdDBMS();
		
		// make sure foe exists:
		try {
			mainDBMS.createDB("foe");
		} catch (Exception e) {
			//System.out.println("foe database already exists!");
		}
		
		// get foe
		try {
			mainDBMS.setUsedDB("foe");
		} catch (Exception e) {
			fail("DB not found!");
		}
		
		return mainDBMS.getUsedDB();
	}
	
	@Test
	public void testAddTable() {

		Database foe = getFoeDB();
		
		// add new table:
		try {
			ColumnIdentifier[] colIDs = new ColumnIdentifier[7];
			colIDs[0] = new ColumnIdentifier("name", String.class);
			colIDs[1] = new ColumnIdentifier("integ", Integer.class);
			colIDs[2] = new ColumnIdentifier("longer", Long.class);
			colIDs[3] = new ColumnIdentifier("floating", Float.class);
			colIDs[4] = new ColumnIdentifier("doubler", Double.class);
			colIDs[5] = new ColumnIdentifier("birth", Date.class);
			colIDs[6] = new ColumnIdentifier("logic", Boolean.class);
			foe.addTable("users", colIDs);
		} catch (Exception e) {
			System.out.println("table users exist!");
		}
		
		try {
			ColumnIdentifier[] colIDs = new ColumnIdentifier[5];
			colIDs[0] = new ColumnIdentifier("tarek", String.class);
			colIDs[1] = new ColumnIdentifier("omar", String.class);
			colIDs[2] = new ColumnIdentifier("mostafa", Float.class);
			colIDs[3] = new ColumnIdentifier("iocoder", Date.class);
			colIDs[4] = new ColumnIdentifier("mostafa2", Boolean.class);
			foe.addTable("students", colIDs);
		} catch (Exception e) {
			System.out.println("table students exist!");
		}
		
		try {
			ColumnIdentifier[] colIDs = new ColumnIdentifier[2];
			colIDs[0] = new ColumnIdentifier("courseName", String.class);
			colIDs[1] = new ColumnIdentifier("courseCode", Integer.class);
			foe.addTable("courses", colIDs);
		} catch (Exception e) {
			System.out.println("table courses exist!");
		}
		
	}

	@Test
	public void testRemoveTable() {
		try {
			getFoeDB().removeTable("courses");
		} catch (Exception e) {
			fail("can't remove courses!");
		}	
	}

}
