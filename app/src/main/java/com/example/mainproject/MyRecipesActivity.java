package com.example.mainproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MyRecipesActivity extends BaseActivity implements RecipeListFragment.onListItemSelectedListener {
    private boolean twoPane;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.activity_my_recipes, null, false);
        contentFrame.addView(v);
        if (savedInstanceState==null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.recipe_main_content, new RecipeListFragment()).commit();
        }
        twoPane = false;
        if (findViewById(R.id.recipe_detail_content)!=null) {
            twoPane = true;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.recipe_list_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_new_recipe:
                Intent intent = new Intent(getApplicationContext(),RecipeCreatorActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemSelected(String name, String volume, String type, String mash, String hops) {
        Bundle args = new Bundle();
        args.putString("name",name);
        args.putString("volume", volume);
        args.putString("type", type);
        args.putString("mash", mash);
        args.putString("hops", hops);
        RecipeDetailFragment detailFragment = new RecipeDetailFragment();
        detailFragment.setArguments(args);
        if (twoPane) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.recipe_detail_content, detailFragment)
                    .addToBackStack(null)
                    .commit();
        } else {
            //Toast.makeText(this,name.toString(), Toast.LENGTH_SHORT).show();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.recipe_main_content, detailFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }
}
