import static org.junit.Assert.*;
import org.junit.Test;

public class StdTableTest {

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
	
	private Table getUsersTable() {
		return getFoeDB().getTable("users");
	}
		
	@Test
	public void testInsert() {
		Table tbl = getUsersTable();
		if (tbl == null)
			fail("can't read users table");
		
		// record 1
		String cols[] = {"longer", "name"   , "logic", "birth"};
		String vals[] = {"113"   , "mido",    "fin",  "2012-01-01T02:00:00"};
		Record r = new Record(cols, vals, tbl);
		try {
			tbl.insert(r);
		} catch (Exception e) {
			fail("can't insert record 1");
		}
	}

	@Test
	public void testSelect() {
		//fail("Not yet implemented");
	}

	@Test
	public void testDelete() {
		//fail("Not yet implemented");
	}

	@Test
	public void testUpdate() {
		//fail("Not yet implemented");
	}

}
