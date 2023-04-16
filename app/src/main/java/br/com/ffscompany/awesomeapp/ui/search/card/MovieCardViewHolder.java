package br.com.ffscompany.awesomeapp.ui.search.card;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.uwetrottmann.tmdb2.entities.Movie;

import br.com.ffscompany.awesomeapp.R;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MovieCardViewHolder extends RecyclerView.ViewHolder {
    private TextView title,overview;
    private ImageView poster;

    public MovieCardViewHolder(@NonNull View itemView){
        super(itemView);
        poster = itemView.findViewById(R.id.imageView);
        title = itemView.findViewById(R.id.title);
        overview = itemView.findViewById(R.id.overview);
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
}
