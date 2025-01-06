package com.example.sovereign;


import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


public class Login extends AppCompatActivity {
    protected LoginManager Manager;
    CheckBox showPass;
    ImageButton login,signup;
    TextView ForgotPass;
    EditText username,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Manager = new LoginManager(this);
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

        showPass.setOnCheckedChangeListener((compoundButton, checked) -> Manager.ShowPass(password,checked));

        login.setOnClickListener(view -> {
            if((Manager.EmptyFields(username) || Manager.EmptyFields(password))){
                Manager.MakeToast("Please fill all fields.");
            }else{
                Manager.UserLogin(username.getText().toString().trim(), password.getText().toString().trim(), new LoginManager.LoginCallback() {
                    @Override
                    public void onLoginSuccess() { //User and Password exists in firebase
                        Manager.RetrieveUserState(username.getText().toString().trim(), new LoginManager.OnRetrieveUserListener() {
                            @Override
                            public void onDataRetrieve(String ID, String UserType, String Department) {
                                Manager.saveUserState(ID,UserType,Department);
                            }
                            @Override
                            public void onFailure(Exception e) {
                                Manager.MakeToast("User type retrieval failed");
                                Log.e("Retrieval","Retrieval failed" + e.getMessage());
                            }
                        });
                        Manager.ToActivity(MainActivity.class);
                        finish();
                    }
                    @Override
                    public void onLoginFailure(String errorMessage) {
                        Manager.MakeToast(errorMessage);
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
}