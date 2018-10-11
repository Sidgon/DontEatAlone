package ch.mse.dea.donteatalone.DataHandling;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Locale;

public class DataFormatter {

    public static DateTimeFormatter timeFormatter = DateTimeFormat.forPattern("HH:mm").withLocale(Locale.getDefault());
    public static DateTimeFormatter dateFormatterLong = DateTimeFormat.forPattern("EEEE, dd.MM.yyyy").withLocale(Locale.getDefault());
    public static DateTimeFormatter dateFormatterNormal = DateTimeFormat.forPattern("dd.MM.yyyy").withLocale(Locale.getDefault());
    public static DateTimeFormatter dateFormatterShort = DateTimeFormat.forPattern("dd.MM.yy").withLocale(Locale.getDefault());

    public static String getTimeAsString(DateTime dateTime){
        return timeFormatter.print(dateTime);
    }

    public static String getTimeAsString(int hourOfDay, int minute){
        DateTime date= DateTime.now().withHourOfDay(hourOfDay).withMinuteOfHour(minute);

        return getTimeAsString(date);
    }

    public static DateTime getTimeFromString(String time) {
        return timeFormatter.parseDateTime(time);
    }

    public static String getDateAsString(DateTime dateTime,String dateType){
        switch (dateType){
            case "long": return dateFormatterLong.print(dateTime);
            case "normal": return dateFormatterNormal.print(dateTime);
            case "short": return dateFormatterShort.print(dateTime);
        }

        return "";
    }

    public static DateTime getDateFromString(String date,String dateType){
        switch (dateType){
            case "long": return dateFormatterLong.parseDateTime(date);
            case "normal": return dateFormatterNormal.parseDateTime(date);
            case "short": return dateFormatterShort.parseDateTime(date);
        }

        return null;
    }

    public static DateTime getDateTimeFromString(String date,String time,String dateType){
        DateTime dateTime=null;
        switch (dateType){
            case "long": dateTime= dateFormatterLong.parseDateTime(date);break;
            case "normal": dateTime= dateFormatterNormal.parseDateTime(date);break;
            case "short": dateTime= dateFormatterShort.parseDateTime(date);break;
        }

        DateTime time1=getTimeFromString(time);

        if (dateTime!=null) {
            return dateTime.withHourOfDay(time1.getHourOfDay()).withMinuteOfHour(time1.getMinuteOfHour());
        }

        return null;
    }



    public static String getDateAsString(int year, int monthOfYear,int dayOfMonth,String dateType){
        DateTime date= DateTime.now().withYear(year).withMonthOfYear(monthOfYear).withDayOfMonth(dayOfMonth);
        return getDateAsString(date,dateType);
    }

}
