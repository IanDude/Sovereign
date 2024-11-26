package com.example.sovereign;

import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class LoginSignUpFunc {
    protected FirebaseFirestore firebase;
    protected Context context;

    protected LoginSignUpFunc(Context context){
        this.firebase = FirebaseFirestore.getInstance();
        this.context = context;
    }

    protected void UserSignUp(EditText UserInput, EditText PassInput, Map<String, Object> Collection, HashMap hashMap){
        AtomicBoolean UserExists = new AtomicBoolean(false);
        String Username = UserInput.getText().toString().trim();
        String Password = PassInput.getText().toString().trim();
        //Check if username already exists
        firebase.collection(Collection.toString())
                .whereEqualTo("Username",Username)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()){ //checks if user is found
                        UserExists.set(true);
                        Toast.makeText(context.getApplicationContext(),"Invalid Username",Toast.LENGTH_SHORT).show();
                    }else{
                        UserExists.set(false);
                    }
                }).addOnFailureListener(e -> {
                        Toast.makeText(context.getApplicationContext(),"Error"+ e.getMessage(),Toast.LENGTH_SHORT).show();
                });

        if (UserExists.equals(false)){
            hashMap.put("Username", Username);
            hashMap.put("Password", Password);
            firebase.collection(Collection.toString())
                    .add(hashMap)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(context.getApplicationContext(),"Sign Up Successfuil",Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context.getApplicationContext(),"Sign Up Failed" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }


    }
    protected boolean EmptyFields(EditText UserInput, EditText PassInput){
        String Username = UserInput.getText().toString().trim();
        String Password = PassInput.getText().toString().trim();
        if (Username.isEmpty() || Password.isEmpty()){
            Toast.makeText(context.getApplicationContext(),"Please fill all fields.",Toast.LENGTH_SHORT);
            return true;
        }else {
            return  false;
        }
    }
    protected void UserCheck (EditText UserInput, EditText PassInput, Map<String, Object> Collection){
        String Username = UserInput.getText().toString();
        String Password = PassInput.getText().toString();
        firebase.collection(Collection.toString())
                .whereEqualTo("Username", Username)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty() ){
                        firebase.collection(Collection.toString())
                                .whereEqualTo("Password", Password)
                                .get().addOnCompleteListener(task2 -> {
                                    if (task2.isSuccessful() && task2.getResult() != null && !task2.getResult().isEmpty()){

                                    }
                                }).addOnFailureListener(e -> {

                                });
                        Toast.makeText(context.getApplicationContext(),"User already exists.", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(context.getApplicationContext(),"",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {

                });
    }


}
