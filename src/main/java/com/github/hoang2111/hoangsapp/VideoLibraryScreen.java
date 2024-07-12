package com.github.hoang2111.hoangsapp;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

public class VideoLibraryScreen {

    private Stage primaryStage;
    private com.github.hoang2111.hoangsapp.VideoLibraryManager videoLibraryManager;

    public VideoLibraryScreen(Stage primaryStage, com.github.hoang2111.hoangsapp.VideoLibraryManager videoLibraryManager) {
        this.primaryStage = primaryStage;
        this.videoLibraryManager = videoLibraryManager;
    }

    public void showVideoLibraryScreen() {
        TabPane tabPane = new TabPane();

        Tab userUploadedTab = new Tab("User Uploaded Videos");
        ListView<com.github.hoang2111.hoangsapp.Video> userUploadedListView = new ListView<>();
        userUploadedListView.getItems().addAll(videoLibraryManager.getUserUploadedVideos());
        userUploadedTab.setContent(userUploadedListView);

        Tab preSelectedTab = new Tab("Pre-Selected Videos by Theme");
        ListView<com.github.hoang2111.hoangsapp.Video> preSelectedListView = new ListView<>();
        for (VideoTheme theme : VideoTheme.values()) {
            List<com.github.hoang2111.hoangsapp.Video> videos = videoLibraryManager.getVideosByTheme(theme);
            Label themeLabel = new Label(theme.name());
            preSelectedListView.getItems().addAll(videos);
        }
        preSelectedTab.setContent(preSelectedListView);

        tabPane.getTabs().addAll(userUploadedTab, preSelectedTab);

        VBox layout = new VBox(10);
        Button backButton = new Button("Back");
        MediaView mediaView = new MediaView();

        backButton.setOnAction(e -> primaryStage.setScene(MainApp.getStartupScene()));
        userUploadedListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                playVideo(newSelection.getFilePathOrUrl(), mediaView);
            }
        });
        preSelectedListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                playVideo(newSelection.getFilePathOrUrl(), mediaView);
            }
        });

        Button uploadButton = new Button("Upload Video");
        uploadButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Video Files", "*.mp4"));
            File selectedFile = fileChooser.showOpenDialog(primaryStage);
            if (selectedFile != null) {
                String title = selectedFile.getName();
                com.github.hoang2111.hoangsapp.Video video = new com.github.hoang2111.hoangsapp.Video(title, selectedFile.toURI().toString(), VideoTheme.GENERAL);
                videoLibraryManager.addUserUploadedVideo(video);
                userUploadedListView.getItems().add(video);
            }
        });

        layout.getChildren().addAll(tabPane, mediaView, uploadButton, backButton);
        Scene scene = new Scene(layout, 800, 600);
        primaryStage.setScene(scene);
    }

    private void playVideo(String filePathOrUrl, MediaView mediaView) {
        Media media = new Media(filePathOrUrl);
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaView.setMediaPlayer(mediaPlayer);
        mediaPlayer.play();
    }
}
