package hr.algebra.java2_vitomirhardi_checkers_projekt;



import hr.algebra.java2_vitomirhardi_checkers_projekt.models.PlayerMove;

import java.util.ArrayList;
import java.util.List;

public class TurnManager {
    List<PlayerMove> moves=new ArrayList<>();
    public void AddMove(PlayerMove move){
        moves.add(move);
    }

    public List<PlayerMove> getMoves() {
        return moves;
    }
}
