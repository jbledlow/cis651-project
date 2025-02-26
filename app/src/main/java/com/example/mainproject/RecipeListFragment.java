package com.example.mainproject;

import android.content.Context;
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

    public interface onListItemSelectedListener {
        public void onListItemSelected(String name, String volume, String type, String mash, String hops);
    }

    onListItemSelectedListener clickListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.recipe_list_fragment,container,false);
        MyRecipeAdapter myRecipeAdapter = new MyRecipeAdapter(getContext(), clickListener);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.scrollToPosition(0);
        RecyclerView mRecyclerView = v.findViewById(R.id.recipe_list_fragment);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(myRecipeAdapter);

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            clickListener = (onListItemSelectedListener)context;
        }
        catch (ClassCastException ex) {
            throw new ClassCastException(context.toString()+"must implement EventTrack");
        }
    }
}
