package br.com.ffscompany.moviehub.view.home.slider;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import br.com.ffscompany.moviehub.R;

public class SliderViewHolder extends RecyclerView.ViewHolder {

    private ImageView imageView;

    private TextView textView;

    public SliderViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.movie_image_slider);
        textView = itemView.findViewById(R.id.movie_title_slider);
    }

    public void setTitle(String title) {
        textView.setText(title);
    }

    public ImageView getImageView() {
        return imageView;
    }
}
