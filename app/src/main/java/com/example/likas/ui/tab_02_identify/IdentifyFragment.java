package com.example.likas.ui.tab_02_identify;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.likas.databinding.Tab02IdentifyBinding;
import com.google.firebase.auth.FirebaseAuth;

import net.glxn.qrgen.android.QRCode;

public class IdentifyFragment extends Fragment {
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Tab02IdentifyBinding binding = Tab02IdentifyBinding.inflate(inflater, container, false);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        Bitmap myBitmap = QRCode.from(mAuth.getUid()).bitmap();
        ImageView myImage = binding.qrimage;
        myImage.setImageBitmap(myBitmap);

        return binding.getRoot();
    }
}