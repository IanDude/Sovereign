package com.example.sovereign;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ClanFragment extends Fragment {

    private EditText postContent;
    private Button addImageButton, submitPostButton;
    private ProgressBar progressBar;
    private ImageView imageView;
    private LinearLayout postsContainer;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference postsRef;
    private Uri selectedImageUri;
    private static final int PICK_IMAGE_REQUEST = 1;

    @SuppressLint("WrongViewCast")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clan, container, false);

        // Initialize Firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        postsRef = firebaseDatabase.getReference("postClan");

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
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            imageView.setImageURI(selectedImageUri); // Show the selected image in the preview
            imageView.setVisibility(View.VISIBLE);
        }
    }

    private void submitPost() {
        String postText = postContent.getText().toString();
        String postType = "Clan";

        // Get the current date and time in AM/PM format
        String currentDateTime = new SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault()).format(Calendar.getInstance().getTime());

        if (postText.isEmpty()) {
            Toast.makeText(getContext(), "Please enter post content.", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        String postId = postsRef.push().getKey();
        String imageBase64 = "";

        if (selectedImageUri != null) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), selectedImageUri);
                imageBase64 = encodeImageToBase64(bitmap);
            } catch (Exception e) {
                Toast.makeText(getContext(), "Error encoding image.", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

        Post newPost = new Post(postText, imageBase64, currentDateTime, postType);

        postsRef.child(postId).setValue(newPost).addOnCompleteListener(task -> {
            progressBar.setVisibility(View.GONE);
            if (task.isSuccessful()) {
                Toast.makeText(getContext(), "Post submitted successfully!", Toast.LENGTH_SHORT).show();
                postContent.setText(""); // Clear content
                imageView.setVisibility(View.GONE); // Hide preview
                selectedImageUri = null; // Reset the selected image
            } else {
                Toast.makeText(getContext(), "Error submitting post.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String encodeImageToBase64(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream); // Compress the image
        byte[] byteArray = outputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private void loadPosts() {
        postsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postsContainer.removeAllViews();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String postId = postSnapshot.getKey(); // Get the post ID
                    Post post = postSnapshot.getValue(Post.class);
                    if (post != null) {
                        LinearLayout postLayout = new LinearLayout(getContext());
                        postLayout.setOrientation(LinearLayout.VERTICAL);
                        postLayout.setPadding(16, 16, 16, 16);
                        postLayout.setBackground(getResources().getDrawable(R.drawable.round_lightopacity, null));
                        LinearLayout.LayoutParams postLayoutParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );

                        postLayoutParams.bottomMargin = 32; // Adjust this value to set the desired spacing
                        postLayout.setLayoutParams(postLayoutParams);





                        // Create a vertical LinearLayout for the post content, icons, and date
                        LinearLayout verticalLayout = new LinearLayout(getContext());
                        verticalLayout.setOrientation(LinearLayout.VERTICAL);
                        verticalLayout.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        ));

// Create a horizontal LinearLayout for the post content and icons
                        LinearLayout contentAndIconsLayout = new LinearLayout(getContext());
                        contentAndIconsLayout.setOrientation(LinearLayout.HORIZONTAL);
                        contentAndIconsLayout.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        ));

// Add Post Content
                        TextView postTextView = new TextView(getContext());
                        postTextView.setText(post.getContent());
                        postTextView.setTextSize(16);

// Set layout parameters with top margin
                        LinearLayout.LayoutParams postTextParams = new LinearLayout.LayoutParams(
                                0, // Use weight to distribute space
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                1.0f // Weight for the content to push icons to the right
                        );
                        postTextParams.topMargin = 0; // Add top margin (adjust value as needed)
                        postTextParams.bottomMargin = 80; // Add top margin (adjust value as needed)

                        postTextView.setLayoutParams(postTextParams);

                        contentAndIconsLayout.addView(postTextView);
// Add Edit Icon
                        ImageButton editIcon = new ImageButton(getContext());
                        editIcon.setImageResource(R.drawable.ic_edit); // Replace with your edit icon resource
                        editIcon.setBackground(null); // Remove default background

// Set layout parameters with reduced right margin
                        LinearLayout.LayoutParams editIconParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        editIconParams.rightMargin = -60; // Reduce right margin (adjust value as needed)
                        editIconParams.topMargin = -20; // Retain reduced top margin
                        editIcon.setLayoutParams(editIconParams);

                        editIcon.setOnClickListener(v -> editPost(postId, post));
                        contentAndIconsLayout.addView(editIcon);

// Add Delete Icon
                        ImageButton deleteIcon = new ImageButton(getContext());
                        deleteIcon.setImageResource(R.drawable.ic_delete); // Replace with your delete icon resource
                        deleteIcon.setBackground(null); // Remove default background

// Set layout parameters with reduced left margin
                        LinearLayout.LayoutParams deleteIconParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        deleteIconParams.leftMargin = -20; // Reduce left margin (adjust value as needed)
                        deleteIconParams.rightMargin= -20;
                        deleteIconParams.topMargin = -20; // Retain reduced top margin
                        deleteIcon.setLayoutParams(deleteIconParams);

                        deleteIcon.setOnClickListener(v -> deletePost(postId));
                        contentAndIconsLayout.addView(deleteIcon);


// Add the horizontal layout (post content + icons) to the vertical layout
                        verticalLayout.addView(contentAndIconsLayout);
// Add Post Date below the horizontal layout
                        TextView postDateView = new TextView(getContext());
                        postDateView.setText("Date: " + post.getDate());

// Set layout parameters with proper top margin
                        LinearLayout.LayoutParams dateParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        dateParams.topMargin = -60; // Add a small positive margin to separate it from the text above

                        postDateView.setLayoutParams(dateParams);

// Add the date view to the vertical layout
                        verticalLayout.addView(postDateView);

// Add the vertical layout to the post layout
                        postLayout.addView(verticalLayout);



                        // Display Image if available
                        if (post.getImageUrl() != null && !post.getImageUrl().isEmpty()) {
                            byte[] imageBytes = Base64.decode(post.getImageUrl(), Base64.DEFAULT);
                            Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

                            if (decodedImage != null) {
                                ImageView postImageView = new ImageView(getContext());
                                postImageView.setImageBitmap(decodedImage);
                                postImageView.setLayoutParams(new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        750
                                ));
                                postImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                postLayout.addView(postImageView);
                            }
                        }

                        // Add post layout to the container
                        postsContainer.addView(postLayout, 0);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Failed to load posts.", Toast.LENGTH_SHORT).show();
            }
            private void editPost(String postId, Post post) {
                // Show confirmation dialog
                new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                        .setTitle("Edit Post")
                        .setMessage("Are you sure you want to edit this post?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            // Prefill the post content and optionally image for editing
                            postContent.setText(post.getContent());
                            selectedImageUri = null; // Reset image selection

                            submitPostButton.setText("Update Post");
                            submitPostButton.setOnClickListener(v -> {
                                String updatedText = postContent.getText().toString();
                                if (updatedText.isEmpty()) {
                                    Toast.makeText(getContext(), "Post content cannot be empty.", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                // Update post in Firebase
                                post.setContent(updatedText);
                                postsRef.child(postId).setValue(post).addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getContext(), "Post updated successfully.", Toast.LENGTH_SHORT).show();
                                        postContent.setText(""); // Clear content
                                        submitPostButton.setText("Submit Post");
                                        submitPostButton.setOnClickListener(view -> submitPost());
                                    } else {
                                        Toast.makeText(getContext(), "Failed to update post.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            });
                        })
                        .setNegativeButton("No", null) // Dismiss dialog if "No" is clicked
                        .show();
            }

            private void deletePost(String postId) {
                // Show confirmation dialog
                new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                        .setTitle("Delete Post")
                        .setMessage("Are you sure you want to delete this post?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            postsRef.child(postId).removeValue().addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    // Toast message after successful deletion
                                    Toast.makeText(getContext(), "Post deleted successfully.", Toast.LENGTH_SHORT).show();
                                } else {
                                    // Toast message for failure
                                    Toast.makeText(getContext(), "Failed to delete post.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        })
                        .setNegativeButton("No", null) // Dismiss dialog if "No" is clicked
                        .show();
            }

        });
    }}
