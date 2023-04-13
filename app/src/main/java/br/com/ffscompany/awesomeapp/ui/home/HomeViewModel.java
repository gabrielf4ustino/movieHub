package br.com.ffscompany.awesomeapp.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<RecyclerView> recyclerViewMutableLiveData;

    public HomeViewModel(RecyclerView recyclerView) {
        recyclerViewMutableLiveData = new MutableLiveData<>();
        recyclerViewMutableLiveData.setValue(recyclerView);
    }

    public LiveData<RecyclerView> getRecyclerView() {
        return recyclerViewMutableLiveData;
    }
}