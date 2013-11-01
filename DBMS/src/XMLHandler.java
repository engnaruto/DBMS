import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.stream.*;
import javax.xml.stream.events.*;

public class XMLHandler {

	private ColumnIdentifier[] colID;
	private XMLEventReader eventReader;
	private XMLEventWriter eventWriter;
	private XMLEventFactory eventFactory;
	private Table table;

	public XMLHandler(File file, ColumnIdentifier[] colID, Table table, boolean write) {
		this.colID = colID;
		this.table = table;
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

	public void readXML(File file) throws Exception {
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

	private void setEnd() throws XMLStreamException {
		// TODO Auto-generated method stub
		XMLEvent end = eventFactory.createDTD("\n");
		eventWriter.add(eventFactory.createEndElement("", "", "Record"));
		eventWriter.add(end);
	}

	private void setStart() throws XMLStreamException {
		// TODO Auto-generated method stub
		XMLEvent end = eventFactory.createDTD("\n");
		// create and write Start Tag

		// create config open tag
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

	public Record readNextRecord() throws Exception {
		String[] columnsNames = new String[colID.length];
		Object[] values = new Object[colID.length];

		// read elements
		int i = 0;
		while (eventReader.hasNext()) {
			XMLEvent event = eventReader.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				// If we have an item element, we create a new item
				if (event.asStartElement().getName().getLocalPart()
						.equals(colID[i].getColumnName())) {
					event = eventReader.nextEvent();
					columnsNames[i] = colID[i].getColumnName();
					values[i] = parser(event.asCharacters().getData(),
							colID[i].getColumnType());
					i++;
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

	private Object parser(String data, Class<?> u) {
		// TODO Auto-generated method stub
		switch (u.getSimpleName()) {
		case "Integer":
			return Integer.parseInt(data);
		case "String":
			return data;
		case "Double":
			return Double.parseDouble(data);
		case "Float":
			return Float.parseFloat(data);
		case "Long":
			return Long.parseLong(data);
		case "Boolean":
			return Boolean.parseBoolean(data);
		case "Date":
			try {
				return getdate(data);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				return data;
			}

		default:
			return data;
		}

	}

	private Date getdate(String data) throws ParseException {
		// TODO Auto-generated method stub
		final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
		String userDate = data;
		SimpleDateFormat dfIn = new SimpleDateFormat(DATE_FORMAT);
		Date d = dfIn.parse(userDate);
		Date dOut = d;
		return dOut;
	}

	public void writeNextRecord(Record record) throws XMLStreamException {
		// Object[] a = record.getContainer();
		// setStart();
		// for (int i = 0; i < a.length; i++)
		// if (colID[i].getColumnType().getSimpleName().equals("Date"))
		// createNode(colID[i].getColumnName(), (new SimpleDateFormat(
		// "yyyy-MM-dd'T'HH:mm:ss")).format(a[i]));
		// else
		// createNode(colID[i].getColumnName(), a[i].toString());
		// setEnd();
	}

	public void readConfig(String configFile) throws Exception {

		// First, create a new XMLInputFactory
		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
		// Setup a new eventReader
		InputStream in = new FileInputStream(configFile);
		XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
		// read the XML document

	}

	public void closeXMLWriter() throws XMLStreamException {
		eventWriter.add(eventFactory.createEndDocument());
		eventWriter.close();
	}

	public void closeXMLReader() throws XMLStreamException {
		eventReader.close();
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
