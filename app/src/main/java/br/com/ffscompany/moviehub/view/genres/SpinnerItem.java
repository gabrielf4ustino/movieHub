package br.com.ffscompany.moviehub.view.genres;

public class SpinnerItem {
    private int id;
    private String name;

    public SpinnerItem(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
