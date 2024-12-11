package com.example.sovereign;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RankingFragment extends Fragment {

    private FirebaseFirestore db;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ranking, container, false);

        db = FirebaseFirestore.getInstance();

        // Set up the ImageButton click listener
        ImageButton update_ranking = view.findViewById(R.id.updt_dptm);
        update_ranking.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ranking_update.class);
            startActivity(intent);
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        View view = getView();
        if (view != null) {
            fetchAndDisplayRankings(view);
        }
    }

    private void fetchAndDisplayRankings(View view) {
        db.collection("ranking").get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                Map<String, Long> rankings = new HashMap<>();

                for (QueryDocumentSnapshot document : task.getResult()) {
                    String department = document.getString("department");
                    Long points = null;

                    if (department != null) {
                        Object pointsObj = document.get("points");

                        if (pointsObj instanceof Number) {
                            points = ((Number) pointsObj).longValue();
                        } else if (pointsObj instanceof String) {
                            try {
                                points = Long.parseLong((String) pointsObj);
                            } catch (NumberFormatException e) {
                                Log.e("Firestore", "Points value is not a valid number: " + pointsObj);
                            }
                        }

                        if (points != null) {
                            rankings.put(department, points);
                        }
                    }
                }

                List<Map.Entry<String, Long>> sortedRankings = new ArrayList<>(rankings.entrySet());
                sortedRankings.sort((entry1, entry2) -> Long.compare(entry2.getValue(), entry1.getValue()));

                updateUIWithTopRankings(view, sortedRankings);
            } else {
                Log.e("Firestore", "Failed to fetch rankings", task.getException());
            }
        });
    }

    private void updateUIWithTopRankings(View view, List<Map.Entry<String, Long>> sortedRankings) {
        TextView sovereignDepartment = view.findViewById(R.id.sovereign_department);
        TextView sovereignPoints = view.findViewById(R.id.sovereign_points);
        TextView supremeDepartment = view.findViewById(R.id.supreme_department);
        TextView supremePoints = view.findViewById(R.id.supreme_points);
        TextView monarchDepartment = view.findViewById(R.id.monarch_department);
        TextView monarchPoints = view.findViewById(R.id.monarch_points);
        TextView highLordDepartment = view.findViewById(R.id.high_lord_department);
        TextView highLordPoints = view.findViewById(R.id.high_lord_points);
        TextView imperialDepartment = view.findViewById(R.id.imperial_department);
        TextView imperialPoints = view.findViewById(R.id.imperial_points);

        sovereignDepartment.setText("");
        sovereignPoints.setText("");
        supremeDepartment.setText("");
        supremePoints.setText("");
        monarchDepartment.setText("");
        monarchPoints.setText("");
        highLordDepartment.setText("");
        highLordPoints.setText("");
        imperialDepartment.setText("");
        imperialPoints.setText("");

        sovereignDepartment.setVisibility(View.GONE);
        sovereignPoints.setVisibility(View.GONE);
        supremeDepartment.setVisibility(View.GONE);
        supremePoints.setVisibility(View.GONE);
        monarchDepartment.setVisibility(View.GONE);
        monarchPoints.setVisibility(View.GONE);
        highLordDepartment.setVisibility(View.GONE);
        highLordPoints.setVisibility(View.GONE);
        imperialDepartment.setVisibility(View.GONE);
        imperialPoints.setVisibility(View.GONE);

        if (!sortedRankings.isEmpty()) {
            Map.Entry<String, Long> entry = sortedRankings.get(0);
            if (entry != null && entry.getKey() != null) {
                sovereignDepartment.setText(entry.getKey());
                sovereignPoints.setText(String.valueOf(entry.getValue()));
                sovereignDepartment.setVisibility(View.VISIBLE);
                sovereignPoints.setVisibility(View.VISIBLE);
            }
        }
        if (sortedRankings.size() > 1) {
            Map.Entry<String, Long> entry = sortedRankings.get(1);
            if (entry != null && entry.getKey() != null) {
                supremeDepartment.setText(entry.getKey());
                supremePoints.setText(String.valueOf(entry.getValue()));
                supremeDepartment.setVisibility(View.VISIBLE);
                supremePoints.setVisibility(View.VISIBLE);
            }
        }
        if (sortedRankings.size() > 2) {
            Map.Entry<String, Long> entry = sortedRankings.get(2);
            if (entry != null && entry.getKey() != null) {
                monarchDepartment.setText(entry.getKey());
                monarchPoints.setText(String.valueOf(entry.getValue()));
                monarchDepartment.setVisibility(View.VISIBLE);
                monarchPoints.setVisibility(View.VISIBLE);
            }
        }
        if (sortedRankings.size() > 3) {
            Map.Entry<String, Long> entry = sortedRankings.get(3);
            if (entry != null && entry.getKey() != null) {
                highLordDepartment.setText(entry.getKey());
                highLordPoints.setText(String.valueOf(entry.getValue()));
                highLordDepartment.setVisibility(View.VISIBLE);
                highLordPoints.setVisibility(View.VISIBLE);
            }
        }
        if (sortedRankings.size() > 4) {
            Map.Entry<String, Long> entry = sortedRankings.get(4);
            if (entry != null && entry.getKey() != null) {
                imperialDepartment.setText(entry.getKey());
                imperialPoints.setText(String.valueOf(entry.getValue()));
                imperialDepartment.setVisibility(View.VISIBLE);
                imperialPoints.setVisibility(View.VISIBLE);
            }
        }
    }
}
