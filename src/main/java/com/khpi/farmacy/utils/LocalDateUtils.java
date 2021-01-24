package com.khpi.farmacy.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

/**
 * Current class have utility methods for LocalDate class
 */
public final class LocalDateUtils {

    private LocalDateUtils() {
    }

    private static final List<String> DATE_PATTERN_LIST = Arrays.asList(
            "dd-MMM-yyyy", "dd/MMM/yyyy", "dd:MMM:yyyy", "dd MMM yyyy",
            "dd-mm-yyyy", "dd/mm/yyyy", "dd:mm:yyyy", "dd mm yyyy",
            "dd-MMM-yy", "dd/MMM/yy", "dd:MMM:yy", "dd MMM yy",
            "DDD-MMM-yyyy", "DDD/MMM/yyyy", "DDD:MMM:yyyy", "DDD MMM yyyy",
            "DDD-mm-yyyy", "DDD/mm/yyyy", "DDD:mm:yyyy", "DDD mm yyyy");

    public static boolean isDateParsable(String date) {
        for (String datePattern : DATE_PATTERN_LIST) {
            if(isValidDatePattern(date, datePattern)) {
                return true;
            }
        }
        return false;
    }

    public static String getProperDatePattern(String date) {
        for (String datePattern : DATE_PATTERN_LIST) {
            if(isValidDatePattern(date, datePattern)) {
                return datePattern;
            }
        }
        throw new RuntimeException("Couldn't find proper date pattern");
    }

    public static boolean isValidDatePattern(String date, String datePattern) {
        try {
            LocalDate.parse(date, DateTimeFormatter.ofPattern(datePattern));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static LocalDate blackBoxParse(String date) {
        String datePattern = getProperDatePattern(date);
        return LocalDate.parse(date, DateTimeFormatter.ofPattern(datePattern));
    }
}
