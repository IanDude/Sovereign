package com.example.sovereign;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.sovereign.databinding.ActivityMainBinding;
import android.content.Intent;


public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
//    LoginManager Manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
//        Manager = new LoginManager(this);
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        changeFragment(new HomeFragment());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

//        boolean isAdmin = Manager.UserState();
//        String Username = Manager.getUsername();
        binding.bottomNavigationMenu.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.Home) {
                changeFragment(new HomeFragment());
            } else if (id == R.id.Recognition) {
                changeFragment(new RecognitionFragment());
            }
            return true;
        });

        binding.btnadmin.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, UserProfile.class);
            startActivity(intent);
        });

    }

    protected void changeFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.Mainlayout, fragment);
        fragmentTransaction.commit();
    }


}