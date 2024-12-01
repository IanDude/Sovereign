package com.example.sovereign;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.util.HashMap;
import java.util.Map;


public class LoginSignup extends AppCompatActivity {
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
        loginSignUp = new LoginSignUpFunc(this);
        username = findViewById(R.id.username_text);
        password = findViewById(R.id.password_text);
        signup = findViewById(R.id.signup_button);
        login = findViewById(R.id.login_button);
        boolean isLoggedIn = getSharedPreferences("AppRefs",MODE_PRIVATE)
                .getBoolean("isLoggedIn",true);
        if (isLoggedIn){
            toMain();
        }else{
            setContentView(R.layout.activity_login_signup);
        }
        login.setOnClickListener(view -> {
//            DataCheck(username,password);
            if(loginSignUp.EmptyFields(username,password)){
                Toast.makeText(getApplicationContext(),"Please fill all fields.",Toast.LENGTH_SHORT).show();
            }else{
                loginSignUp.UserLogin(username, password, "Users", new LoginSignUpFunc.LoginCallback() {
                    @Override
                    public void onLoginSuccess() {
                        Toast.makeText(getApplicationContext(),"Login Successful",Toast.LENGTH_SHORT).show();
                        saveLoginState();
                        toMain();
                    }

                    @Override
                    public void onLoginFailure(String errorMessage) {
                        Toast.makeText(getApplicationContext(),errorMessage,Toast.LENGTH_SHORT).show();
                    }
                });
            }


        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//            UserSignup((HashMap) Users);
                if (loginSignUp.EmptyFields(username,password)){
                    Toast.makeText(getApplicationContext(),"Please fill all fields.",Toast.LENGTH_SHORT).show();
                }else{
                    loginSignUp.UserSignUp(username,password,"Users",Users);
                }
            }
        });
    }
    protected void saveLoginState(){
        getSharedPreferences("AppPrefs",MODE_PRIVATE)
                .edit()
                .putBoolean("isLoggedIn",true)
                .apply();
    }
    protected void logout(){
        getSharedPreferences("AppPrefs",MODE_PRIVATE)
                .edit()
                .remove("isLoggedIn")
                .apply();
        Intent intent = new Intent(this, LoginSignup.class);
        startActivity(intent);
        finish();
    }
    protected void toMain(){
            Intent gotoMain = new Intent(LoginSignup.this,MainActivity.class);
            startActivity(gotoMain);
            finish();
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