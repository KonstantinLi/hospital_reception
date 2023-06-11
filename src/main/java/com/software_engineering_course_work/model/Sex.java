package com.software_engineering_course_work.model;

/**
 * Enum that contains two types <strong>MALE</strong> and <strong>FEMALE</strong> of gender.
 */
public enum Sex {
    /** Male */
    MALE("чоловічий"),
    /** Female */
    FEMALE("жіночий");

    /** The string representation of constant. */
    private final String text;

    /**
     * The lone constructor.
     * @param text text
     */
    Sex(String text) {
        this.text = text;
    }

    /**
     * Return a string representation of constant.
     * @return a string representation
     */
    public String getText() {
        return text;
    }
}
