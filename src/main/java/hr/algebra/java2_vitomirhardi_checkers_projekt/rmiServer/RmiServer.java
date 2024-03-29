package hr.algebra.java2_vitomirhardi_checkers_projekt.rmiServer;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RmiServer {
    private static final int RANDOM_PORT_HINT=0;
    private static final int RMI_PORT=1099;


    public static void Init(){

        try {
            Registry registry = LocateRegistry.createRegistry(RMI_PORT);
            ChatService chatService = new ChatServiceImpl();
            ChatService skeleton = (ChatService) UnicastRemoteObject.exportObject(chatService, RANDOM_PORT_HINT);
            registry.rebind(ChatService.REMOTE_OBJECT_NAME, skeleton);
            System.err.println("Object registered in RMI registry");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

}
