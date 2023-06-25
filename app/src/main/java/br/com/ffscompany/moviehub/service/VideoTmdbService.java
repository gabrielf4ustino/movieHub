package br.com.ffscompany.moviehub.service;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import com.uwetrottmann.tmdb2.Tmdb;
import com.uwetrottmann.tmdb2.entities.Videos;
import com.uwetrottmann.tmdb2.enumerations.VideoType;

import java.io.IOException;
import java.util.List;

import retrofit2.Response;

public class VideoTmdbService extends AsyncTaskLoader<String> {
    private static final Tmdb tmdb = new Tmdb("c32957f584b399b761627b576a78e213");

    private final Integer movieId;

    public VideoTmdbService(@NonNull Context context, Integer movieId) {
        super(context);
        this.movieId = movieId;
    }

    @Nullable
    @Override
    public String loadInBackground() {
        try {
            Response<Videos> response = tmdb.moviesService().videos(movieId, "pt-BR").execute();
            if (response.isSuccessful()) {
                List<Videos.Video> videos = response.body().results;
                for (Videos.Video video : videos) {
                    if (video.type.equals(VideoType.TRAILER) && video.site.equals("YouTube")) {
                        return video.key;
                    }
                }
            } else {
                try {
                    Log.e("TMDbApiClient", response.errorBody().string());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
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
