package com.example.likas.ui.tab_02_identify;

import android.content.Intent;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.example.likas.databinding.Tab02IdentifyAdminBinding;

public class IdentifyFragment extends Fragment implements QRCodeReaderView.OnQRCodeReadListener {

    private Tab02IdentifyAdminBinding binding;
    private IdentifyViewModel locateViewModel;
    private QRCodeReaderView qrCodeReaderView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        locateViewModel = new ViewModelProvider(this).get(IdentifyViewModel.class);

        String type = "admin";

        //noinspection ConstantConditions
        if (type.equals("admin")) {
            binding = Tab02IdentifyAdminBinding.inflate(inflater, container, false);
            View root = binding.getRoot();

            // QR
            qrCodeReaderView = binding.qrdecoderview;
            qrCodeReaderView.setOnQRCodeReadListener(this);
            qrCodeReaderView.setQRDecodingEnabled(true);
            qrCodeReaderView.setAutofocusInterval(2000L);
            qrCodeReaderView.setTorchEnabled(true);
            qrCodeReaderView.setBackCamera();

            return root;
        }

        return null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onQRCodeRead(String text, PointF[] points) {
        binding.textIdentifyId.setText(text);
        if (text.startsWith("http")) {
            binding.countUser.setOnClickListener(view -> {
                Intent httpIntent = new Intent(Intent.ACTION_VIEW);
                httpIntent.setData(Uri.parse(text));
                startActivity(httpIntent);
            });
        }
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