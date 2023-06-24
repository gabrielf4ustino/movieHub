package br.com.ffscompany.moviehub.view.genres.Model;


import android.content.Context;
import android.content.res.Resources;

import br.com.ffscompany.moviehub.R;

public class GenreModel {
    private int[] id;
    private String[] name;

    public GenreModel(Context context) {
        Resources resources = context.getResources();
        String[] meuArray = resources.getStringArray(R.array.genres);
        id = new int[meuArray.length];
        name = new String[meuArray.length];
        for (int i = 0; i < meuArray.length; i++) {
            String[] partes = meuArray[i].split(":");
            id[i] = Integer.parseInt(partes[0]);
            name[i] = partes[1];
        }
    }
    public int searchGen(String text){
        for(int i = 0; i < name.length;i++){
            if(text == name[i]){
                return id[i];
            }
        }
        return 12;
    }

    public int[] getId() {
        return id;
    }

    public String[] getName() {
        return name;
    }
}