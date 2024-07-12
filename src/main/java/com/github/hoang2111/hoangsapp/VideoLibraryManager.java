package com.github.hoang2111.hoangsapp;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class VideoLibraryManager {
    private List<com.github.hoang2111.hoangsapp.Video> userUploadedVideos;
    private List<com.github.hoang2111.hoangsapp.Video> preSelectedVideos;

    public VideoLibraryManager() {
        this.userUploadedVideos = new ArrayList<>();
        this.preSelectedVideos = new ArrayList<>();
        initializePreSelectedVideos();
    }

    private void initializePreSelectedVideos() {
        //preSelectedVideos.add(new Video("Stress Relief Meditation", "https://example.com/stress_relief.mp4", VideoTheme.STRESS_RELIEF));
        preSelectedVideos.add(new com.github.hoang2111.hoangsapp.Video("Sleep Meditation", "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4", VideoTheme.SLEEP));
        preSelectedVideos.add(new com.github.hoang2111.hoangsapp.Video("Sleep Meditation", "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4", VideoTheme.SLEEP));
        // Add more pre-selected videos
    }

    public void addUserUploadedVideo(com.github.hoang2111.hoangsapp.Video video) {
        userUploadedVideos.add(video);
    }

    public List<com.github.hoang2111.hoangsapp.Video> getVideosByTheme(VideoTheme theme) {
        return preSelectedVideos.stream().filter(video -> video.getTheme() == theme).collect(Collectors.toList());
    }

    public List<com.github.hoang2111.hoangsapp.Video> getUserUploadedVideos() {
        return userUploadedVideos;
    }
}