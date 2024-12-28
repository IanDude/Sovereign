package com.example.sovereign;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

public class ClanFragment extends Fragment {

    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private List<Clan_Post_Model> postList;

    private static final int REQUEST_CODE_CREATE_POST = 1; // Request code for starting Clan_Post activity

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clan, container, false);

        // Initialize the RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        postList = new ArrayList<>();
        postAdapter = new PostAdapter(postList);
        recyclerView.setAdapter(postAdapter);

        // Initialize the Add Post Button (fab)
        ImageButton clanPost = view.findViewById(R.id.btn_clanPost);
        clanPost.setOnClickListener(view1 -> new Handler(Looper.getMainLooper()).postDelayed(() -> {
            // Start the Clan_Post activity to create a new post
            Intent intent = new Intent(view1.getContext(), Clan_Post.class);
            startActivityForResult(intent, REQUEST_CODE_CREATE_POST);  // Use startActivityForResult
        }, 1000));

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_CREATE_POST && resultCode == getActivity().RESULT_OK && data != null) {
            // Retrieve the new post from the intent
            Clan_Post_Model newPost = (Clan_Post_Model) data.getSerializableExtra("post");

            if (newPost != null) {
                // Add the new post to the RecyclerView's adapter
                postAdapter.addPost(newPost);
            }
        }
    }
}
