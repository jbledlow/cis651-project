package com.example.mainproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class RecipeCreatorActivity extends BaseActivity
        implements AddMaltDialogFragment.AddMaltDialogListener, AddHopDialogFragment.AddHopDialogListener {
    LinearLayout mrv;
    LinearLayout hrv;
    Button add_mash;
    Button add_hop;
    EditText nameView;
    EditText volumeView;
    EditText typeView;
    private List<String> maltList = new ArrayList<>();
    private List<String> hopList = new ArrayList<>();
    DatabaseReference recipesRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        recipesRef = mRootReference.child("recipes/"+user.getUid());

        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_recipe_creator);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_recipe_creator, null, false);
        contentFrame.addView(view);
        mrv = view.findViewById(R.id.mash_recycler);
        hrv = view.findViewById(R.id.hop_recycler);
        add_hop = view.findViewById(R.id.add_hop);
        add_mash = view.findViewById(R.id.add_mash);
        nameView = view.findViewById(R.id.recipe_name);
        volumeView = view.findViewById(R.id.recipe_volume);
        typeView = view.findViewById(R.id.recipe_type);

        add_mash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new AddMaltDialogFragment();
                newFragment.show(getSupportFragmentManager(),"malt");
            }
        });

        add_hop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new AddHopDialogFragment();
                newFragment.show(getSupportFragmentManager(),"hop");
            }
        });
    }

    @Override
    public void onDialogPositiveClick(String selectedMalt, String weight) {
        LayoutInflater inflater = getLayoutInflater();
        View newView = inflater.inflate(R.layout.layout_recipe_malt_item,mrv,false);
        TextView itemName = newView.findViewById(R.id.malt_name);
        itemName.setText(selectedMalt);
        TextView itemWeight = newView.findViewById(R.id.malt_weight);
        itemWeight.setText(weight);
        mrv.addView(newView);
        RecipeMaltItem malt = new RecipeMaltItem();
        malt.name=selectedMalt;
        malt.weight=weight;
        maltList.add(malt.toString());
    }

    @Override
    public void onDialogNegativeClick(String selectedMalt, String weight) {

    }

    @Override
    public void onDialogPositiveClick(String hop, String weight, String time) {
        LayoutInflater inflater = getLayoutInflater();
        View newView = inflater.inflate(R.layout.layout_recipe_hop_item,hrv,false);
        TextView itemName = newView.findViewById(R.id.hop_name);
        itemName.setText(hop);
        TextView itemWeight = newView.findViewById(R.id.hop_weight);
        itemWeight.setText(weight);
        TextView itemBoilTime = newView.findViewById(R.id.boil_time);
        itemBoilTime.setText(time);
        hrv.addView(newView);
        RecipeHopItem newHop = new RecipeHopItem();
        newHop.name = hop;
        newHop.weight = weight;
        newHop.boilTime = time;
        hopList.add(newHop.toString());
    }

    @Override
    public void onDialogNegativeClick(String hop, String weight, String time) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.create_recipe_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_recipe:
                Recipe recipe = new Recipe();
                recipe.name= nameView.getText().toString();
                recipe.volume = nameView.getText().toString();
                recipe.type = typeView.getText().toString();
                recipe.hops = hopList.toString();
                recipe.mash = maltList.toString();
                DatabaseReference newRecipe = recipesRef.push();
                newRecipe.setValue(recipe);
            case R.id.cancel_recipe:
                return false;
            default:
                return false;
        }
    }
}
