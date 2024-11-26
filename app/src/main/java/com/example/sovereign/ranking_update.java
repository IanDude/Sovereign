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
import java.util.Collections;

public class ranking_update extends AppCompatActivity {
    EditText inputDepartmentName, inputDepartmentPoints, inputEditDepartmentName,
            inputEditDepartmentPoints, inputDeleteDepartmentName;
    Button btnAddDepartment, btnEditDepartmentPoints, btnDeleteDepartment;
    TextView tvRankings;

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
        btnAddDepartment.setOnClickListener(v -> addDepartment());
        btnEditDepartmentPoints.setOnClickListener(v -> editDepartmentPoints());
        btnDeleteDepartment.setOnClickListener(v -> deleteDepartment());
    }
    private void addDepartment() {
        String name = inputDepartmentName.getText().toString();
        String pointsText = inputDepartmentPoints.getText().toString();

        if (name.isEmpty() || pointsText.isEmpty()) {
            tvRankings.setText("Please enter department name and points.");
            return;
        }

        int points = Integer.parseInt(pointsText);
        departments.add(new Department(name, points));
        updateRankings();
    }

    private void editDepartmentPoints() {
        String name = inputEditDepartmentName.getText().toString();
        String pointsText = inputEditDepartmentPoints.getText().toString();

        if (name.isEmpty() || pointsText.isEmpty()) {
            tvRankings.setText("Please enter department name and new points.");
            return;
        }

        int points = Integer.parseInt(pointsText);

        for (Department department : departments) {
            if (department.name.equalsIgnoreCase(name)) {
                department.points = points;
                updateRankings();
                return;
            }
        }

        tvRankings.setText("Department not found.");
    }

    private void deleteDepartment() {
        String name = inputDeleteDepartmentName.getText().toString();

        if (name.isEmpty()) {
            tvRankings.setText("Please enter department name to delete.");
            return;
        }

        for (int i = 0; i < departments.size(); i++) {
            if (departments.get(i).name.equalsIgnoreCase(name)) {
                departments.remove(i);
                updateRankings();
                return;
            }
        }

        tvRankings.setText("Department not found.");
    }

    private void updateRankings() {
        // Sort departments by points (descending)
        Collections.sort(departments, (d1, d2) -> d2.points - d1.points);

        // Update rankings display
        StringBuilder rankings = new StringBuilder();
        for (int i = 0; i < departments.size(); i++) {
            rankings.append((i + 1)).append(". ").append(departments.get(i).toString()).append("\n");
        }
        tvRankings.setText(rankings.toString());
    }

}