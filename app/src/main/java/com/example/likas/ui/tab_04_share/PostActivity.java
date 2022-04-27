package com.example.likas.ui.tab_04_share;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.likas.R;
import com.example.likas.databinding.ActivityPostBinding;
import com.example.likas.ui.tab_03_update.NewsActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.CharacterIterator;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class PostActivity extends AppCompatActivity {

    private ActivityPostBinding binding;
    List<String> tags = new ArrayList<>();

    private DatabaseReference db;
    private String userRef;
    private String uid, post_title, post_body;
    String url = "https://likas-a4330-default-rtdb.asia-southeast1.firebasedatabase.app/";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.back.setOnClickListener(view -> finish());
        uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        db = FirebaseDatabase.getInstance(url).getReference().child("Posts");
        userRef = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getDisplayName();

        Intent intent = getIntent();

        if(intent.hasExtra("EditMode")){
            String key = intent.getExtras().get("key").toString();
            binding.buttonPost.setText("Update");
            binding.inputPost.setText(intent.getExtras().get("content").toString());
            List<String> tagslist = intent.getStringArrayListExtra("tags");
            for(String s:tagslist){
                setChips(s);
            }
        }


        binding.buttonPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(binding.inputPost.getText().toString())){
                    Toast.makeText(PostActivity.this,"Please input post",Toast.LENGTH_SHORT).show();
                }
                else{
                    LocalDateTime temp = LocalDateTime.now();
                    DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                    String date = temp.format(format);

                    String strTags = tags.toString();

                    strTags = strTags.replace("[", "")
                            .replace("]", "");
                    Toast.makeText(PostActivity.this,strTags,Toast.LENGTH_SHORT).show();

                    HashMap post = new HashMap();
                    post.put("uid",uid);
                    post.put("name",userRef);
                    post.put("content",binding.inputPost.getText().toString());
                    post.put("date",date);
                    post.put("tags", tags);

                    String stamp = uid+" "+date;

                    if(intent.hasExtra("EditMode")){
                        String key = intent.getExtras().get("key").toString();
                        stamp = key;
                    }

                    db.child(stamp).updateChildren(post).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if(task.isSuccessful()){
                                if(intent.hasExtra("EditMode")){
                                    Toast.makeText(PostActivity.this,"Updated",Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(PostActivity.this,"Posted",Toast.LENGTH_SHORT).show();
                                }

                                binding.inputPost.setText("");
                                binding.inputTags.setText("");
                                tags.clear();
                                binding.chipTags.removeAllViews();

                            }
                            else{
                                Toast.makeText(PostActivity.this,"Error",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    finish();
                }

            }
        });

        binding.addTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = binding.inputTags.getText().toString();
                if(!text.trim().isEmpty()) {
                    tags.add(text.trim());
                    Toast.makeText(PostActivity.this, tags.toString(), Toast.LENGTH_SHORT).show();
                    setChips(text.trim());
                    binding.inputTags.setText("");
                }

            }
        });
    }

    public void setChips(String e){
        final Chip chip = (Chip) this.getLayoutInflater().inflate(R.layout.single_chip,null,false);
        chip.setText(e);

        String tag = e;

        chip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tags.remove(tag);
                binding.chipTags.removeView(chip);

            }
        });
        binding.chipTags.addView(chip);
    }
}