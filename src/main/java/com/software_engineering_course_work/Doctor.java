package com.software_engineering_course_work;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.List;

/**
 * POJO-class for storing data about particular doctor.
 * @author Linenko Kostyantyn
 * @version 1.0
 */
public class Doctor {

    /** An observable list of class instances needed for further user in the interface. */
    private static final ObservableList<String> DOCTORS = FXCollections.observableArrayList();
    /** Full name. */
    private final String name;
    /** A type of specialization. */
    private final String specialization;
    /** The gender of a doctor. */
    private final Sex sex;
    /** A collection where each day of week corresponds to a work schedule. */
    private final HashMap<DayOfWeek, List<Time>> schedule;

    /**
     * Constructor, that have a direct access the database to retrieve a work schedule of particular doctor.
     * @param name name
     * @param specialization specialization
     * @param sex gender
     * @throws SQLException an exception that provides information on a database access error or other errors.
     * @see DBConnect
     * @see Executor
     */
    public Doctor(String name, String specialization, String sex) throws SQLException {
        this.name = name;
        this.specialization = specialization;
        this.sex = sex.equals("чоловічий") ? Sex.MALE : Sex.FEMALE;

        DBConnect dbConnect = DBConnect.getInstance("jdbc:mysql://localhost:3306/hospital_reception?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",
                "root",
                "root_bd2022");
        Statement statement = dbConnect.getStatement();
        schedule = dbConnect.getExecutor().getScheduleOfDoctor(statement, name);
    }

    /**
     * Returns the list of doctor's names of particular specialization.
     * @param executor executor
     * @param statement statement
     * @param specialization specialization
     * @return the list of doctor's names
     * @see Executor
     * @throws SQLException an exception that provides information on a database access error or other errors.
     */
    public static ObservableList<String> getDoctorsBySpecialization(Executor executor, Statement statement, String specialization) throws SQLException {
        if (!DOCTORS.isEmpty()) {
            DOCTORS.clear();
        }

        ResultSet set = executor.getDoctorsBySpecialization(statement, specialization);
        while (set.next()) {
            DOCTORS.add(set.getString(1));
        }

        return DOCTORS;
    }

    /**
     * Returns the list of doctor's names particular specialization and hospital.
     * @param executor executor
     * @param statement statement
     * @param specialization specialization
     * @param hospital hospital
     * @return the list of doctor's names
     * @see Executor
     * @throws SQLException an exception that provides information on a database access error or other errors.
     */
    public static ObservableList<String> getDoctorsBySpecializationAndHospital(Executor executor, Statement statement, String specialization, String hospital) throws SQLException {
        if (!DOCTORS.isEmpty()) {
            DOCTORS.clear();
        }

        ResultSet set = executor.getDoctorsBySpecializationAndHospital(statement, specialization, hospital);
        while (set.next()) {
            DOCTORS.add(set.getString(1));
        }

        return DOCTORS;
    }

    /**
     * Returns a string representation of this class.
     * @return the full name
     */
    @Override
    public String toString() {
        return name;
    }

    /**
     * Retrieves the full name of a doctor.
     * @return the full name
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the specialization of a doctor.
     * @return the specialization
     */
    public String getSpecialization() {
        return specialization;
    }

    /**
     * Retrieves the gender of a doctor.
     * @return the gender
     */
    public Sex getSex() {
        return sex;
    }

    /**
     * Retrieves a work schedule of a doctor.
     * @return collection where each day of week corresponds to a work schedule
     */
    public HashMap<DayOfWeek, List<Time>> getSchedule() {
        return schedule;
    }
}
