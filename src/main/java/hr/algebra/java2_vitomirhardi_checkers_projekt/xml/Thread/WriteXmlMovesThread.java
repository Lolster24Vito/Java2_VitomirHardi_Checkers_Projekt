package hr.algebra.java2_vitomirhardi_checkers_projekt.xml.Thread;

import hr.algebra.java2_vitomirhardi_checkers_projekt.models.PlayerMove;
import hr.algebra.java2_vitomirhardi_checkers_projekt.xml.XmlConfiguration;
import hr.algebra.java2_vitomirhardi_checkers_projekt.xml.XmlParser;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WriteXmlMovesThread implements Runnable {

    private List<PlayerMove> playerMoves;

    public WriteXmlMovesThread(List<PlayerMove> playerMoves) {
        this.playerMoves = playerMoves;
    }

    @Override
    public void run() {
        synchronized (XmlConfiguration.XML_FILE) {


            while (XmlConfiguration.isWriting) {
                try {
                    XmlConfiguration.XML_FILE.wait();
                } catch (InterruptedException e) {
                    System.out.println("Wait failed");
                    e.printStackTrace();
                }
            }
            XmlConfiguration.isWriting = true;
            try {
                XmlParser.writePlayerMoves(playerMoves);
            } catch (Exception e) {
                Logger.getLogger(WriteXmlMovesThread.class.getName()).log(
                        Level.SEVERE, null, e);
            }
            //Write to document
            XmlConfiguration.isWriting = false;
            XmlConfiguration.XML_FILE.notifyAll();

        }
    }
}
