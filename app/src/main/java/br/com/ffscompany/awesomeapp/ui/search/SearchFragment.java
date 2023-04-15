package br.com.ffscompany.awesomeapp.ui.search;

import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.uwetrottmann.tmdb2.entities.BaseMovie;
import com.uwetrottmann.tmdb2.entities.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import br.com.ffscompany.awesomeapp.R;
import br.com.ffscompany.awesomeapp.databinding.FragmentHomeBinding;
import br.com.ffscompany.awesomeapp.databinding.FragmentSearchBinding;
import br.com.ffscompany.awesomeapp.service.Options;
import br.com.ffscompany.awesomeapp.service.TmdbService;
import br.com.ffscompany.awesomeapp.ui.search.card.MovieCardAdapter;
import retrofit2.Retrofit;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<BaseMovie>>{

    private FragmentSearchBinding binding;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LoaderManager.getInstance(this).initLoader(0, null, this).forceLoad();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
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
        return new TmdbService(requireContext(), Options.POPULAR);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<BaseMovie>> loader, List<BaseMovie> movies) {
        RecyclerView recycler = binding.recyclerView;
        recycler.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,false));
        recycler.setAdapter(new MovieCardAdapter(getContext(), movies));
        SearchView search = binding.searchView;
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                MovieCardAdapter adapter = (MovieCardAdapter) recycler.getAdapter();
                adapter.setMovies(searchMovies(newText,movies));
                adapter.notifyDataSetChanged();
                return true;
            }
        });
    }
    @Override
    public void onLoaderReset(@NonNull Loader<List<BaseMovie>> loader) {
        loader.reset();
    }
    private List<BaseMovie> searchMovies(String text, List<BaseMovie> movies)
    {
        List<BaseMovie> movieSearch = new ArrayList<>();

        for(BaseMovie movie : movies){
            if(movie.original_title.toLowerCase().contains(text.toLowerCase())){
                movieSearch.add(movie);
            }
        }
        if(movieSearch.isEmpty()){
            Toast.makeText(getActivity(), "No movies found", Toast.LENGTH_SHORT).show();
            return movieSearch;
        }
        else{
            return movieSearch;
        }
    }
}
