package hr.algebra.java2_vitomirhardi_checkers_projekt.rmiServer;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class ChatServiceImpl implements ChatService {

    List<String> chatHistoryMessageList;

    public ChatServiceImpl() {
        chatHistoryMessageList=new ArrayList<>();
    }

    @Override
    public void sendMessage(String message) throws RemoteException {
        chatHistoryMessageList.add(message);
    }

    @Override
    public List<String> getChatHistory() throws RemoteException {
        return chatHistoryMessageList;
    }
}
