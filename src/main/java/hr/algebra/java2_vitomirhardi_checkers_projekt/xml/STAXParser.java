package hr.algebra.java2_vitomirhardi_checkers_projekt.xml;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

public class STAXParser
{
private static final String FILENAME="moves.xml";

   private static void parse(String fileName) throws FileNotFoundException, XMLStreamException {
       XMLInputFactory factory = XMLInputFactory.newFactory();
       String filePath = STAXParser.class.getResource(fileName).getFile();

       XMLEventReader reader = factory.createXMLEventReader(new FileInputStream(filePath), String.valueOf(StandardCharsets.UTF_8));
       StringBuilder report = new StringBuilder();
       while (reader.hasNext()) {
           XMLEvent event = reader.nextEvent();
           handleEvent(event, report);
       }
       System.out.println(report);
   }

    public static void handleEvent(XMLEvent event,StringBuilder report) {
        switch (event.getEventType()) {
            case XMLStreamConstants.START_DOCUMENT:
                report.append("Document started\n\n");
                break;
            case XMLStreamConstants.START_ELEMENT: {
                // if brackets used -> variables have restricted scope -> Vuk Vojta
                StartElement startElement = event.asStartElement();
                String qName = startElement.getName().getLocalPart();
                report.append(qName).append(" ");
                Iterator attributes = startElement.getAttributes();
                if (attributes.hasNext()) {
                    report.append("Attributes: ");
                    while (attributes.hasNext()) {
                        report.append(attributes.next()).append(" ");
                    }
                    report.append("\n");
                }
                break;
            }
            case XMLStreamConstants.CHARACTERS:
                String data = event.asCharacters().getData();
                // it collects also String.empty
                if (!data.trim().isEmpty()) {
                    report.append(data);
                }
                break;
            case XMLStreamConstants.END_ELEMENT:
                report.append("\n");
                break;
            case XMLStreamConstants.END_DOCUMENT:
                report.append("Document ended");
                break;
        }
    }

}
