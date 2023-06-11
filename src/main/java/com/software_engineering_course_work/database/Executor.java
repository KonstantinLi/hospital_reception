package com.software_engineering_course_work.database;

import com.software_engineering_course_work.model.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Class for executing SQL requests.
 * @author Linenko Kostyantyn
 * @version 1.0
 */
public class Executor {

    /** The controller of window where we can add a new patient to the database. */
    /**
     * Retrieves a result set containing the data of hospitals that accessible in the database.
     * @param statement statement
     * @return {@link ResultSet}
     * @see Hospital
     * @throws SQLException an exception that provides information on a database access error or other errors.
     */
    public ResultSet getHospitals(Statement statement) throws SQLException {
        return statement.executeQuery(
                "SELECT * FROM hospital"
        );
    }

    /**
     * Retrieves a result set containing the data of specializations that accessible in the database.
     * @param statement statement
     * @return {@link ResultSet}
     * @see Specialization
     * @throws SQLException an exception that provides information on a database access error or other errors.
     */
    public ResultSet getSpecializationsList(Statement statement) throws SQLException {
        return statement.executeQuery(
                "SELECT * FROM specialization s"
        );
    }

    /**
     * Retrieves a result set containing the data of specializations that are provided in <strong>hospital</strong>.
     * @param statement statement
     * @param hospitalName hospital's name
     * @return {@link ResultSet}
     * @see Specialization
     * @throws SQLException an exception that provides information on a database access error or other errors.
     */
    public ResultSet getSpecializationsByHospital(Statement statement, String hospitalName) throws SQLException {
        return statement.executeQuery(
                "SELECT DISTINCT s.id, s.name, s.description FROM specialization s " +
                        "JOIN doctor d on s.id = d.specialization_id " +
                        "JOIN hospital h ON h.id = d.hospital_id " +
                        "WHERE h.name = \'" + hospitalName + "\'"
        );
    }

    /**
     * Retrieves a result set containing doctor's names who have a concrete <strong>specialization</strong>.
     * @param statement statement
     * @param specialization specialization
     * @return {@link ResultSet}
     * @see Doctor
     * @throws SQLException an exception that provides information on a database access error or other errors.
     */
    public ResultSet getDoctorsBySpecialization(Statement statement, String specialization) throws SQLException {
        return statement.executeQuery(
                "SELECT d.name FROM doctor d " +
                        "JOIN specialization s on s.id = d.specialization_id " +
                        "WHERE s.name LIKE \'%" + specialization + "%\'"
        );
    }

    /**
     * Retrieves a result set containing doctor's names who have a concrete <strong>specialization</strong> and works in concrete <strong>hospital</strong>.
     * @param statement statement
     * @param specialization specialization
     * @param hospital hospital
     * @return {@link ResultSet}
     * @see Doctor
     * @throws SQLException an exception that provides information on a database access error or other errors.
     */
    public ResultSet getDoctorsBySpecializationAndHospital(Statement statement, String specialization, String hospital) throws SQLException {
        return statement.executeQuery(
                "SELECT d.name FROM doctor d " +
                        "JOIN specialization s on d.specialization_id = s.id " +
                        "JOIN hospital h on d.hospital_id = h.id " +
                        "WHERE h.name LIKE \'%" + hospital + "%\' AND s.name LIKE \'%" + specialization + "%\'"
        );
    }

    /**
     * Retrieves a result set containing the data of doctor who corresponds the <strong>id</strong>.
     * @param statement statement
     * @param id id
     * @return {@link ResultSet}
     * @see Doctor
     * @throws SQLException an exception that provides information on a database access error or other errors.
     */
    public ResultSet getDoctorById(Statement statement, int id) throws SQLException {
        return statement.executeQuery(
                "SELECT * FROM doctor d " +
                        "WHERE d.id = " + id
        );
    }

    /**
     * Retrieves a result set containing the data of doctor who corresponds the <strong>name</strong>.
     * @param statement statement
     * @param name name
     * @return {@link ResultSet}
     * @see Doctor
     * @throws SQLException an exception that provides information on a database access error or other errors.
     */
    public ResultSet getDoctorByName(Statement statement, String name) throws SQLException {
        return statement.executeQuery(
                "SELECT * FROM doctor d " +
                        "WHERE d.name = \'" + name + "\'"
        );
    }

