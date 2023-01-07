package hr.algebra.java2_vitomirhardi_checkers_projekt.rmiServer;

import javafx.collections.ObservableList;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ChatService extends Remote {
    String REMOTE_OBJECT_NAME = "hr.algebra.rmi.service";

    void sendMessage(String user,String message) throws RemoteException;
    List<String> getChatHistory() throws RemoteException;
    int getChatSize() throws RemoteException;
}
