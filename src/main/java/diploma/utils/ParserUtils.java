package diploma.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.StringTokenizer;

public class ParserUtils {
    public static LocalDateTime parseDate(StringBuilder time) {
        String pattern = "yyyy dd/MM HH:mm";
        time.insert(0, Calendar.getInstance().get(Calendar.YEAR) + " ");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime date = LocalDateTime.parse(time, formatter);

        return date;
    }

    public static double convertDouble(String number) {
        double result;
        try {
            result = Double.valueOf(number);
        } catch (NumberFormatException e) {
            result = 0;
        }
        return result;
    }

    public static String parseTitle(String title) {
        StringBuilder result = new StringBuilder();
        StringTokenizer tokenizer = new StringTokenizer(title, ".");

        for (; tokenizer.hasMoreTokens(); ) {
            String token = tokenizer.nextToken();
            if (!(token.contains("Make a bet online on Parimatch") || token.contains("com")))
                result.append(token + ".");
        }
        return result.toString();
    }

}
