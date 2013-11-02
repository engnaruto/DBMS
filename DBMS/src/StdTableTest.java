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
		String cols[] = {"longer", "name"   , "logic", /*"birth"*/};
		String vals[] = {"118"   , "mina",    "true",  /*"2012-01-01T02:00:00"*/};
		try {
			Record r = new Record(cols, vals, tbl);
			tbl.insert(r);
		} catch (Exception e) {
			fail("can't insert record 1");
		}
	}

	@Test
	public void testSelect() {
		/*Table tbl = getUsersTable();
		String cols[] = {"logic", "name", "longer"};
		Condition cond = new Condition("longer < 120", tbl);
		RecordSet rs = tbl.select(cols, cond);
		for (Record r : rs) {
			System.out.println(r);
		}*/
	}

	@Test
	public void testDelete() {
		//Table tbl = getUsersTable();
		//Condition cond = new Condition("name = tito", tbl);
		/*try {
			//tbl.delete(cond);
		} catch (Exception e) {
			fail("some error in delete");
		}*/
	}

	@Test
	public void testUpdate() {
		/*Table tbl = getUsersTable();
		Condition cond = new Condition("longer > 114", tbl);
		String cols[] = {"name", "logic"};
		Object vals[] = {"toko", "false" };
		try {
			tbl.update(cols, vals, cond);
		} catch (Exception e) {
			fail("some error in update");
		}*/
	}

}
