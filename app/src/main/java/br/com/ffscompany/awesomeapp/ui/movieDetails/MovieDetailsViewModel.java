package br.com.ffscompany.awesomeapp.ui.movieDetails;

import android.widget.VideoView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.uwetrottmann.tmdb2.entities.Videos;

public class MovieDetailsViewModel extends ViewModel {

    private final MutableLiveData<String> title;

    private final MutableLiveData<String> overview;

    public MovieDetailsViewModel(String title, String overview) {
        this.title = new MutableLiveData<>();
        this.title.setValue(title);
        this.overview = new MutableLiveData<>();
        this.overview.setValue(overview);
    }

    public LiveData<String> getTitle() {
        return title;
    }

    public LiveData<String> getOverview() {
        return overview;
    }
}
