package com.example.sovereign;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import java.io.ByteArrayOutputStream;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
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

import java.util.Calendar;

public class HOFFragment extends Fragment {

    private EditText postContent, postDate;
    private Button addImageButton;
    private Button submitPostButton;
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
        View view = inflater.inflate(R.layout.fragment_h_o_f, container, false);

        // Initialize Firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        postsRef = firebaseDatabase.getReference("posts");

        postContent = view.findViewById(R.id.postContent);
        postDate = view.findViewById(R.id.postDate);  // Add date input field
        addImageButton = view.findViewById(R.id.addImageButton);
        submitPostButton = view.findViewById(R.id.submitPostButton);
        progressBar = view.findViewById(R.id.progressBar);
        imageView = view.findViewById(R.id.imageView);
        postsContainer = view.findViewById(R.id.postsContainer);

        // Set up listeners
        addImageButton.setOnClickListener(v -> openImagePicker());
        submitPostButton.setOnClickListener(v -> submitPost());
        postDate.setOnClickListener(v -> showDatePickerDialog(postDate));  // Add date picker listener

        // Load existing posts from Firebase
        loadPosts();

        return view;
    }

    private void showDatePickerDialog(EditText dateField) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String selectedDate = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay;
                    dateField.setText(selectedDate);
                },
                year, month, day
        );
        datePickerDialog.show();
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
        String postDateText = postDate.getText().toString();  // Get date from the input


        if (postDateText.isEmpty()) {
            Toast.makeText(getContext(), "Please select a date.", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        String postId = postsRef.push().getKey();
        String imageBase64 = "";

        if (selectedImageUri != null) {
            try {
                // Convert selected image to Base64
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), selectedImageUri);
                imageBase64 = encodeImageToBase64(bitmap);
            } catch (Exception e) {
                Toast.makeText(getContext(), "Error encoding image.", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

        Post newPost = new Post(postText, imageBase64, postDateText); // Pass date to the Post constructor

        postsRef.child(postId).setValue(newPost).addOnCompleteListener(task -> {
            progressBar.setVisibility(View.GONE);
            if (task.isSuccessful()) {
                Toast.makeText(getContext(), "Post submitted successfully!", Toast.LENGTH_SHORT).show();
                postContent.setText(""); // Clear content
                postDate.setText(""); // Clear date
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
                    Post post = postSnapshot.getValue(Post.class);
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
                        postTextView.setTextColor(Color.BLACK);
                        postTextView.setPadding(8, 8, 8, 8);
                        postLayout.addView(postTextView);

                        // Display the post date
                        TextView postDateView = new TextView(getContext());
                        postDateView.setText("Date: " + post.getDate());
                        postDateView.setTextSize(12);
                        postDateView.setTextColor(Color.GRAY);
                        postLayout.addView(postDateView);

                        // Create a three-dot menu button
                        ImageView threeDotsButton = new ImageView(getContext());
                        threeDotsButton.setImageResource(R.drawable.ic_three_dots); // Make sure you have an icon for the three dots
                        threeDotsButton.setLayoutParams(new LinearLayout.LayoutParams(50, 50));
                        threeDotsButton.setOnClickListener(v -> showPostOptions(postSnapshot.getKey(), v)); // Handle the menu options
                        postLayout.addView(threeDotsButton);

                        // Check if there's an image to display
                        if (post.getImageUrl() != null && !post.getImageUrl().isEmpty()) {
                            byte[] imageBytes = Base64.decode(post.getImageUrl(), Base64.DEFAULT);
                            Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

                            if (decodedImage != null) {
                                ImageView postImageView = new ImageView(getContext());
                                postImageView.setImageBitmap(decodedImage);
                                postImageView.setLayoutParams(new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        800 // Fixed height for consistent UI
                                ));
                                postImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                                // Set click listener for full-screen view
                                postImageView.setOnClickListener(v -> {
                                    Intent intent = new Intent(getContext(), ImageFullViewActivity.class);
                                    intent.putExtra(ImageFullViewActivity.IMAGE_BYTE_ARRAY_KEY, imageBytes);
                                    startActivity(intent);
                                });

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

    private void showPostOptions(String postId, View anchorView) {
        PopupMenu popupMenu = new PopupMenu(getContext(), anchorView, 0, 0, R.style.CustomPopupMenu);

        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.post_options_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {

                case R.id.menu_delete:
                    deletePost(postId);
                    return true;

                default:
                    return false;
            }
        });

        // Show the PopupMenu near the three-dot button (anchorView)
        popupMenu.show();
    }

    private void deletePost(String postId) {
        postsRef.child(postId).removeValue()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "Post deleted.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Error deleting post.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void savePost(String postId) {
        // Implement saving logic here (e.g., save to favorites, etc.)
        Toast.makeText(getContext(), "Save post: " + postId, Toast.LENGTH_SHORT).show();
    }
}
