package hr.algebra.Utils;

public final class TimerUtils {
    private TimerUtils(){}
    public static String secondsToFormat(long totalSeconds){
      long hours = totalSeconds / 3600;
       long minutes = (totalSeconds % 3600) / 60;
       long seconds = totalSeconds % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
