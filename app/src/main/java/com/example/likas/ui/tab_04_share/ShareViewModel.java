package com.example.likas.ui.tab_04_share;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.likas.models.Post;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ShareViewModel extends ViewModel {

    private final MutableLiveData<List<Post>> posts;

    public ShareViewModel() {
        posts = new MutableLiveData<>();
    }

    public void setPosts(List<Post> posts){
        this.posts.setValue(posts);
    }

    public MutableLiveData<List<Post>> getPosts() {
        return posts;
    }
}