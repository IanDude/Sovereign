package com.example.sovereign;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;


public class ranking_update extends AppCompatActivity {

    EditText editDepartment, editPoints;
    Button btnAdd, btnEdit, btnDelete;
    TableLayout tableLayout;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ranking_update);

        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize views
        editDepartment = findViewById(R.id.editDepartment);
        editPoints = findViewById(R.id.editPoints);
        btnAdd = findViewById(R.id.btnAdd);
        btnEdit = findViewById(R.id.btnEdit);
        btnDelete = findViewById(R.id.btnDelete);
        tableLayout = findViewById(R.id.tableLayout);

        // Adjust layout for keyboard visibility
        View mainLayout = findViewById(R.id.main);
        ViewCompat.setOnApplyWindowInsetsListener(mainLayout, (v, insets) -> {
            Insets keyboardInsets = insets.getInsets(WindowInsetsCompat.Type.ime());
            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());

            v.setPadding(
                    systemBarsInsets.left,
                    systemBarsInsets.top,
                    systemBarsInsets.right,
                    Math.max(systemBarsInsets.bottom, keyboardInsets.bottom)
            );

            return insets;
        });

        // Load data from Firestore to display in the table
        loadTableData();

        // Add button click listener
        btnAdd.setOnClickListener(v -> addRow());

        // Edit button click listener
        btnEdit.setOnClickListener(v -> editRow());

        // Delete button click listener
        btnDelete.setOnClickListener(v -> deleteRow());
    }

    private void loadTableData() {
        tableLayout.removeViews(1, Math.max(0, tableLayout.getChildCount() - 1));

        db.collection("ranking")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<Map<String, String>> rankingsList = new ArrayList<>();

                    for (QueryDocumentSnapshot document : querySnapshot) {
                        String department = document.getString("department");
                        String points = document.getString("points");

                        Map<String, String> rankingData = new HashMap<>();
                        rankingData.put("department", department);
                        rankingData.put("points", points);

                        rankingsList.add(rankingData);
                    }

                    // Sort the list manually based on points in descending order
                    rankingsList.sort((o1, o2) -> Integer.parseInt(o2.get("points")) - Integer.parseInt(o1.get("points")));

                    for (Map<String, String> ranking : rankingsList) {
                        addRowToTable(ranking.get("department"), ranking.get("points"));
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to load data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }


    private void addRowToTable(String department, String points) {
        TableRow row = new TableRow(this);
        row.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT
        ));
        row.setWeightSum(2);

        TextView deptTextView = new TextView(this);
        deptTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        deptTextView.setText(department);
        deptTextView.setGravity(android.view.Gravity.CENTER);
        deptTextView.setPadding(8, 8, 8, 8);

        TextView pointsTextView = new TextView(this);
        pointsTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        pointsTextView.setText(points);
        pointsTextView.setGravity(android.view.Gravity.CENTER);
        pointsTextView.setPadding(8, 8, 8, 8);

        row.addView(deptTextView);
        row.addView(pointsTextView);
        tableLayout.addView(row);
    }

    private void addRow() {
        String department = editDepartment.getText().toString().trim();
        String points = editPoints.getText().toString().trim();

        if (!department.isEmpty() && !points.isEmpty()) {
            // Check if department already exists
            db.collection("ranking")
                    .whereEqualTo("department", department)
                    .get()
                    .addOnSuccessListener(querySnapshot -> {
                        if (querySnapshot.isEmpty()) {
                            // Add to Firestore
                            Map<String, Object> rankingData = new HashMap<>();
                            rankingData.put("department", department);
                            rankingData.put("points", points);

                            db.collection("ranking")
                                    .add(rankingData)
                                    .addOnSuccessListener(documentReference -> {
                                        Toast.makeText(this, "Department added to database", Toast.LENGTH_SHORT).show();
                                        loadTableData(); // Refresh table data
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(this, "Failed to add to database: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        } else {
                            Toast.makeText(this, "Department already exists in the database", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Error checking department: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(this, "Please fill both department and points", Toast.LENGTH_SHORT).show();
        }
    }

    private void editRow() {
        String department = editDepartment.getText().toString().trim();
        String points = editPoints.getText().toString().trim();

        if (!department.isEmpty() && !points.isEmpty()) {
            db.collection("ranking")
                    .whereEqualTo("department", department)
                    .get()
                    .addOnSuccessListener(querySnapshot -> {
                        if (!querySnapshot.isEmpty()) {
                            querySnapshot.getDocuments().get(0).getReference()
                                    .update("points", points)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(this, "Points updated for " + department, Toast.LENGTH_SHORT).show();
                                        loadTableData(); // Refresh table data
                                    });
                        } else {
                            Toast.makeText(this, "Department not found in database", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "Please fill both department and points", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteRow() {
        String department = editDepartment.getText().toString().trim();

        if (!department.isEmpty()) {
            db.collection("ranking")
                    .whereEqualTo("department", department)
                    .get()
                    .addOnSuccessListener(querySnapshot -> {
                        if (!querySnapshot.isEmpty()) {
                            querySnapshot.getDocuments().get(0).getReference()
                                    .delete()
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(this, department + " deleted from database", Toast.LENGTH_SHORT).show();
                                        loadTableData(); // Refresh table data
                                    });
                        } else {
                            Toast.makeText(this, "Department not found in database", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "Please fill department", Toast.LENGTH_SHORT).show();
        }
    }
}
