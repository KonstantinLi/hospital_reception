package com.software_engineering_course_work.controllers;

import com.software_engineering_course_work.model.Patient;
import com.software_engineering_course_work.model.Sex;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import java.time.format.DateTimeFormatter;

/**
 * The controller that implements logic for the window of the patient's data.
 * @author Linenko Kostyantyn
 * @version 1.0
 */
public class PatientController {

    /**
     * Patient object.
     */
    private Patient patient;
    /** The field of address. */
    @FXML
    private TextField fieldAddress;
    /** The field of birthday. */
    @FXML
    private TextField fieldBirthday;
    /** The field of email. */
    @FXML
    private TextField fieldEmail;
    /** The field of phone number. */
    @FXML
    private TextField fieldPhone;
    /** The image of gender icon. */
    @FXML
    private ImageView imageSex;
    /** The label of name. */
    @FXML
    private Label labelName;
    /** The label of patient. */
    @FXML
    private Label labelPatient;
    /** The clickable image, that provides copying the address to the clipboard. */
    @FXML
    private ImageView copyAddress;
    /** The clickable image, that provides copying the birthday to the clipboard. */
    @FXML
    private ImageView copyBirthday;
    /** The clickable image, that provides copying the email to the clipboard. */
    @FXML
    private ImageView copyEmail;
    /** The clickable image, that provides copying the phone number to the clipboard. */
    @FXML
    private ImageView copyPhone;
    /** The button that shows the data of the patient. */
    @FXML
    private Button buttonGetData;

    /**
     * Launch the initial configuration of the window.
     * Formats the phone number in more readable format.
     * After clicking on the button <strong>buttonGetData</strong>
     * window shows all data of concrete patient.
     * This window opens when user click on button of record in schedule of the doctor.
     */
    @FXML
    public void initialize() {

        buttonGetData.setOnAction(actionEvent -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            String oldPhone = patient.getPhone();
            String newPhone = "+38 (" + oldPhone.substring(0, 3)
                    + ")-" + oldPhone.substring(3, 6) + "-" + oldPhone.substring(6);

            labelPatient.setText("Пацієнт –");
            labelName.setText(patient.getName());
            fieldPhone.setText(newPhone);
            fieldAddress.setText(patient.getAddress());
            fieldEmail.setText(patient.getEmail());
            fieldBirthday.setText(patient.getDateBirth().toLocalDate().format(formatter));

            if (patient.getSex() == Sex.MALE) {
                imageSex.setImage(new Image(getClass().getResource("/icons/man.png").toExternalForm()));
            } else {
                imageSex.setImage(new Image(getClass().getResource("/icons/woman.png").toExternalForm()));
            }

            copyPhone.setOnMouseClicked(mouseEvent -> setClipBoard(fieldPhone.getText()));
            copyAddress.setOnMouseClicked(mouseEvent -> setClipBoard(fieldAddress.getText()));
            copyEmail.setOnMouseClicked(mouseEvent -> setClipBoard(fieldEmail.getText()));
            copyBirthday.setOnMouseClicked(mouseEvent -> setClipBoard(fieldBirthday.getText()));
        });

    }

    /**
     * Set the patient of this window.
     * @see ReceptionController
     * @param patient patient
     */
    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    /**
     * Adds the copied field to the clipboard.
     * @param copy copied string
     */
    private void setClipBoard(String copy) {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(copy);
        clipboard.setContent(content);
    }
}
