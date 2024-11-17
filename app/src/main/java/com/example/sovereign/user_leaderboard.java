package com.example.sovereign;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class user_leaderboard extends AppCompatActivity {

    // Data structure to hold college and points
    class College {
        String name;
        int points;

        College(String name, int points) {
            this.name = name;
            this.points = points;
        }
    }

    // List to hold college rankings
    private ArrayList<College> colleges;

    // TextViews for displaying rankings
    private TextView sovereignCollege, sovereignPoints;
    private TextView supremeCollege, supremePoints;
    private TextView monarchCollege, monarchPoints;
    private TextView highLordCollege, highLordPoints;
    private TextView imperialCollege, imperialPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_leaderboard); // Replace with your XML file name

        // Initialize TextViews (these IDs must match your XML layout)
        sovereignCollege = findViewById(R.id.sovereign_college);
        sovereignPoints = findViewById(R.id.sovereign_points);

        supremeCollege = findViewById(R.id.supreme_college);
        supremePoints = findViewById(R.id.supreme_points);

        monarchCollege = findViewById(R.id.monarch_college);
        monarchPoints = findViewById(R.id.monarch_points);

        highLordCollege = findViewById(R.id.high_lord_college);
        highLordPoints = findViewById(R.id.high_lord_points);

        imperialCollege = findViewById(R.id.imperial_college);
        imperialPoints = findViewById(R.id.imperial_points);

        // Initialize colleges and their points
        initializeColleges();

        // Sort colleges based on points
        rankColleges();

        // Update the UI
        updateRankings();
    }

    // Initialize the colleges and their initial points
    private void initializeColleges() {
        colleges = new ArrayList<>();
        colleges.add(new College("CICT", 12900)); // Sovereign
        colleges.add(new College("CJJ", 14000)); // Supreme
        colleges.add(new College("CBM", 13000)); // Monarch
        colleges.add(new College("CAS", 12000)); // High Lord
        colleges.add(new College("ABC", 11000)); // Imperial
    }

    // Sort the colleges by points in descending order
    private void rankColleges() {
        Collections.sort(colleges, new Comparator<College>() {
            @Override
            public int compare(College c1, College c2) {
                return Integer.compare(c2.points, c1.points); // Descending order
            }
        });
    }

    // Update the UI based on the sorted rankings
    private void updateRankings() {
        // Assign sorted colleges to TextViews
        sovereignCollege.setText(colleges.get(0).name);
        sovereignPoints.setText(String.valueOf(colleges.get(0).points));

        supremeCollege.setText(colleges.get(1).name);
        supremePoints.setText(String.valueOf(colleges.get(1).points));

        monarchCollege.setText(colleges.get(2).name);
        monarchPoints.setText(String.valueOf(colleges.get(2).points));

        highLordCollege.setText(colleges.get(3).name);
        highLordPoints.setText(String.valueOf(colleges.get(3).points));

        imperialCollege.setText(colleges.get(4).name);
        imperialPoints.setText(String.valueOf(colleges.get(4).points));
    }
}
