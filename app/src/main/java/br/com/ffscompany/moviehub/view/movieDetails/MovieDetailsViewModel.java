package br.com.ffscompany.moviehub.view.movieDetails;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MovieDetailsViewModel extends ViewModel {

    private final MutableLiveData<String> title;

    private final MutableLiveData<String> overview;

    private final MutableLiveData<String> rating;

    private final MutableLiveData<String> releaseDate;

    public MovieDetailsViewModel(String title, String overview, String rating, String releaseDate) {
        this.title = new MutableLiveData<>();
        this.title.setValue(title);

        this.overview = new MutableLiveData<>();
        this.overview.setValue(overview);

        this.rating = new MutableLiveData<>();
        this.rating.setValue(rating);

        this.releaseDate = new MutableLiveData<>();
        this.releaseDate.setValue(releaseDate);
    }

    public LiveData<String> getTitle() {
        return title;
    }

    public LiveData<String> getOverview() {
        return overview;
    }

    public LiveData<String> getRating() {
        return rating;
    }

    public LiveData<String> getReleaseDate() {
        return releaseDate;
    }
}
