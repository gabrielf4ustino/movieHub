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

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.genres, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        RecyclerView gridGen = binding.genGrid;
        gridGen.setLayoutManager(new GridLayoutManager(getContext(), 4));

        loaderManager = LoaderManager.getInstance(getActivity());

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                int index = selectedItem.indexOf(':');
                String genreId = null;
                if (index != -1) {
                    genreId = selectedItem.substring(0, index);
                }
                String finalGenreId = genreId;
                loaderManager.initLoader(loaderId++, null, new LoaderManager.LoaderCallbacks<List<BaseMovie>>() {
                    @NonNull
                    @Override
                    public Loader<List<BaseMovie>> onCreateLoader(int id, @Nullable Bundle args) {
                        return new GenreTmdbService(getContext(), Integer.parseInt(finalGenreId));
                    }

                    @Override
                    public void onLoadFinished(@NonNull Loader<List<BaseMovie>> loader, List<BaseMovie> data) {
                        Log.d("DATA", data.get(1).title);
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
