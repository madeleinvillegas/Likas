package com.example.likas.ui.tab_02_identify;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.likas.databinding.Tab02IdentifyBinding;

public class IdentifyFragment extends Fragment {

    private Tab02IdentifyBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        IdentifyViewModel locateViewModel = new ViewModelProvider(this).get(IdentifyViewModel.class);

        binding = Tab02IdentifyBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textIdentify;
        locateViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}