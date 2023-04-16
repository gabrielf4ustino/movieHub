package br.com.ffscompany.awesomeapp.ui.home.recyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.uwetrottmann.tmdb2.entities.BaseMovie;

import br.com.ffscompany.awesomeapp.R;
import br.com.ffscompany.awesomeapp.databinding.FragmentMovieDetailsBinding;
import br.com.ffscompany.awesomeapp.ui.home.slider.SliderViewAdapter;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MovieViewHolder extends RecyclerView.ViewHolder{

    private ImageView imageView;

    public MovieViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.movieImage);
    }

    public ImageView getImageView() {
        return imageView;
    }
}
