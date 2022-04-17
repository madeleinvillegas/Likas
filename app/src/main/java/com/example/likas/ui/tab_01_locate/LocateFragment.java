package com.example.likas.ui.tab_01_locate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.likas.databinding.Tab01LocateBinding;

public class LocateFragment extends Fragment {

    private Tab01LocateBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LocateViewModel locateViewModel = new ViewModelProvider(this).get(LocateViewModel.class);

        binding = Tab01LocateBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}