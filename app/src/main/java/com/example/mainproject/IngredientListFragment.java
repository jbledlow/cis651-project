package com.example.mainproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class IngredientListFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private IngredientRecyclerAdapter mAdapter;
    private FloatingActionButton fab;
    private FloatingActionButton fab1;
    private FloatingActionButton fab2;
    //private FloatingActionButton fab3;
    private boolean isFABOpen = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.ingredient_list_fragment,container, false);
        mRecyclerView = view.findViewById(R.id.ingredients_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new IngredientRecyclerAdapter(getContext());
        mRecyclerView.setAdapter(mAdapter);
        fab = view.findViewById(R.id.fab);
        fab1 = view.findViewById(R.id.fab1);
        fab2 = view.findViewById(R.id.fab2);
        //fab3 = view.findViewById(R.id.fab3);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isFABOpen) {
                    showFABMenu();
                } else {
                    closeFABMenu();
                }
            }
        });
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putInt("type", 1);
                Intent intent = new Intent(getContext(),AddIngredientActivity.class);
                intent.putExtras(args);
                startActivity(intent);
                closeFABMenu();
            }
        });
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putInt("type", 2);
                Intent intent = new Intent(getContext(),AddIngredientActivity.class);
                intent.putExtras(args);
                startActivity(intent);
                closeFABMenu();
            }
        });
        return view;
    }
    private void showFABMenu() {
        isFABOpen = true;
        fab1.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        fab2.animate().translationY(-getResources().getDimension(R.dimen.standard_105));
        //fab3.animate().translationY(-getResources().getDimension(R.dimen.standard_155));

    }

    private void closeFABMenu() {
        isFABOpen = false;
        fab1.animate().translationY(0);
        fab2.animate().translationY(0);
        //fab3.animate().translationY(0);
    }
}
