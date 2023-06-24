package br.com.ffscompany.moviehub.view.home.recyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.uwetrottmann.tmdb2.entities.BaseMovie;

import java.util.List;

import br.com.ffscompany.moviehub.R;


public class MovieViewAdapter extends RecyclerView.Adapter<MovieViewHolder> {

    private final Context context;

    private final List<BaseMovie> movies;

    private OnItemClickListener listener;

    public MovieViewAdapter(Context context, List<BaseMovie> movies, OnItemClickListener listener) {
        this.context = context;
        this.movies = movies;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_image, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {

        Glide.with(context).load("https://image.tmdb.org/t/p/w500" + movies.get(position).poster_path).into(holder.getImageView());

        holder.getImageView().setOnClickListener(v -> listener.onItemClick(movies.get(position)));
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public interface OnItemClickListener {
        void onItemClick(BaseMovie movie);
    }
}