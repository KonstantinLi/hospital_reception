/**
 * Provides the graphic user interface.
 */
module com.software_engineering.course_work.graphic_interface {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires jfxtras.controls;
    requires org.apache.logging.log4j;
    requires org.apache.logging.log4j.core;

    opens com.software_engineering_course_work to javafx.fxml;
    exports com.software_engineering_course_work;
    opens com.software_engineering_course_work.controllers to javafx.fxml;
    exports com.software_engineering_course_work.controllers;
    exports com.software_engineering_course_work.model;
    opens com.software_engineering_course_work.model to javafx.fxml;
    opens com.software_engineering_course_work.database to javafx.fxml;
    exports com.software_engineering_course_work.database;
}