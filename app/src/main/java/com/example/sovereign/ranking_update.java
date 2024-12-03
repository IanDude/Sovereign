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

public class ranking_update extends AppCompatActivity {

    EditText editDepartment, editPoints;
    Button btnAdd, btnEdit, btnDelete;
    TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ranking_update);

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

        // Add button click listener to add a new row
        btnAdd.setOnClickListener(v -> addRow());

        // Edit button click listener
        btnEdit.setOnClickListener(v -> editRow());

        // Delete button click listener
        btnDelete.setOnClickListener(v -> deleteRow());
    }

    private void addRow() {
        String department = editDepartment.getText().toString().trim();
        String points = editPoints.getText().toString().trim();

        if (!department.isEmpty() && !points.isEmpty()) {
            // Check for duplicate departments
            for (int i = 0; i < tableLayout.getChildCount(); i++) {
                View row = tableLayout.getChildAt(i);
                if (row instanceof TableRow) {
                    TextView deptTextView = (TextView) ((TableRow) row).getChildAt(0);
                    if (deptTextView.getText().toString().equalsIgnoreCase(department)) {
                        Toast.makeText(this, "Department already exists", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }

            // Create new row
            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT
            ));
            row.setWeightSum(2);

            // Create department TextView
            TextView deptTextView = new TextView(this);
            TableRow.LayoutParams deptParams = new TableRow.LayoutParams(
                    0,
                    TableRow.LayoutParams.WRAP_CONTENT,
                    1f  // Weight of 1 for equal distribution
            );
            deptTextView.setLayoutParams(deptParams);
            deptTextView.setText(department);
            deptTextView.setGravity(android.view.Gravity.CENTER);
            deptTextView.setPadding(8, 8, 8, 8);
            deptTextView.setTextColor(getResources().getColor(android.R.color.black));

            // Create points TextView
            TextView pointsTextView = new TextView(this);
            TableRow.LayoutParams pointsParams = new TableRow.LayoutParams(
                    0,
                    TableRow.LayoutParams.WRAP_CONTENT,
                    1f  // Weight of 1 for equal distribution
            );
            pointsTextView.setLayoutParams(pointsParams);
            pointsTextView.setText(points);
            pointsTextView.setGravity(android.view.Gravity.CENTER);
            pointsTextView.setPadding(8, 8, 8, 8);
            pointsTextView.setTextColor(getResources().getColor(android.R.color.black));

            // Add views to row
            row.addView(deptTextView);
            row.addView(pointsTextView);

            // Add row to table (skip header row)
            tableLayout.addView(row);

            // Clear input fields
            editDepartment.setText("");
            editPoints.setText("");
        } else {
            Toast.makeText(this, "Please fill both department and points", Toast.LENGTH_SHORT).show();
        }
    }

    // Edit the points of a department if it exists
    private void editRow() {
        String department = editDepartment.getText().toString().trim();
        String points = editPoints.getText().toString().trim();

        if (!department.isEmpty() && !points.isEmpty()) {
            for (int i = 0; i < tableLayout.getChildCount(); i++) {
                View row = tableLayout.getChildAt(i);
                if (row instanceof TableRow) {
                    TextView deptTextView = (TextView) ((TableRow) row).getChildAt(0);
                    if (deptTextView.getText().toString().equalsIgnoreCase(department)) {
                        TextView pointsTextView = (TextView) ((TableRow) row).getChildAt(1);
                        pointsTextView.setText(points);
                        Toast.makeText(this, "Points updated for " + department, Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }
            Toast.makeText(this, "Department not found", Toast.LENGTH_SHORT).show();
        }
    }

    // Delete a department if it exists
    private void deleteRow() {
        String department = editDepartment.getText().toString().trim();

        if (!department.isEmpty()) {
            for (int i = 0; i < tableLayout.getChildCount(); i++) {
                View row = tableLayout.getChildAt(i);
                if (row instanceof TableRow) {
                    TextView deptTextView = (TextView) ((TableRow) row).getChildAt(0);
                    if (deptTextView.getText().toString().equalsIgnoreCase(department)) {
                        tableLayout.removeViewAt(i);
                        Toast.makeText(this, department + " deleted", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }
            Toast.makeText(this, "Department not found", Toast.LENGTH_SHORT).show();
        }
    }
}
