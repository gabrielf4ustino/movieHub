package br.com.ffscompany.awesomeapp.ui.search.card;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.uwetrottmann.tmdb2.entities.BaseMovie;
import com.uwetrottmann.tmdb2.entities.Movie;

import java.util.List;

import br.com.ffscompany.awesomeapp.R;
import lombok.NonNull;

public class MovieCardAdapter extends RecyclerView.Adapter<MovieCardViewHolder> {


    private Context context;
    private List<BaseMovie> movies;

    public void setSearchMovies(List<BaseMovie> searchMovies){
        this.movies = searchMovies;
        notifyDataSetChanged();
    }
    public MovieCardAdapter(Context context , List<BaseMovie> movies){
        this.context = context;
        this.movies = movies;
    }
    @androidx.annotation.NonNull
    @Override
    public MovieCardViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.movie_info , parent ,false);
        return new MovieCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieCardViewHolder holder, int position) {
        BaseMovie movie = movies.get(position);

        holder.setTitle(movie.original_title);
        holder.setOverview(movie.overview);

        Glide.with(context).load("https://image.tmdb.org/t/p/w500" + movies.get(position).poster_path).into(holder.getPosterImage());

    }
    public void setMovies(List<BaseMovie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return  movies.size();
    }

    public class MovieHolder extends RecyclerView.ViewHolder{

        public ImageView imageView;
        public TextView title, overview;
        public MovieHolder(@NonNull View itemView){
            super(itemView);
                imageView = itemView.findViewById(R.id.imageView);
                title = itemView.findViewById(R.id.title);
                overview = itemView.findViewById(R.id.overview);
            }
    }
}
