package com.example.sovereign;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
   
public class RankingFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_ranking, container, false);
        ImageButton update_ranking = view.findViewById(R.id.updt_dptm);
        update_ranking.setOnClickListener(view1 -> new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent= new Intent(view1.getContext(), ranking_update.class);
            startActivity(intent);
        },1000));

        return view;

    }


}
