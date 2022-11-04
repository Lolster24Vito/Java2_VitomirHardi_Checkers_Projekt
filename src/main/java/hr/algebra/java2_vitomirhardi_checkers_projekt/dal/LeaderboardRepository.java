package hr.algebra.java2_vitomirhardi_checkers_projekt.dal;

import hr.algebra.java2_vitomirhardi_checkers_projekt.LeaderboardResult;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface LeaderboardRepository {
     List<LeaderboardResult> getLeaderboardResults() throws IOException, ClassNotFoundException;
     void setLeaderboardResult(LeaderboardResult leaderboardResult) throws IOException, ClassNotFoundException;
}
