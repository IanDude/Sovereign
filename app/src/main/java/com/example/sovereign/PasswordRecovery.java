package com.example.sovereign;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PasswordRecovery extends AppCompatActivity {
    LoginManager Manager;
    EditText username,otp_input,newpass,newcpass;
    Button resend_otp,check_otp,update_pass;
    LinearLayout layout_changepass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Manager = new LoginManager(this);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_password_recovery);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        username = findViewById(R.id.username);
        otp_input = findViewById(R.id.OTP_input);
        newpass = findViewById(R.id.new_pass);
        newcpass = findViewById(R.id.c_pass);
        layout_changepass = findViewById(R.id.layout_changepass);
        resend_otp = findViewById(R.id.OTP_button);
        check_otp = findViewById(R.id.check_OTP);
        update_pass = findViewById(R.id.updatepass);

    }
}