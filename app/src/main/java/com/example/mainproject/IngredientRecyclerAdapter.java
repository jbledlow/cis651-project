package com.example.mainproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.PointerIcon;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class IngredientRecyclerAdapter extends RecyclerView.Adapter<IngredientRecyclerAdapter.ViewHolder> {
    Context context;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    DatabaseReference mRootReference = FirebaseDatabase.getInstance().getReference();
    DatabaseReference userIngredientsRef = mRootReference.child("user-ingredients/"+user.getUid().toString());
    public List<Ingredient> ingredientList = new ArrayList<Ingredient>();

    public IngredientRecyclerAdapter(Context _context) {
        context = _context;
        userIngredientsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Ingredient ingredient = new Ingredient();
                ingredient.name = dataSnapshot.child("name").getValue().toString();
                ingredient.type = Integer.parseInt(dataSnapshot.child("type").getValue().toString());
                ingredient.data = dataSnapshot.child("data").getValue().toString();
                SimpleDateFormat localDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                String time=dataSnapshot.child("date").getValue().toString();
                Long t=Long.parseLong(time);
                ingredient.date = localDateFormat.format(new Date(t));
                ingredientList.add(ingredient);
                IngredientRecyclerAdapter.this.notifyItemInserted(ingredientList.size()-1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @NonNull
    @Override
    public IngredientRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_list_item,parent,false);
        final ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientRecyclerAdapter.ViewHolder holder, int position) {
        holder.ingredientName.setText(ingredientList.get(position).name);
        holder.ingredientType.setText(Integer.toString(ingredientList.get(position).type));
        holder.ingredientData.setText(ingredientList.get(position).data);
        holder.ingredientDate.setText(ingredientList.get(position).date);
        switch (ingredientList.get(position).type) {
            case Ingredient.INGREDIENT_MALT:
                holder.ingredientIcon.setImageResource(R.drawable.malt);
                break;
            case Ingredient.INGREDIENT_HOP:
                holder.ingredientIcon.setImageResource(R.drawable.hops);
                break;
            default:
                holder.ingredientIcon.setImageResource(R.drawable.default_profile_pic);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return ingredientList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ingredientIcon;
        TextView ingredientName;
        TextView ingredientType;
        TextView ingredientData;
        TextView ingredientDate;
        public ViewHolder(View itemView) {
            super(itemView);
            ingredientIcon = itemView.findViewById(R.id.ingredient_type_icon);
            ingredientName = itemView.findViewById(R.id.ingredient_name);
            ingredientType = itemView.findViewById(R.id.ingredient_type);
            ingredientData = itemView.findViewById(R.id.ingredient_data);
            ingredientDate = itemView.findViewById(R.id.ingredient_date);
        }
    }
}
