package br.com.ffscompany.moviehub.view.search;

import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.uwetrottmann.tmdb2.entities.BaseMovie;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import br.com.ffscompany.moviehub.R;
import br.com.ffscompany.moviehub.databinding.FragmentSearchBinding;
import br.com.ffscompany.moviehub.service.Options;
import br.com.ffscompany.moviehub.service.TmdbService;
import br.com.ffscompany.moviehub.view.search.card.MovieCardAdapter;

public class SearchFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<BaseMovie>> {

    private FragmentSearchBinding binding;

    private boolean isToastShown = false;

    private List<BaseMovie> allMovies = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LoaderManager.getInstance(this).initLoader(0, null, this).forceLoad();
        LoaderManager.getInstance(this).initLoader(1, null, this).forceLoad();
        LoaderManager.getInstance(this).initLoader(2, null, this).forceLoad();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @NonNull
    @Override
    public Loader<List<BaseMovie>> onCreateLoader(int id, @Nullable Bundle args) {
        switch (id) {
            case 0:
                return new TmdbService(requireContext(), Options.NOW_PLAYING);
            case 1:
                return new TmdbService(requireContext(), Options.POPULAR);
            case 2:
                return new TmdbService(requireContext(), Options.UP_COMING);
            default:
                // Retorna null caso o ID seja inválido
                return null;
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<BaseMovie>> loader, List<BaseMovie> movies) {
        allMovies.addAll(movies);
        if(loader.getId() == 2){
            renderMovies();
        }
    }

    private void renderMovies(){
        List<BaseMovie> uniqueList = new ArrayList<>();
        List<String> uniqueTitles = new ArrayList<>();
        for (BaseMovie movie : allMovies) {
            if (!uniqueTitles.contains(movie.title)) {
                uniqueTitles.add(movie.title);
                uniqueList.add(movie);
            }
        }

        RecyclerView moviesRecycler = binding.recyclerView;
        moviesRecycler.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        moviesRecycler.setAdapter(new MovieCardAdapter(getContext(), uniqueList, movie -> {
            Bundle args = new Bundle();
            assert movie.id != null;
            args.putInt("id", movie.id);
            args.putString("title", movie.title);
            args.putString("overview", movie.overview);
            args.putIntegerArrayList("genres", (ArrayList<Integer>) movie.genre_ids);
            args.putString("rating", String.valueOf(movie.vote_average));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                args.putString("release_date", String.valueOf(movie.release_date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getYear()));
            }

            assert getParentFragment() != null;
            NavHostFragment.findNavController(getParentFragment()).navigate(R.id.action_navigation_search_to_navigation_movie_details, args);
        }));

        SearchView search = binding.searchView;

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String searchText) {
                MovieCardAdapter adapter = (MovieCardAdapter) moviesRecycler.getAdapter();
                assert adapter != null;
                List<BaseMovie> movieSearch = searchMovies(searchText, uniqueList);
                adapter.setMovies(movieSearch);
                return true;
            }
        });
    }

    private List<BaseMovie> searchMovies(String text, List<BaseMovie> movies) {
        List<BaseMovie> movieSearch = new ArrayList<>();

        for (BaseMovie movie : movies) {
            assert movie.title != null;
            if (movie.title.toLowerCase().contains(text.toLowerCase()) || movie.original_title.toLowerCase().contains(text.toLowerCase())) {
                movieSearch.add(movie);
            }
        }
        if (movieSearch.isEmpty() && !isToastShown) {
            Toast toast = Toast.makeText(getContext(), "Não encontrado", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
            isToastShown = true;
            return new ArrayList<>();
        } else {
            isToastShown = false;
            return movieSearch;
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<BaseMovie>> loader) {
        loader.reset();
    }
}
