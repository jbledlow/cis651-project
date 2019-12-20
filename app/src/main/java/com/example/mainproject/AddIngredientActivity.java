package com.example.mainproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;

public class AddIngredientActivity extends BaseActivity {
    private EditText name;
    private TextView type;
    private EditText data;
    private Button submitButton;

    //private DatabaseReference userIngredientsRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getIntent().getExtras();
        final int typeStr = args.getInt("type");
        //userIngredientsRef = mRootReference.child("user_ingredients").child(user.getUid().toString());
        //setContentView(R.layout.activity_add_ingredient);
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.activity_add_ingredient, contentFrame, false);
        contentFrame.addView(view);
        name = view.findViewById(R.id.new_ingredient_name);
        type = view.findViewById(R.id.new_ingredient_type);
        //type.setText(typeStr);
        data = view.findViewById(R.id.new_ingredient_data);
        // Handle Button click event
        submitButton = view.findViewById(R.id.submit_new_ingredient);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString()!=null && data.getText().toString()!=null) {
                    DatabaseReference mUserIngRef = mRootReference.child("user-ingredients").child(user.getUid().toString());
                    String key = mUserIngRef.push().getKey();
                    Map<String, Object> ingredient = new HashMap<>();
                    ingredient.put("name", name.getText().toString());
                    ingredient.put("type", typeStr);
                    ingredient.put("data", data.getText().toString());
                    ingredient.put("date", ServerValue.TIMESTAMP);

                    mUserIngRef.child(key).setValue(ingredient);
                    finish();
                } else {
                    Toast.makeText(AddIngredientActivity.this,"All Fields must be filled out", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
