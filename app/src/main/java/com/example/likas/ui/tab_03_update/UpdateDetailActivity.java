package com.example.likas.ui.tab_03_update;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.likas.R;
import com.example.likas.databinding.NewsDetailBinding;
import com.example.likas.models.News;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdateDetailActivity extends AppCompatActivity {

    private DatabaseReference db;
    String url = "https://likas-a4330-default-rtdb.asia-southeast1.firebasedatabase.app/";

    private NewsDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String pos = getIntent().getExtras().get("Position").toString();

        binding = NewsDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.newsBack.setOnClickListener(view -> finish());

        db = FirebaseDatabase.getInstance(url).getReference().child("News");


        db.child(pos).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    binding.setNews(snapshot.getValue(News.class));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}