package com.github.hoang2111.hoangsapp;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class SoundCustomizationManager {

    private Stage primaryStage;
    private com.github.hoang2111.hoangsapp.MediaManager mediaManager;
    private File selectedSoundFile;

    public SoundCustomizationManager(Stage primaryStage, com.github.hoang2111.hoangsapp.MediaManager mediaManager) {
        this.primaryStage = primaryStage;
        this.mediaManager = mediaManager;
    }

    public void showSoundCustomizationScreen() {
        VBox layout = new VBox(10);
        Button chooseSoundButton = new Button("Choose Sound");
        Label chosenSoundLabel = new Label("No sound selected");
        Button backButton = new Button("Back");

        chooseSoundButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("MP3 Files", "*.mp3"));
            selectedSoundFile = fileChooser.showOpenDialog(primaryStage);
            if (selectedSoundFile != null) {
                chosenSoundLabel.setText("Selected Sound: " + selectedSoundFile.getName());
                mediaManager.setBackgroundMusic(selectedSoundFile.toURI().toString());
                mediaManager.playBackgroundMusic();
            }
        });

        backButton.setOnAction(e -> primaryStage.setScene(MainApp.getStartupScene()));

        layout.getChildren().addAll(chooseSoundButton, chosenSoundLabel, backButton);

        Scene scene = new Scene(layout, 400, 300);
        primaryStage.setScene(scene);
    }
}