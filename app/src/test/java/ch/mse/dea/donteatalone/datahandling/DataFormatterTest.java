package ch.mse.dea.donteatalone.datahandling;

import org.joda.time.DateTime;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DataFormatterTest {

    DateTime dateTime = new DateTime(2018, 11, 07, 17, 19, 40, 10);

    @Test
    public void getTimeAsString() {
        assertEquals("17:19", DataFormatter.getTimeAsString(dateTime));
    }

    @Test
    public void getTimeAsString1() {
        assertEquals("17:19", DataFormatter.getTimeAsString(17, 19));
    }

    @Test
    public void getDateAsString() {
        assertEquals("", DataFormatter.getDateAsString(dateTime, "sljdakgk"));
        assertEquals("07.11.18", DataFormatter.getDateAsString(dateTime, "short"));
        assertEquals("07.11.2018", DataFormatter.getDateAsString(dateTime, "normal"));
        assertEquals("Wednesday, 07.11.2018", DataFormatter.getDateAsString(dateTime, "long"));
    }

    @Test
    public void getDateAsString1() {
        assertEquals("", DataFormatter.getDateAsString(2018, 11, 07, "sljdakgk"));
        assertEquals("07.11.18", DataFormatter.getDateAsString(2018, 11, 07,  "short"));
        assertEquals("07.11.2018", DataFormatter.getDateAsString(2018, 11, 07,  "normal"));
        assertEquals("Wednesday, 07.11.2018", DataFormatter.getDateAsString(2018, 11, 07,  "long"));
    }

    @Test
    public void getDateFromString() {
        assertNull(DataFormatter.getDateFromString(dateTime.toString(), "sljdakgk"));
    }

    @Test
    public void getDateTimeFromString() {
        assertNull(DataFormatter.getDateFromString(dateTime.toString(), "sljdakgk"));
    }


}