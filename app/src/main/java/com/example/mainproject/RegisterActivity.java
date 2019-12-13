package com.example.mainproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterActivity extends BaseActivity {
    private EditText email;
    private EditText password;
    private EditText username;
    private Button button;
    //private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_register,contentFrame,false);
        contentFrame.addView(view);
        // Initialize view elements
        email = view.findViewById(R.id.register_email);
        password = view.findViewById(R.id.register_password);
        username = view.findViewById(R.id.register_username);
        button = view.findViewById(R.id.register_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser(email, password, username);
            }
        });
    }

    private void registerUser(EditText email, EditText password, EditText username) {
        if(email.getText().toString().matches("") || password.getText().toString().matches("") ||
        username.getText().toString().matches(""))
        {
            Toast.makeText(this, "Enter both Email and Password",
                    Toast.LENGTH_SHORT).show();
        }
        else
            createAccount(email.getText().toString(),password.getText().toString());
    }

    private void createAccount(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(this,
                new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        sendVerification(user);
                    }
                }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterActivity.this, "Failure:"+e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void sendVerification(final FirebaseUser user) {
        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "Email Sent!", Toast.LENGTH_LONG).show();
                    user.updateProfile(new UserProfileChangeRequest.Builder()
                        .setDisplayName(username.getText().toString()).build());
                    finish();
                }
            }
        });
    }


}
