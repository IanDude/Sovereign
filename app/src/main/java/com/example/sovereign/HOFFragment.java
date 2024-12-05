package com.example.sovereign;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class HOFFragment extends Fragment {

    private EditText postContent;
    private Button addImageButton;
    private Button submitPostButton;
    private ProgressBar progressBar;
    private ImageView imageView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_h_o_f, container, false);

        // Initialize the views
        postContent = view.findViewById(R.id.postContent);
        addImageButton = view.findViewById(R.id.addImageButton);
        submitPostButton = view.findViewById(R.id.submitPostButton);
        progressBar = view.findViewById(R.id.progressBar);
        imageView = view.findViewById(R.id.imageView);

        // Set click listeners
        addImageButton.setOnClickListener(v -> openImagePicker());
        submitPostButton.setOnClickListener(v -> submitPost());

        return view;
    }

    // Method to open an image picker (you can implement the logic based on your requirements)
    private void openImagePicker() {
        // TODO: Add code to open an image picker and display the selected image in imageView
        // For now, we will simulate adding an image.
        imageView.setVisibility(View.VISIBLE);
        imageView.setImageResource(R.drawable.sample_image); // Replace with the image from picker
    }

    // Method to submit the post
    private void submitPost() {
        String postText = postContent.getText().toString();

        // Show loading indicator
        progressBar.setVisibility(View.VISIBLE);

        // Simulate post submission (replace with actual post logic)
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            // Hide loading indicator after a delay (simulating a network request)
            progressBar.setVisibility(View.GONE);

            // Show a success message
            Toast.makeText(getContext(), "Post submitted successfully!", Toast.LENGTH_SHORT).show();

            // Optionally, clear the post content and image
            postContent.setText("");
            imageView.setVisibility(View.GONE);  // Hide image preview after submission
        }, 2000);  // Simulate network delay

        }

    }

