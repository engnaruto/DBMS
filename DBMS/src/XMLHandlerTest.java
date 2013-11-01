import static org.junit.Assert.*;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.xml.stream.XMLStreamException;
import org.junit.Test;

public class XMLHandlerTest {

	private String getTestFileName() {
		return System.getProperty("user.home") + File.separatorChar +
				"Desktop" + File.separatorChar + "test.xml";
	}
	
	@Test
	public void testReadNextRecord() {
		File testFile = new File(getTestFileName());
		ColumnIdentifier[] colIDs = new ColumnIdentifier[4];
		colIDs[0] = new ColumnIdentifier("firstName", String.class);
		colIDs[1] = new ColumnIdentifier("lastName", String.class);
		colIDs[2] = new ColumnIdentifier("phone", Integer.class);
		colIDs[3] = new ColumnIdentifier("birth", Date.class);
		Table x = new StdTable("test", testFile, colIDs);
		XMLHandler hndl = new XMLHandler(testFile, colIDs, x, false);
		Record r;
		try {
			while((r = hndl.readNextRecord()) != null)
				System.out.println("record: " + r);
		} catch (Exception e) {
			fail("error: exception while readNextRecord()");
		}
		hndl.close();
	}

	@Test
	public void testWriteNextRecord() {
		File testFile = new File(getTestFileName());
		ColumnIdentifier[] colIDs = new ColumnIdentifier[4];
		colIDs[0] = new ColumnIdentifier("firstName", String.class);
		colIDs[1] = new ColumnIdentifier("lastName", String.class);
		colIDs[2] = new ColumnIdentifier("phone", Integer.class);
		colIDs[3] = new ColumnIdentifier("birth", Date.class);
		Table x = new StdTable("test", testFile, colIDs);
		XMLHandler hndl = new XMLHandler(testFile, colIDs, x, true);
		
		// record 1:
		Record r = new Record(x);
		r.setCell("firstName", "Mostafa");
		r.setCell("lastName", "Abd el aziz");
		r.setCell("phone", new Integer(12211));
		final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.S";
        String userDate = "2009-07-28T13:37:24.797383";
        SimpleDateFormat dfIn = new SimpleDateFormat(DATE_FORMAT);
        Date d = new Date(0);
		try {
			d = (Date) dfIn.parse(userDate);
		} catch (ParseException e1) {
			System.out.println("error casting");
		}
		r.setCell("birth", d);
		try {
			hndl.writeNextRecord(r);
		} catch (XMLStreamException e) {
			fail("XML error 1");
		}
		
		// record 2:
		r = new Record(x);
		r.setCell("firstName", "");
		try {
			hndl.writeNextRecord(r);
		} catch (XMLStreamException e) {
			fail("XML error 2");
		}
		
		// done:
		hndl.close();
	}

}
