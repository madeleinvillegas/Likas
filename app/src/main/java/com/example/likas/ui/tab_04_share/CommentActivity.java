package com.example.likas.ui.tab_04_share;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.likas.R;
import com.example.likas.databinding.ActivityCommentBinding;
import com.example.likas.databinding.CommentItemBinding;
import com.example.likas.databinding.PostItemBinding;
import com.example.likas.models.Comment;
import com.example.likas.models.News;
import com.example.likas.models.Post;
import com.example.likas.ui.tab_03_update.NewsActivity;
import com.example.likas.ui.tab_03_update.UpdateFragment;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class CommentActivity extends AppCompatActivity {

    private DatabaseReference db;
    private String userRef;
    private Boolean mode = false;
    private String key;
    private String post;

    private String uid, comment;
    String url = "https://likas-a4330-default-rtdb.asia-southeast1.firebasedatabase.app/";


    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            switch(direction) {
                case ItemTouchHelper.LEFT:
                    Toast.makeText(CommentActivity.this, "Deleting", Toast.LENGTH_SHORT).show();
                    ((CommentViewHolder) viewHolder).deleteItem();
                    break;
                case ItemTouchHelper.RIGHT:
                    Toast.makeText(CommentActivity.this, "Editing", Toast.LENGTH_SHORT).show();
                    mode = true;
                    key = ((CommentViewHolder) viewHolder).pos;
                    binding.inputComment.setText(((CommentViewHolder) viewHolder).comment.getBody().toString());
                    break;
            }
        }

        @Override
        public int getSwipeDirs(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
            String pos = ((CommentViewHolder) viewHolder).pos;
            String[] arr = pos.split(" ");
            String uid = arr[0];
            if(!uid.equals(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())) return 0;
            return super.getSwipeDirs(recyclerView, viewHolder);
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c,recyclerView,viewHolder,dX,dY,actionState,isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(CommentActivity.this,R.color.prim))
                    .addSwipeLeftActionIcon(R.drawable.ic_baseline_delete_24)
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(CommentActivity.this,R.color.google_blue))
                    .addSwipeRightActionIcon(R.drawable.ic_baseline_edit_24)
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };



    private ActivityCommentBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        String pos = getIntent().getExtras().get("Position").toString();
        db = FirebaseDatabase.getInstance(url).getReference().child("Posts").child(pos).child("Comments");
        uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        userRef = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getDisplayName();
        binding = ActivityCommentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.commentsRecyclerView.setLayoutManager(new LinearLayoutManager(CommentActivity.this));
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(binding.commentsRecyclerView);

        Log.w("SKS",mode.toString());

        if(mode){
            binding.inputComment.setText(post);
            Log.w("SKS","I went here");
        }

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

                    HashMap comments = new HashMap();

                    Toast.makeText(CommentActivity.this,"Error",Toast.LENGTH_SHORT).show();

                    comments.put("body",binding.inputComment.getText().toString());
                    comments.put("user",userRef);
                    comments.put("date",date);

                    if(mode){
                        stamp = key;
                        Log.w("SKS","I went here");
                    }

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
                String pos = getRef(position).getKey();
                holder.commentItemBinding.setComment(model);
                holder.commentItemBinding.executePendingBindings();

                db.child(pos).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            final Comment comment = snapshot.getValue(Comment.class);
                            holder.setKey(pos,comment);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
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

    public class CommentViewHolder extends  RecyclerView.ViewHolder{

        private CommentItemBinding commentItemBinding;
        private String pos;
        private Comment comment;

        //grabbing views from recycler
        //similar to onCreate()

        public CommentViewHolder(@NonNull CommentItemBinding commentItemBinding){
            super(commentItemBinding.getRoot());
            this.commentItemBinding = commentItemBinding;


            commentItemBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }

        public void setKey(String pos,Comment comment){
            this.pos = pos;
            this.comment = comment;
        }

        public void deleteItem(){
            db.child(pos).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Snackbar.make(binding.commentsRecyclerView,"News Deleted",Snackbar.LENGTH_LONG)
                            .setAction("Undo", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    HashMap temp = new HashMap();
                                    temp.put("body",comment.getBody());
                                    temp.put("user",comment.getUser());
                                    temp.put("date",comment.getDate());

                                    db.child(pos).updateChildren(temp);
                                }
                            }).show();
                }
            });
        }
    }
}