module com.example.reversesoundfile {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.reversesoundfile to javafx.fxml;
    exports com.example.reversesoundfile;
}