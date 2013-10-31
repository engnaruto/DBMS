import java.io.*;
import java.util.ArrayList;

import javax.xml.stream.*;
import javax.xml.stream.events.*;

public class XMLHandler {

	private ColumnIdentifier[] colID;
	private XMLEventReader eventReader;
	private XMLEventWriter eventWriter;
	private XMLEventFactory eventFactory;

	public XMLHandler(File file, ColumnIdentifier[] colID, boolean write) {
		this.colID = colID;
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

	public void writeNextRecord(Record record) throws XMLStreamException {
		Object[] a = record.getContainer();
		setStart();
		for (int i = 0; i < a.length; i++)
			createNode(colID[i].getColumnName(), a[i].toString());
		setEnd();
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

}
