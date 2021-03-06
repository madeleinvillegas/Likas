package com.example.likas.ui.tab_03_update;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.likas.databinding.ActivityNewsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Objects;

public class NewsActivity extends AppCompatActivity {

    private ActivityNewsBinding binding;

    private String uid;
    private DatabaseReference newsRef;
    private String userRef;
    String url = "https://likas-a4330-default-rtdb.asia-southeast1.firebasedatabase.app/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        newsRef =FirebaseDatabase.getInstance(url).getReference().child("News");
        userRef = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getDisplayName();

        Intent intent = getIntent();

        binding = ActivityNewsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.backButton.setOnClickListener(view->finish());

        if(intent.hasExtra("EditMode")){
            String key = intent.getExtras().get("key").toString();
            binding.buttonUplaod.setText("Update");
            binding.inputTitle.setText(intent.getExtras().get("title").toString());
            binding.inputPost.setText(intent.getExtras().get("description").toString());
            binding.buttonUplaod.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LocalDateTime temp = LocalDateTime.now();
                    DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                    String date = temp.format(format);

                    HashMap news = new HashMap();
                    news.put("title",binding.inputTitle.getText().toString());
                    news.put("author",userRef);
                    news.put("date",date);
                    news.put("description",binding.inputPost.getText().toString());

                    newsRef.child(key).updateChildren(news).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(NewsActivity.this,"Success",Toast.LENGTH_SHORT).show();
                                binding.inputTitle.setText("");
                                binding.inputPost.setText("");
                            }
                            else{
                                Toast.makeText(NewsActivity.this,"Error",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    finish();
                }
            });
        }
        else {
            binding.buttonUplaod.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(TextUtils.isEmpty(binding.inputTitle.getText().toString()) || TextUtils.isEmpty(binding.inputPost.getText().toString())){
                        Toast.makeText(NewsActivity.this,"Please input both title and content",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        LocalDateTime temp = LocalDateTime.now();
                        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                        String date = temp.format(format);

                        HashMap news = new HashMap();
                        news.put("title",binding.inputTitle.getText().toString());
                        news.put("author",userRef);
                        news.put("date",date);
                        news.put("description",binding.inputPost.getText().toString());
                        String stamp = uid+" "+date;


                        newsRef.child(stamp).updateChildren(news).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(NewsActivity.this,"Posted",Toast.LENGTH_SHORT).show();
                                    binding.inputTitle.setText("");
                                    binding.inputPost.setText("");
                                }
                                else{
                                    Toast.makeText(NewsActivity.this,"Error",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                }
            });

        }
    }
}