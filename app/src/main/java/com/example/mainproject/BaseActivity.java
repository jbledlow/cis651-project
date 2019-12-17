package com.example.mainproject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.mainproject.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.ref.WeakReference;

public class BaseActivity extends AppCompatActivity {
    protected FirebaseUser user;
    protected String currentPicUri = "";
    Toolbar toolbar;
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    protected ImageView headerPic;
    protected FrameLayout contentFrame;
    protected FirebaseAuth mAuth;
    protected DatabaseReference mRootReference = FirebaseDatabase.getInstance().getReference();
    protected DatabaseReference mUserProfileReference = mRootReference.child("user_profiles");

    final static class WorkerDownloadImage extends AsyncTask<String, String, Bitmap> {
        private final WeakReference<Context> parentRef;
        private final WeakReference<ImageView> imageViewRef;
        public  WorkerDownloadImage(final Context parent, final ImageView imageview)
        {
            parentRef=new WeakReference<Context>(parent);
            imageViewRef=new WeakReference<ImageView>(imageview);
        }
        @Override
        protected Bitmap doInBackground(String... urls) {
            Bitmap result= HTTP_METHODS.downloadImageUsingHTTPGetRequest(urls[0]);
            return result;
        }
        @Override
        protected void onPostExecute(final Bitmap result){
            final ImageView iv=imageViewRef.get();
            if(iv!=null)
            {
                iv.setImageBitmap(result);

            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        setContentView(R.layout.activity_base_layout);
        contentFrame = findViewById(R.id.content_frame);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navigationView = findViewById(R.id.nav);
        headerPic = navigationView.getHeaderView(0).findViewById(R.id.header_profile_pic);
        if (user!=null) {
            currentPicUri=user.getPhotoUrl().toString();
            new WorkerDownloadImage(this,headerPic).execute(currentPicUri);
            //Toast.makeText(this, "Set Photo", Toast.LENGTH_SHORT).show();
        } else {
            headerPic.setImageResource(R.drawable.default_profile_pic);
        }
        drawerLayout = findViewById(R.id.drawer);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menu_profile:
                        //Toast.makeText(getApplicationContext(), "You picked Profile", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                        //intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent);
                        break;
                    case R.id.menu_settings:
                        intent = new Intent(getApplicationContext(), SettingsActivity.class);
                        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent);
                        break;
                    case R.id.menu_recipes:
                        intent = new Intent(getApplicationContext(), MyRecipesActivity.class);
                        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent);
                        break;
                    case R.id.menu_ingredients:
                        intent = new Intent(getApplicationContext(), IngredientsActivity.class);
                        //intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent);
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.ndopen,R.string.ndclose);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (hasProfilePicChanged()) {
            currentPicUri=user.getPhotoUrl().toString();
            new WorkerDownloadImage(this,headerPic).execute(currentPicUri);
        }
    }

    protected boolean hasProfilePicChanged() {
        return user.getPhotoUrl().toString()!=currentPicUri;
    }
}
