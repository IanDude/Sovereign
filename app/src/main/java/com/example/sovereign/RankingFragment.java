package com.example.sovereign;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class RankingFragment extends Fragment {

    // UI Components
    private EditText inputDepartmentName, inputDepartmentPoints;
    private EditText inputEditDepartmentName, inputEditDepartmentPoints;
    private EditText inputDeleteDepartmentName;
    private Button btnAddDepartment, btnEditDepartmentPoints, btnDeleteDepartment;
    private TextView tvRankings;

    // Department class to store data
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

    // ArrayList to store departments
    private ArrayList<Department> departments = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_kingdom, container, false);

        // Initialize UI components
        inputDepartmentName = view.findViewById(R.id.inputDepartmentName);
        inputDepartmentPoints = view.findViewById(R.id.inputDepartmentPoints);
        inputEditDepartmentName = view.findViewById(R.id.inputEditDepartmentName);
        inputEditDepartmentPoints = view.findViewById(R.id.inputEditDepartmentPoints);
        inputDeleteDepartmentName = view.findViewById(R.id.inputDeleteDepartmentName);
        btnAddDepartment = view.findViewById(R.id.btnAddDepartment);
        btnEditDepartmentPoints = view.findViewById(R.id.btnEditDepartmentPoints);
        btnDeleteDepartment = view.findViewById(R.id.btnDeleteDepartment);
        tvRankings = view.findViewById(R.id.tvRankings);

        // Set button listeners
        btnAddDepartment.setOnClickListener(v -> addDepartment());
        btnEditDepartmentPoints.setOnClickListener(v -> editDepartmentPoints());
        btnDeleteDepartment.setOnClickListener(v -> deleteDepartment());

        return view;
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
