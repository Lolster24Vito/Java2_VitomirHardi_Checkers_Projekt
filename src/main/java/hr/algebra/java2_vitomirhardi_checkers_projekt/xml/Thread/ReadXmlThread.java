package hr.algebra.java2_vitomirhardi_checkers_projekt.xml.Thread;

import hr.algebra.java2_vitomirhardi_checkers_projekt.models.PlayerMove;
import hr.algebra.java2_vitomirhardi_checkers_projekt.xml.XmlConfiguration;
import hr.algebra.java2_vitomirhardi_checkers_projekt.xml.XmlParser;

import java.lang.module.Configuration;
import java.util.List;
import java.util.concurrent.Callable;


public class ReadXmlThread implements Callable<List<PlayerMove>> {

    @Override
    public List<PlayerMove> call() throws Exception {
        synchronized (XmlConfiguration.XML_FILE) {

            while (XmlConfiguration.isWriting) {
                try {
                    XmlConfiguration.XML_FILE.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            XmlConfiguration.isWriting=true;
            List<PlayerMove> playerMoves = XmlParser.readPlayerMoves();
            XmlConfiguration.isWriting=false;

            XmlConfiguration.XML_FILE.notifyAll();
            return playerMoves;
        }
    }
}
