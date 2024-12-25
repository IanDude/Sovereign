package com.example.sovereign;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Splashscreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Use full-screen immersive mode for a better splash screen experience
        setContentView(R.layout.activity_spashscreen); // Ensure the layout file is correctly named

        // Handle system UI insets (e.g., status bar, navigation bar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Delay for the splash screen before launching the next activity
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(Splashscreen.this, LoginSignup.class); // Change to the next activity (e.g., LoginSignup)
            startActivity(intent);
            finish();  // Ensure this activity is finished so the user cannot go back to it
        }, 2000); // 2 seconds delay, adjust if needed
    }
}
