package com.example.likas.ui.tab_01_locate;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LocateViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public LocateViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Locate Fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}