module com.example.cpsatadvexam {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.poi.poi;

    requires org.apache.poi.ooxml;
    //requires org.apache.poi.ooxml;
    requires org.apache.commons.compress;
    requires org.seleniumhq.selenium.firefox_driver;
    requires org.seleniumhq.selenium.chrome_driver;

    requires org.seleniumhq.selenium.api;

    requires org.testng;
    requires dev.failsafe.core;
    requires org.apache.commons.io;


    exports Utilities;

    opens com.example.cpsatadvexam to javafx.fxml;
    exports com.example.cpsatadvexam;
}