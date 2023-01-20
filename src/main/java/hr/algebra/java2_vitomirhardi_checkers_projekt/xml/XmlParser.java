package hr.algebra.java2_vitomirhardi_checkers_projekt.xml;

import hr.algebra.java2_vitomirhardi_checkers_projekt.HelloApplication;
import hr.algebra.java2_vitomirhardi_checkers_projekt.models.*;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class XmlParser
{
private  static Document createDocument(String elementName) throws ParserConfigurationException {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();
    DOMImplementation domImplementation=builder.getDOMImplementation();
    DocumentType documentType=domImplementation.createDocumentType("DOCTYPE",null,"moves.dtd");
    return domImplementation.createDocument(null, elementName, documentType);
}
    public static void testSaveDocument(){
    try{


        List<PlayerMove> playerMoves=new ArrayList<>();
        //PlayerMove playerMove1 = readNextPlayerMove();
     //   PlayerMove playerMove2 = readNextPlayerMove();
        System.out.println("done");
        }
    catch (Exception e) {
        e.printStackTrace();
    }
    }
    public  static  void writePlayerMoves(List<PlayerMove> playerMoves) throws TransformerException, ParserConfigurationException {
        Document document=createDocument("moves");

        int counter=0;
        for (PlayerMove playerMove:playerMoves
             ) {
            Element moveElement=document.createElement("PlayerMove");
            document.getDocumentElement().appendChild(moveElement);

            moveElement.setAttributeNode(createAttribute(document,"id",String.valueOf(counter)));
            if(playerMove.isJump()){

                //
                moveElement.appendChild(createElement(document, "xPos",
                        String.valueOf(playerMove.getPiecePosition().getX())));
                moveElement.appendChild(createElement(document, "yPos",
                        String.valueOf(playerMove.getPiecePosition().getY())));

                //
                moveElement.appendChild(createElement(document, "fromPosX",
                        String.valueOf(playerMove.getPosition().getX())));
                //
                moveElement.appendChild(createElement(document, "fromPosY",
                        String.valueOf(playerMove.getPosition().getY())));
            }
            else{
                moveElement.appendChild(createElement(document, "xPos",
                        String.valueOf(playerMove.getPosition().getX())));
                moveElement.appendChild(createElement(document, "yPos",
                        String.valueOf(playerMove.getPosition().getY())));

                moveElement.appendChild(createElement(document, "fromPosX",
                        String.valueOf(playerMove.getPiecePosition().getX())));
                moveElement.appendChild(createElement(document, "fromPosY",
                        String.valueOf(playerMove.getPiecePosition().getY())));
            }



            moveElement.appendChild(createElement(document, "isKing",
                    String.valueOf(playerMove.getPieceToMove().getIsKing())));
            moveElement.appendChild(createElement(document, "PlayerColor",
                    String.valueOf(playerMove.getPieceToMove().getPieceColor())));
            moveElement.appendChild(createElement(document, "isJump",
                    String.valueOf(playerMove.getMoveJump())));
            if(playerMove.isJump()){
                moveElement.appendChild(createElement(document,"takenPieceX",
                        String.valueOf(playerMove.getTakenPiecePosition().getX())) );
                moveElement.appendChild(createElement(document,"takenPieceY",
                        String.valueOf(playerMove.getTakenPiecePosition().getY())) );
            }
            counter++;
        }

        saveDocument(document,XmlConfiguration.FILENAME);
    }

    private static void saveDocument(Document document, String fileName) throws TransformerException {
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, document.getDoctype().getSystemId());
        //transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

        //transformer.transform(new DOMSource(document), new StreamResult(System.out));
        String filePath = HelloApplication.class.getResource("").getPath() +  fileName;
        //todo add
        transformer.transform(new DOMSource(document), new StreamResult(new File(filePath)));
    }

    private static Attr createAttribute(Document document, String name, String value) {

    Attr attr=document.createAttribute(name);
    attr.setValue(value);
    return attr;
}
    private static Node createElement(Document document, String tagName, String data) {
        Element element = document.createElement(tagName);
        Text text = document.createTextNode(data);
        element.appendChild(text);
        return element;
    }

    public static List<PlayerMove> readPlayerMoves() throws FileNotFoundException, XMLStreamException {
       XMLInputFactory factory = XMLInputFactory.newFactory();

       XMLEventReader reader = createEventReadXML(factory);
       StringBuilder report = new StringBuilder();
        List<PlayerMove> playerMoves=new ArrayList<>();
        MoveXmlElementTypes currentType=MoveXmlElementTypes.none;
        PlayerMoveXmlData playerMoveXmlData=new PlayerMoveXmlData();

       while (reader.hasNext()) {
           XMLEvent event = reader.nextEvent();
           switch (event.getEventType()) {
               case XMLStreamConstants.START_DOCUMENT:
                   //report.append("Document started\n\n");
                   break;
               case XMLStreamConstants.START_ELEMENT: {
                   // if brackets used -> variables have restricted scope -> Vuk Vojta
                   StartElement startElement = event.asStartElement();
                   String qName = startElement.getName().getLocalPart();


                   //switch qName
                   report.append(qName).append(" ");
                   Iterator attributes = startElement.getAttributes();
                   if (attributes.hasNext()) {
                       report.append("Attributes: ");
                       while (attributes.hasNext()) {
                           Attribute attribute= (Attribute) attributes.next();
                           Optional<String> id=getAttributeId(attribute);
                           if(id.isPresent()){
                               playerMoveXmlData.setId(id.get());
                           }
                          /*

                           */

                         //  report.append(attributes.next()).append(" ");
                       }
                     //  report.append("\n");
                   }
                   try {
                       currentType=MoveXmlElementTypes.valueOf(qName);
                   }
                   catch (Exception e){
                       continue;
                   }
                   break;
               }
               case XMLStreamConstants.CHARACTERS:
                   String data = event.asCharacters().getData();
                   // it collects also String.empty
                   //get string
                   switch (currentType) {

                       case xPos -> { playerMoveXmlData.setX(Integer.valueOf(data));
                       }
                       case yPos -> {playerMoveXmlData.setY(Integer.valueOf(data));
                       }
                       case isKing -> {playerMoveXmlData.setKing(Boolean.valueOf(data));
                       }
                       case isJump->{playerMoveXmlData.setJump(Boolean.valueOf(data));}
                       case PlayerColor -> { playerMoveXmlData.setPlayerColor(PlayerColor.valueOf(data));
                       }
                       case takenPieceX -> { playerMoveXmlData.setTakenPieceX(Integer.valueOf(data));
                       }
                       case takenPieceY -> {
                           playerMoveXmlData.setTakenPieceY(Integer.valueOf(data));
                       }
                   }
                   if (!data.trim().isEmpty()) {
                       report.append(data);
                   }
                   break;
               case XMLStreamConstants.END_ELEMENT:
                   EndElement endElement = event.asEndElement();
                   if(endElement.getName().getLocalPart()=="PlayerMove") {
                       PlayerMove playerMove;
                       if (playerMoveXmlData.isJump()) {
                           playerMove = new PlayerMove(
                                   new PieceData(new Position(playerMoveXmlData.getxPos(), playerMoveXmlData.getyPos()), playerMoveXmlData.isKing(), playerMoveXmlData.getPlayerColor()),
                                   new Position(playerMoveXmlData.getTakenPieceX(), playerMoveXmlData.getTakenPieceY()),
                                   new Position(playerMoveXmlData.getFromPosX(), playerMoveXmlData.getFromPosY()),
                                   playerMoveXmlData.isJump()
                           );
                       }
                       else{
                           playerMove=new PlayerMove(
                                   new PieceData(new Position(playerMoveXmlData.getxPos(), playerMoveXmlData.getyPos()), playerMoveXmlData.isKing(), playerMoveXmlData.getPlayerColor()),
                                   new Position(playerMoveXmlData.getFromPosX(), playerMoveXmlData.getyPos())

                           );
                       }
                       playerMoves.add(playerMove);
                       playerMove=new PlayerMove();
                   }

                   currentType=MoveXmlElementTypes.none;
                   break;
               case XMLStreamConstants.END_DOCUMENT:
                   report.append("Document ended");
                   break;
           }

       }
       System.out.println(report);
       return playerMoves;
   }

    private static Optional<String> getAttributeId(Attribute attribute) {
    Optional<String> idVal=Optional.empty();
    if(attribute.getName().toString().equals("id")){
        Optional<String> id=Optional.of(attribute.getValue());
        return id;
    }
    return Optional.empty();
     /*   if(attribute.getName().toString().equals("id")){

        }*/
    }


    private static XMLEventReader nextReader;
    public static Optional<PlayerMove> readNextPlayerMove() throws FileNotFoundException, XMLStreamException {
        XMLInputFactory factory = XMLInputFactory.newFactory();
        if(nextReader==null)
         nextReader = createEventReadXML(factory);
        StringBuilder report = new StringBuilder();
        MoveXmlElementTypes currentType=MoveXmlElementTypes.none;
        PlayerMoveXmlData playerMoveXmlData=new PlayerMoveXmlData();
        boolean foundElement=false;
        while (nextReader.hasNext()) {
            XMLEvent event = nextReader.nextEvent();
            switch (event.getEventType()) {
                case XMLStreamConstants.START_ELEMENT: {
                    // if brackets used -> variables have restricted scope -> Vuk Vojta
                    StartElement startElement = event.asStartElement();
                    String qName = startElement.getName().getLocalPart();
                    //switch qName
                    Iterator attributes = startElement.getAttributes();
//if has correct count
                    if (attributes.hasNext()) {
                        report.append("Attributes: ");
                        while (attributes.hasNext()) {

                            Attribute attribute= (Attribute) attributes.next();
                            Optional<String> id=getAttributeId(attribute);
                                if(id.isPresent()){
                                    //continue as normal
                                    playerMoveXmlData.setId(id.get());
                                }


                          //  report.append(attributes.next()).append(" ");
                        }
                        //  report.append("\n");
                    }
                    try {
                        currentType=MoveXmlElementTypes.valueOf(qName);
                    }
                    catch (Exception e){
                        continue;
                    }
                    break;
                }
                case XMLStreamConstants.CHARACTERS:
                    String data = event.asCharacters().getData();
                    // it collects also String.empty
                    //get string
                    switch (currentType) {

                        case xPos -> { playerMoveXmlData.setX(Integer.valueOf(data));
                        }
                        case yPos -> {playerMoveXmlData.setY(Integer.valueOf(data));
                        }
                        case isKing -> {playerMoveXmlData.setKing(Boolean.valueOf(data));
                        }
                        case isJump->{playerMoveXmlData.setJump(Boolean.valueOf(data));}
                        case fromPosX -> {playerMoveXmlData.setFromPosX(Integer.valueOf(data));}
                        case fromPosY -> {playerMoveXmlData.setFromPosY(Integer.valueOf(data));}

                        case PlayerColor -> { playerMoveXmlData.setPlayerColor(PlayerColor.valueOf(data));
                        }
                        case takenPieceX -> { playerMoveXmlData.setTakenPieceX(Integer.valueOf(data));
                        }
                        case takenPieceY -> {
                            playerMoveXmlData.setTakenPieceY(Integer.valueOf(data));
                        }
                    }
                    if (!data.trim().isEmpty()) {
                        report.append(data);
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    EndElement endElement = event.asEndElement();
                    if(endElement.getName().getLocalPart()=="PlayerMove") {
                        PlayerMove playerMove;
                        if (playerMoveXmlData.isJump()) {
                            playerMove = new PlayerMove(
                                    new PieceData(new Position(playerMoveXmlData.getxPos(), playerMoveXmlData.getyPos()), playerMoveXmlData.isKing(), playerMoveXmlData.getPlayerColor()),
                                    new Position(playerMoveXmlData.getTakenPieceX(), playerMoveXmlData.getTakenPieceY()),
                                    new Position(playerMoveXmlData.getFromPosX(), playerMoveXmlData.getFromPosY()),
                                    playerMoveXmlData.isJump()
                            );
                        }
                        else{
                            playerMove=new PlayerMove(
                                    new PieceData(new Position(playerMoveXmlData.getFromPosX(), playerMoveXmlData.getFromPosY()), playerMoveXmlData.isKing(), playerMoveXmlData.getPlayerColor()),
                                    new Position(playerMoveXmlData.getxPos(), playerMoveXmlData.getyPos())

                            );
                        }
                        return Optional.of(playerMove);
                    }

                    currentType=MoveXmlElementTypes.none;
                    break;
                case XMLStreamConstants.END_DOCUMENT:
                    report.append("Document ended");
                    break;
            }

        }

        System.out.println(report);

        return Optional.empty();
    }

    private static XMLEventReader createEventReadXML(XMLInputFactory factory) throws XMLStreamException, FileNotFoundException {
        return factory.createXMLEventReader(new FileInputStream(XmlConfiguration.XML_FILE), String.valueOf(StandardCharsets.UTF_8));
    }

    public static Optional<PlayerMove> readPlayerMove(int moveCounter) {

    }
}
