package br.com.ffscompany.awesomeapp.ui.home.recyclerView;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import br.com.ffscompany.awesomeapp.R;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MovieViewHolder extends RecyclerView.ViewHolder {

    private ImageView imageView;

    public MovieViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.movieImage);
    }

    public ImageView getImageView() {
        return imageView;
    }
}
