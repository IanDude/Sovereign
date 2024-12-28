package com.example.sovereign;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class Clan_Post extends AppCompatActivity {

    private EditText titleEditText, descriptionEditText;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clan_post);  // Set the correct layout

        // Initialize the views
        titleEditText = findViewById(R.id.titleEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        submitButton = findViewById(R.id.submitButton);

        // Set up the button to handle post submission
        submitButton.setOnClickListener(v -> {
            // Get data from the input fields
            String title = titleEditText.getText().toString();
            String description = descriptionEditText.getText().toString();

            // Create a new Clan_Post_Model object
            Clan_Post_Model newPost = new Clan_Post_Model(title, description);

            // Create an intent to send the new post back to ClanFragment
            Intent resultIntent = new Intent();
            resultIntent.putExtra("post", newPost);

            // Set the result and finish the activity
            setResult(Activity.RESULT_OK, resultIntent);
            finish();  // Close the activity and return to ClanFragment
        });
    }
}
