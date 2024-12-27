package com.example.sovereign;

import static android.app.Activity.RESULT_OK;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.graphics.Color;
import android.os.Bundle;
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

public class HOFFragment extends Fragment {

    private EditText postContent, postDate;
    private Button addImageButton, submitPostButton;
    private ProgressBar progressBar;
    private ImageView imageView;
    private LinearLayout postsContainer;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference postsRef;
    private Uri selectedImageUri;
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_h_o_f, container, false);

        // Initialize Firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        postsRef = firebaseDatabase.getReference("posts");

        postContent = view.findViewById(R.id.postContent);
        postDate = view.findViewById(R.id.postDate);
        addImageButton = view.findViewById(R.id.addImageButton);
        submitPostButton = view.findViewById(R.id.submitPostButton);
        progressBar = view.findViewById(R.id.progressBar);
        imageView = view.findViewById(R.id.imageView);
        postsContainer = view.findViewById(R.id.postsContainer);

        // Set up listeners
        postDate.setOnClickListener(v -> showDatePickerDialog(postDate));
        addImageButton.setOnClickListener(v -> openImagePicker());
        submitPostButton.setOnClickListener(v -> submitPost());

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
        String postDateText = postDate.getText().toString();

        if (postText.isEmpty()) {
            Toast.makeText(getContext(), "Please enter some content.", Toast.LENGTH_SHORT).show();
            return;
        }

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

        Post newPost = new Post(postText, imageBase64, postDateText);

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
                List<Post> postsList = new ArrayList<>();
                postsContainer.removeAllViews();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Post post = postSnapshot.getValue(Post.class);
                    if (post != null) {
                        postsList.add(post);
                    }
                }

                // Sort posts by date in descending order
                Collections.sort(postsList, (p1, p2) -> {
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        Date date1 = sdf.parse(p1.getDate());
                        Date date2 = sdf.parse(p2.getDate());
                        return date2.compareTo(date1); // Descending order
                    } catch (ParseException e) {
                        e.printStackTrace();
                        return 0;
                    }
                });

                // Display sorted posts
                for (Post post : postsList) {
                    LinearLayout postLayout = createPostLayout(post);
                    postsContainer.addView(postLayout);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Failed to load posts.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private LinearLayout createPostLayout(Post post) {
        LinearLayout postLayout = new LinearLayout(getContext());
        postLayout.setOrientation(LinearLayout.VERTICAL);
        postLayout.setPadding(16, 16, 16, 16);
        postLayout.setBackground(getResources().getDrawable(R.drawable.round_lightopacity, null));

        // Display post content
        TextView postTextView = new TextView(getContext());
        postTextView.setText(post.getContent());
        postLayout.addView(postTextView);

        // Display post date
        TextView postDateView = new TextView(getContext());
        postDateView.setText("Date: " + post.getDate());
        postDateView.setTextSize(12);
        postDateView.setTextColor(Color.GRAY);
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
                        800 // Fixed height for consistent UI
                ));
                postImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                postLayout.addView(postImageView);
            }
        }

        return postLayout;
    }
}
