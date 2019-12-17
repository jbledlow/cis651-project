package com.example.mainproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyRecipeAdapter extends RecyclerView.Adapter<MyRecipeAdapter.ViewHolder> {
    private List<Recipe> recipes=new ArrayList<>();
    Context context;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    DatabaseReference mRootReference = FirebaseDatabase.getInstance().getReference();
    DatabaseReference recipesRef = mRootReference.child("recipes/"+user.getUid());
    public MyRecipeAdapter(Context _context) {
        context = _context;
        recipesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot recipeSnapshot: dataSnapshot.getChildren()) {
                    Recipe recipe = new Recipe();
                    recipe.name = recipeSnapshot.child("name").getValue().toString();
                    recipe.type = recipeSnapshot.child("type").getValue().toString();
                    recipe.volume = recipeSnapshot.child("volume").getValue().toString();
                    recipe.mash = recipeSnapshot.child("mash").getValue().toString();
                    recipe.hops = recipeSnapshot.child("hops").getValue().toString();
                    recipes.add(recipe);
                    MyRecipeAdapter.this.notifyItemInserted(recipes.size()-1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public TextView recipeType;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.recipe_item);
            recipeType = itemView.findViewById(R.id.recipe_item_type);
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
        holder.textView.setText(recipes.get(position).name.toString());
        holder.recipeType.setText(recipes.get(position).type.toString());
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }
}
