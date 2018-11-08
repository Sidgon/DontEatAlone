package ch.mse.dea.donteatalone.datahandling;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Locale;

public class DataFormatter {

    private static DateTimeFormatter timeFormatter = DateTimeFormat.forPattern("HH:mm").withLocale(Locale.getDefault());
    private static DateTimeFormatter dateFormatterLong = DateTimeFormat.forPattern("EEEE, dd.MM.yyyy").withLocale(Locale.getDefault());
    private static DateTimeFormatter dateFormatterNormal = DateTimeFormat.forPattern("dd.MM.yyyy").withLocale(Locale.getDefault());
    private static DateTimeFormatter dateFormatterShort = DateTimeFormat.forPattern("dd.MM.yy").withLocale(Locale.getDefault());

    private static final String LONG= "long";
    private static final String NORMAL= "normal";
    private static final String SHORT= "short";

    private DataFormatter() {
    }

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
            case LONG: return dateFormatterLong.print(dateTime);
            case NORMAL: return dateFormatterNormal.print(dateTime);
            case SHORT: return dateFormatterShort.print(dateTime);
            default:
                break;
        }

        return "";
    }


    public static String getDateAsString(int year, int monthOfYear,int dayOfMonth,String dateType){
        DateTime date= DateTime.now().withYear(year).withMonthOfYear(monthOfYear).withDayOfMonth(dayOfMonth);
        return getDateAsString(date,dateType);
    }

    public static DateTime getDateFromString(String date,String dateType){
        switch (dateType){
            case LONG: return dateFormatterLong.parseDateTime(date);
            case NORMAL: return dateFormatterNormal.parseDateTime(date);
            case SHORT: return dateFormatterShort.parseDateTime(date);
            default:
                break;
        }

        return null;
    }

    public static DateTime getDateTimeFromString(String date,String time,String dateType){
        DateTime dateTime=null;
        switch (dateType){
            case LONG: dateTime= dateFormatterLong.parseDateTime(date);break;
            case NORMAL: dateTime= dateFormatterNormal.parseDateTime(date);break;
            case SHORT: dateTime= dateFormatterShort.parseDateTime(date);break;
            default:
                break;
        }

        DateTime time1=getTimeFromString(time);

        if (dateTime!=null) {
            return dateTime.withHourOfDay(time1.getHourOfDay()).withMinuteOfHour(time1.getMinuteOfHour());
        }

        return null;
    }


}
