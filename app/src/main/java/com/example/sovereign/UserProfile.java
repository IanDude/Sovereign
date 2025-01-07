package com.example.sovereign;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class UserProfile extends AppCompatActivity {
    LoginManager Manager;
    TextView username,id_number,firstname,lastname,email_add,department,edituserlabel;
    ImageButton update,logout,edituser,editdept;
    FrameLayout edituserlayout;
    LinearLayout buttons2;
    protected String UserType;
    protected ArrayList<String> DeptItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Manager = new LoginManager(this);
        UserType = Manager.getUserType();
        DeptItems = Manager.getDeptList();
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        username = findViewById(R.id.username);
        id_number = findViewById(R.id.id_number);
        firstname = findViewById(R.id.firstname);
        lastname = findViewById(R.id.lastname);
        email_add = findViewById(R.id.email_add);
        department = findViewById(R.id.department);

        editdept = findViewById(R.id.edit_dept_button);

        edituser = findViewById(R.id.edit_user_button);
        edituserlabel = findViewById(R.id.edit_user_label);
        edituserlayout = findViewById(R.id.edit_user_layout);

        buttons2 = findViewById(R.id.buttons2);

        update = findViewById(R.id.update);
        logout = findViewById(R.id.logout);

        if (UserType.equals("Admin")){
//            edituserlayout.setVisibility(View.VISIBLE);
//            edituserlayout.setBackgroundResource(R.drawable.button_selector);
//            edituserlabel.setVisibility(View.VISIBLE);
            buttons2.setVisibility(View.VISIBLE);
        }

        Manager.RetrieveData(Manager.getUserID(), new LoginManager.OnDataRetrieval() {
            @Override
            public void onDataRetrieved(String Username, String ID_Number, String Department, String FirstName, String LastName, String Email_Add) {
                username.setText(Username);
                id_number.setText(ID_Number);
                firstname.setText(FirstName);
                department.setText(Department);
                lastname.setText(LastName);
                email_add.setText(Email_Add);
            }
            @Override
            public void onFailure(Exception e) {
                Manager.MakeToast("Could not load data");
                Log.e("Firestore","Failed to load: " + e.getMessage());
            }
        });

        edituser.setOnClickListener(view ->{
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            TextView title1 = new TextView(this);
            title1.setText("Enter Username");
            title1.setTextColor(getColor(R.color.white));
            title1.setTextSize(20);
            title1.setPadding(30,30,30,30);
            title1.setTypeface(null,Typeface.BOLD);
            builder1.setCustomTitle(title1);

            View customLayout1 = getLayoutInflater().inflate(R.layout.update_dialog_edittext,null);
            builder1.setView(customLayout1);

            EditText username_input = customLayout1.findViewById(R.id.newInput);
            username_input.requestFocus();
            username_input.setHint("Username");
            username_input.setInputType(InputType.TYPE_CLASS_TEXT);

            builder1.setPositiveButton("Next",(dialog1,which1) -> {
                AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                TextView title = new TextView(this);
                title.setText("Select New User Type:");
                title.setTextColor(getColor(R.color.white));
                title.setTextSize(20);
                title.setTypeface(null,Typeface.BOLD);
                title.setPadding(30,30,30,30);
                builder2.setCustomTitle(title);

                View customLayout = getLayoutInflater().inflate(R.layout.usertype_dialog,null);
                builder2.setView(customLayout);

                RadioGroup radiogroup = customLayout.findViewById(R.id.usertype_radiogroup);
                builder2.setPositiveButton("Update",(dialog,which) -> {
                    int selectedId = radiogroup.getCheckedRadioButtonId();
                    if (selectedId != -1){
                        RadioButton selectedRadiobutton = customLayout.findViewById(selectedId);
                        String selectedType = selectedRadiobutton.getText().toString();
                        Manager.RetrieveID(username_input.getText().toString(), new LoginManager.OnIDRetrieved() {
                            @Override
                            public void onSuccess(String ID) {
                                Manager.UpdateData(ID,"UserType",selectedType);
                            }
                            @Override
                            public void onFailure(Exception e) {
                                Manager.MakeToast("Username does not exist");
                            }
                        });
                    }
                });
                builder2.setNegativeButton("Cancel",(dialog2,which2) -> dialog2.dismiss());

                AlertDialog updateDialog = builder2.create();
                updateDialog.getWindow().setBackgroundDrawableResource(R.drawable.background_gradient);
                updateDialog.show();

                updateDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.WHITE);
                updateDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.WHITE);
            });
            builder1.setNegativeButton("Cancel",(dialog1,which1) -> dialog1.dismiss());
            AlertDialog updateDialog1 = builder1.create();
            updateDialog1.getWindow().setBackgroundDrawableResource(R.drawable.background_gradient);
            updateDialog1.show();

            updateDialog1.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.WHITE);
            updateDialog1.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.WHITE);

        });

        editdept.setOnClickListener(view -> {
            AlertDialog.Builder builder3 = new AlertDialog.Builder(this);
            TextView title1 = new TextView(this);
            title1.setText("Enter New Department");
            title1.setTextColor(getColor(R.color.white));
            title1.setTextSize(20);
            title1.setPadding(30,30,30,30);
            title1.setTypeface(null,Typeface.BOLD);
            builder3.setCustomTitle(title1);

            View customLayout1 = getLayoutInflater().inflate(R.layout.update_dialog_edittext,null);
            builder3.setView(customLayout1);

            EditText new_input = customLayout1.findViewById(R.id.newInput);
            new_input.requestFocus();
            new_input.setHint("Department");
            new_input.setInputType(InputType.TYPE_CLASS_TEXT);

            builder3.setPositiveButton("Add",(dialog3,which3) ->{
                if(new_input.getText().toString().isEmpty()){
                    Manager.MakeToast("Invalid Input");
                }else {
                    DeptItems.add(new_input.getText().toString());
                    Manager.saveDeptList(DeptItems);
                    Manager.MakeToast(new_input.getText().toString() + " Added");
                }

            });
            builder3.setNeutralButton("Remove",(dialog3,which3) ->{
                AlertDialog.Builder builder4 = new AlertDialog.Builder(this);
                TextView title4 = new TextView(this);
                title4.setText("Select a Department");
                title4.setTextColor(getColor(R.color.white));
                title4.setTextSize(20);
                title4.setPadding(30,30,30,30);
                title4.setTypeface(null,Typeface.BOLD);
                builder4.setCustomTitle(title4);

                View customLayout4 = getLayoutInflater().inflate(R.layout.delete_dept_dropdown,null);
                builder4.setView(customLayout4);
                ArrayList<String> items = Manager.getDeptList();
                Spinner Department = customLayout4.findViewById(R.id.spinner_item);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this,R.layout.spinner_item,items);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                Department.setAdapter(adapter);
                String selectedItem = Department.getSelectedItem().toString();

                builder4.setPositiveButton("Remove",(dialog4,which4) -> {
                    DeptItems.remove(selectedItem);
                    Manager.saveDeptList(DeptItems);
                    Manager.MakeToast(selectedItem + " Removed");
                });
                builder4.setNegativeButton("Cancel",(dialog4,which4) -> dialog4.dismiss());

                AlertDialog removeDept = builder4.create();
                removeDept.getWindow().setBackgroundDrawableResource(R.drawable.background_gradient);
                removeDept.show();

                removeDept.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.WHITE);
                removeDept.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.WHITE);

            });


            builder3.setNegativeButton("Cancel",(dialog3,which3) ->dialog3.dismiss());

            AlertDialog updateDept = builder3.create();
            updateDept.getWindow().setBackgroundDrawableResource(R.drawable.background_gradient);
            updateDept.show();

            updateDept.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.WHITE);
            updateDept.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.WHITE);
            updateDept.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(Color.WHITE);

        });


        update.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            TextView title = new TextView(this);
            title.setText("Choose an option:");
            title.setTextColor(getColor(R.color.white));
            title.setTextSize(20);
            title.setTypeface(null, Typeface.BOLD);
            title.setPadding(30,30,30,30);
            builder.setCustomTitle(title);

            View customLayout = getLayoutInflater().inflate(R.layout.update_dialog,null);
            builder.setView(customLayout);

            RadioGroup radiogroup = customLayout.findViewById(R.id.radioGroup);

            builder.setPositiveButton("Next",(dialog,which) ->{
                int selectedId = radiogroup.getCheckedRadioButtonId();
                if(selectedId != -1){
                    RadioButton selectedRadioButton = customLayout.findViewById(selectedId);
                    String selectedOption = selectedRadioButton.getText().toString();
                    //Another dialog with editText for user input

                    AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                    TextView title2 = new TextView(this);
                    title2.setText("Enter New " + selectedOption);
                    title2.setTextColor(getColor(R.color.white));
                    title2.setTextSize(20);
                    title2.setTypeface(null, Typeface.BOLD);
                    title2.setPadding(30,30,30,30);
                    builder2.setCustomTitle(title2);
                    View customLayout2 = getLayoutInflater().inflate(R.layout.update_dialog_edittext,null);
                    builder2.setView(customLayout2);

                    EditText newInput = customLayout2.findViewById(R.id.newInput);
                    newInput.requestFocus();

                    CheckBox showpass = customLayout2.findViewById(R.id.showpass);
                    newInput.setHint(selectedOption);
                    switch (selectedOption) {
                        case "Username":
                        case "FirstName":
                        case "LastName":
                        case "Department":
                            newInput.setInputType(InputType.TYPE_CLASS_TEXT);
                            newInput.requestFocus();
                            break;
                        case "Email Address":
                            newInput.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                            newInput.requestFocus();
                            break;
                        case "ID Number":
                            newInput.setInputType(InputType.TYPE_CLASS_NUMBER);
                            newInput.requestFocus();
                            break;
                        case "Password":
                            newInput.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            showpass.setVisibility(View.VISIBLE);
                            newInput.requestFocus();
                            break;
                    }
                    showpass.setOnCheckedChangeListener((compoundButton, checked) -> Manager.ShowPass(newInput,checked));
                    builder2.setPositiveButton("Confirm",(dialog2,which2) ->{
                        if (newInput.getText().toString().isEmpty()){
                            Manager.MakeToast("Empty Fields");
                        }else{
                            Manager.UpdateData(Manager.getUserID(),selectedOption,newInput.getText().toString());
                            Manager.MakeToast("Update Successful");
                        }

                    });
                    builder2.setNegativeButton("Cancel", (dialog2,which2) -> dialog2.dismiss());
                    AlertDialog updateDialog2 = builder2.create();
                    updateDialog2.getWindow().setBackgroundDrawableResource(R.drawable.background_gradient);
                    updateDialog2.show();

                    updateDialog2.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.WHITE);
                    updateDialog2.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.WHITE);

                }else{
                    Manager.MakeToast("No option selected");
                }
            });
            builder.setNegativeButton("Cancel",(dialog,which)->dialog.dismiss());

            AlertDialog updateDialog = builder.create();
            updateDialog.getWindow().setBackgroundDrawableResource(R.drawable.background_gradient);
            updateDialog.show();

            updateDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.WHITE);
            updateDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.WHITE);
        });

        logout.setOnClickListener(view -> {
            Manager.logout();
            finish();
        });

    }
}