package com.example.mainproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends BaseActivity {
    private RecyclerView mRecyclerView;
    private WallData wd = new WallData();
    private final MyRecyclerAdapter myRecyclerAdapter = new MyRecyclerAdapter(wd.getWallList());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_main, null, false);
        mRecyclerView = view.findViewById(R.id.wall_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.scrollToPosition(0);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(myRecyclerAdapter);
        //myRecyclerAdapter.notifyDataSetChanged();
        contentFrame.addView(view);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_app_bar_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_post:
                Intent intent = new Intent(getApplicationContext(),AppPostActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
