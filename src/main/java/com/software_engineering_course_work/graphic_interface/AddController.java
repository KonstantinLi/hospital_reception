package com.software_engineering_course_work.graphic_interface;

import com.software_engineering_course_work.DBConnect;
import com.software_engineering_course_work.Executor;
import com.software_engineering_course_work.Sex;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import jfxtras.scene.control.LocalDatePicker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.format.DateTimeFormatter;

/**
 * The controller that implements logic for the window of adding a new patient to the database.
 * @author Linenko Kostyantyn
 * @version 1.0
 */
public class AddController {

    /** The logger */
    private static final Logger LOGGER = LogManager.getLogger(ReceptionController.class);
    /** User marker */
    private static final Marker USER_MARKER = MarkerManager.getMarker("USER");
    /** Exceptions marker */
    private static final Marker EXCEPTIONS_MARKER = MarkerManager.getMarker("EXCEPTIONS");
    /** The controller of the main window. */
    RecordController mainController;
    /** Calendar. */
    @FXML
    private LocalDatePicker calendar;
    /** The field of address. */
    @FXML
    private TextField fieldAddress;
    /** the field of email. */
    @FXML
    private TextField fieldEmail;
    /** The field of name. */
    @FXML
    private TextField fieldName;
    /** The field of phone number. */
    @FXML
    private TextField fieldPhone;
    /** Write button. */
    @FXML
    private Button writeButton;
    /** Radio button of male. */
    @FXML
    private RadioButton radioMan;
    /** Radio button of female. */
    @FXML
    private RadioButton radioWoman;
    /** {@link Sex} constant. */
    private Sex sex;

    /**
     * Launches the initial configuration of the window.
     * Connects the database for further using in the program.
     * After filling all data and clicking the button <strong>writeButton</strong>
     * Insert request is executed. It won't execute, if any field is empty.
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

        ToggleGroup tg = new ToggleGroup();
        radioMan.setToggleGroup(tg);
        radioWoman.setToggleGroup(tg);

        tg.selectedToggleProperty().addListener((observableValue, oldVal, newVal) -> {
            RadioButton rb = (RadioButton) newVal;
            if (rb.getText().equals("Чоловік")) {
                sex = Sex.MALE;
            } else {
                sex = Sex.FEMALE;
            }
        });

        writeButton.setOnAction(actionEvent -> {
            if (isCorrectData()) {
                try {
                    String name = fieldName.getText();
                    String phone = fieldPhone.getText();
                    String address = fieldAddress.getText();
                    String birthday = calendar.getLocalDate()
                            .format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
                    String email = fieldEmail.getText();

                    executor.addNewPatient(statement, name, phone,
                            address, calendar.getLocalDate(), email, sex);

                    mainController.fillPatientData(executor, statement);

                    writeButton.setText("Successful");
                    Thread.sleep(1000);
                    Stage stage = (Stage) writeButton.getScene().getWindow();
                    stage.close();

                    LOGGER.info(USER_MARKER, """
                                    Added the new patient:
                                    \tName - {}
                                    \tSex - {}
                                    \tPhone - {}
                                    \tAddress - {}
                                    \tBirthday - {}
                                    \tEmail - {}""",
                            name, sex.getText(), phone, address, birthday, email);
                } catch (SQLException | InterruptedException e) {
                    LOGGER.error(e);
                    throw new RuntimeException(e);
                }
            } else {
                if (!isCorrectName()) {
                    System.out.println("Incorrect name");
                }

                if (!isCorrectPhone()) {
                    System.out.println("Incorrect phone");
                }

                if (!isCorrectEmail()) {
                    System.out.println("Incorrect email");
                }

                if (fieldAddress.getText().equals("")) {
                    System.out.println("Address is empty");
                }

                if (calendar.getLocalDate() == null) {
                    System.out.println("Birthday isn't chosen");
                }

                if (sex == null) {
                    System.out.println("Sex isn't set");
                }
            }
        });
    }

    /**
     * Checks the correct data of a new patient.
     * @return true or false
     */
    private boolean isCorrectData() {
        return isCorrectName()
                && isCorrectPhone()
                && isCorrectEmail()
                && !fieldAddress.getText().equals("")
                && calendar.getLocalDate() != null
                && sex != null;
    }

    /**
     * Checks the correct full names of a new patient.
     * The field <strong>fieldName</strong> must contain <i>three words</i>.
     * @return true or false
     */
    private boolean isCorrectName() {
        return fieldName.getText().trim().split("\s+").length == 3;
    }

    /**
     * Checks the correct phone number of a new patient.
     * The field <strong>fieldPhone</strong> must contain 10 digits.
     * @return true or false
     */
    private boolean isCorrectPhone() {
        return fieldPhone.getText().length() == 10;
    }

    /**
     * Check the correct email address of a new patient.
     * The field <strong>fieldEmail</strong> must contain symbol <strong>'@'</strong> and
     * the correct domain.
     * @return true or false
     */
    private boolean isCorrectEmail() {
        String email = fieldEmail.getText().toLowerCase().trim();
        return email.contains("@")
                && email.substring(email.indexOf('@') + 1).matches("[a-z]+.[a-z]+");
    }

    /**
     * Set the parent controller for this window.
     * @param controller controller
     */
    public void setParent(RecordController controller) {
        mainController = controller;
    }
}

