package hr.algebra.java2_vitomirhardi_checkers_projekt.rmiServer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class ChatServiceImpl implements ChatService {

   List<String> chatHistoryMessageList;


    public ChatServiceImpl() {
        chatHistoryMessageList=new ArrayList<>();
    }

    @Override
    public void sendMessage(String user,String message) throws RemoteException {
        chatHistoryMessageList.add(user+">"+message);
    }

    @Override
    public List<String> getChatHistory() throws RemoteException {
        return chatHistoryMessageList;
    }

    @Override
    public int getChatSize() throws RemoteException {
return chatHistoryMessageList.size();
    }
}
