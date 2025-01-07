package com.example.sovereign;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Splashscreen extends AppCompatActivity {

    private MediaPlayer mediaPlayer;  // MediaPlayer for background music
    private MotionLayout motionLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spashscreen);

        TextView splashText = findViewById(R.id.splashText);
        ImageView imageView2 = findViewById(R.id.imageView2);

        // Load the fade-in animation
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);

        // Start the animation
        splashText.startAnimation(fadeIn);
        imageView2.startAnimation(fadeIn);

        // Handle system UI insets (status bar, navigation bar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        try {
            // Initialize MediaPlayer for background music
            mediaPlayer = MediaPlayer.create(this, R.raw.intro); // Ensure the file exists in res/raw
            if (mediaPlayer != null) {
                mediaPlayer.setLooping(true);  // Set music to loop
                mediaPlayer.start();  // Start playing the background music
            } else {
                Log.e("Splashscreen", "MediaPlayer failed to initialize");
            }

            // Duration for splash screen (e.g., 10 seconds)
            int splashDuration = 10000;

            // Stop music and transition to next activity after splash duration
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                // Stop the music after splash duration ends
                if (mediaPlayer != null) {
                    try {
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        mediaPlayer = null; // Nullify reference to prevent leaks
                    } catch (Exception e) {
                        Log.e("Splashscreen", "Error stopping media player", e);
                    }
                }

                // Transition to the next activity
                Intent intent = new Intent(Splashscreen.this, Login.class);
                startActivity(intent);
                finish();  // Close the splash screen activity
            }, splashDuration);

        } catch (Exception e) {
            Log.e("Splashscreen", "Error initializing MediaPlayer", e);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Ensure the music is stopped and resources are released when the activity is destroyed
        if (mediaPlayer != null) {
            try {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null; // Nullify reference
            } catch (Exception e) {
                Log.e("Splashscreen", "Error releasing MediaPlayer", e);
            }
        }
    }
}
