package com.example.likas;

import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

public class QRActivity extends AppCompatActivity implements QRCodeReaderView.OnQRCodeReadListener {
    private static final String DB_URL = "https://likas-a4330-default-rtdb.asia-southeast1.firebasedatabase.app/";
    private QRCodeReaderView qrCodeReaderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_scanner);

        // Back Button
        ImageView back = findViewById(R.id.user_back);
        back.setOnClickListener(view -> finish());

        // QR
        qrCodeReaderView = findViewById(R.id.qrdecoderview);
        qrCodeReaderView.setOnQRCodeReadListener(this);
        qrCodeReaderView.setQRDecodingEnabled(true);
        qrCodeReaderView.setAutofocusInterval(2000L);
        qrCodeReaderView.setTorchEnabled(true);
        qrCodeReaderView.setBackCamera();
    }

    @Override
    public void onQRCodeRead(String text, PointF[] points) {
        ((TextView) findViewById(R.id.note)).setText(text);
        findViewById(R.id.count_user).setOnClickListener(view -> {
            DatabaseReference mDatabase = FirebaseDatabase.getInstance(DB_URL).getReference();
            DatabaseReference d = mDatabase.child("facilities/" + getIntent().getStringExtra("key") + "/slotsTaken");
            d.setValue(ServerValue.increment(1));
            Log.e("QR", "Success");
        });
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