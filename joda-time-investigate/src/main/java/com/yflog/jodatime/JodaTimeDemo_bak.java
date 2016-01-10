package com.yflog.jodatime;

import org.joda.time.DateTime;
import org.joda.time.format.*;


/**
 * Created by vincent on 1/10/16.
 */
public class JodaTimeDemo_bak {

    private static final String TEMPLATE = "2016-01-10T11:00:33.183+08:00";

    private static final DateTimeParser[] parsers = {

            DateTimeFormat.forPattern("yyyy-MM-ddHH:mm:ss.SSSZZZ").getParser(),
            DateTimeFormat.forPattern("yyyy-MM-ddHH:mm:ss.SSS").getParser(),
            DateTimeFormat.forPattern("yyyy-MM-ddHH:mm:ss").getParser(),
            // All ISO8601 formats (use 'T' as separator)
            ISODateTimeFormat.dateTimeParser().withOffsetParsed().getParser(),

            // Fri Jun 07 09:27:37 GMT 2015
            DateTimeFormat.forPattern("EEE MMM dd HH:mm:ss zzz yyyy").getParser(),
            // Wed, 09 Feb 1994 22:23:32 GMT
            DateTimeFormat.forPattern("EEE, dd MMM yyyy HH:mm:ss zzz").getParser(),
            // Thu Feb 3 00:00:00 1994
            DateTimeFormat.forPattern("EEE MMM dd HH:mm:ss yyyy").getParser(),
            // Tuesday, 08-Feb-94 14:15:29 GMT
            DateTimeFormat.forPattern("EEEE, dd-MMM-yy HH:mm:ss zzz").getParser(),
            // Tuesday, 08-Feb-1994 14:15:29 GMT
            DateTimeFormat.forPattern("EEEE, dd-MMM-yyyy HH:mm:ss zzz").getParser(),
            //  common logfile format 03/Feb/1994:17:03:55 -0700
            DateTimeFormat.forPattern("dd/MMM/yyy:HH:mm:ss Z").getParser(),
            //  HTTP format (no weekday)  09 Feb 1994 22:23:32 GMT
            DateTimeFormat.forPattern("dd MMM yyyy HH:mm:ss zzz").getParser(),
            // rfc850 format (no weekday) 08-Feb-94 14:15:29 GMT
            DateTimeFormat.forPattern("dd-MMM-yy HH:mm:ss zzz").getParser(),
            // broken rfc850 format (no weekday) 08-Feb-1994 14:15:29 GMT
            DateTimeFormat.forPattern("dd-MMM-yyyy HH:mm:ss zzz").getParser()
    };


    public static void main(String[] args) {
//        testISO8601_2();
        String time = "Thu Feb 03 17:03:55 CST 1994";
        DateTimeFormatter fmtter = new DateTimeFormatterBuilder().append(null, parsers).toFormatter();
        DateTime dt = DateTime.parse(time, new DateTimeFormatterBuilder().append(null, parsers).toFormatter());
        System.out.println(dt.toString());

        System.out.println(fmtter.parseDateTime(time).toString());

        testHttpTime();
    }

    private static void testHttpTime() {
        String time = "Fri Jun 07 09:27:37 CST 2015";
        DateTimeFormatter fmtter = new DateTimeFormatterBuilder().append(null, parsers).toFormatter();
        System.out.println(fmtter.parseDateTime(time));
    }

    private static void testAllHttpTime() {

    }

    private static void testISOParsers() {
        DateTimeFormatter timeElement = timeElement();
        DateTimeFormatter offsetElement = offsetElement();
        DateTimeParser time = new DateTimeFormatterBuilder()
                .appendLiteral('T')
                .append(timeElement.getParser())
                .appendOptional(offsetElement.getParser())
                .toParser();


        DateTimeParser dateOptionalTimeParser = dateOptionalTimeParser().getParser();
        DateTimeFormatter dateTimeFormatter = new DateTimeFormatterBuilder()
                .append(null, new DateTimeParser[]{
                        time,
                        dateOptionalTimeParser
                }).toFormatter();
    }

    private static DateTimeFormatter dateOptionalTimeParser() {
        DateTimeParser timeOrOffset = new DateTimeFormatterBuilder()
                .appendLiteral('T')
                .appendOptional(timeElement().getParser())
                .appendOptional(offsetElement().getParser())
                .toParser();

        return new DateTimeFormatterBuilder()/*.append(dateElementParser())*/
                .appendOptional(timeOrOffset)
                .toFormatter();
    }

    private static DateTimeFormatter offsetElement() {
        return new DateTimeFormatterBuilder()
                .appendTimeZoneOffset("Z", true, 2, 4).toFormatter();
    }

