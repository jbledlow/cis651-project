package com.example.mainproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.UUID;

public class AddPostActivity extends BaseActivity {
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 111;
    private Button addPicture;
    private ImageView postPicture;
    private EditText postText;
    private Button submit;
    private FirebaseStorage storage=FirebaseStorage.getInstance();
    private DatabaseReference postRef = mRootReference.child("posts");
    private String imageLink= "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_add_post, null, false);
        contentFrame.addView(view);
        addPicture = findViewById(R.id.add_picture_btn);
        postPicture = findViewById(R.id.new_post_image);
        postText = findViewById(R.id.post_text);
        submit = findViewById(R.id.submit_post);
        // Add button event Listeners
        addPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendPhotoMessage(v);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitPost();
            }
        });
    }

    private void submitPost() {

        Post post = new Post();
        post.id = user.getUid().toString();
        post.un = user.getDisplayName();
        post.text = postText.getText().toString();
        post.link = imageLink;
        post.pp_link = user.getPhotoUrl().toString();
        post.likes = 0;
        post.like_list = "";
        post.time = ServerValue.TIMESTAMP;
        DatabaseReference newPostRef = postRef.push();
        newPostRef.setValue(post);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_post_appbar_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cancel_post:
                finish();
                break;
        }
        return true;
    }

    public void SendPhotoMessage(View view){
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {


            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        }
        else
            uploadImage();
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode ==REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Toast.makeText(this,"Image Captured",Toast.LENGTH_SHORT).show();
            Bundle extras = data.getExtras();
            Bitmap bm = (Bitmap) extras.get("data");
            bm = rotateImage(bm,90);
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
            byte[] pixel_data=baos.toByteArray();
            String path="images/"+ UUID.randomUUID()+".jpg";
            final StorageReference imageRef=storage.getReference(path);
            UploadTask uploadTask=imageRef.putBytes(pixel_data);


            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return imageRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        imageLink = downloadUri.toString();
                        postPicture.getLayoutParams().height = 600;
                        new WorkerDownloadImage(AddPostActivity.this, postPicture).execute(downloadUri.toString());
                    } else {
                        // Handle failures
                        Toast.makeText(AddPostActivity.this, "Upload failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
