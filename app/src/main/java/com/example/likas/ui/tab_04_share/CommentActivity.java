package com.example.likas.ui.tab_04_share;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.likas.databinding.ActivityCommentBinding;
import com.example.likas.databinding.CommentItemBinding;
import com.example.likas.databinding.PostItemBinding;
import com.example.likas.models.Comment;
import com.example.likas.models.Post;
import com.example.likas.ui.tab_03_update.UpdateFragment;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class CommentActivity extends AppCompatActivity {

    private DatabaseReference db;
    private DatabaseReference userRef;

    private String uid, comment;
    String url = "https://likas-a4330-default-rtdb.asia-southeast1.firebasedatabase.app/";


    private ActivityCommentBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCommentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String pos = getIntent().getExtras().get("Position").toString();

        db = FirebaseDatabase.getInstance(url).getReference().child("Posts").child(pos).child("Comments");
        uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        userRef = FirebaseDatabase.getInstance(url).getReference().child("Users").child(uid);

        binding.commentsRecyclerView.setLayoutManager(new LinearLayoutManager(CommentActivity.this));

        binding.sendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comment = binding.inputComment.getText().toString();
                if(TextUtils.isEmpty(comment)){
                    Toast.makeText(CommentActivity.this, "Comment empty", Toast.LENGTH_SHORT).show();
                }
                else{
                    LocalDateTime temp = LocalDateTime.now();
                    DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                    String date = temp.format(format);
                    String stamp = uid+" "+date;

                    userRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                Toast.makeText(CommentActivity.this, "Try", Toast.LENGTH_SHORT).show();
                                String name = snapshot.child("name").getValue().toString();
                                HashMap comments = new HashMap();

                                Toast.makeText(CommentActivity.this,"Error",Toast.LENGTH_SHORT).show();

                                comments.put("body",binding.inputComment.getText().toString());
                                comments.put("user",name);
                                comments.put("date",date);

                                db.child(stamp).updateChildren(comments).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(CommentActivity.this,"Posted",Toast.LENGTH_SHORT).show();
                                            binding.inputComment.setText("");
                                        }
                                        else{
                                            Toast.makeText(CommentActivity.this,"Error",Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

    }


    @Override
    public void onStart() {
        super.onStart();
        Query q = db.orderByChild("date");
        FirebaseRecyclerOptions<Comment> options = new FirebaseRecyclerOptions.Builder<Comment>().setQuery(q,Comment.class).build();
        FirebaseRecyclerAdapter<Comment,CommentViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Comment, CommentViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CommentViewHolder holder, int position, @NonNull Comment model) {
                holder.commentItemBinding.setComment(model);
                holder.commentItemBinding.executePendingBindings();
            }

            @NonNull
            @Override
            public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
                CommentItemBinding commentItemBinding = CommentItemBinding.inflate(layoutInflater,parent,false);
                return new CommentViewHolder(commentItemBinding);
            }
        };
        binding.commentsRecyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }

    public static class CommentViewHolder extends  RecyclerView.ViewHolder{

        private CommentItemBinding commentItemBinding;

        //grabbing views from recycler
        //similar to onCreate()

        public CommentViewHolder(@NonNull CommentItemBinding commentItemBinding){
            super(commentItemBinding.getRoot());
            this.commentItemBinding = commentItemBinding;
        }
    }
}