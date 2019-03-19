package diploma.utils;

import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Calendar;

import static org.junit.Assert.*;

public class ParseDateTest {
    @Test
    public void testParseDate_shouldReturnLocalDateTime(){
        StringBuilder time=new StringBuilder("11/05 11:34");
        LocalDateTime date= ParserUtils.parseDate(time);

        assertEquals(LocalDateTime.of(Calendar.getInstance().get(Calendar.YEAR),5,11,11,34),date);
    }

}