package com.example.likas.ui.tab_02_identify;

import android.graphics.PointF;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.example.likas.R;
import com.example.likas.databinding.Tab02IdentifyBinding;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class IdentifyFragment extends Fragment implements QRCodeReaderView.OnQRCodeReadListener {

    private Tab02IdentifyBinding binding;
    @SuppressWarnings("unused")
    private QRCodeReaderView qrCodeReaderView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        IdentifyViewModel locateViewModel = new ViewModelProvider(this).get(IdentifyViewModel.class);

        binding = Tab02IdentifyBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textIdentify;
        locateViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        // QR
        qrCodeReaderView = binding.qrdecoderview;
        qrCodeReaderView.setOnQRCodeReadListener(this);
        qrCodeReaderView.setQRDecodingEnabled(true);
        qrCodeReaderView.setAutofocusInterval(2000L);
        qrCodeReaderView.setTorchEnabled(true);
        qrCodeReaderView.setBackCamera();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onQRCodeRead(String text, PointF[] points) {
        binding.textIdentify.setText(text);
    }

    @Override
    public void onResume() {
        super.onResume();
        qrCodeReaderView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        qrCodeReaderView.stopCamera();
    }
}