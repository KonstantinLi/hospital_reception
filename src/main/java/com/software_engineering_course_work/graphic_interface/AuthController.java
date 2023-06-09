package com.software_engineering_course_work.graphic_interface;

import com.software_engineering_course_work.DBConnect;
import com.software_engineering_course_work.Executor;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * The controller that implements logic for the window of authorization.
 * @author Linenko Kostyantyn
 * @version 1.0
 */
public class AuthController {

    /** The logger */
    private static final Logger LOGGER = LogManager.getLogger(AuthController.class);
    /** Authorization marker */
    private static final Marker AUTH_MARKER = MarkerManager.getMarker("AUTH");
    /** Exceptions marker */
    private static final Marker EXCEPTIONS_MARKER = MarkerManager.getMarker("EXCEPTIONS");
    /**
     * The main controller.
     */
    private ReceptionController mainController;
    /** The authorization button. */
    @FXML
    private ToggleButton buttonAuth;
    /** The closing button. */
    @FXML
    private ToggleButton buttonClose;
    /** The field of email. */
    @FXML
    private TextField fieldEmail;
    /** The field of password. */
    @FXML
    private PasswordField fieldPassword;

    /**
     * Launch the initial configuration of the window.
     * Connects the database for further using in the program.
     * If login and passwords are correct, after clicking on button
     * <strong>buttonAuth</strong>, the method <strong>logIn(String login)</strong>
     * of {@link ReceptionController} will be executed.
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

        buttonAuth.setOnAction(actionEvent -> {
            String login = fieldEmail.getText().trim();
            String password = fieldPassword.getText().trim();

            try {
                if (!login.isEmpty() && !password.isEmpty() &&
                isCorrectAuthorization(executor, statement, login, password)) {
                    mainController.logIn(login);
                    mainController.fillBoxHospital(executor, statement);
                    closeWindow();
                    LOGGER.info(AUTH_MARKER, "User \"{login: {}, password: {}}\" is authorized", login, password);
                } else {
                    LOGGER.warn(AUTH_MARKER, "User \"{login: {}, password: {}}\" is not found", login, password);
                }
            } catch (SQLException e) {
                LOGGER.error(EXCEPTIONS_MARKER, "SQL exception", e);
                throw new RuntimeException(e);
            }
        });

        buttonClose.setOnAction(actionEvent -> closeWindow());
    }

    /**
     * Closes the window without authorization.
     */
    private void closeWindow() {
        Stage stage = (Stage) buttonClose.getScene().getWindow();
        stage.close();
    }

    /**
     * Checks the correct login and password of root user.
     * @param executor executor
     * @param statement statement
     * @param login login
     * @param password password
     * @return true or false
     * @see Executor
     * @throws SQLException an exception that provides information on a database access error or other errors.
     */
    private boolean isCorrectAuthorization(Executor executor, Statement statement, String login, String password) throws SQLException {
        ResultSet set = executor.getRootData(statement, login);
        return set.next() && set.getString("password").equals(password);
    }

    /**
     * Set the parent controller of this window.
     * @param controller controller
     */
    public void setParent(ReceptionController controller) {
        mainController = controller;
    }
}
