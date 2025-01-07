package com.example.sovereign;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.lang.reflect.Type;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;



public class LoginManager {
    protected FirebaseFirestore firebase;
    protected Context context;
    protected static final String PREF_NAME = "AppPrefs";
    protected static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    protected static final String SAVED_USER_ID = "SavedUserID";
    protected static final String SAVED_USERTYPE = "UserType";
    protected static final String SAVED_USER_DEPT = "SaveUserDepartment";

    protected static final String DEPT_ITEMS = "DeptList";
    protected static final String SAVED_ARRAYLIST = "ArrayList";


    protected LoginManager(Context context){
        this.firebase = FirebaseFirestore.getInstance();
        this.context = context;
    }

    protected void saveDeptList(ArrayList<String> deptlist){
        Gson gson = new Gson();
        String json = gson.toJson(deptlist);
        context.getSharedPreferences(DEPT_ITEMS,Context.MODE_PRIVATE)
                .edit()
                .putString(SAVED_ARRAYLIST,json)
                .apply();
    }



    protected ArrayList<String> getDeptList(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(DEPT_ITEMS,Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(SAVED_ARRAYLIST,null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        return json != null ? gson.fromJson(json,type) : new ArrayList<>();
    }


    protected void saveUserState(String UserID,String UserType,String Department){
        context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE)
                .edit()
                .putBoolean(KEY_IS_LOGGED_IN,true)
                .putString(SAVED_USER_ID,UserID)
                .putString(SAVED_USERTYPE,UserType)
                .putString(SAVED_USER_DEPT,Department)
                .apply();
    }

    protected boolean isLoggedIn(){
        return context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE)
                .getBoolean(KEY_IS_LOGGED_IN,false);
    }


