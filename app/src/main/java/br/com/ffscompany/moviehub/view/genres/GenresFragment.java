package br.com.ffscompany.moviehub.view.genres;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.uwetrottmann.tmdb2.entities.BaseMovie;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.ffscompany.moviehub.R;
import br.com.ffscompany.moviehub.databinding.FragmentGenresBinding;
import br.com.ffscompany.moviehub.service.GenreTmdbService;
import br.com.ffscompany.moviehub.view.home.recyclerView.MovieViewAdapter;

public class GenresFragment extends Fragment {

    private FragmentGenresBinding binding;
    private int loaderId = 0;
    private LoaderManager loaderManager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGenresBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Spinner spinner = binding.genSpinner;
        List<SpinnerItem> itemList = new ArrayList<>(Arrays.asList(
                new SpinnerItem(28, "Ação"),
                new SpinnerItem(12, "Aventura"),
                new SpinnerItem(16, "Animação"),
                new SpinnerItem(35, "Comédia"),
                new SpinnerItem(80, "Crime"),
                new SpinnerItem(99, "Documentário"),
                new SpinnerItem(18, "Drama"),
                new SpinnerItem(10751, "Família"),
                new SpinnerItem(14, "Fantasia"),
                new SpinnerItem(36, "História"),
                new SpinnerItem(27, "Terror"),
                new SpinnerItem(10402, "Música"),
                new SpinnerItem(9648, "Mistério"),
                new SpinnerItem(10749, "Romance"),
                new SpinnerItem(878, "Ficção científica"),
                new SpinnerItem(10770, "Filme para TV"),
                new SpinnerItem(53, "Suspense"),
                new SpinnerItem(10752, "Guerra"),
                new SpinnerItem(37, "Ocidental")
        ));


        ArrayAdapter<SpinnerItem> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, itemList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        RecyclerView gridGen = binding.genGrid;
        gridGen.setLayoutManager(new GridLayoutManager(getContext(), 4));

        loaderManager = LoaderManager.getInstance(getActivity());

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SpinnerItem selectedItem = (SpinnerItem) parent.getItemAtPosition(position);
                loaderManager.initLoader(loaderId++, null, new LoaderManager.LoaderCallbacks<List<BaseMovie>>() {
                    @NonNull
                    @Override
                    public Loader<List<BaseMovie>> onCreateLoader(int id, @Nullable Bundle args) {
                        return new GenreTmdbService(getContext(), selectedItem.getId());
                    }

                    @Override
                    public void onLoadFinished(@NonNull Loader<List<BaseMovie>> loader, List<BaseMovie> data) {
                        RecyclerView upComingMovies = binding.genGrid;
                        upComingMovies.setLayoutManager(new GridLayoutManager(getContext(), 4));
                        upComingMovies.setAdapter(new MovieViewAdapter(getContext(), data, movie -> navigate(movie, R.id.action_navigation_genres_to_navigation_movie_details)));
                    }

                    @Override
                    public void onLoaderReset(@NonNull Loader<List<BaseMovie>> loader) {
                        loader.reset();
                    }
                }).forceLoad();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        return root;
    }

    private void navigate(BaseMovie movie, int id) {
        Bundle args = new Bundle();
        args.putInt("id", movie.id);
        args.putString("title", movie.title);
        args.putString("overview", movie.overview);
        args.putIntegerArrayList("genres", (ArrayList<Integer>) movie.genre_ids);
        args.putString("rating", String.valueOf(movie.vote_average));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            args.putString("release_date", String.valueOf(movie.release_date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getYear()));
        }
        assert getParentFragment() != null;
        NavHostFragment.findNavController(getParentFragment()).navigate(id, args);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
