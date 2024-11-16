package com.example.sovereign;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class LoginSignup extends AppCompatActivity {
    FirebaseFirestore firebase = FirebaseFirestore.getInstance();
    Button login;
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

        login = findViewById(R.id.login_button);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Userinput((HashMap) Users);

            }
        });

    }
    protected void toMain(){
        new Handler(Looper.getMainLooper()).postDelayed(()->{
            Intent gotoMain = new Intent(LoginSignup.this,MainActivity.class);
            startActivity(gotoMain);
            finish();
        },1000);
    }
    protected void Userinput(HashMap hashMap){
        hashMap.put("IanDude","0123");
        firebase.collection("Users").add(hashMap)
                .addOnSuccessListener(documentReference -> Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_SHORT).show());
        toMain();
    }
}