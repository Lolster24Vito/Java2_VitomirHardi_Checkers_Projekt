package hr.algebra.java2_vitomirhardi_checkers_projekt.xml;

import hr.algebra.java2_vitomirhardi_checkers_projekt.HelloApplication;

import java.io.File;

public class XmlConfiguration {

    public static boolean isWriting;
    public static final String FILENAME="moves.xml";

    public static final File XML_FILE=new File(HelloApplication.class.getResource("").getPath() +  FILENAME);
   public final static String filePath=HelloApplication.class.getResource("").getPath() +  FILENAME;

    static {
        isWriting=false;
    }
}
