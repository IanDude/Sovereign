package com.example.sovereign;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HOFFragment extends Fragment {

    private EditText postContent;
    private Button addImageButton;
    private Button submitPostButton;
    private ProgressBar progressBar;
    private ImageView imageView;
    private LinearLayout postsContainer;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference postsRef;
    private static final int PICK_IMAGE_REQUEST = 1;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_h_o_f, container, false);

        // Initialize Firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        postsRef = firebaseDatabase.getReference("posts");

        postContent = view.findViewById(R.id.postContent);
        addImageButton = view.findViewById(R.id.addImageButton);
        submitPostButton = view.findViewById(R.id.submitPostButton);
        progressBar = view.findViewById(R.id.progressBar);
        imageView = view.findViewById(R.id.imageView);
        postsContainer = view.findViewById(R.id.postsContainer);

        // Set up listeners
        addImageButton.setOnClickListener(v -> openImagePicker());
        submitPostButton.setOnClickListener(v -> submitPost());

        // Load existing posts from Firebase
        loadPosts();

        return view;
    }

    private void openImagePicker() {
            // Create an intent to pick an image from the device storage
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");  // Filter to allow only images
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        }


    private void submitPost() {
        String postText = postContent.getText().toString();

        if (postText.isEmpty()) {
            Toast.makeText(getContext(), "Please enter some content.", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        // Create a new post object
        String postId = postsRef.push().getKey();  // Generate unique ID for the post
        Post newPost = new Post(postText, "");  // Empty image URL or set image if available

        // Save the post in Firebase
        postsRef.child(postId).setValue(newPost).addOnCompleteListener(task -> {
            progressBar.setVisibility(View.GONE);
            if (task.isSuccessful()) {
                Toast.makeText(getContext(), "Post submitted successfully!", Toast.LENGTH_SHORT).show();
                postContent.setText("");  // Clear the input field
                imageView.setVisibility(View.GONE);  // Hide image preview
            } else {
                Toast.makeText(getContext(), "Error submitting post.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadPosts() {
        postsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postsContainer.removeAllViews();  // Clear existing posts
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Post post = postSnapshot.getValue(Post.class);  // Get the post object from the snapshot

                    if (post != null) {
                        // Create a new TextView for the post content
                        TextView postTextView = new TextView(getContext());
                        postTextView.setText(post.getContent());
                        postTextView.setPadding(16, 16, 16, 16);
                        postTextView.setBackground(getResources().getDrawable(R.drawable.round_lightopacity, null));
                        postTextView.setTextSize(16);
                        postTextView.setTextColor(Color.BLACK);

                        // If the post has an image URL, load the image using Glide
                        if (post.getImageUrl() != null && !post.getImageUrl().isEmpty()) {
                            ImageView postImageView = new ImageView(getContext());
                            postImageView.setLayoutParams(new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    200 // Image height
                            ));
                            Glide.with(getContext())
                                    .load(post.getImageUrl())
                                    .into(postImageView);
                            postsContainer.addView(postImageView);
                        }

                        // Add the post text to the feed
                        postsContainer.addView(postTextView, 0);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Failed to load posts.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
