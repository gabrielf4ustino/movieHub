package br.com.ffscompany.awesomeapp.ui.notifications;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationBarView;
import com.uwetrottmann.tmdb2.entities.BaseMovie;
import com.uwetrottmann.tmdb2.entities.Genre;

import java.util.List;

import br.com.ffscompany.awesomeapp.R;
import br.com.ffscompany.awesomeapp.databinding.FragmentNotificationsBinding;
import br.com.ffscompany.awesomeapp.service.Options;
import br.com.ffscompany.awesomeapp.service.TmdbService;
import br.com.ffscompany.awesomeapp.ui.search.card.MovieCardAdapter;

public class NotificationsFragment extends Fragment  implements LoaderManager.LoaderCallbacks<List<BaseMovie>>{

    private FragmentNotificationsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Spinner spinner = binding.genSpinner;

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.generos, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        RecyclerView gridGen = binding.genGrid;
        gridGen.setLayoutManager(new GridLayoutManager(getContext(),4));
        Genre gender = new Genre();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                //LoaderManager.getInstance(this).initLoader(0, null, new MovieCardAdapter.OnItemClickListener()).forceLoad();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @NonNull
    @Override
    public Loader<List<BaseMovie>> onCreateLoader(int id, @Nullable Bundle args) {
        Spinner spinner = binding.genSpinner;
        //return new TmdbService(requireContext(), Options.GENRE,0);
        return null;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<BaseMovie>> loader, List<BaseMovie> movies) {

    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<BaseMovie>> loader) {
        loader.reset();
    }
}
