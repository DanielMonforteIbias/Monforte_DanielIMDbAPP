package edu.pmdm.monforte_danielimdbapp.ui.searchMovie;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SearchMovieViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public SearchMovieViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}