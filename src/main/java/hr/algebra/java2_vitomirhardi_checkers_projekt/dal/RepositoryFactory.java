package hr.algebra.java2_vitomirhardi_checkers_projekt.dal;

public class RepositoryFactory {

    private static LeaderboardRepository instance;

    private RepositoryFactory(){}

    public static LeaderboardRepository getLeaderboardRepository(){
        if(instance == null){
            instance = new LeaderboardFileRepository();
        }
        return instance;
    }
}
