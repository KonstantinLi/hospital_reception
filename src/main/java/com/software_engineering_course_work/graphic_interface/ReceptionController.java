package com.software_engineering_course_work.graphic_interface;

import com.software_engineering_course_work.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jfxtras.scene.control.LocalDatePicker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The controller that implements logic for the main window.
 * @author Linenko Kostyantyn
 * @version 1.0
 */
public class ReceptionController {
    /** Properties path */
    private static final String PROPERTIES_PATH = "src/main/resources/config/application.properties";

    /** The logger */
    private static final Logger LOGGER = LogManager.getLogger(ReceptionController.class);
    /** Authorization marker */
    private static final Marker AUTH_MARKER = MarkerManager.getMarker("AUTH");
    /** User marker */
    private static final Marker USER_MARKER = MarkerManager.getMarker("USER");
    /** Exceptions marker */
    private static final Marker EXCEPTIONS_MARKER = MarkerManager.getMarker("EXCEPTIONS");
    /** The list of constants in {@link Interval} enum. */
    private static final ObservableList<Interval> TIME_INTERVALS = FXCollections.observableArrayList(Arrays.stream(Interval.getIntervals()).toList());
    /** The image of icon in window. */
    private final Image hospitalIcon = new Image(getClass().getResource("/icons/hospital.png").toExternalForm());
    /** The list of rectangles that are currently in the stage. */
    private final ArrayList<Rectangle> rectangles = new ArrayList<>();
    /** The list of labels that are currently in the stage. */
    private final ArrayList<Label> labels = new ArrayList<>();
    /** The list of buttons that are currently in the stage. */
    private final ArrayList<Button> buttons = new ArrayList<>();

    /** The controller of authorization window. */
    private AuthController authController;
    /** The controller of window with data of concrete patient. */
    private PatientController patientController;
    /** The screen time display format. */
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM.dd");

    /** HBox pane of schedule. */
    @FXML
    private HBox HBoxSchedule;
    /** The main VBox pane. */
    @FXML
    private VBox mainVBox;
    /** The anchor pane of tables. */
    @FXML
    private AnchorPane paneTables;
    /** The anchor pane of buttons. */
    @FXML
    private AnchorPane paneButtons;
    /** The combo-box of hospitals. */
    @FXML
    private ComboBox<Hospital> boxHospital;
    /** The combo-box of specialists. */
    @FXML
    private ComboBox<String> boxSpecialist;
    /** The combo-box of specializations. */
    @FXML
    private ComboBox<Specialization> boxSpecialization;
    /** The button of authorization. */
    @FXML
    private Button buttonAuthorization;
    /** The button that clear the data. */
    @FXML
    private ToggleButton buttonClear;
    /** The button that expand the {@link ReceptionController#HBoxSchedule} */
    @FXML
    private Button buttonExpand;
    /** The button that show schedule of the doctor week. */
    @FXML
    private Button buttonShowSchedule;
    /** Button for logging out. */
    @FXML
    private Button buttonLogOut;
    /** The calendar. */
    @FXML
    private LocalDatePicker calendar;
    /** The table of schedule for 1st day. */
    @FXML
    private TableView<Interval> table1;
    /** The table of schedule for 2nd day. */
    @FXML
    private TableView<Interval> table2;
    /** The table of schedule for 3rd day. */
    @FXML
    private TableView<Interval> table3;
    /** The table of schedule for 4th day. */
    @FXML
    private TableView<Interval> table4;
    /** The table of schedule for 5th day. */
    @FXML
    private TableView<Interval> table5;
    /** The table of schedule for 6th day. */
    @FXML
    private TableView<Interval> table6;
    /** The table of schedule for 7th day. */
    @FXML
    private TableView<Interval> table7;
    /** The column of schedule for 1st day. */
    @FXML
    private TableColumn<Interval, String> column1;
    /** The column of schedule for 2nd day. */
    @FXML
    private TableColumn<Interval, String> column2;
    /** The column of schedule for 3rd day. */
    @FXML
    private TableColumn<Interval, String> column3;
    /** The column of schedule for 4th day. */
    @FXML
    private TableColumn<Interval, String> column4;
    /** The column of schedule for 5th day. */
    @FXML
    private TableColumn<Interval, String> column5;
    /** The column of schedule for 6th day. */
    @FXML
    private TableColumn<Interval, String> column6;
    /** The column of schedule for 7th day. */
    @FXML
    private TableColumn<Interval, String> column7;
    /** The image of gender icon. */
    @FXML
    private ImageView imageSex;
    /** The image of user. */
    @FXML
    private ImageView imageProfile;
    /** The image of hospital. */
    @FXML
    private ImageView imageHospital;
    /** The label of user's login. */
    @FXML
    private Label labelLogin;
    /** The label of doctor's name. */
    @FXML
    private Label labelNameOfDoctor;

