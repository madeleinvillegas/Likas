package com.example.likas.ui.tab_04_share;

import androidx.lifecycle.LiveData;
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
        List<Post> posts = new ArrayList<>();

        List<String> temp = new ArrayList<>();
        temp.add("Hello");
        temp.add("Wassup");
        posts.add(new Post("id","name","content",new Date(),temp));
        this.posts.setValue(posts);
    }

    public void setPosts(){
        List<Post> posts = new ArrayList<>();
        this.posts.setValue(posts);
    }

    public void addPost(Post post){

    }

    public void deletePost(Post post){

    }

    public void udpatePost(Post post){

    }

    public MutableLiveData<List<Post>> getPosts() {
        return posts;
    }
}