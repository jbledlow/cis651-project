package com.example.mainproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.List;

public class IngredientsActivity extends BaseActivity {
    private boolean twoPane;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setContentView(R.layout.activity_ingredients);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_ingredients,contentFrame,false);
        contentFrame.addView(view);

        if (savedInstanceState==null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.ingredient_main_content, new IngredientListFragment()).commit();
        }
        twoPane = false;


    }
    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.ingredient_list_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_new_ingredient:
                Intent intent = new Intent(this,AddIngredientActivity.class);
                startActivity(intent);
                return true;
            default:
                return true;
        }
    }

     */
}
