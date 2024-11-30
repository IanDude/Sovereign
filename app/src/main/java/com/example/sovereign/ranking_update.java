package com.example.sovereign;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ranking_update extends AppCompatActivity {

    EditText editDepartment, editPoints;
    Button btnAdd, btnEdit, btnDelete;
    TableLayout tableLayout;
    int selectedRowIndex = -1;

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

        // Set onApplyWindowInsetsListener for layout padding adjustment
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Add button click listener to add a new row
        btnAdd.setOnClickListener(v -> addRow());

        // Edit button click listener to update the selected row
        btnEdit.setOnClickListener(v -> editRow());

        // Delete button click listener to remove the selected row
        btnDelete.setOnClickListener(v -> deleteRow());
    }

    // Add a new row to the TableLayout
    private void addRow() {
        String department = editDepartment.getText().toString().trim();
        String points = editPoints.getText().toString().trim();

        if (!department.isEmpty() && !points.isEmpty()) {
            TableRow row = new TableRow(this);
            TextView deptTextView = new TextView(this);
            TextView pointsTextView = new TextView(this);

            deptTextView.setText(department);
            pointsTextView.setText(points);

            deptTextView.setPadding(8, 8, 8, 8);
            pointsTextView.setPadding(8, 8, 8, 8);

            row.addView(deptTextView);
            row.addView(pointsTextView);

            // Add click listener to allow row selection for editing or deleting
            row.setOnClickListener(v -> selectRow(row, department, points));

            tableLayout.addView(row);

            // Clear input fields after adding
            editDepartment.setText("");
            editPoints.setText("");
        }
    }

    // Edit the selected row
    private void editRow() {
        if (selectedRowIndex != -1) {
            String department = editDepartment.getText().toString().trim();
            String points = editPoints.getText().toString().trim();

            if (!department.isEmpty() && !points.isEmpty()) {
                TableRow selectedRow = (TableRow) tableLayout.getChildAt(selectedRowIndex);
                TextView deptTextView = (TextView) selectedRow.getChildAt(0);
                TextView pointsTextView = (TextView) selectedRow.getChildAt(1);

                deptTextView.setText(department);
                pointsTextView.setText(points);

                // Clear input fields after editing
                editDepartment.setText("");
                editPoints.setText("");

                selectedRowIndex = -1; // Reset selection after edit
            }
        }
    }

    // Delete the selected row
    private void deleteRow() {
        if (selectedRowIndex != -1) {
            tableLayout.removeViewAt(selectedRowIndex);
            selectedRowIndex = -1; // Reset selection after deletion
        }
    }

    // Select a row for editing or deletion
    private void selectRow(TableRow row, String department, String points) {
        // Highlight the selected row (optional)
        row.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));

        // Set input fields with the selected row data for editing
        editDepartment.setText(department);
        editPoints.setText(points);

        // Store the index of the selected row for editing or deletion
        selectedRowIndex = tableLayout.indexOfChild(row);
    }
}