package com.example.sovereign;


import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.util.HashMap;
import java.util.Map;


public class Login extends AppCompatActivity {
//    FirebaseFirestore firebase = FirebaseFirestore.getInstance();
    protected LoginManager Manager;
    CheckBox showPass;
    Button login,signup;
    TextView ForgotPass;
    EditText username,password;
//    Map<String, Object> Users  = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Manager = new LoginManager(this);
//        boolean isLoggedIn = getSharedPreferences("AppPrefs",MODE_PRIVATE)
//                .getBoolean("isLoggedIn",false);
        Manager.isLoggedIn();
        if (Manager.isLoggedIn()){
            Manager.ToActivity(MainActivity.class);
            return;
        }
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        username = findViewById(R.id.username_text);
        password = findViewById(R.id.password_text);
        ForgotPass = findViewById(R.id.forgotpass);
        signup = findViewById(R.id.signup_button);
        login = findViewById(R.id.login_button);
        showPass = findViewById(R.id.checkBox);

        showPass.setOnCheckedChangeListener((compoundButton, checked) -> {
            if (!checked){
                password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                password.setSelection(password.getText().length());
            }else{
                password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                password.setSelection(password.getText().length());
            }

        });

        login.setOnClickListener(view -> {
//            DataCheck(username,password);
            if(Manager.EmptyFields(username,password)){
                Manager.MakeToast("Please fill all fields.");
//                Toast.makeText(getApplicationContext(),"Please fill all fields.",Toast.LENGTH_SHORT).show();
            }else{
                Manager.UserLogin(username, password, "Users", new LoginManager.LoginCallback() {
                    @Override
                    public void onLoginSuccess() {
//                        saveLoginState();
                        Manager.saveLoginState();
                        Manager.ToActivity(MainActivity.class);
                        finish();
                    }

                    @Override
                    public void onLoginFailure(String errorMessage) {
                        Manager.MakeToast(errorMessage);
//                        Toast.makeText(getApplicationContext(),errorMessage,Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        signup.setOnClickListener(view -> {
            Manager.ToActivity(SignUp.class);
        });
        ForgotPass.setOnClickListener(view -> {
            Manager.ToActivity(PasswordRecovery.class);
        });

    }
//    protected void toActivity(Class<?> activity){
//        Intent gotoActivity = new Intent(this,activity);
//        startActivity(gotoActivity);
//        finish();
//    }
//    protected void saveLoginState(){
//        getSharedPreferences("AppPrefs",MODE_PRIVATE)
//                .edit()
//                .putBoolean("isLoggedIn",true)
//                .apply();
//    }
//    protected void logout(){
//        getSharedPreferences("AppPrefs",MODE_PRIVATE)
//                .edit()
//                .remove("isLoggedIn")
//                .apply();
//        Intent intent = new Intent(this, LoginSignup.class);
//        startActivity(intent);
//        finish();
//    }


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