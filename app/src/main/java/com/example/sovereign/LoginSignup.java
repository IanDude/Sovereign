package com.example.sovereign;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class LoginSignup extends AppCompatActivity {
    private MediaPlayer mediaPlayer;

//    FirebaseFirestore firebase = FirebaseFirestore.getInstance();
    private LoginSignUpFunc loginSignUp;
    Button login,signup;
    EditText username,password;
    Map<String, Object> Users  = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_signup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mediaPlayer = MediaPlayer.create(this, R.raw.trumpet); // Use the music file in res/raw
        mediaPlayer.setLooping(true);  // Set the music to loop
        mediaPlayer.start();  // Start playing the music

        loginSignUp = new LoginSignUpFunc(this);
        username = findViewById(R.id.username_text);
        password = findViewById(R.id.password_text);
        signup = findViewById(R.id.signup_button);
        login = findViewById(R.id.login_button);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//            DataCheck(username,password);
                toMain();

            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//            UserSignup((HashMap) Users);
            }
        });



    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Stop the music when the activity is destroyed to release resources
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }
    protected void toMain(){
        new Handler(Looper.getMainLooper()).postDelayed(()->{
            Intent gotoMain = new Intent(LoginSignup.this,MainActivity.class);
            startActivity(gotoMain);
            finish();
        },1000);
    }
//    protected void UserSignup(HashMap hashMap){
//        String usernameInput = username.getText().toString();
//        String passwordInput = password.getText().toString();
//        hashMap.put("Username",usernameInput);
//        hashMap.put("Password",passwordInput);
//        firebase.collection("Users")
//                .add(hashMap)
//                .addOnSuccessListener(documentReference -> {Toast.makeText(getApplicationContext(),"Sign Up Successful",Toast.LENGTH_SHORT).show();
//                    toMain();
//                })
//                .addOnFailureListener(e -> {Toast.makeText(getApplicationContext(),"Sign Up Failed",Toast.LENGTH_SHORT).show();
//                });
//    }
//    protected void DataCheck(EditText usernameInput, EditText passwordInput) {
//        String user = usernameInput.getText().toString().trim();
//        String password = passwordInput.getText().toString().trim();
//
//        if (user.isEmpty() || password.isEmpty()) {
//            Toast.makeText(getApplicationContext(), "Please fill all fields.", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        firebase.collection("Users")
//                .whereEqualTo("Username", user)
//                .whereEqualTo("Password", password)
//                .get()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
//                        Toast.makeText(getApplicationContext(), "User Already Exists.", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(getApplicationContext(), "User not found. You can Sign-Up!", Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .addOnFailureListener(e -> {
//                    Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                });
//    }




}