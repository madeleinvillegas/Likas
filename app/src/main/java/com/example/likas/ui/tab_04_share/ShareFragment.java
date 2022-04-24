package com.example.likas.ui.tab_04_share;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.likas.R;
import com.example.likas.databinding.Tab04ShareBinding;
import com.example.likas.models.Post;
import com.google.android.material.chip.Chip;

import java.util.List;

public class ShareFragment extends Fragment {

    private Tab04ShareBinding binding;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ShareViewModel shareViewModel = new ViewModelProvider(this).get(ShareViewModel.class);

        binding = Tab04ShareBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.createPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PostActivity.class);
                startActivity(intent);
            }
        });

        ShareRecyclerAdapter shareRecyclerAdapter = new ShareRecyclerAdapter();
        binding.postsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.postsRecyclerView.setAdapter(shareRecyclerAdapter);

        shareViewModel.getPosts().observe(getViewLifecycleOwner(), new Observer<List<Post>>() {
            @Override
            public void onChanged(List<Post> posts) {
                shareRecyclerAdapter.setPosts(posts);
            }
        });

        shareRecyclerAdapter.setListener(new ShareRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Post post) {
                Intent intent = new Intent(getActivity(),CommentActivity.class);
                intent.putExtra(CommentActivity.uid,post.getUid());
                startActivity(intent);
            }
        });




        //locateViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void setChips(String e){
        final Chip chip = (Chip) this.getLayoutInflater().inflate(R.layout.single_chip,null,false);
        chip.setText(e);

        chip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.chipTags.removeView(chip);

            }
        });
        binding.chipTags.addView(chip);
    }
}