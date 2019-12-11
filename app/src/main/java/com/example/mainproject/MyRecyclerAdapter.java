package com.example.mainproject;

import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Map;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder> {
    private List<Map<String,?>> wd;
    public MyRecyclerAdapter(List<Map<String,?>> list) {wd=list;}

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView profile_pic_thumb;
        public TextView username;
        public ImageView post_image;
        public TextView post_content;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profile_pic_thumb = itemView.findViewById(R.id.profile_pic_thumb);
            username = itemView.findViewById(R.id.card_username);
            post_image = itemView.findViewById(R.id.post_pic);
            post_content = itemView.findViewById(R.id.post_text);
        }
    }

    @NonNull
    @Override
    public MyRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_wall, parent,false);
        final ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.profile_pic_thumb.setImageResource(Integer.parseInt(wd.get(position).get("profile_pic").toString()));
        holder.username.setText(wd.get(position).get("username").toString());
        holder.post_image.setImageResource(Integer.parseInt(wd.get(position).get("image").toString()));
        holder.post_content.setText(wd.get(position).get("content").toString());
    }

    @Override
    public int getItemCount() {
        return wd.size();
    }
}
