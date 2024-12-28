package com.example.sovereign;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private List<Clan_Post_Model> posts;

    public PostAdapter(List<Clan_Post_Model> posts) {
        this.posts = posts;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item_post layout for each item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Clan_Post_Model post = posts.get(position);
        holder.titleTextView.setText(post.getTitle());
        holder.descriptionTextView.setText(post.getDescription());
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void addPost(Clan_Post_Model post) {
        posts.add(post);
        notifyItemInserted(posts.size() - 1);  // Notify adapter that new item was added
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, descriptionTextView;

        public PostViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.postTitle);
            descriptionTextView = itemView.findViewById(R.id.postDescription);
        }
    }
}
