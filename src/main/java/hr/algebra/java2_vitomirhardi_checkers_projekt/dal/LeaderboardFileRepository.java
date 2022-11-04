package hr.algebra.java2_vitomirhardi_checkers_projekt.dal;

import hr.algebra.java2_vitomirhardi_checkers_projekt.LeaderboardResult;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LeaderboardFileRepository implements LeaderboardRepository{

    private final String FILE_NAME="Leaderboard.ser";
    @Override
    public List<LeaderboardResult> getLeaderboardResults() throws IOException, ClassNotFoundException {
        List<LeaderboardResult> leaderboardResults=new ArrayList<>();
        try (ObjectInputStream deserializator = new ObjectInputStream((
                new FileInputStream(FILE_NAME)
        ))) {
             leaderboardResults = (List<LeaderboardResult>) deserializator.readObject();
        }
        catch (FileNotFoundException e){
            return leaderboardResults;
        }
        return  leaderboardResults;






    }

    @Override
    public void setLeaderboardResult(LeaderboardResult leaderboardResult) throws IOException, ClassNotFoundException {
        List<LeaderboardResult> leaderboardResults=getLeaderboardResults();
        leaderboardResults.add(leaderboardResult);
        try (ObjectOutputStream serializator = new ObjectOutputStream(
                new FileOutputStream(FILE_NAME))) {
            serializator.writeObject(leaderboardResults);
        }
    }
}
