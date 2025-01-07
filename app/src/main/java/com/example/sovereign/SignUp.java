package com.example.sovereign;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class SignUp extends AppCompatActivity {
    protected LoginManager Manager;
    EditText ID_No,Username,FirstName,LastName,Password,Confirm_pass,Email_add;
    ImageButton SignUp;
    CheckBox showPass;
    Spinner Department;
    protected ArrayList<String> DeptList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Manager = new LoginManager(this);
        DeptList = Manager.getDeptList();
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ID_No = findViewById(R.id.ID_No);
        FirstName = findViewById(R.id.firstname);
        LastName = findViewById(R.id.lastname);
        Username = findViewById(R.id.username);
        Password = findViewById(R.id.password);
        Confirm_pass = findViewById(R.id.confirm_pass);
        Email_add = findViewById(R.id.email_add);
        SignUp = findViewById(R.id.sign_up_button);
        showPass = findViewById(R.id.showpass);
        Department = findViewById(R.id.department);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,R.layout.spinner_item,DeptList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Department.setAdapter(adapter);

        showPass.setOnCheckedChangeListener((compoundButton, checked) -> {
            Manager.ShowPass(Password,checked);
            Manager.ShowPass(Confirm_pass,checked);
        });




        SignUp.setOnClickListener(view -> {
            if (Manager.EmptyFields(ID_No) || Manager.EmptyFields(FirstName) || Manager.EmptyFields(LastName) ||
                    Manager.EmptyFields(Username) || Manager.EmptyFields(Password) ||
                    Manager.EmptyFields(Confirm_pass) || Manager.EmptyFields(Email_add)) {

                Manager.MakeToast("Please fill all fields");
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(Email_add.getText().toString()).matches()) {
                Manager.MakeToast("Input valid email address");
                return;
            }

            String selectedDepartment = Department.getSelectedItem().toString(); // Get selected department directly

            if (selectedDepartment == null || selectedDepartment.isEmpty()) {
                Manager.MakeToast("Select at least 1 Department");
                return;
            }

            Manager.UserSignUp(
                    ID_No.getText().toString().trim(),
                    Username.getText().toString().trim(),
                    FirstName.getText().toString().trim(),
                    LastName.getText().toString().trim(),
                    selectedDepartment,
                    Password.getText().toString().trim(),
                    Confirm_pass.getText().toString().trim(),
                    Email_add.getText().toString().trim()
            );
        });

    }
}