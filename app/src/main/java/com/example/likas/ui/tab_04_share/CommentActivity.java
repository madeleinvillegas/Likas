package com.example.likas.ui.tab_04_share;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;

import com.example.likas.databinding.ActivityCommentBinding;
import com.example.likas.models.Comment;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommentActivity extends AppCompatActivity {

    public static final String uid = "Test";


    private ActivityCommentBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_comment);
        CommentViewModel commentViewModel = new ViewModelProvider(this).get(CommentViewModel.class);


        binding = ActivityCommentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        CommentRecyclerAdapter commentRecyclerAdapter = new CommentRecyclerAdapter(getList());
        binding.commentsRecyclerView.setAdapter(commentRecyclerAdapter);

        commentViewModel.getComments().observe(this, new Observer<List<Comment>>() {
            @Override
            public void onChanged(List<Comment> comments) {
                commentRecyclerAdapter.setComments(comments);
            }
        });
    }

    private List<Comment> getList(){
        List<Comment> commentList = new ArrayList<>();
        commentList.add(new Comment("dave","comment",new Date()));
        return commentList;
    }
}