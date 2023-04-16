package br.com.ffscompany.awesomeapp.ui.search.card;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.uwetrottmann.tmdb2.entities.BaseMovie;

import java.util.List;

import br.com.ffscompany.awesomeapp.R;
import lombok.NonNull;

public class MovieCardAdapter extends RecyclerView.Adapter<MovieCardViewHolder> {

    private Context context;

    private List<BaseMovie> movies;

    private OnItemClickListener listener;

    public MovieCardAdapter(Context context , List<BaseMovie> movies, OnItemClickListener listener){
        this.context = context;
        this.movies = movies;
        this.listener = listener;
    }
    @NonNull
    @Override
    public MovieCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.movie_info , parent ,false);
        return new MovieCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieCardViewHolder holder, int position) {
        BaseMovie movie = movies.get(position);

        holder.setTitle(movie.title);
        holder.setOverview(movie.overview);

        Glide.with(context).load("https://image.tmdb.org/t/p/w500" + movies.get(position).poster_path).into(holder.getPosterImage());

        holder.getMovieInfo().setOnClickListener(v -> listener.onItemClick(movies.get(position)));
    }

    public void setMovies(List<BaseMovie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return  movies.size();
    }

    public interface OnItemClickListener {
        void onItemClick(BaseMovie movie);
    }
}
