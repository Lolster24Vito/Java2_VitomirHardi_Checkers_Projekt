package hr.algebra.java2_vitomirhardi_checkers_projekt.xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class MovesHandler extends DefaultHandler {
        @Override
        public void startDocument() throws SAXException {
            System.out.println("Document started");
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            StringBuilder report = new StringBuilder("Element start: ");
            report
                    .append(System.lineSeparator())
                    .append("\tqName: ")
                    .append(qName);
            if (attributes.getLength() > 0) {
                report
                        .append(System.lineSeparator())
                        .append("\tAttributes: ")
                        .append(System.lineSeparator());

                for (int i = 0; i < attributes.getLength(); i++) {
                    report.append("\t\tqName: ").append(attributes.getQName(i)).append(System.lineSeparator())
                            .append("\t\ttype: ").append(attributes.getType(i)).append(System.lineSeparator())
                            .append("\t\tvalue: ").append(attributes.getValue(i));
                }
            }
            System.out.println(report);
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            System.out.println("\tText: " + new String(ch, start, length));
        }

        @Override
        public void warning(SAXParseException e) throws SAXException {
            System.err.println("Warning: " + e.getMessage());
        }

        @Override
        public void error(SAXParseException e) throws SAXException {
            System.err.println("Error: " + e.getMessage());
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            StringBuilder report = new StringBuilder("Element end: ");
            report
                    .append(System.lineSeparator())
                    .append("\tqName: ").append(qName);
            System.out.println(report);
        }

        @Override
        public void endDocument() throws SAXException {
            System.out.println("Document ended");
        }
}
