package com.example.likas.ui.tab_02_identify;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class IdentifyViewModel extends ViewModel {

    private final MutableLiveData<String> UserID;

    public IdentifyViewModel() {
        UserID = new MutableLiveData<>();
        UserID.setValue("This is Identify Fragment");
    }

    public LiveData<String> setUserID() {
        return UserID;
    }

    public LiveData<String> getUserID() {
        return UserID;
    }
}