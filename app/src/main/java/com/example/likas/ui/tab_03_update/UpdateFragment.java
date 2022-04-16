package com.example.likas.ui.tab_03_update;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.likas.databinding.Tab03UpdateBinding;

public class UpdateFragment extends Fragment {

    private Tab03UpdateBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        UpdateViewModel locateViewModel = new ViewModelProvider(this).get(UpdateViewModel.class);

        binding = Tab03UpdateBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textUpdate;
        locateViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}