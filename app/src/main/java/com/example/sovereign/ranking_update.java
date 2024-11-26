package com.example.sovereign;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.Serializable;
import java.util.ArrayList;

public class ranking_update extends AppCompatActivity {
    private EditText inputDepartmentName, inputDepartmentPoints;
    private EditText inputEditDepartmentName, inputEditDepartmentPoints;
    private EditText inputDeleteDepartmentName;
    private Button btnAddDepartment, btnEditDepartmentPoints, btnDeleteDepartment;
    private TextView tvRankings;

    public static class Department implements Serializable {
        String name;
        int points;

        Department(String name, int points) {
            this.name = name;
            this.points = points;
        }

        @Override
        public String toString() {
            return name + " - " + points;
        }
    }
    private ArrayList<ranking_update.Department> departments = new ArrayList<>();

    @Nullable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking_update);

        // Edge-to-Edge UI setup
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize UI components
        inputDepartmentName = findViewById(R.id.inputDepartmentName);
        inputDepartmentPoints = findViewById(R.id.inputDepartmentPoints);
        inputEditDepartmentName = findViewById(R.id.inputEditDepartmentName);
        inputEditDepartmentPoints = findViewById(R.id.inputEditDepartmentPoints);
        inputDeleteDepartmentName = findViewById(R.id.inputDeleteDepartmentName);
        btnAddDepartment = findViewById(R.id.btnAddDepartment);
        btnEditDepartmentPoints = findViewById(R.id.btnEditDepartmentPoints);
        btnDeleteDepartment = findViewById(R.id.btnDeleteDepartment);
        tvRankings = findViewById(R.id.tvRankings);

        // Set button listeners
//        btnAddDepartment.setOnClickListener(v -> addDepartment());
//        btnEditDepartmentPoints.setOnClickListener(v -> editDepartmentPoints());
//        btnDeleteDepartment.setOnClickListener(v -> deleteDepartment());
    }

}