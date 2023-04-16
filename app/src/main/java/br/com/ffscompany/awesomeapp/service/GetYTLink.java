package br.com.ffscompany.awesomeapp.service;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;
import com.google.api.services.youtube.model.VideoPlayer;

import java.io.IOException;
import java.util.List;

public class GetYTLink extends AsyncTaskLoader<VideoPlayer> {

    private String videoId;

    public GetYTLink(@NonNull Context context, String videoId) {
        super(context);
        this.videoId = videoId;
    }

    @Nullable
    @Override
    public VideoPlayer loadInBackground() {
        VideoPlayer videoUrl;
        try {
            YouTube youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(),
                    request -> {
                    }).setApplicationName("AwesomeApp").build();

            YouTube.Videos.List request = youtube.videos().list("player");
            request.setId(videoId);
            request.setKey("AIzaSyDXbCKYf5Uh8bTgHIC66fhQPHtmVs6aRhE");
            VideoListResponse response = request.execute();

            List<Video> videos = response.getItems();
            videoUrl = videos.get(0).getPlayer();
            return videoUrl;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }
}
