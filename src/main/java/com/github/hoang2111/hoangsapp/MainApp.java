package com.github.hoang2111.hoangsapp;

import javafx.application.Application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MainApp extends Application {

    private SessionManager sessionManager;
    private CustomizationManager customizationManager;
    private com.github.hoang2111.hoangsapp.AnalyticsManager analyticsManager;
    private ThemeCustomizationManager themeCustomizationManager;
    private SoundCustomizationManager soundCustomizationManager;
    private VideoLibraryScreen videoLibraryScreen;
    private static Scene startupScene;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("MeditationApp");

        com.github.hoang2111.hoangsapp.DatabaseManager databaseManager = new com.github.hoang2111.hoangsapp.DatabaseManager();
        databaseManager.createNewTable();  // Ensure the database table is created

        MediaManager mediaManager = new MediaManager(primaryStage);
        VideoLibraryManager videoLibraryManager = new VideoLibraryManager();

        sessionManager = new SessionManager(primaryStage, mediaManager, databaseManager, this, videoLibraryManager);
        customizationManager = new CustomizationManager(sessionManager, primaryStage);
        analyticsManager = new com.github.hoang2111.hoangsapp.AnalyticsManager(primaryStage, databaseManager, this);
        themeCustomizationManager = new ThemeCustomizationManager(primaryStage, createStartupScene(primaryStage));
        soundCustomizationManager = new SoundCustomizationManager(primaryStage, mediaManager);
        videoLibraryScreen = new VideoLibraryScreen(primaryStage, videoLibraryManager);

        startupScene = createStartupScene(primaryStage);
        primaryStage.setScene(startupScene);
        primaryStage.show();
    }

    private Scene createStartupScene(Stage primaryStage) {
        VBox startupLayout = new VBox(10);
        startupLayout.setAlignment(Pos.CENTER);
        Button meditateButton = new Button("Meditate");
        Button eyeExerciseButton = new Button("Eye Exercise");
        Button customizeButton = new Button("Settings");
        Button analyticsButton = new Button("Analytics");
        Button themeCustomizeButton = new Button("Customize Theme");
        Button soundCustomizeButton = new Button("Customize Sound");
        Button videoLibraryButton = new Button("Video Library");
        Button stopButton = new Button("Stop");

        meditateButton.setOnAction(e -> sessionManager.startSession("Meditation"));
        eyeExerciseButton.setOnAction(e -> sessionManager.startSession("Eye Exercise"));
        customizeButton.setOnAction(e -> customizationManager.showCustomizationScreen());
        analyticsButton.setOnAction(e -> analyticsManager.showAnalytics());
        themeCustomizeButton.setOnAction(e -> themeCustomizationManager.showThemeCustomizationScreen());
        soundCustomizeButton.setOnAction(e -> soundCustomizationManager.showSoundCustomizationScreen());
        videoLibraryButton.setOnAction(e -> videoLibraryScreen.showVideoLibraryScreen());
        stopButton.setOnAction(e -> primaryStage.close());

        HBox buttonLayout = new HBox(10);
        buttonLayout.setAlignment(Pos.CENTER);
        buttonLayout.getChildren().addAll(meditateButton, eyeExerciseButton, customizeButton, analyticsButton, themeCustomizeButton, soundCustomizeButton, stopButton);
        buttonLayout.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(2))));
        buttonLayout.setPadding(new Insets(10));
        startupLayout.getChildren().add(buttonLayout);
        return new Scene(startupLayout, 800, 500);
    }

    public static Scene getStartupScene() {
        return startupScene;
    }
}