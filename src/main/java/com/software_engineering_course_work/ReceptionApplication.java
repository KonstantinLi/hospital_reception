package com.software_engineering_course_work;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * The {@link Application}-inherited class, that launch the main window.
 * @author Linenko Kostyantyn
 * @version 1.0
 */
public class ReceptionApplication extends Application {
    /**
     * Connects <strong>.fxml</strong> file that writes component hierarchy in GUI.
     * The main feature of this file is the separation of a visual component of the program
     * from functionality. It leads to realization of pattern MVC.
     * @param stage stage
     * @see FXMLLoader
     * @see Stage
     * @see Scene
     * @throws IOException signals that an I/O exception of some sort has occurred.
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/schedule.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Розклад консультацій");
        stage.setScene(scene);
        stage.getIcons().add(new Image(getClass().getResource("/icons/window.png").toExternalForm()));
        stage.setHeight(407);
        stage.show();
    }

    /**
     * The main method of Java project.
     * @param args args
     */
    public static void main(String[] args) {
        launch();
    }
}