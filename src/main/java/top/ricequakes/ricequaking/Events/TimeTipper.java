package top.ricequakes.ricequaking.Events;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TimeTipper {
    public static String getTime() {
        LocalTime time = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return time.format(formatter);
    }
}
