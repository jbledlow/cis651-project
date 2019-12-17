package com.example.mainproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddMaltDialogFragment extends DialogFragment implements AdapterView.OnItemSelectedListener {
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    DatabaseReference mRootReference = FirebaseDatabase.getInstance().getReference();
    DatabaseReference userIngredientsRef = mRootReference.child("user-ingredients/"+user.getUid().toString());
    public String selectedMalt;
    public String weight;
    AddMaltDialogListener listener;
    // View Items
    private Spinner spinner;
    private EditText malt_weight;


    public interface AddMaltDialogListener {
        public void onDialogPositiveClick(String malt, String weight);
        public void onDialogNegativeClick(String malt, String weight);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (AddMaltDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()+" must implement DialogListener!");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Pick your malt!")
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (malt_weight.getText().toString().length()>0) {
                            weight = malt_weight.getText().toString();
                            listener.onDialogPositiveClick(selectedMalt, weight);
                        } else {
                            Toast.makeText(getContext(),"You must Specify a weight", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(), "Canceled", Toast.LENGTH_SHORT).show();

                    }
                });
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dView = inflater.inflate(R.layout.dialog_add_malt,null);
        spinner = dView.findViewById(R.id.dialog_malt_spinner);
        malt_weight = dView.findViewById(R.id.dialog_malt_weight);
        spinner.setOnItemSelectedListener(this);
        userIngredientsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final List<String> ingredients = new ArrayList<String>();

                for (DataSnapshot ingredientSnapshot: dataSnapshot.getChildren()) {
                    if (Integer.parseInt(ingredientSnapshot.child("type").getValue().toString())==Ingredient.INGREDIENT_MALT) {
                        String name = ingredientSnapshot.child("name").getValue().toString();
                        String data = ingredientSnapshot.child("data").getValue().toString();
                        ingredients.add(name+" ("+data+")");
                    }
                }
                ArrayAdapter<String> ingredientsAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, ingredients);
                ingredientsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(ingredientsAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        builder.setView(dView);
        return builder.create();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedMalt = parent.getItemAtPosition(position).toString();
        //Toast.makeText(getContext(),"You chose "+selectedMalt, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
