package ba.unsa.etf.rpr.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.io.IOException;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(ApplicationExtension.class)
public class LogInControllerTest {

    @Start
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
        stage.setTitle("Log in");
        stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        stage.setResizable(false);
        //primaryStage.setMinHeight(490);
        //primaryStage.setMinWidth(740);
        stage.show();
        stage.toFront();
    }

    @Test
    void loginBtnClickTest(FxRobot robot) {
        Button loginButton = robot.lookup("#loginBtn").queryAs(Button.class);
        assertNotNull(loginButton);
        robot.clickOn("#fieldUsername");
        robot.write("neko123");
        robot.clickOn("#fieldPassword");
        robot.write("nekooo");
        robot.clickOn("#loginBtn");

        robot.lookup("#userNameLabel").tryQuery().isPresent();
        Label userFullName = robot.lookup("#userNameLabel").queryAs(Label.class);
        assertNotNull(userFullName);
    }
}
