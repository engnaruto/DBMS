import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFileChooser;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XMLHandler {

	private ColumnIdentifier[] colID;
	private XMLEventReader eventReader;
	private XMLEventWriter eventWriter;

	private String tableName;
	private ArrayList<String> columnsName;
	private ArrayList<Record> records;

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
		eventReader.nextEvent();
		eventReader.nextEvent();

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

	private void addAllRecordes() throws XMLStreamException {
		for (Record l : records) {
			Object[] q = l.getContainer();
			setStart();
			for (int i = 0; i < columnsName.size(); i++) {
				createNode(columnsName.get(i), q[i].toString());
			}
			setEnd();
		}
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

	// public void addRecord(Record record) throws XMLStreamException {
	// records.add(record);
	// try {
	// createXML();
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// // readConfig(tableName+".xml");
	// }

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

	// event driven
	public Record readNextRecord() throws Exception {
		String[] columnsNames = new String[colID.length];
		Object[] values = new Object[colID.length];
		// read <Record>
		eventReader.nextEvent();
		eventReader.nextEvent();
		eventReader.nextEvent();
		
		for (int i = 0; i < colID.length; i ++){
			XMLEvent event = eventReader.nextEvent();
			if (event.isStartElement()) {
				System.out.println(event.asStartElement().getName());
				//System.out.println(event.asCharacters().getData());
			} else {
				System.out.println("not start");
				i--;
			}
		}
		/*
		// read elements
		while (eventReader.hasNext()) {
			XMLEvent event = eventReader.nextEvent();
			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				System.out.println(event.asStartElement().getName());
//				System.out.println(event.asCharacters().getData());
				// If we have an item element, we create a new item

				if (event.asStartElement().getName().getLocalPart()
						.equals(colID[i].getColumnName())) {
					event = eventReader.nextEvent();
					// item.add(event.asCharacters().getData());
					columnsNames[i] = colID[i].getColumnName();
					values[i++] = event.asCharacters().getData();
					continue;
				}

				// If we reach the end of an item element, we add it to the list
				if (event.isEndElement()) {
					EndElement endElement = event.asEndElement();
					if (endElement.getName().getLocalPart().equals("Record")) {
						System.out.println("ended");
						// System.out.println(values[0]);
						return (new Record(columnsNames, values));
					}
				}

			}

		}*/
		System.out.println(";S");
		return null;
	}

	public void writeNextRecord(Record record) {

	}

	public void readConfig(String configFile) throws Exception {

		// First, create a new XMLInputFactory
		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
		// Setup a new eventReader
		InputStream in = new FileInputStream(configFile);
		XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
		// read the XML document

	}

	public void closeXML() throws XMLStreamException {
		eventWriter.add(eventFactory.createEndDocument());
		eventWriter.close();
	}

}
