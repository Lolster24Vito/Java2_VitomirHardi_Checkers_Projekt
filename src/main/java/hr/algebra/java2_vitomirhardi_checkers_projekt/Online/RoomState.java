package hr.algebra.java2_vitomirhardi_checkers_projekt.Online;

import java.io.Serializable;

public enum RoomState implements Serializable
{
    NotExists,
    ExistsAndWaitingForPlayers,
    ExistsAndEnoughPlayers,
}
