package com.software_engineering_course_work;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * POJO-class for storing data about particular hospital.
 * @author Linenko Kostyantyn
 * @version 1.0
 */
public class Hospital {
    /** List of {@link Hospital} objects/ */
    private static final ObservableList<Hospital> HOSPITALS = FXCollections.observableArrayList();
    /** The name of the hospital. */
    private final String name;
    /** The name of city where the hospital is located. */
    private final String city;
    /** The file's name of image where the hospital is shown. */
    private final String image;

    /**
     * The lone constructor.
     * @param name name
     * @param city city
     * @param image image
     */
    public Hospital(String name, String city, String image) {
        this.name = name;
        this.city = city;
        this.image = image;
    }

    /**
     * Returns all {@link Hospital} objects that accessible in the database.
     * @param executor executor
     * @param statement statement
     * @return {@link ObservableList}
     * @see Executor
     * @throws SQLException an exception that provides information on a database access error or other errors.
     */
    public static ObservableList<Hospital> getHospitalList(Executor executor, Statement statement) throws SQLException {
        if (!HOSPITALS.isEmpty()) {
            HOSPITALS.clear();
        }

        ResultSet set = executor.getHospitals(statement);
        while (set.next()) {
            HOSPITALS.add(new Hospital(set.getString("name"),
                    set.getString("city"),
                    set.getString("image")));
        }

        return HOSPITALS;
    }

    /**
     * Returns the name of the hospital.
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the city where the hospital is located.
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * Returns the file's name of image where the hospital is shown.
     * @return the name of a file
     */
    public String getImage() {
        return image;
    }

    /**
     * Returns a string representation of the object.
     * @return a string representation.
     */
    @Override
    public String toString() {
        return name;
    }
}