    /**
     * Retrieves a result set containing the data of doctors whose full name contains a <strong>specified part</strong>.
     * @param statement statement
     * @param partName part of name
     * @return {@link ResultSet}
     * @see Doctor
     * @throws SQLException an exception that provides information on a database access error or other errors.
     */
    public ResultSet getDoctorByPartName(Statement statement, String partName) throws SQLException {
        return statement.executeQuery(
                "SELECT * FROM doctor d " +
                        "WHERE d.name LIKE \'%" + partName + "%\'"
        );
    }

    /**
     * Retrieves a result set containing password of <i>root user</i> with <strong>specified login</strong>.
     * @param statement statement
     * @param login login
     * @return {@link ResultSet}
     * @throws SQLException an exception that provides information on a database access error or other errors.
     */
    public ResultSet getRootData(Statement statement, String login) throws SQLException {
        return statement.executeQuery(
                "SELECT * FROM root_user r " +
                        "WHERE r.login = \'" + login + "\'"
        );
    }

    /**
     * Retrieves a result set containing the data of all patients in the database.
     * @param statement statement
     * @return {@link ResultSet}
     * @see Patient
     * @throws SQLException an exception that provides information on a database access error or other errors.
     */
    public ResultSet getPatients(Statement statement) throws SQLException {
        return statement.executeQuery(
                "SELECT * FROM patient"
        );
    }

    /**
     * Retrieves a result set containing the data of patient with particular <strong>id</strong>.
     * @param statement statement
     * @param id id
     * @return {@link ResultSet}
     * @see Patient
     * @throws SQLException an exception that provides information on a database access error or other errors.
     */
    public ResultSet getPatientById(Statement statement, int id) throws SQLException {
        return statement.executeQuery(
                "SELECT * FROM patient p " +
                        "WHERE p.id = " + id
        );
    }

    /**
     * Filters patients by parameters and retrieves the appropriate result set.
     * If any variable is equal to <strong>null</strong> or the <strong>empty string</strong>,
     * this parameter doesn't take a part in condition.
     * @param statement statement
     * @param firstName the 1st name
     * @param secondName the 2nd name
     * @param thirdName the 3rd name
     * @param phone phone number
     * @param address address
     * @param dateBirth date of birthday
     * @param email email
     * @return {@link ResultSet}
     * @see Patient
     * @throws SQLException an exception that provides information on a database access error or other errors.
     */
    public ResultSet getPatientsByFilter(Statement statement, String firstName, String secondName,
            String thirdName, String phone, String address, LocalDate dateBirth, String email) throws SQLException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String firstNameFilter = !firstName.equals("")
                ? "p.name LIKE \'" + firstName + " %\'"
                : "";
        String secondNameFilter = !secondName.equals("")
                ? "p.name LIKE \'% " + secondName + " %\'"
                : "";
        String thirdNameFilter = !thirdName.equals("")
                ? "p.name LIKE \'% " + thirdName + "\'"
                : "";
        String phoneFilter = !phone.equals("")
                ? "p.phone = \'" + phone + "\'"
                : "";
        String addressFilter = !address.equals("")
                ? "p.address LIKE \'%" + address + "%\'"
                : "";
        String birthdayFilter = dateBirth != null
                ? "p.birthday =\'" + dateBirth.format(formatter) + "\'"
                : "";
        String emailFilter = !email.equals("")
                ? "p.email = \'" + email + "\'"
                : "";

        ArrayList<String> filters = new ArrayList<>(Arrays.asList(
                firstNameFilter, secondNameFilter, thirdNameFilter, phoneFilter,
                addressFilter, birthdayFilter, emailFilter
        ));

        StringJoiner joiner = new StringJoiner(" AND ");
        filters.forEach(filter -> {
            if (!filter.equals("")) {
                joiner.add(filter);
            }
        });

