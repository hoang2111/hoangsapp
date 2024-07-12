package com.github.hoang2111.hoangsapp;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;

public class SessionManager {

    private Stage primaryStage;
    private com.github.hoang2111.hoangsapp.MediaManager mediaManager;
    private com.github.hoang2111.hoangsapp.DatabaseManager databaseManager;
    private MainApp mainApp;
    private VideoLibraryManager videoLibraryManager;
    private File selectedMediaFile;
    private int sessionDuration = 5; // default duration in minutes
    private String reminderMessage = "Time for your meditation!";
    private int intervalDuration = 30; // default interval in minutes

    public SessionManager(Stage primaryStage, com.github.hoang2111.hoangsapp.MediaManager mediaManager, com.github.hoang2111.hoangsapp.DatabaseManager databaseManager, MainApp mainApp, VideoLibraryManager videoLibraryManager) {
        this.primaryStage = primaryStage;
        this.mediaManager = mediaManager;
        this.databaseManager = databaseManager;
        this.mainApp = mainApp;
        this.videoLibraryManager = videoLibraryManager;
    }

    public void startSession(String sessionType) {
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        Button uploadButton = new Button("Upload Media");
        Button playButton = new Button("Play");
        Button stopButton = new Button("Stop");
        Button backButton = new Button("Back");
        Button selectPreSelectedButton = new Button("Select Pre-Selected Sources");
        Label sessionLabel = new Label("Session Type: " + sessionType);

        uploadButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Media Files", "*.mp4"));
            selectedMediaFile = fileChooser.showOpenDialog(primaryStage);
            if (selectedMediaFile != null) {
                mediaManager.uploadMedia(selectedMediaFile);
            }
        });

        playButton.setOnAction(e -> {
            mediaManager.playMedia();
            startMeditationTimer();
        });

        stopButton.setOnAction(e -> mediaManager.stopMedia());
        backButton.setOnAction(e -> primaryStage.setScene(MainApp.getStartupScene()));
        selectPreSelectedButton.setOnAction(e -> showPreSelectedSources(sessionType));

        HBox buttonLayout = new HBox(10);
        buttonLayout.setAlignment(Pos.CENTER);
        buttonLayout.getChildren().addAll(uploadButton, selectPreSelectedButton, playButton, stopButton, backButton);
        buttonLayout.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(2))));
        buttonLayout.setPadding(new Insets(10));

        layout.getChildren().addAll(sessionLabel, mediaManager.getMediaView(), buttonLayout);

        Scene sessionScene = new Scene(layout, 600, 400);
        primaryStage.setScene(sessionScene);
    }

    private void startMeditationTimer() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.minutes(sessionDuration), e -> {
            mediaManager.stopMedia();
            showReminderMessage();
        }));
        timeline.setCycleCount(1);
        timeline.play();
    }

    private void showReminderMessage() {
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        Label messageLabel = new Label(reminderMessage);
        Button continueButton = new Button("Continue");

        continueButton.setOnAction(e -> startIntervalTimer());

        layout.getChildren().addAll(messageLabel, continueButton);
        Scene reminderScene = new Scene(layout, 300, 200);
        primaryStage.setScene(reminderScene);
    }

    private void startIntervalTimer() {
        PauseTransition pause = new PauseTransition(Duration.minutes(intervalDuration));
        pause.setOnFinished(e -> primaryStage.setScene(MainApp.getStartupScene()));
        pause.play();
    }

    private void showPreSelectedSources(String sessionType) {
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        Label sessionLabel = new Label("Select Pre-Selected " + sessionType + " Videos");

        ListView<Video> preSelectedListView = new ListView<>();

        // Filter videos based on session type
        VideoTheme theme;
        if (sessionType.equals("Meditation")) {
            theme = VideoTheme.SLEEP;
        } else if (sessionType.equals("Eye Exercise")) {
            theme = VideoTheme.EYE_EXERCISE;
        } else {
            theme = VideoTheme.GENERAL;
        }
        preSelectedListView.getItems().addAll(videoLibraryManager.getVideosByTheme(theme));

        preSelectedListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                String filePathOrUrl = newSelection.getFilePathOrUrl();
                if (filePathOrUrl.startsWith("http") || filePathOrUrl.startsWith("https")) {
                    mediaManager.uploadMedia(filePathOrUrl);
                } else {
                    mediaManager.uploadMedia(new File(filePathOrUrl));
                }
                mediaManager.playMedia();
                startMeditationTimer();
            }
        });

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> primaryStage.setScene(MainApp.getStartupScene()));

        layout.getChildren().addAll(sessionLabel, preSelectedListView, backButton);
        Scene scene = new Scene(layout, 600, 400);
        primaryStage.setScene(scene);
    }

    public int getSessionDuration() {
        return sessionDuration;
    }

    public void setSessionDuration(int sessionDuration) {
        this.sessionDuration = sessionDuration;
    }

    public String getReminderMessage() {
        return reminderMessage;
    }

    public void setReminderMessage(String reminderMessage) {
        this.reminderMessage = reminderMessage;
    }

    public int getIntervalDuration() {
        return intervalDuration;
    }

    public void setIntervalDuration(int intervalDuration) {
        this.intervalDuration = intervalDuration;
    }
}