package com.software_engineering_course_work.controllers;

import com.software_engineering_course_work.database.DBConnect;
import com.software_engineering_course_work.database.Executor;
import com.software_engineering_course_work.model.Doctor;
import com.software_engineering_course_work.model.Patient;
import com.software_engineering_course_work.model.Specialization;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jfxtras.scene.control.LocalDatePicker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

/**
 * The controller that implements logic for the window of records the patient to the doctor.
 * @author Linenko Kostyantyn
 * @version 1.0
 */
public class RecordController {

    /** The logger */
    private static final Logger LOGGER = LogManager.getLogger(ReceptionController.class);
    /** User marker */
    private static final Marker USER_MARKER = MarkerManager.getMarker("USER");
    /** Exceptions marker */
    private static final Marker EXCEPTIONS_MARKER = MarkerManager.getMarker("EXCEPTIONS");
    /** The controller of window where we can add a new patient to the database. */
    AddController addController;
    /** The combo-box of patients. */
    @FXML
    private ComboBox<Patient> boxPatient;
    /** The calendar. */
    @FXML
    private LocalDatePicker calendar;
    /** The combo-box of doctors. */
    @FXML
    private ComboBox<String> boxDoctor;
    /** The combo-box of specializations. */
    @FXML
    private ComboBox<Specialization> boxSpecialization;
    /** The field of time. */
    @FXML
    private TextField timeField;
    /** The write button. */
    @FXML
    private Button writeButton;

    /**
     * Launches the initial configuration of the window.
     * Connects the database for further using in the program.
     * Combo-boxes {@link RecordController#boxSpecialization} and {@link RecordController#boxPatient} are filled by all names of hospital in the database.
     * After choosing a specialization and hiding the combo-box, the combo-box {@link RecordController#boxDoctor}
     * is filled by doctors that have this specialization.
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

        fillPatientData(executor, statement);
        fillSpecialization(executor, statement);

        boxSpecialization.setOnHidden(event -> {
            Specialization specialization = boxSpecialization.getValue();
            if (specialization != null) {
                try {
                    fillSpecialist(executor, statement, specialization.getName());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        writeButton.setOnAction(actionEvent -> {
            Patient patient = boxPatient.getValue();
            Specialization specialization = boxSpecialization.getValue();
            String doctor = boxDoctor.getValue();
            LocalDate date = calendar.getLocalDate();
            String time = timeField.getText();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy hh:mm");

            if (patient != null && specialization != null && doctor != null && date != null && !time.equals("")) {
                int indexSeparator = time.indexOf(':');
                if (indexSeparator == -1) {
                    System.out.println("Incorrect format of time");
                } else {
                    try {
                        int hours = Integer.parseInt(time.substring(0, indexSeparator).trim());
                        int minutes = Integer.parseInt(time.substring(indexSeparator + 1).trim());
                        LocalDateTime dateTime = date.atTime(hours, minutes);

                        ResultSet set = executor.getDoctorByName(statement, doctor);
                        set.next();

                        int doctorId = set.getInt("id");
                        int patientId = patient.getId();

                        executor.recordPatientToDoctor(statement, patientId, doctorId, dateTime);
                        LOGGER.info(USER_MARKER, "New record " +
                                "{patient: {}, doctor: {}, date: {}} is scheduled",
                                patient.getName(), doctor, dateTime.format(formatter));
                    } catch (NumberFormatException e) {
                        LOGGER.error(EXCEPTIONS_MARKER, "Wrong number format", e);
                        throw new RuntimeException(e);
                    } catch (SQLException e) {
                        LOGGER.error(EXCEPTIONS_MARKER, "SQL exception", e);
                        throw new RuntimeException(e);
                    } catch (IllegalArgumentException e) {
                        LOGGER.warn(USER_MARKER, "Weekend was chosen to schedule the record");
                    }
                }
            } else {
                LOGGER.warn(USER_MARKER, "To schedule a new record, not all fields are chosen");
            }
        });
    }

    /**
     * Fills the combo-box {@link RecordController#boxPatient} with all patients in the database.
     * @param executor executor
     * @param statement statement
     * @see Patient
     * @throws SQLException an exception that provides information on a database access error or other errors.
     */
    public void fillPatientData(Executor executor, Statement statement) throws SQLException {
        ObservableList<Patient> patients = Patient.getPatients(executor, statement);
        patients.sort(Comparator.comparing(Patient::getName));
        boxPatient.setItems(patients);
    }

    /**
     * Fills the combo-box {@link RecordController#boxSpecialization} with
     * all specializations in the database.
     * @param executor executor
     * @param statement statement
     * @see Specialization
     * @throws SQLException an exception that provides information on a database access error or other errors.
     */
    public void fillSpecialization(Executor executor, Statement statement) throws SQLException {
        ObservableList<Specialization> specializations = Specialization.getSpecializations(executor, statement);
        boxSpecialization.setItems(specializations);
    }

    /**
     * Fills the combo-box {@link RecordController#boxDoctor} with
     * doctors who have a specified specialization.
     * @param executor executor
     * @param statement statement
     * @param specialization specialization
     * @see Doctor
     * @throws SQLException an exception that provides information on a database access error or other errors.
     */
    public void fillSpecialist(Executor executor, Statement statement, String specialization) throws SQLException {
        ObservableList<String> doctors = Doctor.getDoctorsBySpecialization(executor, statement, specialization);
        boxDoctor.setItems(doctors);
    }

    /**
     * Launch the window of adding a new patient.
     * @param actionEvent action event
     * @see Patient
     * @throws IOException signals that an I/O exception of some sort has occurred.
     */
    @FXML
    private void showAddWindow(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/new.fxml"));
        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.setTitle("Додати нового пацієнта");
        stage.setResizable(false);
        stage.getIcons().add(new Image(getClass().getResource("/icons/window.png").toExternalForm()));
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
        stage.show();

        addController = loader.getController();
        addController.setParent(this);
    }
}
