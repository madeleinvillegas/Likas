package com.example.likas.ui.tab_02_identify;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class IdentifyViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public IdentifyViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Identify Fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}