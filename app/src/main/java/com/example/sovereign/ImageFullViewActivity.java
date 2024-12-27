package com.example.sovereign;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

public class ImageFullViewActivity extends Activity {

    public static final String IMAGE_BYTE_ARRAY_KEY = "imageByteArray";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_full_view);

        ImageView fullImageView = findViewById(R.id.fullImageView);

        // Retrieve the byte array passed through the intent
        byte[] imageByteArray = getIntent().getByteArrayExtra(IMAGE_BYTE_ARRAY_KEY);
        if (imageByteArray != null) {
            // Decode the byte array to a Bitmap and set it to the ImageView
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
            fullImageView.setImageBitmap(bitmap);
        } else {
            // Show an error message if the image data is missing
            Toast.makeText(this, "Image not available", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
