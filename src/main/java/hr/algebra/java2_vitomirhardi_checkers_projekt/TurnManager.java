package hr.algebra.java2_vitomirhardi_checkers_projekt;



import hr.algebra.java2_vitomirhardi_checkers_projekt.models.PlayerMove;
import hr.algebra.java2_vitomirhardi_checkers_projekt.xml.Thread.ReadXmlThread;
import hr.algebra.java2_vitomirhardi_checkers_projekt.xml.Thread.WriteXmlMoveThread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TurnManager {

    ExecutorService executorService;
    List<PlayerMove> moves=new ArrayList<>();


    public synchronized void addMove(PlayerMove move){
        moves.add(move);
        executorService.execute(new WriteXmlMoveThread(move));
    }
    public TurnManager(){
        executorService = Executors.newCachedThreadPool();
    }


    public  List<PlayerMove> getMoves() throws ExecutionException, InterruptedException {
        Future<List<PlayerMove>> moves;
             moves= executorService.submit(new ReadXmlThread());
             return moves.get();

    }


}
