package com.example.mainproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewProfileActivity extends BaseActivity {
    ImageView ppic;
    TextView un;
    TextView em;
    TextView loc;
    DatabaseReference userProfileRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_view_profile);
        Bundle b = getIntent().getExtras();
        String userID = b.get("userID").toString();
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_view_profile, contentFrame, false);
        contentFrame.addView(view);
        ppic = view.findViewById(R.id.view_profile_pic);
        un = view.findViewById(R.id.view_user_name);
        em = view.findViewById(R.id.view_user_email);
        loc = view.findViewById(R.id.view_user_location);
        mRootReference = FirebaseDatabase.getInstance().getReference();
        userProfileRef = mRootReference.child("user_profiles/"+userID);
        userProfileRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                un.setText(dataSnapshot.child("username").getValue().toString());
                em.setText(dataSnapshot.child("email").getValue().toString());
                loc.setText(dataSnapshot.child("location").getValue().toString());
                new WorkerDownloadImage(ViewProfileActivity.this,ppic)
                        .execute(dataSnapshot.child("pp_link").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
