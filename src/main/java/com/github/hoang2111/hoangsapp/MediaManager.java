package com.github.hoang2111.hoangsapp;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

import java.io.File;

public class MediaManager {
    private MediaPlayer mediaPlayer;
    private MediaView mediaView;
    private Stage primaryStage;

    public MediaManager(Stage primaryStage) {
        this.primaryStage = primaryStage;
        mediaView = new MediaView();
    }

    public MediaView getMediaView() {
        return mediaView;
    }

    public void uploadMedia(File mediaFile) {
        if (mediaPlayer != null) {
            mediaPlayer.dispose();
        }
        Media media = new Media(mediaFile.toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaView.setMediaPlayer(mediaPlayer);
    }

    public void uploadMedia(String mediaUrl) {
        if (mediaPlayer != null) {
            mediaPlayer.dispose();
        }
        Media media = new Media(mediaUrl);
        mediaPlayer = new MediaPlayer(media);
        mediaView.setMediaPlayer(mediaPlayer);
    }

    public void playMedia() {
        if (mediaPlayer != null) {
            mediaPlayer.play();
        }
    }

    public void stopMedia() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    public void setBackgroundMusic(String musicFileUri) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        Media media = new Media(musicFileUri);
        mediaPlayer = new MediaPlayer(media);
    }

    public void playBackgroundMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.play();
        }
    }
}