    protected String getUserType(){
        return  context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE)
                .getString(SAVED_USERTYPE,"UserType");
    }
    protected String getUserID(){
        return context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE)
                .getString(SAVED_USER_ID,"SavedUserID");
    }

    protected String getUserDepartment(){
        return context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE)
                .getString(SAVED_USER_DEPT,"SAVED DEPARTMENT");
    }

    protected void logout(){
        context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE)
                .edit()
                .clear()
                .apply();
        Intent intent = new Intent(context, Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    protected void UserSignUp(String ID_No, String Username, String FirstName, String LastName, String Department, String Password,String C_Password,String EmailAdd){

        Map<String,Object> hashMap = new HashMap<>();
        if(Password.length()<8){
            MakeToast("Password should be 8 characters longs.");
            return;
        }
        if (Password.equals(C_Password)){
            String hashedPassword = hashPassword(Password);

            firebase.collection("Users")
                    .whereEqualTo("Username",Username)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()){
                            //username already in database
//                            Toast.makeText(context.getApplicationContext(),"Invalid Username",Toast.LENGTH_SHORT).show();
                            MakeToast("Username already exists");
                        }else{
                            //username not in database, adds the new data
                            hashMap.put("ID Number",ID_No);
                            hashMap.put("Username", Username);
                            hashMap.put("FirstName", FirstName);
                            hashMap.put("LastName",LastName);
                            hashMap.put("Department",Department);
                            hashMap.put("Password", hashedPassword);
                            hashMap.put("Email Address",EmailAdd);
                            hashMap.put("UserType","Regular");
                            firebase.collection("Users")
                                    .add(hashMap)
                                    .addOnSuccessListener(documentReference -> {
//                                        Toast.makeText(context.getApplicationContext(),"Sign Up Successful",Toast.LENGTH_SHORT).show();
                                        MakeToast("Sign Up Successful");
                                    })
                                    .addOnFailureListener(e -> {
//                                        Toast.makeText(context.getApplicationContext(),"Sign Up Failed" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        MakeToast("Sign Up Failed" + e.getMessage());
                                        Log.e("Firestore","Sign Up Failed: " + e.getMessage());
                                    });
                            ToActivity(Login.class);
                        }
                    }).addOnFailureListener(e -> {
//                        Toast.makeText(context.getApplicationContext(),"Error"+ e.getMessage(),Toast.LENGTH_SHORT).show();
                        MakeToast("Error" + e.getMessage());
                    });
        }else{
//            Toast.makeText(context.getApplicationContext(),"Passwords don't match",Toast.LENGTH_SHORT).show();
            MakeToast("Passwords don't match.");
        }

    }

    protected void UserLogin (String Username, String Password, LoginCallback callback){
        firebase.collection("Users")
                .whereEqualTo("Username", Username)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty() ){
//                            String storedhashPassword = task.getResult().getDocuments().get(0).getString("Password");
//                            String enteredhashPassword = hashPassword(Password);
//                            if (storedhashPassword.equals(enteredhashPassword)){
//                                callback.onLoginSuccess();
//                            }else {
//                                callback.onLoginFailure("Incorrect Password");
//                            }


                        firebase.collection("Users")
                                .whereEqualTo("Password", Password)
                                .get()
                                .addOnCompleteListener(task2 -> {
                                    if (task2.isSuccessful() && task2.getResult() != null && !task2.getResult().isEmpty()){
                                        callback.onLoginSuccess();
                                    }
                                    else {
                                        //password doesnt match with username
                                        callback.onLoginFailure("Incorrect Password");
                                    }
                                }).addOnFailureListener(e -> callback.onLoginFailure("Error checking Username or Password: " + e.getMessage()));
                    }else{
//                        Toast.makeText(context.getApplicationContext(),"User not found.",Toast.LENGTH_SHORT).show();
                        MakeToast("Invalid Username.");
                    }
                })
                .addOnFailureListener(e -> callback.onLoginFailure("Error checking username: " + e.getMessage()));
    }
    protected interface LoginCallback{
        void onLoginSuccess();
        void onLoginFailure(String errorMessage);
    }

    protected void SendEmailOTP(String Username,String OTP_Code){
        firebase.collection("Users")
                .whereEqualTo("Username",Username)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()){
                        QueryDocumentSnapshot document = (QueryDocumentSnapshot) task.getResult().getDocuments().get(0);
                        String Email = document.getString("Email Address");
                        if (Email != null){
                            String fromEmail = "sovereignofficial0@gmail.com";
                            String fromPassword = "jgjx vnrh fcvk yaza";

                            List<String> toEmailList = Arrays.asList(Email);
                            String emailSubject = "Password Update Authentication";
                            String emailBody = MessageFormat.format("<body style=\"font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f7f7f7;\">\n" +
                                    "    <div style=\"max-width: 600px; margin: 20px auto; background: #ffffff; border: 1px solid #dddddd; border-radius: 8px; overflow: hidden;\">\n" +
                                    "        <!-- Header -->\n" +
                                    "        <div style=\"background: #FF2626; color: white; padding: 20px; text-align: center;\">\n" +
                                    "            <h1 style=\"margin: 0;\">Your One-Time Password</h1>\n" +
                                    "        </div>\n" +
                                    "        <!-- Body -->\n" +
                                    "        <div style=\"padding: 20px; text-align: center;\">\n" +
                                    "            <p style=\"margin: 0 0 10px;\">Dear {0},</p>\n" +
                                    "            <p style=\"margin: 0 0 20px;\">Your one-time password (OTP) for verification with <strong style=\"font-family: 'Trahu', Arial, sans-serif;\">Sovereign</strong> is:</p>\n" +
                                    "            <div style=\"font-size: 24px; font-weight: bold; color: #FF2626; margin: 20px 0;\">{1}</div>\n" +
                                    "            <p style=\"margin: 0 0 10px;\">Please use this code to complete your action. This OTP is valid for the next 10 minutes.</p>\n" +
                                    "            <p style=\"margin: 0;\">If you did not request this OTP, please ignore this email or contact <strong style=\"font-family: 'Trahu', Arial, sans-serif;\">Sovereign</strong>.</p>\n" +
                                    "        </div>\n" +
                                    "        <!-- Footer -->\n" +
                                    "        <div style=\"background: #f1f1f1; padding: 10px; text-align: center; font-size: 12px; color: #555555;\">\n" +
                                    "            <p style=\"margin: 0;\">&copy; 2024 Sovereign. All rights reserved.</p>\n" +
                                    "        </div>\n" +
                                    "    </div>\n" +
                                    "</body>\n",Username,OTP_Code);

                            new Thread(() -> {
                                try {
                                    Gmail gmail = new Gmail(fromEmail, fromPassword, toEmailList, emailSubject, emailBody);
                                    gmail.createEmailMessage();
                                    gmail.sendEmail();
                                } catch (Exception e) {
                                    Log.e("GMail", "Error while sending email: " + e.getMessage(), e);
                                }
                            }).start();
                            MakeToast("Email Sent");
                        }else{
                            MakeToast("Email not found.");
                        }
                    }else{
                        MakeToast("User not found.");
                    }
                }).addOnFailureListener(e -> MakeToast("Error" + e.getMessage()));
    }

    protected void RetrieveUserState(String Username, OnRetrieveUserListener listener){
        firebase.collection("Users")
                .whereEqualTo("Username", Username)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()){
                        QueryDocumentSnapshot document = (QueryDocumentSnapshot) task.getResult().getDocuments().get(0);
                        String DocumentID = document.getId();
                        String UserType = document.getString("UserType");
                        String Department = document.getString("Department");
                        listener.onDataRetrieve(DocumentID,UserType,Department);
                    }
                })
                .addOnFailureListener(e -> {
                    MakeToast("Retrieval Failed");
                    Log.e("Firestore","Retrieve Failed: " + e.getMessage());
                    listener.onFailure(e);
                });
    }

    protected interface OnRetrieveUserListener{
        void onDataRetrieve(String ID, String UserType, String Department);
        void onFailure(Exception e);
    }
    
    protected void RetrieveData(String DocumentID, OnDataRetrieval RetrieveData){
        firebase.collection("Users")
                .whereEqualTo(FieldPath.documentId(),DocumentID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()){
                        QueryDocumentSnapshot document = (QueryDocumentSnapshot) task.getResult().getDocuments().get(0);
                        String username = document.getString("Username");
                        String id_number = document.getString("ID Number");
                        String department = document.getString("Department");
                        String firstname = document.getString("FirstName");
                        String lastname = document.getString("LastName");
                        String email_add = document.getString("Email Address");
                        RetrieveData.onDataRetrieved(username, id_number,department,firstname,lastname,email_add);
                    }
                }).addOnFailureListener(RetrieveData ::onFailure);
    }

    protected interface OnDataRetrieval{
        void onDataRetrieved(String Username,String ID_Number, String Department, String FirstName, String LastName, String Email_Add);
        void onFailure(Exception e);
    }

    protected void RetrieveID(String Username,OnIDRetrieved ID){
        firebase.collection("Users")
                .whereEqualTo("Username",Username)
                .get()
                .addOnCompleteListener(task ->{
                    QueryDocumentSnapshot document = (QueryDocumentSnapshot) task.getResult().getDocuments().get(0);
                    String documentID = document.getId();
                    ID.onSuccess(documentID);
                }).addOnFailureListener(e -> {
                    ID.onFailure(e);
                    Log.e("Firestore","Document not found");
                });
    }
    protected interface OnIDRetrieved{
        void onSuccess(String ID);
        void onFailure(Exception e);
    }

    protected void UpdateData(String DocumentId, String FieldName, String NewData) {
        // Hash the password if updating the Password field
        if (FieldName.equals("Password")) {
            NewData = hashPassword(NewData);
        }

        // Update the document directly using the Document ID
        String finalNewData = NewData; // Required for use in lambda expressions
        firebase.collection("Users")
                .document(DocumentId)
                .update(FieldName, finalNewData)
                .addOnSuccessListener(aVoid -> {
                    MakeToast(FieldName + " Successfully Updated");
                    Log.i("Firestore", FieldName + " updated successfully for Document ID: " + DocumentId);
                })
                .addOnFailureListener(e -> {
                    MakeToast("Update Failed: " + e.getMessage());
                    Log.e("Firestore", "Failed to update " + FieldName + " for Document ID: " + DocumentId + ". Error: " + e.getMessage());
                });
    }


    protected static String hashPassword(String Password){
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = messageDigest.digest(Password.getBytes());
            StringBuilder stringBuilder = new StringBuilder();
            for (byte b : hashBytes){
                stringBuilder.append(String.format("%02x",b));
            }
            return stringBuilder.toString();
        }catch (NoSuchAlgorithmException e){
            throw new RuntimeException("Error hashing password",e);
        }
    }

    protected void MakeToast(String message){
        Toast.makeText(context.getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }
    protected boolean EmptyFields(EditText editText){
        String editTEXT = editText.getText().toString().trim();
        return editTEXT.isEmpty();
    }
    protected void ToActivity(Class<?> activity){
        Intent intent = new Intent(context, activity);
        context.startActivity(intent);
    }
    protected String OTP_Generate(){
//        return String.format("%06d", new java.util.Random().nextInt(1000000));
        return String.format(Locale.ENGLISH,"%06d",new java.util.Random().nextInt(1000000));
    }

    protected void ShowPass(EditText editText,boolean checked){
        if(!checked){
            editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            editText.setSelection(editText.getText().length());
        }else{
            editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            editText.setSelection(editText.getText().length());
        }
    }

}
