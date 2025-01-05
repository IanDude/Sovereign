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

public class KingdomFragment extends Fragment {

    private EditText postContent;
    private Button addImageButton, submitPostButton;
    private ProgressBar progressBar;
    private ImageView imageView;
    private LinearLayout postsContainer;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference postsRef;
    private Uri selectedImageUri;
    private static final int PICK_IMAGE_REQUEST = 1;

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kingdom, container, false);

        // Initialize Firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        postsRef = firebaseDatabase.getReference("postKingdom");

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

        Post2 newPost = new Post2(postText, imageBase64, currentDateTime); // Only 3 parameters

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
                    Post2 post = postSnapshot.getValue(Post2.class);
                    if (post != null) {
                        LinearLayout postLayout = new LinearLayout(getContext());
                        postLayout.setOrientation(LinearLayout.VERTICAL);
                        postLayout.setPadding(16, 16, 16, 16);
                        postLayout.setBackground(getResources().getDrawable(R.drawable.round_lightopacity, null));
                        postLayout.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        ));

                        // Create and style the text view
                        TextView postTextView = new TextView(getContext());
                        postTextView.setText(post.getContent());
                        postTextView.setTextSize(16);
                        postLayout.addView(postTextView);

                        // Display the post date
                        TextView postDateView = new TextView(getContext());
                        postDateView.setText("Date: " + post.getDate());
                        postLayout.addView(postDateView);

                        // Check if there's an image to display
                        if (post.getImageUrl() != null && !post.getImageUrl().isEmpty()) {
                            byte[] imageBytes = Base64.decode(post.getImageUrl(), Base64.DEFAULT);
                            Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

                            if (decodedImage != null) {
                                ImageView postImageView = new ImageView(getContext());
                                postImageView.setImageBitmap(decodedImage);
                                postImageView.setLayoutParams(new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        750 // Fixed height for consistent UI
                                ));
                                postImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                postLayout.addView(postImageView);
                            }
                        }

                        // Add the combined layout to the container
                        postsContainer.addView(postLayout, 0);
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
