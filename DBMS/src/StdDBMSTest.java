import static org.junit.Assert.*;
import org.junit.Test;

public class StdDBMSTest {

	@Test
	public void testDB() {
		DBMS mainDBMS = new StdDBMS();
		try {
			mainDBMS.createDB("foe");
		} catch (Exception e) {
			System.out.println("foe database already exists!");
		}
		
		try {
			mainDBMS.setUsedDB("foe");
		} catch (Exception e) {
			fail("DB not found!");
		}
	}

}
