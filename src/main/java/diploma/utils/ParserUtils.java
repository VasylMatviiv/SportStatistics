package diploma.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class ParserUtils {
    public static LocalDateTime parseDate(StringBuilder time) {
        String pattern="yyyy dd/MM HH:mm";
        time.insert(0, Calendar.getInstance().get(Calendar.YEAR)+" ");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime date = LocalDateTime.parse(time, formatter);

        return date;
    }
}
