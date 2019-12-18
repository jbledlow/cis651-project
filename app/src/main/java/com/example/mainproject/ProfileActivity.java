package com.example.mainproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.UUID;
import java.util.zip.Inflater;

public class ProfileActivity extends BaseActivity {
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 111;
    private Button update_button;
    private Button update_pic_button;
    private ImageView ppv;
    private EditText uv;
    private EditText ev;
    private EditText lv;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private DatabaseReference userProfileRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mRootReference = FirebaseDatabase.getInstance().getReference();
        userProfileRef = mRootReference.child("user_profiles/"+user.getUid());
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_profile, null, false);
        contentFrame.addView(view);
        ppv = view.findViewById(R.id.profile_pic);
        uv = view.findViewById(R.id.profile_user_name);
        ev = view.findViewById(R.id.profile_email);
        lv = view.findViewById(R.id.profile_location);
        update_pic_button = view.findViewById(R.id.update_profile_pic);

        if (user.getPhotoUrl()!=null) {
            new WorkerDownloadImage(this,ppv).execute(user.getPhotoUrl().toString());
        } else {
            ppv.setImageResource(R.drawable.default_profile_pic);
        }
        try {
            uv.setText(user.getDisplayName().toString());
        } catch (NullPointerException e) {
            uv.setText("Error");
        }
        ev.setText(user.getEmail().toString());
        mUserProfileReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                lv.setText(dataSnapshot.child("location").getValue().toString());
                try {
                    new WorkerDownloadImage(ProfileActivity.this,ppv).execute(user.getPhotoUrl().toString());
                } catch (NullPointerException e){}
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) {
                Toast.makeText(ProfileActivity.this, "Child has changed", Toast.LENGTH_SHORT).show();
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

        update_button = view.findViewById(R.id.updateBtn);
        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                        .setDisplayName(uv.getText().toString())
                        .build();
                user.updateProfile(profileUpdate)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    //Toast.makeText(ProfileActivity.this,"Successfully updated Username",Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
                user.updateEmail(ev.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ProfileActivity.this,"Success", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ProfileActivity.this,e.toString(),Toast.LENGTH_SHORT);
                            }
                        });
                userProfileRef.child("email").setValue(ev.getText().toString());
                userProfileRef.child("location").setValue(lv.getText().toString());
                userProfileRef.child("username").setValue(uv.getText().toString());
            }
        });

        update_pic_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(ProfileActivity.this,
                        android.Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {


                    ActivityCompat.requestPermissions(ProfileActivity.this,
                            new String[]{android.Manifest.permission.CAMERA},
                            MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                }
                else
                    uploadImage();

            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted
                    uploadImage();

                } else {

                    Toast.makeText(this,"Need Permission to use Camera",Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap obm = (Bitmap) extras.get("data");
            // Rotate the image
            Bitmap bm = rotateImage(obm,90);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] pixel_data = baos.toByteArray();
            String path = "images/"+ UUID.randomUUID()+".jpg";
            final StorageReference imageRef = storage.getReference(path);
            UploadTask uploadTask = imageRef.putBytes(pixel_data);

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    return imageRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                .setPhotoUri(downloadUri)
                                .build();
                        user.updateProfile(profileUpdate)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            new WorkerDownloadImage(ProfileActivity.this,ppv).execute(user.getPhotoUrl().toString());
                                            new WorkerDownloadImage(ProfileActivity.this,headerPic).execute(user.getPhotoUrl().toString());
                                            userProfileRef.child("pp_link").setValue(user.getPhotoUrl().toString());
                                        }
                                    }
                                });
                    }
                }
            });
        }
    }

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private void uploadImage(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out:
                mAuth.signOut();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                finishAffinity();
                startActivity(intent);
                finish();
                return true;
            default:
                return false;
        }
    }
}
