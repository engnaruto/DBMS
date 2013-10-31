import java.io.*;
import javax.xml.stream.*;
import javax.xml.stream.events.*;

public class XMLHandler {

	private ColumnIdentifier[] colID;
	private XMLEventReader eventReader;
	private XMLEventWriter eventWriter;
	private XMLEventFactory eventFactory;

	public XMLHandler(File file, ColumnIdentifier[] colID, boolean write) {
		this.colID = colID;
		this.eventReader = null;
		this.eventWriter = null;
		this.eventFactory = null;
		try {
			if (!write) {
				readXML(file);
			} else {
				writeXML(file);
			}
		} catch (Exception e) {
			System.out.println("some error!");
		}
	}

	private void readXML(File file) throws Exception {
		// First, create a new XMLInputFactory
		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
		// Setup a new eventReader
		InputStream in = new FileInputStream(file);
		eventReader = inputFactory.createXMLEventReader(in);
	}

	private void writeXML(File file) throws Exception {
		// create an XMLOutputFactory
		XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
		// create XMLEventWriter
		eventWriter = outputFactory.createXMLEventWriter(new FileOutputStream(
				file));
		// create an EventFactory
		eventFactory = XMLEventFactory.newInstance();
		StartDocument startDocument = eventFactory.createStartDocument();
		eventWriter.add(startDocument);
		eventWriter.add(eventFactory.createDTD("\n"));
		StartElement configStartElement = eventFactory.createStartElement("",
				"", "config");
		eventWriter.add(configStartElement);
		eventWriter.add(eventFactory.createDTD("\n"));
	}

	public Record readNextRecord() throws Exception {
		String[] columnsNames = new String[colID.length];
		Object[] values = new Object[colID.length];

		// read elements
		int i = 0;
		while (eventReader.hasNext()) {
			XMLEvent event = eventReader.nextEvent();
			if (event.isStartElement()) {
				System.out.println(event.asStartElement().getName());
				// If we have an item element, we create a new item
				if (event.asStartElement().getName().getLocalPart()
						.equals(colID[i].getColumnName())) {
					event = eventReader.nextEvent();
					columnsNames[i] = colID[i].getColumnName();
					System.out.println(event.asCharacters().getData());
					values[i++] = event.asCharacters().getData();
					continue;
				}
			}
			// If we reach the end of an item element, we add it to the list
			if (event.isEndElement()) {
				EndElement endElement = event.asEndElement();
				if (endElement.getName().getLocalPart().equals("Record")) {
					return (new Record(columnsNames, values));
				}

			}

		}
		return null;
	}

	private void setStart() throws XMLStreamException {
		XMLEvent end = eventFactory.createDTD("\n");
		// create and write Start Tag

		// create configuration open tag
		StartElement configStartElement = eventFactory.createStartElement("",
				"", "Record");
		eventWriter.add(configStartElement);
		eventWriter.add(end);
	}

	private void createNode(String name, String value)
			throws XMLStreamException {

		XMLEventFactory eventFactory = XMLEventFactory.newInstance();
		XMLEvent end = eventFactory.createDTD("\n");
		XMLEvent tab = eventFactory.createDTD("\t");
		// create Start node
		StartElement sElement = eventFactory.createStartElement("", "", name);
		eventWriter.add(tab);
		eventWriter.add(sElement);
		// create Content
		Characters characters = eventFactory.createCharacters(value);
		eventWriter.add(characters);
		// create End node
		EndElement eElement = eventFactory.createEndElement("", "", name);
		eventWriter.add(eElement);
		eventWriter.add(end);

	}
	
	private void setEnd() throws XMLStreamException {
		XMLEvent end = eventFactory.createDTD("\n");
		eventWriter.add(eventFactory.createEndElement("", "", "Record"));
		eventWriter.add(end);
	}

	public void writeNextRecord(Record record) throws XMLStreamException {
		String[] a = record.getColNames();
		setStart();
		for (int i = 0; i < a.length; i++)
			createNode(colID[i].getColumnName(), a[i]);
		setEnd();
	}

	private void closeXMLReader() throws XMLStreamException {
		eventReader.close();
	}
	
	private void closeXMLWriter() throws XMLStreamException {
		eventWriter.add(eventFactory.createEndDocument());
		eventWriter.close();
	}
	
	public void close() {
		try {
			if (eventReader != null)
				closeXMLReader();
			if (eventWriter != null)
				closeXMLWriter();
		} catch (Exception e) {
			System.out.println("Some error during close.");
		}
	}

}
