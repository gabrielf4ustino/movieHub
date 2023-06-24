package br.com.ffscompany.moviehub.view.home.slider;

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


public class SliderViewAdapter extends RecyclerView.Adapter<SliderViewHolder> {

    private final Context context;

    private final List<BaseMovie> movies;

    private OnItemClickListener listener;

    public SliderViewAdapter(Context context, List<BaseMovie> movies, OnItemClickListener listener) {
        this.context = context;
        this.movies = movies;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.slider_image, parent, false);
        return new SliderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {

        Glide.with(context).load("https://image.tmdb.org/t/p/w500" + movies.get(position).backdrop_path).into(holder.getImageView());
        holder.setTitle(movies.get(position).title);
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
