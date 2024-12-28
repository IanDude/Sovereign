package com.example.sovereign;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.sovereign.ItemAdapter;
import com.example.sovereign.R;

import java.util.ArrayList;

public class ClanFragment extends Fragment {

    private RecyclerView recyclerView;
    private EditText editTextItem;
    private Button buttonAdd;
    private ItemAdapter itemAdapter;
    private ArrayList<String> itemList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clan, container, false);

        // Initialize views
        recyclerView = view.findViewById(R.id.recyclerView);
        editTextItem = view.findViewById(R.id.editTextItem);
        buttonAdd = view.findViewById(R.id.buttonAdd);

        // Initialize list and adapter
        itemList = new ArrayList<>();
        itemAdapter = new ItemAdapter(itemList);

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(itemAdapter);

        // Add button click listener
        buttonAdd.setOnClickListener(v -> {
            String item = editTextItem.getText().toString().trim();
            if (!item.isEmpty()) {
                itemList.add(item);
                itemAdapter.notifyItemInserted(itemList.size() - 1);
                editTextItem.setText("");
            }
        });

        return view;
    }
}