    /**
     * Launch the initial configuration of the main window.
     * Connects the database for further using in the program.
     * Primarily, you need to log in, otherwise the program will deny access for you.
     * The combo-box {@link ReceptionController#boxHospital} is filled by all names of hospital in the database.
     * Onwards, after choosing a hospital and hiding the combo-box, the combo-box {@link ReceptionController#boxSpecialization}
     * is filled by specializations that are accessible in this hospital.
     * Exactly the same actions are performed with combo-box {@link ReceptionController#boxSpecialist},
     * but already two values (hospital and specialization) take a part in filtering.
     * After clicking on button {@link ReceptionController#buttonShowSchedule} the pane of schedule becomes visible.
     * This pane contains names of hospital and specialist, schedule and records of patients in appropriate buttons.
     * Buttons is clickable: after clicking the window of the patient's data opens.
     * @see DBConnect
     * @see Executor
     * @see Hospital
     * @see Doctor
     * @see Patient
     * @throws SQLException an exception that provides information on a database access error or other errors.
     */
    @FXML
    public void initialize() throws SQLException {
        Properties properties = null;
        try {
            properties = new Properties();
            properties.load(new FileInputStream(PROPERTIES_PATH));
        } catch (IOException ex) {
            Exception exception = new RuntimeException("Properties file not found");
            LOGGER.error(exception);
        }

        LOGGER.info(USER_MARKER, "Database initialization...");
        DBConnect dbConnect = DBConnect.getInstance(properties.getProperty("db.url"),
                properties.getProperty("db.user"),
                properties.getProperty("db.password"));
        dbConnect.connect();
        Executor executor = dbConnect.getExecutor();
        Statement statement = dbConnect.getStatement();
        LOGGER.info(USER_MARKER, "Successful connection!");

        ArrayList<TableView> tables = new ArrayList<>(Arrays.asList(
                table1,
                table2,
                table3,
                table4,
                table5,
                table6,
                table7
        ));

        ArrayList<TableColumn> columns = new ArrayList<>(Arrays.asList(
                column1,
                column2,
                column3,
                column4,
                column5,
                column6,
                column7
        ));

        buttonExpand.setDisable(true);

        fillBoxHospital(executor, statement);

        PropertyValueFactory<Interval, String> factory = new PropertyValueFactory<>("time");
        for (int i = 0; i < 7; i++) {
            columns.get(i).setCellValueFactory(factory);
        }

        boxHospital.setOnShown(event -> {
            boxSpecialization.setItems(FXCollections.emptyObservableList());
            boxSpecialist.setItems(FXCollections.emptyObservableList());
        });
        boxSpecialization.setOnShown(event -> boxSpecialist.setItems(FXCollections.emptyObservableList()));

        boxHospital.setOnHidden(event -> {
            Hospital hospital = boxHospital.getValue();
            if (hospital != null) {
                LOGGER.info(USER_MARKER, "Hospital \"{}\" is chosen by user {}",
                        hospital.getName(), labelLogin.getText());
                String imageName = hospital.getImage();
                imageHospital.setImage(
                        new Image(getClass().getResource("/images/" + imageName).toExternalForm()));
                fillBoxSpecialization(executor, statement, hospital);
                fillBoxSpecialist(executor, statement, boxSpecialization.getValue(), hospital);
            }
        });

        boxSpecialization.setOnHidden(event -> {
            if (boxSpecialization.getValue() != null && boxHospital.getValue() != null) {
                Specialization specialization = boxSpecialization.getValue();
                LOGGER.info(USER_MARKER, "Specialization \"{}\" is chosen by user {}",
                        specialization.getName(), labelLogin.getText());
                fillBoxSpecialist(executor, statement, specialization, boxHospital.getValue());
            }
        });

        boxSpecialist.setOnHidden(event -> {
            if (boxSpecialist.getValue() != null) {
                String specialist = boxSpecialist.getValue();
                LOGGER.info(USER_MARKER, "Specialist \"{}\" is chosen by user {}",
                        specialist, labelLogin.getText());
            }
        });

        buttonShowSchedule.setOnAction(event -> {

            if (calendar.getLocalDate() != null &&
                    boxSpecialization.getValue() != null &&
                    boxSpecialist.getValue() != null) {

                buttonExpand.setDisable(false);
                expandHBox();
                if (!HBoxSchedule.isVisible()) {
                    setPictureOnExpandButton();
                }

                ArrayList<Integer> daysOff = new ArrayList<>();
                int dayNoWork = 0;

                LocalDate localDate = calendar.getLocalDate();
                tables.forEach(tableView -> tableView.setItems(FXCollections.emptyObservableList()));

                for (int i = 0; i < 7; i++) {

                    DayOfWeek currentDay = localDate.getDayOfWeek();
                    if (isWeekDay(currentDay)) {
                        tables.get(i).setItems(TIME_INTERVALS);
                    } else if (currentDay == DayOfWeek.FRIDAY) {
                        dayNoWork = i;
                    } else {
                        daysOff.add(i);
                    }

                    columns.get(i).setText(localDate.format(formatter) +
                            " " +
                            currentDay.getDisplayName(TextStyle.FULL, Locale.US));
                    localDate = localDate.plusDays(1);
                }

                insertNoWorkDayRectangle(dayNoWork, daysOff);
                insertNoWorkDayLabel(dayNoWork, daysOff);


                try {
                    String nameOfDoctor = boxSpecialist.getValue();
                    ResultSet resultSet = executor.getDoctorByName(statement, nameOfDoctor);
                    if (resultSet.next()) {
                        imageSex.setVisible(true);
                        if (resultSet.getString("sex").equals("чоловічий")) {
                            imageSex.setImage(new Image(getClass().getResource("/icons/man.png").toExternalForm()));
                        } else {
                            imageSex.setImage(new Image(getClass().getResource("/icons/woman.png").toExternalForm()));
                        }

                        int doctorId = resultSet.getInt("id");
                        insertRecordButtons(executor, statement, calendar.getLocalDate(), doctorId);
                    }
                    labelNameOfDoctor.setText(nameOfDoctor);

                } catch (SQLException e) {
                    LOGGER.error(EXCEPTIONS_MARKER, "SQL exception", e);
                    throw new RuntimeException(e);
                }
            } else {
                LOGGER.warn(USER_MARKER, "To display the schedule, not all fields are selected");
            }
        });

        buttonExpand.setOnAction(actionEvent -> {
            setPictureOnExpandButton();
            resizeHBox();
        });

        buttonClear.setOnAction(actionEvent -> {
            clearData(tables, columns);

            try {
                fillBoxHospital(executor, statement);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            collapseHBox();
            buttonExpand.setDisable(true);
        });

        buttonLogOut.setOnAction(actionEvent -> logOut(tables, columns));
    }

    /**
     * Inserts rectangles that indicate about weekends.
     * @param dayNoWork dayNoWork
     * @param daysOff daysOff
     */
    // width: 119, height: 174, margin-left: 10 + 141 * i
    public void insertNoWorkDayRectangle(int dayNoWork, List<Integer> daysOff) {
        paneTables.getChildren().removeAll(rectangles);
        rectangles.clear();

        Rectangle rectangleBlue = new Rectangle(119, 174);
        rectangleBlue.getStyleClass().addAll("non-work-time-rectangle", "shadow-rectangle");
        AnchorPane.setLeftAnchor(rectangleBlue, 10.0 + dayNoWork * 141);
        AnchorPane.setTopAnchor(rectangleBlue, 35.0);
        rectangles.add(rectangleBlue);

        daysOff.forEach(number -> {
            Rectangle rectangleOrange = new Rectangle(119, 174);
            rectangleOrange.getStyleClass().addAll("day-off-rectangle", "shadow-rectangle");
            AnchorPane.setLeftAnchor(rectangleOrange, 10.0 + number * 141);
            AnchorPane.setTopAnchor(rectangleOrange, 35.0);
            rectangles.add(rectangleOrange);
        });

        paneTables.getChildren().addAll(rectangles);
    }

    /**
     * Inserts labels that indicate about weekends.
     * @param dayNoWork dayNoWork
     * @param daysOff daysOff
     */
    public void insertNoWorkDayLabel(int dayNoWork, List<Integer> daysOff) {
        paneTables.getChildren().removeAll(labels);
        labels.clear();

        // margin-left: 22 + 141 * i, margin-top: 40
        Label label = new Label("Не робочий час");
        label.getStyleClass().add("non-work-label");
        AnchorPane.setLeftAnchor(label, 22.0 + dayNoWork * 141);
        AnchorPane.setTopAnchor(label, 40.0);
        labels.add(label);

        // margin-left: 25 + 141 * i, margin-top: 40
        daysOff.forEach(number -> {
            Label labelNew = new Label("Вихідний день");
            labelNew.getStyleClass().add("non-work-label");
            AnchorPane.setLeftAnchor(labelNew, 25.0 + number * 141);
            AnchorPane.setTopAnchor(labelNew, 40.0);
            labels.add(labelNew);
        });

        paneTables.getChildren().addAll(labels);
    }

    /**
     * Inserts buttons that tell us about scheduled records.
     * If you click on any button, you will go to the window of the patient's data who arranged the appointment.
     * @param executor executor
     * @param statement statement
     * @param date date
     * @param doctorId doctor id
     * @throws SQLException an exception that provides information on a database access error or other errors.
     */
    // margin-left: 141 * i, margin-top: 25 + (Interval.getIntervalNumberByTime(Time time) - 1) * j
    public void insertRecordButtons(Executor executor, Statement statement, LocalDate date, int doctorId) throws SQLException {
        Map<DayOfWeek, Map<Patient, List<String>>> patients = new TreeMap<>();
        String doctor = "";
        paneTables.getChildren().removeAll(buttons);
        buttons.clear();

        for (int i = 0; i < 7; i++) {
            DayOfWeek currentDayOfWeek = date.getDayOfWeek();
            if (isWeekDay(currentDayOfWeek)) {
                ArrayList<Time[]> records = new ArrayList<>();
                ArrayList<Integer> patientsIdOnDay = new ArrayList<>();

                ResultSet doctorSet = executor.getDoctorById(statement, doctorId);
                while(doctorSet.next()) {
                    doctor = doctorSet.getString("name");
                }

                ResultSet resultSet = executor.getRecordsOnDay(statement, doctorId, date);
                while (resultSet.next()) {
                    patientsIdOnDay.add(resultSet.getInt("patient_id"));

                    Time srcDate = resultSet.getTime(4, Calendar.getInstance(TimeZone.getDefault()));
                    Time dstDate = resultSet.getTime(5, Calendar.getInstance(TimeZone.getDefault()));
                    records.add(new Time[] {srcDate, dstDate});
                }

                for (int j = 0; j < records.size(); j++) {
                    Patient patient = Patient.getPatientById(executor, statement, patientsIdOnDay.get(j));

                    String startRecord = records.get(j)[0].toString();
                    String endRecord = records.get(j)[1].toString();

                    startRecord = startRecord.substring(0, startRecord.length() - 3);
                    endRecord = endRecord.substring(0, endRecord.length() - 3);

                    Button button = new Button(startRecord + " - " + endRecord);
                    button.getStyleClass().add("record-button");

                    button.setOnAction(actionEvent -> {
                        try {
                            showPatientInfoWindow(actionEvent, patient);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });

                    if (!patients.containsKey(currentDayOfWeek)) {
                        patients.put(currentDayOfWeek, new HashMap<>());
                    }
                    patients.get(currentDayOfWeek).put(patient, new ArrayList<>(Arrays.asList(startRecord, endRecord)));

                    AnchorPane.setLeftAnchor(button, 141.0 * i);
                    AnchorPane.setTopAnchor(button, 25.0 + (Interval.getIntervalNumberByTime(records.get(j)[1]) - 1) * 24);
                    buttons.add(button);
                }
            }
            date = date.plusDays(1);
        }

        StringJoiner daysJoiner = new StringJoiner("\n");
        daysJoiner.add("Schedule of specialist " + doctor + " on " +
                date.minusDays(7).format(formatter) + " - " + date.format(formatter));
        patients.keySet().forEach(day -> {
            StringJoiner patientsJoiner = new StringJoiner(",\n\t ");
            patients.get(day).keySet().forEach(patient -> {
                patientsJoiner.add(patient.getName() + " - " + String.join(" : ", patients.get(day).get(patient)));
            });
            daysJoiner.add(day + ":\n\t " + patientsJoiner);
        });

        LOGGER.info(USER_MARKER, daysJoiner.toString());
        paneTables.getChildren().addAll(buttons);
    }

    /**
     * Fills the combo-box <strong>boxHospital</strong> with hospital in the database.
     * @param executor executor
     * @param statement statement
     * @throws SQLException an exception that provides information on a database access error or other errors.
     */
    public void fillBoxHospital(Executor executor, Statement statement) throws SQLException {
        ObservableList<Hospital> hospitals = Hospital.getHospitalList(executor, statement);
        boxHospital.setItems(hospitals);
    }

    /**
     * Fills the combo-box <strong>boxSpecialization</strong> with specialization that are accessible in specified hospital.
     * @param executor executor
     * @param statement statement
     * @param hospital hospital
     * @throws SQLException an exception that provides information on a database access error or other errors.
     */
    private void fillBoxSpecialization(Executor executor, Statement statement, Hospital hospital){
        try {
            ObservableList<Specialization> specializations;
            specializations = Specialization.getSpecializationsListByHospital(
                    executor,
                    statement,
                    hospital != null ? hospital.getName() : "");
            boxSpecialization.setItems(specializations);
        } catch (SQLException e) {
            LOGGER.error(EXCEPTIONS_MARKER, "Specialization in hospital \"{}\" is not found", hospital.getName());
            throw new RuntimeException(e);
        }
    }

    /**
     * Fills the combo-box <strong>boxSpecialist</strong> with doctors who have a concrete specialization and work in concrete hospital.
     * @param executor executor
     * @param statement statement
     * @param specialization specialization
     * @param hospital hospital
     * @throws SQLException an exception that provides information on a database access error or other errors.
     */
    private void fillBoxSpecialist(Executor executor, Statement statement, Specialization specialization, Hospital hospital) {
        try {
            ObservableList<String> doctors;
            doctors = Doctor.getDoctorsBySpecializationAndHospital(
                    executor,
                    statement,
                    specialization != null ? specialization.getName() : "",
                    hospital != null ? hospital.getName() : "");
            boxSpecialist.setItems(doctors);
        } catch (SQLException e) {
            LOGGER.error(EXCEPTIONS_MARKER, "Doctors with specialization \"{}\" in hospital \"{}\" is not found",
                    specialization.getName(), hospital.getName());
            throw new RuntimeException(e);
        }
    }

    /**
     * Expands the pane of schedule.
     */
    public void expandHBox() {
        HBoxSchedule.setVisible(true);
        HBoxSchedule.getScene().getWindow().setHeight(740);
    }

    /**
     * Collapse the pane of schedule.
     */
    public void collapseHBox() {
        HBoxSchedule.setVisible(false);
        HBoxSchedule.getScene().getWindow().setHeight(407);
    }

    /**
     * Resize the pane of schedule.
     */
    private void resizeHBox() {
        if (HBoxSchedule.isVisible()) {
            collapseHBox();
        } else {
            expandHBox();
        }
    }

    /**
     * Enables the window. It happens when root user logs in.
     */
    private void enableWindow() {
        paneButtons.setDisable(false);
        mainVBox.setDisable(false);
    }

    /**
     * Disable the window. It happens when root user logs out.
     */
    private void disableWindow() {
        paneButtons.setDisable(true);
        mainVBox.setDisable(true);
    }

    /**
     * Exchange arrows ↑ and ↓ in the expand button after clicking.
     */
    private void setPictureOnExpandButton() {
        if (!HBoxSchedule.isVisible()) {
            buttonExpand.setStyle("-fx-background-image: url(" +
                    getClass().getResource("/icons/collapse.png").toExternalForm() + ");");
        } else {
            buttonExpand.setStyle("-fx-background-image: url(" +
                    getClass().getResource("/icons/expand.png").toExternalForm() + ");");
        }
    }

    /**
     * Configures the program after logging in.
     * @param login login
     */
    void logIn(String login) {
        buttonAuthorization.setVisible(false);
        imageProfile.setVisible(true);
        buttonLogOut.setVisible(true);
        labelLogin.setVisible(true);
        labelLogin.setText(login);

        enableWindow();
    }

    /**
     * Configures the program after logging out.
     * @param tables tables
     * @param columns columns
     */
    void logOut(ArrayList<TableView> tables, ArrayList<TableColumn> columns) {
        String login = labelLogin.getText();
        buttonAuthorization.setVisible(true);
        imageProfile.setVisible(false);
        buttonLogOut.setVisible(false);
        labelLogin.setVisible(false);
        labelLogin.setText("");

        clearData(tables, columns);
        collapseHBox();
        buttonExpand.setDisable(true);
        disableWindow();

        LOGGER.info(AUTH_MARKER, "User {} logs out", login);
    }

    /**
     * Checks if a day is weekday.
     * @param day day
     * @return true or false
     */
    private boolean isWeekDay(DayOfWeek day) {
        return !(day == DayOfWeek.FRIDAY ||
                day == DayOfWeek.SATURDAY ||
                day == DayOfWeek.SUNDAY);
    }

    /**
     * Clear a data from the window.
     * @param tables tables
     * @param columns columns
     */
    private void clearData(ArrayList<TableView> tables, ArrayList<TableColumn> columns) {
        boxHospital.setItems(FXCollections.emptyObservableList());
        boxSpecialization.setItems(FXCollections.emptyObservableList());
        boxSpecialist.setItems(FXCollections.emptyObservableList());

        for (int i = 0; i < tables.size(); i++) {
            tables.get(i).setItems(FXCollections.emptyObservableList());
            columns.get(i).setText("No data");
        }

        paneTables.getChildren().removeAll(rectangles);
        paneTables.getChildren().removeAll(labels);
        paneTables.getChildren().removeAll(buttons);

        imageSex.setVisible(false);
        labelNameOfDoctor.setText("");
        imageHospital.setImage(hospitalIcon);

        LOGGER.info(USER_MARKER, "The data is cleared by user {}", labelLogin.getText());
    }

    /**
     * Launches the window of authorization.
     * @param actionEvent action event
     * @see AuthController
     * @throws IOException signals that an I/O exception of some sort has occurred.
     */
    // width: 663, height: 375
    @FXML
    private void showAuthWindow(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/auth.fxml"));
        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.setTitle("Вхід у систему");
        stage.setResizable(false);
        stage.getIcons().add(new Image(getClass().getResource("/icons/window.png").toExternalForm()));
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
        stage.show();

        authController = loader.getController();
        authController.setParent(this);
    }

    /**
     * Launches the window of table with patients.
     * @param actionEvent action event
     * @see TableController
     * @throws IOException signals that an I/O exception of some sort has occurred.
     */
    @FXML
    private void showTableWindow(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/patients.fxml"));
        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.setTitle("База даних пацієнтів");
        stage.setResizable(false);
        stage.getIcons().add(new Image(getClass().getResource("/icons/window.png").toExternalForm()));
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
        stage.show();
    }

    /**
     * Launches the window with recording a patient.
     * @param actionEvent action event
     * @see RecordController
     * @throws IOException signals that an I/O exception of some sort has occurred.
     */
    @FXML
    private void showRecordWindow(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/record.fxml"));
        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.setTitle("Запис на консультацію");
        stage.setResizable(false);
        stage.getIcons().add(new Image(getClass().getResource("/icons/window.png").toExternalForm()));
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
        stage.show();
    }

    /**
     * Launches the window of information about the patient.
     * @param actionEvent action event
     * @param patient patient
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     */
    private void showPatientInfoWindow(ActionEvent actionEvent, Patient patient) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/info.fxml"));
        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.setTitle("Інформація про пацієнта");
        stage.setResizable(false);
        stage.getIcons().add(new Image(getClass().getResource("/icons/window.png").toExternalForm()));
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
        stage.show();

        LOGGER.info(USER_MARKER, "Showing info about patient {}", patient.getName());
        patientController = loader.getController();
        patientController.setPatient(patient);
    }
}