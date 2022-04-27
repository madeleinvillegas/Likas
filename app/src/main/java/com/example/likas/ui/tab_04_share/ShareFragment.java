package com.example.likas.ui.tab_04_share;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.likas.R;
import com.example.likas.databinding.Tab04ShareBinding;
import com.example.likas.models.Post;
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


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class ShareFragment extends Fragment {

    List<String> tags = new ArrayList<>();
    List<Post> posts = new ArrayList<>();

    private Tab04ShareBinding binding;
    private DatabaseReference db,dblikes;
    private String uid;
    private boolean liked;
    private ShareRecyclerAdapter shareRecyclerAdapter = new ShareRecyclerAdapter();;
    private ShareViewModel shareViewModel;
    String url = "https://likas-a4330-default-rtdb.asia-southeast1.firebasedatabase.app/";



    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            String pos = ((ShareRecyclerAdapter.ShareViewHolder)viewHolder).getPos();
            Post post = ((ShareRecyclerAdapter.ShareViewHolder)viewHolder).getPost();
            int position = ((ShareRecyclerAdapter.ShareViewHolder)viewHolder).getAdapterPosition();

            switch(direction) {

                case ItemTouchHelper.LEFT:
                    Toast.makeText(getActivity(),"Deleting",Toast.LENGTH_SHORT).show();
                    deleteItem(position,pos,post);
                    break;
                case ItemTouchHelper.RIGHT:
                    Toast.makeText(getActivity(), "Editing", Toast.LENGTH_SHORT).show();
                    editItem(pos,post);
                    shareRecyclerAdapter.notifyDataSetChanged();
                    break;
            }
        }

        @Override
        public int getSwipeDirs(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
            String pos = ((ShareRecyclerAdapter.ShareViewHolder) viewHolder).getPos();
            String[] arr = pos.split(" ");
            String user = arr[0];
            if(!user.equals(uid)) return 0;
            return super.getSwipeDirs(recyclerView, viewHolder);
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c,recyclerView,viewHolder,dX,dY,actionState,isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(getActivity(),R.color.prim))
                    .addSwipeLeftActionIcon(R.drawable.ic_baseline_delete_24)
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(getActivity(),R.color.google_blue))
                    .addSwipeRightActionIcon(R.drawable.ic_baseline_edit_24)
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        shareViewModel = new ViewModelProvider(this).get(ShareViewModel.class);

        binding = Tab04ShareBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        db = FirebaseDatabase.getInstance(url).getReference().child("Posts");
        uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();



        binding.postsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.postsRecyclerView.setAdapter(shareRecyclerAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(binding.postsRecyclerView);

        shareViewModel.getPosts().observe(getViewLifecycleOwner(), new Observer<List<Post>>() {
            @Override
            public void onChanged(List<Post> posts) {
                shareRecyclerAdapter.setPosts(posts);
            }
        });

        if(db!=null){
            db.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    posts.clear();
                    if(snapshot.exists()){
                        for(DataSnapshot ds:snapshot.getChildren()){
                            Post post = ds.getValue(Post.class);
                            post.setKey(ds.getKey().toString());
                            posts.add(post);
                            Log.w("TAGG","Listening");
                            Log.d("Taww",posts.toString());
                        }
                        shareViewModel.setPosts(posts);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }

        binding.createPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PostActivity.class);
                startActivity(intent);
            }
        });

        binding.inputTags.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String text = binding.inputTags.getQuery().toString();
                if(!text.trim().isEmpty()) {
                    tags.add(text.trim());
                    Toast.makeText(getActivity(), tags.toString(), Toast.LENGTH_SHORT).show();
                    setChips(text.trim());
                    binding.inputTags.setQuery("", false);
                    binding.inputTags.clearFocus();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Log.e("Tag",newText.toString());
                filter(newText);
                return false;
            }
        });

        shareRecyclerAdapter.setListener(new ShareRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Post post, String pos,int button) {

                if(button == 1){
                    Intent intent = new Intent(getActivity(),CommentActivity.class);
                    intent.putExtra("Position",pos);
                    startActivity(intent);
                }
                else{
                    liked = true;

                    dblikes = FirebaseDatabase.getInstance(url).getReference().child("Posts").child(pos).child("Likes");
                    dblikes.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(liked){
                                if(snapshot.hasChild(uid)) {
                                    dblikes.child(uid).removeValue();
                                }
                                else{
                                    dblikes.child(uid).setValue(true);
                                }
                                liked = false;
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }


            }
        });

        return root;
    }

    public void editItem(String pos,Post post){
        Intent intent = new Intent(getActivity(), PostActivity.class);
        intent.putExtra("EditMode",true);
        intent.putExtra("content",post.getContent());
        intent.putStringArrayListExtra("tags",new ArrayList<>(post.getListTags()));
        intent.putExtra("key",pos);
        startActivity(intent);
    }

    public void deleteItem(int position, String pos, Post post){
        DatabaseReference db = FirebaseDatabase.getInstance(url).getReference().child("Posts");

        db.child(pos).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                posts.remove(post);
                shareRecyclerAdapter.notifyDataSetChanged();

                Snackbar.make(binding.postsRecyclerView,"Post Deleted",Snackbar.LENGTH_LONG)
                        .setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                HashMap temp = new HashMap();
                                temp.put("uid",post.getUid());
                                temp.put("name",post.getName());
                                temp.put("content",post.getContent());
                                temp.put("date",post.getDate());
                                temp.put("tags",post.getListTags());
                                db.child(pos).updateChildren(temp);
                                shareRecyclerAdapter.notifyDataSetChanged();
                            }
                        }).show();
            }
        });
    }

    private void filter(String sub){
        List<Post> filtered = new ArrayList<>();
        Boolean flag = true;
         for(Post p: posts) {
             if(!tags.isEmpty()){
                 for(String s:tags){
                     if(!contains(p,s)){
                         flag = false;
                         break;
                     }
                 }
             }
             if(flag){
                 if(contains(p,sub)){
                     filtered.add(p);
                 }
             }
             flag = true;
             //Log.e("Tag",filtered.toString());
             shareViewModel.setPosts(filtered);
         }

    }

    private boolean contains(Post p, String s){
        if(p.getTags().contains(s) || p.getContent().contains(s) || p.getName().contains(s)){
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void setChips(String e){
        final Chip chip = (Chip) this.getLayoutInflater().inflate(R.layout.single_chip,null,false);
        chip.setText(e);
        String tag = e;
        filter(binding.inputTags.getQuery().toString());

        chip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tags.remove(tag);
                binding.chipTags.removeView(chip);
                filter(binding.inputTags.getQuery().toString());
            }
        });
        binding.chipTags.addView(chip);
    }
}