package br.com.ffscompany.awesomeapp.ui.home.slider;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import br.com.ffscompany.awesomeapp.R;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SliderViewHolder extends RecyclerView.ViewHolder {

    private ImageView imageView;

    public SliderViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.movieImageSlider);
    }

    public ImageView getImageView() {
        return imageView;
    }
}
