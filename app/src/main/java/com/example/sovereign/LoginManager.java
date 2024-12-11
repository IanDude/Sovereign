package com.example.sovereign;

import android.content.Context;
import android.content.Intent;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Map;


public class LoginManager {
    protected FirebaseFirestore firebase;
    protected Context context;
    protected static final String Pref_name = "AppPrefs";
    protected static final String key_is_logged_in = "isLoggedIn";

    protected LoginManager(Context context){
        this.firebase = FirebaseFirestore.getInstance();
        this.context = context;
    }

    protected void saveLoginState(){
        context.getSharedPreferences(Pref_name,Context.MODE_PRIVATE)
                .edit()
                .putBoolean(key_is_logged_in,true)
                .apply();
    }

    protected boolean isLoggedIn(){
        return context.getSharedPreferences(Pref_name,Context.MODE_PRIVATE)
                .getBoolean(key_is_logged_in,false);
    }

    protected void logout(Class<?> loginactivityclass){
        context.getSharedPreferences(Pref_name,Context.MODE_PRIVATE)
                .edit()
                .remove(key_is_logged_in)
                .apply();
        Intent intent = new Intent(context,loginactivityclass);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
    protected void ToMain(Class<?> mainactivity){
        Intent intent = new Intent(context, mainactivity);

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
                                }).addOnFailureListener(e -> callback.onLoginFailure("Error checking password: " + e.getMessage()));
                    }else{
                        Toast.makeText(context.getApplicationContext(),"User not found",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> callback.onLoginFailure("Error checking username: " + e.getMessage()));
    }
    protected interface LoginCallback{
        void onLoginSuccess();
        void onLoginFailure(String errorMessage);
    }


}