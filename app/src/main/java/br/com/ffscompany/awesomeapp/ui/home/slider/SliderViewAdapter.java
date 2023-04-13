package br.com.ffscompany.awesomeapp.ui.home.slider;

import android.content.Context;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.view.GestureDetectorCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.bumptech.glide.Glide;
import com.uwetrottmann.tmdb2.entities.BaseMovie;

import java.util.List;

import br.com.ffscompany.awesomeapp.R;


public class SliderViewAdapter extends RecyclerView.Adapter<SliderViewHolder> {

    private final Context context;
    private final List<BaseMovie> movies;

    public SliderViewAdapter(Context context, List<BaseMovie> movies) {
        this.context = context;
        this.movies = movies;
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.slider_image, parent, false);
        return new SliderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        // Obtenha o objeto de dados para esta posição

        // Defina o texto e outras propriedades do ViewHolder
//        holder.textView.setText(dataObject.getText());
//        Log.d("movie", movies.get(position).toString());

        // Carregue a imagem usando o Glide
        Glide.with(context).load("https://image.tmdb.org/t/p/w500" + movies.get(position).poster_path).into(holder.getImageView());

        // Defina um ouvinte de clique na imagem
        holder.getImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ação a ser executada quando a imagem for clicada
                // Por exemplo, exibir uma mensagem de log ou abrir uma nova atividade
            }
        });



    }

    @Override
    public int getItemCount() {
        return movies.size();
    }
}
