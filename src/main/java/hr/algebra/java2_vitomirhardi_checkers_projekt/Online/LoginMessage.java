package hr.algebra.java2_vitomirhardi_checkers_projekt.Online;

import javafx.scene.control.TextField;

import java.io.*;

public class LoginMessage implements Externalizable {
    private static final long serialVersionUID = 5L;

    private String username;
    private String roomCode;

    public String getRoomCode() {
        return roomCode;
    }

    public String getUsername() {
        return username;
    }

    public LoginMessage(String username,String roomCode)
    {
    this.username=username;
    this.roomCode=roomCode;
    }
    public LoginMessage(){}

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeUTF(username);
        out.writeUTF(roomCode);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    username=in.readUTF();
    roomCode=in.readUTF();
    }
}
