package com.example.likas;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.likas.classes.Facility;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class NewEvacActivity extends AppCompatActivity {
    private static final String DB_URL = "https://likas-a4330-default-rtdb.asia-southeast1.firebasedatabase.app/";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_evac);

        // Back
        ImageView back = findViewById(R.id.user_back);
        back.setOnClickListener(view -> finish());

        // Coordinates
        Intent intent = getIntent();
        double lat = intent.getDoubleExtra("lat", 0);
        double lon = intent.getDoubleExtra("long", 0);
        TextView coordinates = findViewById(R.id.coordinates);
        String tmpTxt = "Latitude: " + lat + "\nLongitude: " + lon;
        coordinates.setText(tmpTxt);

        // Admin?
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance(DB_URL).getReference();
        String UID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        Log.e("UID", UID);

        // Slots
        EditText slotsTaken = findViewById(R.id.slots_taken);
        EditText slotsMax = findViewById(R.id.slots_max);
        slotsTaken.setText(String.valueOf(intent.getIntExtra("slotsTaken", 0)));
        slotsMax.setText(String.valueOf(intent.getIntExtra("slotsMax", 0)));

        mDatabase.child("admins").child(UID).get().addOnCompleteListener(task -> {
            boolean admin = String.valueOf(task.getResult().getValue()).equals("true");
            if (!admin) {
                slotsTaken.setVisibility(View.GONE);
                slotsMax.setVisibility(View.GONE);
            }

            // Evacuation Name
            EditText evacName = findViewById(R.id.evac_name);
            String intentName = intent.getStringExtra("name");
            evacName.setText(intentName);

            // Submit
            Button submit = findViewById(R.id.submit);
            submit.setOnClickListener(view -> {
                if (evacName.getText().toString().trim().length() < 8) {
                    Toast.makeText(this, "Centre Name is Too Short", Toast.LENGTH_SHORT).show();
                    return;
                }
                String type = "Evacuation Centers (Unverified)";
                if (admin) type = "Evacuation Centers (Verified)";
                int n_Taken = 0;
                int n_Max = 0;
                if (admin) {
                    if (Double.parseDouble(slotsTaken.getText().toString()) > Double.parseDouble(slotsMax.getText().toString())) {
                        Toast.makeText(this, "Slots Taken Must be Less Than Slots Max", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    n_Taken = Integer.parseInt(slotsTaken.getText().toString());
                    n_Max = Integer.parseInt(slotsMax.getText().toString());
                }

                String key = mDatabase.child("facilities").push().getKey();
                String editKey = intent.getStringExtra("key");
                if (editKey != null) key = editKey;

                Facility facility = new Facility(evacName.getText().toString().trim(), String.valueOf(lat), String.valueOf(lon), type, n_Taken, n_Max);

                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("facilities/" + key, facility);

                mDatabase.updateChildren(childUpdates);
                Toast.makeText(this, "Evacuation Centre Added to Database", Toast.LENGTH_SHORT).show();

                finish();
            });
        });
    }
}