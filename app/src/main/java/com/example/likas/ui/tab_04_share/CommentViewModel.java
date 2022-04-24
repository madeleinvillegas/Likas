package com.example.likas.ui.tab_04_share;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.example.likas.models.Comment;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommentViewModel extends ViewModel {
    private final MutableLiveData<List<Comment>> comments;

    public CommentViewModel(){
        comments = new MutableLiveData<>();
        List<Comment> comments = new ArrayList<>();
        comments.add(new Comment("title","author",new Date()));
        comments.add(new Comment("title","author",new Date()));
        comments.add(new Comment("title","author",new Date()));
        comments.add(new Comment("title","author",new Date()));
        comments.add(new Comment("title","author",new Date()));
        comments.add(new Comment("title","author",new Date()));
        comments.add(new Comment("title","author",new Date()));
        comments.add(new Comment("title","author",new Date()));

        this.comments.setValue(comments);
    }

    public void setComments(){
        List<Comment> comments = new ArrayList<>();
        comments.add(new Comment("title","author",new Date()));
        comments.add(new Comment("title","author",new Date()));
        comments.add(new Comment("title","author",new Date()));
        this.comments.setValue(comments);
    }

    public void addComment(Comment comment){

    }

    public void deleteComment(Comment comment){

    }

    public void udpateComment(Comment comment){

    }

    public MutableLiveData<List<Comment>> getComments() {
        return comments;
    }
}
