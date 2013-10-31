import java.io.File;
import java.util.Hashtable;

public class StdDBMS implements DBMS {

	private StdDatabase usedDatabase;
	private String path;
	private String slash;
	File DBDir;
	
	public StdDBMS() {
		
		// get path
		slash = File.separatorChar + "";
		path = System.getProperty("user.home") + slash + "Databases" + slash;
		
		// make sure the database directory exists
		DBDir = new File(path);
		if (!DBDir.exists()) {
			DBDir.mkdir();
		}
		
		// no database is loaded
		usedDatabase = null;
	}

	@Override
	public void createDB(String DBName) throws Exception {
		File newDB = new File(path + DBName);
		if (newDB.exists())
			throw new Exception("Database exists!");
		newDB.mkdir();
	}

	@Override
	public void setUsedDB(String dbName) throws Exception {
		File usedDB = new File(path + dbName);
		if (!usedDB.exists()) {
			throw new Exception("Database doesn't exist!");
		}
		usedDatabase = new StdDatabase(usedDB);
	}
	
	@Override
	public StdDatabase getUsedDB() {
		return usedDatabase;
	}



}
