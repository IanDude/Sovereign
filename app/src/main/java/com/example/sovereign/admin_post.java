package com.example.sovereign;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button; // Ensure you import the Button class
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class admin_post extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_post);

        // Adjust padding for system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Find the button and set an OnClickListener
        Button btnuser = findViewById(R.id.btnuser); // Ensure btnuser exists in activity_admin_post.xml
        btnuser.setOnClickListener(view -> {
            Intent intent = new Intent(admin_post.this, MainActivity.class);
            startActivity(intent);
        });

        Button btnPost = findViewById(R.id.btnPost);
        EditText editTextTitle = findViewById(R.id.editTextTitle);
        EditText editTextCaption = findViewById(R.id.editTextCaption);

        btnPost.setOnClickListener(v -> {
            String title = editTextTitle.getText().toString();
            String caption = editTextCaption.getText().toString();

            // Assuming ClanFragment is the destination fragment
            ClanFragment clanFragment = new ClanFragment();
            Bundle bundle = new Bundle();
            bundle.putString("postTitle", title);
            bundle.putString("postCaption", caption);
            clanFragment.setArguments(bundle);

            // Load ClanFragment (could be done from MainActivity or admin_post activity)
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.Mainlayout, clanFragment)
                    .commit();
        });

    }
}
