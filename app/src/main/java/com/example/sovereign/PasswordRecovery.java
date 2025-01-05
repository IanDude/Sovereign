package com.example.sovereign;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PasswordRecovery extends AppCompatActivity {
    LoginManager Manager;
    EditText username, otp_input, newpass, newcpass;
    ImageButton send_otp, check_otp, update_pass;
    LinearLayout layout_changepass;
    TextView confirm_label;
    FrameLayout confirm;
    CheckBox shownewpass;
    protected String OTP_Code;
    protected long initialTime = 0;

    // Handler and Runnable for periodic time check
    Handler handler = new Handler();
    Runnable otpExpirationCheck = new Runnable() {
        @Override
        public void run() {
            long currentTime = System.currentTimeMillis();

            if ((currentTime - initialTime) > 120000) {
                Manager.MakeToast("One-Time Password expired.");
                layout_changepass.setVisibility(View.GONE);
                confirm.setVisibility(View.GONE);
                check_otp.setVisibility(View.GONE);
                otp_input.setText("");
                username.setText("");
                username.setEnabled(true);
                otp_input.setEnabled(false);
                confirm_label.setVisibility(View.GONE);
            } else {
                handler.postDelayed(this, 1000);
            }
        }
    };

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
        send_otp = findViewById(R.id.OTP_button);
        check_otp = findViewById(R.id.check_OTP);
        update_pass = findViewById(R.id.updatepass);
        shownewpass = findViewById(R.id.shownewpass);
        confirm_label = findViewById(R.id.confirmlabel);
        confirm = findViewById(R.id.confirm);

        send_otp.setOnClickListener(view -> {
            if (Manager.EmptyFields(username)) {
                Manager.MakeToast("Please fill your Username.");
            } else {
                OTP_Code = Manager.OTP_Generate();
                Manager.SendEmailOTP(username.getText().toString(), OTP_Code);

                // Reset all views to initial state before showing new OTP process
                username.setEnabled(false);
                otp_input.setEnabled(true);
                confirm.setVisibility(View.VISIBLE);
                confirm_label.setVisibility(View.VISIBLE);
                check_otp.setVisibility(View.VISIBLE); // Ensure the check OTP button is visible
                check_otp.setBackgroundResource(R.drawable.button_selector); // Reset button background

                // Start the timer again
                initialTime = System.currentTimeMillis();
                handler.post(otpExpirationCheck);
            }
        });

        check_otp.setOnClickListener(view -> {
            long currentTime = System.currentTimeMillis();
            if ((currentTime - initialTime) <= 120000) {
                if (OTP_Code.equals(otp_input.getText().toString().trim())) {
                    Manager.MakeToast("Authentication Verified");
                    username.setEnabled(false);
                    layout_changepass.setVisibility(View.VISIBLE);
                } else {
                    Manager.MakeToast("Authentication Denied");
                }
            } else {
                Manager.MakeToast("One-Time Password expired.");
                layout_changepass.setVisibility(View.GONE);
            }
        });

        update_pass.setOnClickListener(view -> {
            if (Manager.EmptyFields(newpass) || Manager.EmptyFields(newcpass)) {
                Manager.MakeToast("Please fill all fields.");
            } else {
                if (newpass.getText().toString().trim().length() < 8 || newcpass.getText().toString().trim().length() < 8) {
                    if (newpass.getText().toString().trim().equals(newcpass.getText().toString().trim())) {
                        Manager.RetrieveID(username.getText().toString().trim(), new LoginManager.OnIDRetrieved() {
                            @Override
                            public void onSuccess(String ID) {
                                Manager.UpdateData(ID, "Password", newpass.getText().toString());
                            }
                            @Override
                            public void onFailure(Exception e) {
                                Manager.MakeToast("Error: " + e.getMessage());
                            }
                        });
                    } else {
                        Manager.MakeToast("Passwords don't match.");
                    }
                } else {
                    Manager.MakeToast("Passwords should be 8 characters long.");
                }
            }
        });

        shownewpass.setOnCheckedChangeListener((compoundButton, checked) -> {
            Manager.ShowPass(newpass, checked);
            Manager.ShowPass(newcpass, checked);
        });
    }
}
