package br.com.ffscompany.awesomeapp.ui.home.recyclerView;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.uwetrottmann.tmdb2.entities.BaseMovie;

import java.util.List;

import br.com.ffscompany.awesomeapp.R;


public class MovieViewAdapter extends RecyclerView.Adapter<MovieViewHolder> {

    private final Context context;
    private final List<BaseMovie> movies;

    public MovieViewAdapter(Context context, List<BaseMovie> movies) {
        this.context = context;
        this.movies = movies;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_image, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
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
                Log.d("CLICK", movies.get(position).original_title);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }
}
