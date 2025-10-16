package com.farzan;
import javafx.scene.text.Font;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/ui/main.fxml")));
        Scene scene = new Scene(root, 900, 640);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/theme.css")).toExternalForm());
        Font.loadFont(Objects.requireNonNull(getClass().getResource("/fonts/TwemojiMozilla.ttf")).toExternalForm(), 14);
        stage.setTitle("ALICE - AI Powered Intelligent Chatbot");
        stage.setScene(scene);

        // ----- Smart sizing: use the visual bounds so taskbar is accounted for -----
        Rectangle2D visual = javafx.stage.Screen.getPrimary().getVisualBounds();

        // desired initial size (you can adjust these)
        double preferredWidth = 900;
        double preferredHeight = 620;

        // ensure preferred size doesn't exceed available screen area
        double initWidth = Math.min(preferredWidth, visual.getWidth() * 0.95);
        double initHeight = Math.min(preferredHeight, visual.getHeight() * 0.92);

        stage.setWidth(initWidth);
        stage.setHeight(initHeight);

        // sensible minimum so the UI doesn't collapse
        stage.setMinWidth(700);
        stage.setMinHeight(500);

        // Make sure it's not accidentally maximized by prior run
        stage.setMaximized(false);

        // center within the visual bounds (not full screen)
        stage.setX(visual.getMinX() + (visual.getWidth() - initWidth) / 2);
        stage.setY(visual.getMinY() + (visual.getHeight() - initHeight) / 2);

        // allow user resizing
        stage.setResizable(true);

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
