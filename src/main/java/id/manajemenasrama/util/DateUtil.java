package id.manajemenasrama.util;

import java.time.LocalDate;

public class DateUtil {
    public static int getDayOfWeek(LocalDate date) {
        return date.getDayOfWeek().getValue();
    }

}
