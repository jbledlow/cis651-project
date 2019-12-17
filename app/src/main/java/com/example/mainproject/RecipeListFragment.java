package com.example.mainproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecipeListFragment extends Fragment {
    private List<String> recipes = new ArrayList();

    //add some dummy data

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.recipe_list_fragment,container,false);
        MyRecipeAdapter myRecipeAdapter = new MyRecipeAdapter(getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.scrollToPosition(0);
        RecyclerView mRecyclerView = v.findViewById(R.id.recipe_list_fragment);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(myRecipeAdapter);
        return v;
    }
}
