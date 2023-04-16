package br.com.ffscompany.awesomeapp.ui.home.recyclerView;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.uwetrottmann.tmdb2.entities.BaseMovie;

import java.util.List;

import br.com.ffscompany.awesomeapp.R;


public class MovieViewAdapter extends RecyclerView.Adapter<MovieViewHolder> {

    private final Context context;

    private final Fragment fragment;

    private final List<BaseMovie> movies;

    public MovieViewAdapter(Context context,@NonNull Fragment fragment, List<BaseMovie> movies) {
        this.context = context;
        this.movies = movies;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_image, parent, false);
        return new MovieViewHolder(view, fragment);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {

        Glide.with(context).load("https://image.tmdb.org/t/p/w500" + movies.get(position).poster_path).into(holder.getImageView());

        holder.onItemClick(movies.get(position));
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public interface OnClickItemListener {
        void onItemClick(BaseMovie movie);
    }
}
