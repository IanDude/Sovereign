package com.example.sovereign;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sovereign.R;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private final ArrayList<String> itemList;

    public ItemAdapter(ArrayList<String> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String item = itemList.get(position);
        holder.textViewItem.setText(item);
        holder.editTextItem.setText(item);

        // Edit button logic
        holder.buttonEdit.setOnClickListener(v -> {
            if (holder.editTextItem.getVisibility() == View.GONE) {
                // Show EditText and hide TextView
                holder.textViewItem.setVisibility(View.GONE);
                holder.editTextItem.setVisibility(View.VISIBLE);
                holder.buttonEdit.setText("Save"); // Change button to Save
            } else {
                // Save changes and update list
                String updatedItem = holder.editTextItem.getText().toString().trim();
                if (!updatedItem.isEmpty()) {
                    itemList.set(position, updatedItem);
                    notifyItemChanged(position); // Notify RecyclerView of the change
                }
                // Hide EditText and show TextView
                holder.textViewItem.setVisibility(View.VISIBLE);
                holder.editTextItem.setVisibility(View.GONE);
                holder.buttonEdit.setText("Edit"); // Change button back to Edit
            }
        });

        // Delete button logic
        holder.buttonDelete.setOnClickListener(v -> {
            itemList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, itemList.size());
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewItem;  // For displaying the item
        EditText editTextItem;  // For editing the item
        Button buttonEdit, buttonDelete; // For Edit and Delete actions

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Ensure these IDs match with the XML file
            textViewItem = itemView.findViewById(R.id.textViewItem);
            editTextItem = itemView.findViewById(R.id.editTextItem);
            buttonEdit = itemView.findViewById(R.id.buttonEdit);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }
    }

}
