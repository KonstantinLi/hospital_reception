package com.software_engineering_course_work;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * POJO-class for storing data about particular specialization.
 * @author Linenko Kostyantyn
 * @version 1.0
 */
public class Specialization {
    /** The list of all specializations in the database. */
    private static final ObservableList<Specialization> SPECIALIZATIONS = FXCollections.observableArrayList();
    /** The list of specialization that are accessible in specified <strong>hospital</strong>. */
    private static final ObservableList<Specialization> SPECIALIZATIONS_BY_HOSPITAL = FXCollections.observableArrayList();
    /** The <strong>id</strong> of the specialization. */
    private final int id;
    /** The <strong>name</strong> of the specialization. */
    private final String name;
    /** The <strong>description</strong> of the specialization. */
    private final String description;

    /**
     * The lone constructor.
     * @param id id
     * @param name name
     * @param description description
     */
    public Specialization(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    /**
     * Return an observable list containing the data of all specializations.
     * @param executor executor
     * @param statement statement
     * @return {@link ObservableList}
     * @see Executor
     * @throws SQLException an exception that provides information on a database access error or other errors.
     */
    public static ObservableList<Specialization> getSpecializations(Executor executor, Statement statement) throws SQLException {
        if (!SPECIALIZATIONS.isEmpty()) {
            SPECIALIZATIONS.clear();
        }

        ResultSet set = executor.getSpecializationsList(statement);
        while (set.next()) {
            SPECIALIZATIONS.add(new Specialization(set.getInt(1),
                    set.getString(2),
                    set.getString(3)));
        }

        return SPECIALIZATIONS;
    }

    /**
     * Returns an observable list containing the data of specializations that are accessible in specified <strong>hospital</strong>.
     * @param executor executor
     * @param statement statement
     * @param hospitalName hospital's name
     * @return {@link ObservableList}
     * @throws SQLException an exception that provides information on a database access error or other errors.
     */
    public static ObservableList<Specialization> getSpecializationsListByHospital(Executor executor, Statement statement, String hospitalName) throws SQLException {
        if (!SPECIALIZATIONS_BY_HOSPITAL.isEmpty()) {
            SPECIALIZATIONS_BY_HOSPITAL.clear();
        }

        ResultSet set = executor.getSpecializationsByHospital(statement, hospitalName);
        while (set.next()) {
            SPECIALIZATIONS_BY_HOSPITAL.add(new Specialization(set.getInt(1),
                    set.getString(2),
                    set.getString(3)));
        }

        return SPECIALIZATIONS_BY_HOSPITAL;
    }

    /**
     * Returns a string representation of object.
     * @return a string representation
     */
    @Override
    public String toString() {
        return name;
    }

    /**
     * Return the id of specialization.
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the name of specialization.
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the description of specialization.
     * @return the description
     */
    public String getDescription() {
        return description;
    }
}
