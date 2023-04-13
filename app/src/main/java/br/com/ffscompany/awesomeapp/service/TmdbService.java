package br.com.ffscompany.awesomeapp.service;

import android.os.AsyncTask;
import android.util.Log;

import com.uwetrottmann.tmdb2.Tmdb;
import com.uwetrottmann.tmdb2.entities.BaseMovie;
import com.uwetrottmann.tmdb2.entities.MovieResultsPage;

import java.io.IOException;
import java.util.List;

import retrofit2.Response;

public class TmdbService extends AsyncTask<String, Void, List<BaseMovie>> {
    private static final String API_KEY = "c7dd226ddb8f77f0536becda18f3c4fb";

    private final String option;

    public TmdbService(String option) {
        this.option = option;
    }

    @Override
    protected List<BaseMovie> doInBackground(String... voids) {
        Tmdb tmdb = new Tmdb(API_KEY);
        Response<MovieResultsPage> response = null;
        switch (this.option) {
            case "nowPlaying":
                try {
                    response = tmdb.moviesService().nowPlaying(null, null, null).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "popular":
                try {
                    response = tmdb.moviesService().popular(null, null, null).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        if (response.isSuccessful()) {
            return response.body().results;
        } else {
            try {
                Log.e("TMDbApiClient", response.errorBody().string());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<BaseMovie> movies) {
        if (movies != null) {
        }
    }
}

