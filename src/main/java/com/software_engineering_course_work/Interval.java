package com.software_engineering_course_work;

import java.sql.Time;

/**
 * Enum that contains all accessible intervals to record patients to doctors.
 * @author Linenko Kostyantyn
 * @version 1.0
 */
public enum Interval {
    /** The singleton instance for the interval of 8:00 - 8:30. */
    FIRST(1, "8:00 - 8:30"),
    /** The singleton instance for the interval of 8:30 - 9:00. */
    SECOND(2, "8:30 - 9:00"),
    /** The singleton instance for the interval of 9:00 - 9:30. */
    THIRD(3, "9:00 - 9:30"),
    /** The singleton instance for the interval of 9:30 - 10:00. */
    FOURTH(4, "9:30 - 10:00"),
    /** The singleton instance for the interval of 10:00 - 10:30. */
    FIFTH(5, "10:00 - 10:30"),
    /** The singleton instance for the interval of 10:30 - 11:00. */
    SIXTH(6, "10:30 - 11:00"),
    /** The singleton instance for the interval of 11:00 - 11:30. */
    SEVENTH(7, "11:00 - 11:30"),
    /** The singleton instance for the interval of 11:30 - 12:00. */
    EIGHTH(8, "11:30 - 12:00");

    /** Array of all values in enum */
    private static Interval[] ENUMS = Interval.values();
    /** The number of the constant in the range of values */
    private final int number;
    /** A string representation of specified interval. */
    private final String time;

    /**
     * The lone constructor.
     * @param number number
     * @param time time
     */
    Interval(int number, String time) {
        this.number = number;
        this.time = time;
    }

    /**
     * Returns a constant with appropriated number in array <strong>ENUMS</strong>
     * @param number number
     * @return a constant of enum
     */
    public static Interval of (int number) {
        return ENUMS[number - 1];
    }

    /**
     * Returns the array of all values.
     * @return the array of all values
     */
    public static Interval[] getIntervals() {
        return ENUMS;
    }

    /**
     * Return the number of interval in enum by time in this constant.
     * If the time doesn't suit any interval, method return <strong>-1</strong>.
     * @param time time
     * @see Time
     * @return the number of interval, else <strong>-1</strong>
     */
    public static int getIntervalNumberByTime(Time time) {
        for (Interval interval : ENUMS) {
            String strInterval = interval.getTime();
            int hyphenIndex = strInterval.indexOf('-');

            Time start = Time.valueOf(strInterval.substring(0, hyphenIndex - 1).concat(":00"));
            Time end = Time.valueOf(strInterval.substring(hyphenIndex + 2).concat(":00"));

            if (time.after(start) && time.before(end) || time.compareTo(start) == 0 || time.compareTo(end) == 0) {
                return interval.getNumber();
            }
        }

        return -1;
    }

    /**
     * Returns the number of the constant in enum.
     * @return the number of the constant
     */
    public int getNumber() {
        return number;
    }

    /**
     * Returns the string representation of interval.
     * @return the string representation of interval
     */
    public String getTime() {
        return time;
    }
}