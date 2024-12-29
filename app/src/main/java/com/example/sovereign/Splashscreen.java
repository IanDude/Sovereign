package com.example.sovereign;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
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

        // Set up MotionLayout for the splash screen animation
        motionLayout = findViewById(R.id.main);

        // Handle system UI insets (status bar, navigation bar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        try {
            // Initialize MediaPlayer for background music
            mediaPlayer = MediaPlayer.create(this, R.raw.intro); // Make sure the file is in the res/raw folder
            mediaPlayer.setLooping(true);  // Set music to loop
            mediaPlayer.start();  // Start playing the background music

            // Duration for splash screen (e.g., 2000ms for 2 seconds)
            int splashDuration = 10000;  // 2 seconds

            // Ensure the music stops after splash duration
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Stop the music after splash screen duration ends
                    if (mediaPlayer != null) {
                        mediaPlayer.stop(); // Stop the music
                        mediaPlayer.release(); // Release resources
                    }

                    // Transition to the next activity (LoginSignup in this case)
                    Intent intent = new Intent(Splashscreen.this, LoginSignup.class);
                    startActivity(intent);
                    finish();  // Close the splash screen activity
                }
            }, splashDuration);  // 2000ms = 2 seconds

        } catch (Exception e) {
            // Catch any errors related to MediaPlayer
            Log.e("Splashscreen", "Error initializing media player", e);
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
            } catch (Exception e) {
                Log.e("Splashscreen", "Error stopping media player", e);
            }
        }
    }
}
