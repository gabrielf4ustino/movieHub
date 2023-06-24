package br.com.ffscompany.moviehub.view.search.card;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import br.com.ffscompany.moviehub.R;

public class MovieCardViewHolder extends RecyclerView.ViewHolder {

    private TextView title, overview;

    private ImageView poster;

    private CardView movieInfo;

    public MovieCardViewHolder(@NonNull View itemView) {
        super(itemView);
        poster = itemView.findViewById(R.id.image_view);
        title = itemView.findViewById(R.id.title);
        overview = itemView.findViewById(R.id.overview);
        movieInfo = itemView.findViewById(R.id.movie_info);
    }

    public void setTitle(String titleV) {
        title.setText(titleV);
    }

    public void setOverview(String overviewV) {
        overview.setText(overviewV);
    }

    public ImageView getPosterImage() {
        return poster;
    }

    public CardView getMovieInfo() {
        return movieInfo;
    }
}
