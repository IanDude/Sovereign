package com.example.sovereign;

import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {
    protected LoginManager Manager;
    EditText Student_No,Username,Password,Confirm_pass,Email_add;
    Button SignUp;
    CheckBox showPass, showCPass;
    Map<String,Object> NewUser = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Manager = new LoginManager(this);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Student_No = findViewById(R.id.student_no);
        Username = findViewById(R.id.username);
        Password = findViewById(R.id.password);
        Confirm_pass = findViewById(R.id.confirm_pass);
        Email_add = findViewById(R.id.email_add);
        SignUp = findViewById(R.id.sign_up_button);
        showPass = findViewById(R.id.showpass);
        showCPass = findViewById(R.id.showcpass);

        showPass.setOnCheckedChangeListener((compoundButton, checked) -> {
            if (!checked){
                Password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                Password.setSelection(Password.getText().length());
            }else{
                Password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                Password.setSelection(Password.getText().length());
            }
        });

        showCPass.setOnCheckedChangeListener((compoundButton, checked) -> {
            if (!checked){
                Confirm_pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                Confirm_pass.setSelection(Confirm_pass.getText().length());
            }else{
                Confirm_pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                Confirm_pass.setSelection(Confirm_pass.getText().length());
            }
        });

        SignUp.setOnClickListener(view -> Manager.UserSignUp(Student_No,Username,Password,Confirm_pass,Email_add,"Users", NewUser));
    }
}