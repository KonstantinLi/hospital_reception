package com.software_engineering_course_work.graphic_interface;

import com.software_engineering_course_work.DBConnect;
import com.software_engineering_course_work.Executor;
import com.software_engineering_course_work.Patient;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

/**
 * The controller that implements logic for the window of the patient's filterable table.
 * @author Linenko Kostyantyn
 * @version 1.0
 */
public class TableController {

    /** The logger */
    private static final Logger LOGGER = LogManager.getLogger(ReceptionController.class);
    /** User marker */
    private static final Marker USER_MARKER = MarkerManager.getMarker("USER");
    /** Exceptions marker */
    private static final Marker EXCEPTIONS_MARKER = MarkerManager.getMarker("EXCEPTIONS");
    /** The controller of window where we can add a new patient to the database. */
    /** The clear button. */
    @FXML
    private Button buttonClear;
    /** The search button. */
    @FXML
    private Button buttonSearch;
    /** The column of address. */
    @FXML
    private TableColumn<Patient, String> columnAddress;
    /** The column of birthday. */
    @FXML
    private TableColumn<Patient, Date> columnBirth;
    /** The column of email. */
    @FXML
    private TableColumn<Patient, String> columnEmail;
    /** The column of id. */
    @FXML
    private TableColumn<Patient, Integer> columnId;
    /** The column of name. */
    @FXML
    private TableColumn<Patient, String> columnName;
    /** The column of number phone. */
    @FXML
    private TableColumn<Patient, String> columnPhone;
    /** The date picker. */
    @FXML
    private DatePicker datePickerBirth;
    /** The field of address. */
    @FXML
    private TextField fieldAddress;
    /** The field of email. */
    @FXML
    private TextField fieldEmail;
    /** The field of the 1st name. */
    @FXML
    private TextField fieldFirstName;
    /** The field of the 2nd name. */
    @FXML
    private TextField fieldSecondName;
    /** The field of the 3rd name. */
    @FXML
    private TextField fieldThirdName;
    /** The field of phone number. */
    @FXML
    private TextField fieldPhone;
    /** The table of patients. */
    @FXML
    private TableView<Patient> tablePatients;

    /**
     * Launches the initial configuration of the window.
     * Connects the database for further using in the program.
     * Fills the table with {@link Patient} objects.
     * @throws SQLException an exception that provides information on a database access error or other errors.
     */
    @FXML
    public void initialize() throws SQLException {
        DBConnect dbConnect = DBConnect.getInstance("jdbc:mysql://localhost:3306/hospital_reception?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",
                "root",
                "root_bd2022");
        dbConnect.connect();
        Executor executor = dbConnect.getExecutor();
        Statement statement = dbConnect.getStatement();

        TableColumn[] columns = new TableColumn[] {
                columnName,
                columnPhone,
                columnAddress,
                columnEmail
        };

        String[] fields = new String[] {
                "name",
                "phone",
                "address",
                "email"
        };

        ObservableList<Patient> allPatients = Patient.getPatients(executor, statement);
        tablePatients.setItems(allPatients);

        for (int i = 0; i < 4; i++) {
            PropertyValueFactory<Patient, String> factory = new PropertyValueFactory<>(fields[i]);
            columns[i].setCellValueFactory(factory);
        }
        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnBirth.setCellValueFactory(new PropertyValueFactory<>("dateBirth"));

        buttonSearch.setOnAction(actionEvent -> {
            ObservableList<Patient> patientsByFilter;
            try {
                String name = fieldFirstName.getText();
                String surName = fieldSecondName.getText();
                String thirdName = fieldThirdName.getText();
                String phone = fieldPhone.getText();
                String address = fieldAddress.getText();
                String birthday = String.valueOf(datePickerBirth.getValue()) != "null"
                        ? String.valueOf(datePickerBirth.getValue())
                        : "";
                String email = fieldEmail.getText();

                patientsByFilter = Patient.getPatientsByFilter(executor, statement,
                        name, surName, thirdName, phone,
                        address, datePickerBirth.getValue(), email);

                LOGGER.info(USER_MARKER, """
                                Filtering patients by filters:
                                \tName - "{}"
                                \tSurname - "{}"
                                \tThird name - "{}"
                                \tPhone - "{}"
                                \tAddress - "{}"
                                \tBirthday - "{}"
                                \tEmail - "{}\"""",
                        name, surName, thirdName, phone, address, birthday, email);
            } catch (SQLException e) {
                LOGGER.error(EXCEPTIONS_MARKER, "SQL exception", e);
                throw new RuntimeException(e);
            }

            tablePatients.setItems(patientsByFilter);
        });

        buttonClear.setOnAction(actionEvent -> {
            tablePatients.setItems(allPatients);
            LOGGER.info("Filters are deleted");
        });
    }
}
