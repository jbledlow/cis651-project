package com.example.mainproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Layout;
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

public class AddHopDialogFragment extends DialogFragment implements AdapterView.OnItemSelectedListener {
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    DatabaseReference mRootReference = FirebaseDatabase.getInstance().getReference();
    DatabaseReference userIngredientsRef = mRootReference.child("user-ingredients/"+user.getUid().toString());
    public String selectedHop;
    public String weight;
    public String boilTime;
    private Spinner spinner;
    private EditText hopWeightET;
    private EditText hopBoilTimeET;
    AddHopDialogListener listener;

    public interface AddHopDialogListener {
        public void onDialogPositiveClick(String hop, String weight, String time);
        public void onDialogNegativeClick(String hop, String weight, String time);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_hop,null);
        spinner = view.findViewById(R.id.dialog_hop_spinner);
        spinner.setOnItemSelectedListener(this);
        hopWeightET = view.findViewById(R.id.dialog_hop_weight);
        hopBoilTimeET = view.findViewById(R.id.dialog_hop_boil_time);
        userIngredientsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final List<String> ingredients = new ArrayList<>();

                for (DataSnapshot ingredientSnapshot: dataSnapshot.getChildren()) {
                    if (Integer.parseInt(ingredientSnapshot.child("type").getValue().toString())==Ingredient.INGREDIENT_HOP) {
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

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Pick your malt!")
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (hopWeightET.getText().toString().length()>0 && hopBoilTimeET.getText().toString().length()>0) {
                            weight = hopWeightET.getText().toString();
                            boilTime = hopBoilTimeET.getText().toString();
                            listener.onDialogPositiveClick(selectedHop, weight, boilTime);
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
        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (AddHopDialogFragment.AddHopDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()+" must implement DialogListener!");
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedHop = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }




}
