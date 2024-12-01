package com.example.sovereign;

import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Map;


public class LoginSignUpFunc {
    protected FirebaseFirestore firebase;
    protected Context context;

    protected LoginSignUpFunc(Context context){
        this.firebase = FirebaseFirestore.getInstance();
        this.context = context;
    }

    protected void UserSignUp(EditText UserInput, EditText PassInput, String Collection, Map<String, Object> hashMap){
        String Username = UserInput.getText().toString().trim();
        String Password = PassInput.getText().toString().trim();

        firebase.collection(Collection)
                .whereEqualTo("Username",Username)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()){
                        //username already in database
                        Toast.makeText(context.getApplicationContext(),"Invalid Username",Toast.LENGTH_SHORT).show();
                    }else{
                        //username not in database, adds the new data
                        hashMap.put("Username", Username);
                        hashMap.put("Password", Password);
                        firebase.collection(Collection)
                                .add(hashMap)
                                .addOnSuccessListener(documentReference -> Toast.makeText(context.getApplicationContext(),"Sign Up Successfuil",Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e -> Toast.makeText(context.getApplicationContext(),"Sign Up Failed" + e.getMessage(), Toast.LENGTH_SHORT).show());
                    }
                }).addOnFailureListener(e -> Toast.makeText(context.getApplicationContext(),"Error"+ e.getMessage(),Toast.LENGTH_SHORT).show());

    }
    protected boolean EmptyFields(EditText UserInput, EditText PassInput){
        String Username = UserInput.getText().toString().trim();
        String Password = PassInput.getText().toString().trim();
        return Username.isEmpty() || Password.isEmpty();
    }
    protected void UserLogin (EditText UserInput, EditText PassInput, String Collection, LoginCallback callback){
        String Username = UserInput.getText().toString();
        String Password = PassInput.getText().toString();
        firebase.collection(Collection)
                .whereEqualTo("Username", Username)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty() ){
                        firebase.collection(Collection)
                                .whereEqualTo("Username",Username)
                                .whereEqualTo("Password", Password)
                                .get().addOnCompleteListener(task2 -> {
                                    if (task2.isSuccessful() && task2.getResult() != null && !task2.getResult().isEmpty()){
                                        Toast.makeText(context.getApplicationContext(),"Login Successful",Toast.LENGTH_SHORT).show();
                                        callback.onLoginSuccess();
                                    }
                                    else {
                                        //password doesnt match with username
                                        callback.onLoginFailure("Incorrect username or password.");
                                    }
                                }).addOnFailureListener(e -> { callback.onLoginFailure("Error checking password: " + e.getMessage());
                                });
                    }else{
                        Toast.makeText(context.getApplicationContext(),"User not found",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    callback.onLoginFailure("Error checking username: " + e.getMessage());
                });
    }
    protected interface LoginCallback{
        void onLoginSuccess();
        void onLoginFailure(String errorMessage);
    }

}
