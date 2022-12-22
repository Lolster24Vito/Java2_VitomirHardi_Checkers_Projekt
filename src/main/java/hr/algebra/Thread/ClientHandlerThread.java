package hr.algebra.Thread;

import hr.algebra.java2_vitomirhardi_checkers_projekt.Online.LoginMessage;
import hr.algebra.server.Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class ClientHandlerThread implements Runnable {

    private Socket socket;


    public final int PORT_NUMBER=1987;

    public ClientHandlerThread(Socket clientSocket) {
        this.socket = clientSocket;

    }

    @Override
    public void run() {


                try (
                     ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())
                ) {
                    //oos.flush();
                    LoginMessage loginMessage = (LoginMessage) ois.readObject();
                    System.out.println("Received new game state DTO with the name: " + loginMessage.getUsername());
                   oos.writeObject(new LoginMessage("Login success","blamaaaa"));
                    System.out.println("Server sent login success message to " + loginMessage.getUsername());
                }
                catch(IOException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }

    }