        return statement.executeQuery(
                "SELECT * FROM patient p " +
                        "WHERE " + (joiner.toString().isBlank() ? 1 : joiner)
        );
    }

    /**
     * Adds a new patient to the database.
     * Field <i>id</i> insertion is not provided cause of it's <i><strong>auto increment</strong></i> type.
     * @param statement statement
     * @param name name
     * @param phone phone
     * @param address address
     * @param birthday birthday
     * @param email email
     * @param sex gender
     * @see Patient
     * @throws SQLException an exception that provides information on a database access error or other errors.
     */
    public void addNewPatient(Statement statement, String name, String phone, String address, LocalDate birthday, String email, Sex sex) throws SQLException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        statement.executeUpdate(
                "INSERT INTO patient (`name`, `phone`, `address`, `birthday`, `email`, `sex`) VALUES " +
                        "(\'" + name + "\', \'"
                        + phone + "\', \'"
                        + address + "\', \'"
                        + birthday.format(formatter) + "\', \'"
                        + email + "\', \'"
                        + sex.getText() + "\')"
        );
    }

    /**
     * Add a new record of the patient to the doctor in free time.
     * If the value in {@link LocalDateTime} <strong>srcDate</strong> corresponds to weekends or
     * scheduled time of another patients, this record won't be added.
     * @param statement statement
     * @param patientId patient's id
     * @param doctorId doctor's id
     * @param srcDate source date
     * @see Patient
     * @see Interval
     * @throws SQLException an exception that provides information on a database access error or other errors.
     */
    public void recordPatientToDoctor(Statement statement, int patientId, int doctorId, LocalDateTime srcDate) throws SQLException, IllegalArgumentException {
        LocalDateTime left = srcDate.toLocalDate().atTime(8, 0);
        LocalDateTime right = srcDate.toLocalDate().atTime(12, 0);

        if (srcDate.isAfter(left)
                && srcDate.isBefore(right)
                && !(srcDate.getDayOfWeek() == DayOfWeek.FRIDAY ||
                    srcDate.getDayOfWeek() == DayOfWeek.SATURDAY ||
                    srcDate.getDayOfWeek() == DayOfWeek.SUNDAY)) {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String srcDateInFormat = srcDate.format(formatter);

            ResultSet set = statement.executeQuery(
                    "SELECT COUNT(*) FROM record r " +
                            "WHERE \'" + srcDateInFormat + "\' BETWEEN " +
                            "r.src_date AND r.dst_date"
            );

            if (set.next() && set.getInt(1) == 0) {
                LocalDateTime dstDate = srcDate;
                for (Interval interval : Interval.getIntervals()) {
                    String time = interval.getTime();
                    int indexHyphen = time.indexOf('-');
                    int indexSeparator = time.substring(0, indexHyphen - 1).indexOf(':');

                    int hours = Integer.parseInt(time.substring(0, indexSeparator));
                    int minutes = Integer.parseInt(time.substring(indexSeparator + 1, indexHyphen - 1));

                    LocalDateTime currentInterval = srcDate.toLocalDate().atTime(hours, minutes);
                    if (srcDate.isBefore(currentInterval)) {
                        dstDate = currentInterval;
                        break;
                    }
                }
                String dstDateInFormat = dstDate.format(formatter);

                statement.executeUpdate(
                        "INSERT INTO record (patient_id, doctor_id, src_date, dst_date) VALUES " +
                                "(" + patientId + ", " + doctorId + ", \'" + srcDateInFormat + "\', \'" + dstDateInFormat + "\')"
                );
                System.out.println("Successful!");
            } else {
                System.out.println("Time is already taken by another patient");
            }
        } else {
            throw new IllegalArgumentException("Time was chosen in no work time");
        }
    }

    /**
     * Retrieves a result set containing records of concrete <strong>day</strong>.
     * @param statement statement
     * @param doctorId doctor's id
     * @param date date
     * @return {@link ResultSet}
     * @throws SQLException an exception that provides information on a database access error or other errors.
     */
    public ResultSet getRecordsOnDay(Statement statement, int doctorId, LocalDate date) throws SQLException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return statement.executeQuery(
                "SELECT * FROM record " +
                        "WHERE doctor_id = " + doctorId +
                        " AND DATE(src_date) = \'" + date.format(formatter) + "\'"
        );
    }

    /**
     * Retrieves a collection containing each day of week and appropriate work schedule of the doctor.
     * @param statement statement
     * @param doctorName doctor's name
     * @return {@link HashMap}
     * @see Doctor
     * @throws SQLException an exception that provides information on a database access error or other errors.
     */
    public HashMap<DayOfWeek, List<Time>> getScheduleOfDoctor(Statement statement, String doctorName) throws SQLException {
        HashMap<DayOfWeek, List<Time>> schedule = new HashMap<>();

        ResultSet shiftOfWeek = statement.executeQuery(
                "SELECT start_shift, end_shift FROM schedule " +
                        "JOIN doctor d on schedule.doctor_id = d.id " +
                        "WHERE d.name = \'" + doctorName + "\'"
        );
        int dayOfWeek = 0;
        while (shiftOfWeek.next()) {
            dayOfWeek++;
            schedule.put(DayOfWeek.of(dayOfWeek),
                    Arrays.asList(shiftOfWeek.getTime(1), shiftOfWeek.getTime(2)));
        }

        return schedule;
    }
}