    private static DateTimeFormatter timeElement() {
        DateTimeParser decimalPoint = new DateTimeFormatterBuilder()
                .append(null, new DateTimeParser[]{
                        new DateTimeFormatterBuilder().appendLiteral('.').toParser(),
                        new DateTimeFormatterBuilder().appendLiteral(',').toParser()
                })
                .toParser();



        return new DateTimeFormatterBuilder()
                // time-element
                .append(hourElement())
                .append
                        (null, new DateTimeParser[] {
                                new DateTimeFormatterBuilder()
                                        // minute-element
                                        .append(minuteElement())
                                        .append
                                                (null, new DateTimeParser[] {
                                                        new DateTimeFormatterBuilder()
                                                                // second-element
                                                                .append(secondElement())
                                                                        // second fraction
                                                                .appendOptional(new DateTimeFormatterBuilder()
                                                                        .append(decimalPoint)
                                                                        .appendFractionOfSecond(1, 9)
                                                                        .toParser())
                                                                .toParser(),
                                                        // minute fraction
                                                        new DateTimeFormatterBuilder()
                                                                .append(decimalPoint)
                                                                .appendFractionOfMinute(1, 9)
                                                                .toParser(),
                                                        null
                                                })
                                        .toParser(),
                                // hour fraction
                                new DateTimeFormatterBuilder()
                                        .append(decimalPoint)
                                        .appendFractionOfHour(1, 9)
                                        .toParser(),
                                null
                        })
                .toFormatter();
    }

    private static DateTimeFormatter dateTimeFormatter() {
        return new DateTimeFormatterBuilder()
                .append(null, new DateTimeParser[] {
                        new DateTimeFormatterBuilder()
                                .append(yearElement())
                                .appendOptional
                                        (new DateTimeFormatterBuilder()
                                                .append(monthElement())
                                                .appendOptional(dayOfMonthElement().getParser())
                                                .toParser())
                                .toParser(),
                        new DateTimeFormatterBuilder()
                                .append(weekyearElement())
                                .append(weekElement())
                                .appendOptional(dayOfWeekElement().getParser())
                                .toParser(),
                        new DateTimeFormatterBuilder()
                                .append(yearElement())
                                .append(dayOfYearElement())
                                .toParser()
                })
                .toFormatter();
    }

    private static DateTimeFormatter yearElement() {
        return new DateTimeFormatterBuilder()
                .appendYear(4, 9)
                .toFormatter();
    }

    private static DateTimeFormatter monthElement() {
        return new DateTimeFormatterBuilder()
                .appendLiteral('-')
                .appendMonthOfYear(2)
                .toFormatter();
    }

    private static DateTimeFormatter weekyearElement() {
        return new DateTimeFormatterBuilder()
                .appendWeekyear(4, 9)
                .toFormatter();
    }

    private static DateTimeFormatter weekElement() {
        return new DateTimeFormatterBuilder()
                .appendLiteral("-W")
                .appendWeekOfWeekyear(2)
                .toFormatter();
    }

    private static DateTimeFormatter dayOfWeekElement() {
        return new DateTimeFormatterBuilder()
                .appendLiteral('-')
                .appendDayOfWeek(1)
                .toFormatter();
    }

    private static DateTimeFormatter dayOfYearElement() {
        return new DateTimeFormatterBuilder()
                .appendLiteral('-')
                .appendDayOfYear(3)
                .toFormatter();
    }

    private static DateTimeFormatter dayOfMonthElement() {
        return new DateTimeFormatterBuilder().toFormatter();
    }

    private static DateTimeFormatter hourElement() {
        return new DateTimeFormatterBuilder().appendHourOfDay(2).toFormatter();
    }

    private static DateTimeFormatter minuteElement() {
        return new DateTimeFormatterBuilder().appendLiteral(':').appendMinuteOfHour(2).toFormatter();
    }

    private static DateTimeFormatter secondElement() {
        return new DateTimeFormatterBuilder().appendLiteral(':').appendSecondOfMinute(2).toFormatter();
    }

    private static void testISO8601_2() {
        DateTimeParser[] parsers = {
                ISODateTimeFormat.dateTimeParser().withOffsetParsed().getParser()
        };

        String standard = "2016-01-10T11:00:33.183+08:00";
        String noTimzeZone = "2016-01-10T11:00:33.183";
        String noMilliSeconds = "2016-01-10T11:00:33";

        String noTAsSeperator = "2016-01-10 11:00:33.183+08:00";
        String noTTimzeZone = "2016-01-10 11:00:33.183";
        String noTMilliSeconds = "2016-01-10 11:00:33";

        DateTimeFormatter fmtter = new DateTimeFormatterBuilder().append(null, parsers).toFormatter();


//        System.out.println((fmtter.parseDateTime(standard)).toString());
//        System.out.println((fmtter.parseDateTime(noTimzeZone)).toString());
//        System.out.println((fmtter.parseDateTime(noMilliSeconds)).toString());

//        System.out.println((fmtter.parseDateTime(noTAsSeperator)).toString());
//        System.out.println((fmtter.parseDateTime(noTTimzeZone)).toString());
        System.out.println((fmtter.parseDateTime(noTMilliSeconds)).toString());

    }

    private static void testISO8601() {
        // T as separator; milliseconds; timezone


        DateTimeFormatter fmtter = new DateTimeFormatterBuilder().append(null, parsers).toFormatter();

        String standard = "2016-01-10T11:00:33.183+08:00";
        String noTimzeZone = "2016-01-10T11:00:33.183";
        String noMilliSeconds = "2016-01-10T11:00:33";

        DateTime time = new DateTime(standard);
        System.out.println(time.toString());
        System.out.println((new DateTime(noTimzeZone)).toString());
        System.out.println((new DateTime(noMilliSeconds)).toString());

        System.out.println((fmtter.parseDateTime(standard)).toString());
        System.out.println((fmtter.parseDateTime(noTimzeZone)).toString());
        System.out.println((fmtter.parseDateTime(noMilliSeconds)).toString());
    }
}
