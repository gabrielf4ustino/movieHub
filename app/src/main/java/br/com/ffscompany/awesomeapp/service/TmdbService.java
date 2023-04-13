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

    @Override
    protected List<BaseMovie> doInBackground(String... option) {
        Tmdb tmdb = new Tmdb(API_KEY);
        switch (option[0]) {
            case "nowPlaying":
                try {
                    Response<MovieResultsPage> response = tmdb.moviesService().nowPlaying(null, null, null).execute();
                    if (response.isSuccessful()) {
                        return response.body().results;
                    } else {
                        Log.e("TMDbApiClient", response.errorBody().string());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "popular":
                try {
                    Response<MovieResultsPage> response = tmdb.moviesService().popular(null, null, null).execute();
                    if (response.isSuccessful()) {
                        return response.body().results;
                    } else {
                        Log.e("TMDbApiClient", response.errorBody().string());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
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

