package hr.algebra.java2_vitomirhardi_checkers_projekt.xml.Thread;

import hr.algebra.java2_vitomirhardi_checkers_projekt.models.PlayerMove;
import hr.algebra.java2_vitomirhardi_checkers_projekt.xml.XmlConfiguration;
import hr.algebra.java2_vitomirhardi_checkers_projekt.xml.XmlParser;

import java.util.logging.Level;
import java.util.logging.Logger;

public class WriteXmlMoveThread  implements Runnable {
    private PlayerMove playerMove;

    public WriteXmlMoveThread(PlayerMove playerMoves) {
        this.playerMove = playerMoves;
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
                XmlParser.writePlayerMove(playerMove);
            } catch (Exception e) {
                Logger.getLogger(WriteXmlMoveThread.class.getName()).log(
                        Level.SEVERE, null, e);
            }
            //Write to document
            XmlConfiguration.isWriting = false;
            XmlConfiguration.XML_FILE.notifyAll();

        }
    }
}
