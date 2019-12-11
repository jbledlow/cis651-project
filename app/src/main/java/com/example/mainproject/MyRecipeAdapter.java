package com.example.mainproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyRecipeAdapter extends RecyclerView.Adapter<MyRecipeAdapter.ViewHolder> {
    private List<String> recipes;
    public MyRecipeAdapter(List<String> list) {recipes=list;}

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.recipe_item);
        }
    }
    @NonNull
    @Override
    public MyRecipeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView.setText(recipes.get(position).toString());
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }
}
