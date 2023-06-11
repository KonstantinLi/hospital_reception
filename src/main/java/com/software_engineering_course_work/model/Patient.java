package com.software_engineering_course_work.model;

import com.software_engineering_course_work.database.Executor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * POJO-class for storing data about particular patient.
 * @author Linenko Kostyantyn
 * @version 1.0
 */
public class Patient {
    /** Contains all patients. */
    private static final ObservableList<Patient> PATIENTS = FXCollections.observableArrayList();
    /** Contains patients who matches with filter. */
    private static final ObservableList<Patient> PATIENTS_BY_FILTER = FXCollections.observableArrayList();
    /** The <strong>id</strong> of the patient */
    private final int id;
    /** The <strong>full name</strong> of the patient */
    private final String name;
    /** The <strong>phone number</strong> of the patient */
    private final String phone;
    /** The <strong>address</strong> of the patient */
    private final String address;
    /** The <strong>email</strong> of the patient */
    private final String email;
    /** The <strong>date of birthday</strong> of the patient */
    private final Date dateBirth;
    /** The <strong>gender</strong> of the patient */
    private final Sex sex;

    /**
     * The lone constructor.
     * @param id id
     * @param name name
     * @param phone phone number
     * @param address address
     * @param email email
     * @param dateBirth date of birthday
     * @param sex gender
     * @throws DateTimeParseException an exception thrown when an error occurs during parsing.
     */
    public Patient(int id, String name, String phone, String address, String email, Date dateBirth, Sex sex) throws DateTimeParseException {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.email = email;
        this.dateBirth = dateBirth;
        this.sex = sex;
    }

    /**
     * Returns the {@link Patient} object by id.
     * @param executor executor
     * @param statement statement
     * @param id id
     * @return {@link Patient} or null
     * @throws SQLException an exception that provides information on a database access error or other errors.
     */
    public static Patient getPatientById(Executor executor, Statement statement, int id) throws SQLException {
        ResultSet set = executor.getPatientById(statement, id);
        if (set.next()) {
            return new Patient(set.getInt("id"),
                    set.getString("name"),
                    set.getString("phone"),
                    set.getString("address"),
                    set.getString("email"),
                    set.getDate("birthday"),
                    set.getString("sex").equals("чоловічий") ? Sex.MALE : Sex.FEMALE);
        }
        return null;
    }

    /**
     * Returns a list of all patients in the database.
     * @param executor executor
     * @param statement statement
     * @return {@link ObservableList}
     * @see Executor
     * @throws SQLException an exception that provides information on a database access error or other errors.
     */
    public static ObservableList<Patient> getPatients(Executor executor, Statement statement) throws SQLException {
        if (!PATIENTS.isEmpty()) {
            PATIENTS.clear();
        }

        ResultSet set = executor.getPatients(statement);
        while (set.next()) {
            PATIENTS.add(new Patient(
                    set.getInt("id"),
                    set.getString("name"),
                    set.getString("phone"),
                    set.getString("address"),
                    set.getString("email"),
                    set.getDate("birthday"),
                    set.getString("sex").equals("чоловічий") ? Sex.MALE : Sex.FEMALE));
        }

        return PATIENTS;
    }

    /**
     * Return a list of patients who matches with filter.
     * @param executor executor
     * @param statement statement
     * @param firstName the 1st name
     * @param secondName the 2nd name
     * @param thirdName the 3rd name
     * @param phone phone number
     * @param address address
     * @param dateBirth date of birthday
     * @param email email
     * @return {@link ObservableList}
     * @see Executor
     * @throws SQLException an exception that provides information on a database access error or other errors.
     */
    public static ObservableList<Patient> getPatientsByFilter(Executor executor,
                                                      Statement statement,
                                                      String firstName,
                                                      String secondName,
                                                      String thirdName,
                                                      String phone,
                                                      String address,
                                                      LocalDate dateBirth,
                                                      String email) throws SQLException {
        if (!PATIENTS_BY_FILTER.isEmpty()) {
            PATIENTS_BY_FILTER.clear();
        }

        ResultSet set = executor.getPatientsByFilter(statement, firstName, secondName,
                thirdName, phone, address, dateBirth, email);

        while (set.next()) {
            PATIENTS_BY_FILTER.add(new Patient(
                    set.getInt("id"),
                    set.getString("name"),
                    set.getString("phone"),
                    set.getString("address"),
                    set.getString("email"),
                    set.getDate("birthday"),
                    set.getString("sex").equals("чоловічий") ? Sex.MALE : Sex.FEMALE));
        }

        return PATIENTS_BY_FILTER;
    }

    /**
     * Returns the <strong>id</strong> of the patient.
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the <strong>full name</strong> of the patient.
     * @return the full name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the <strong>date of birthday</strong> of the patient.
     * @return the date of birthday
     */
    public Date getDateBirth() {
        return dateBirth;
    }

    /**
     * Returns the <strong>phone number</strong> of the patient.
     * @return the phone number
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Returns the <strong>address</strong> of the patient.
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Returns the <strong>email</strong> of the patient.
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Returns the <strong>gender</strong> of the patient.
     * @see Sex
     * @return the id
     */
    public Sex getSex() {
        return sex;
    }

    /**
     * Returns a string representation of object.
     * @return a string representation
     */
    @Override
    public String toString() {
        return name;
    }
}
