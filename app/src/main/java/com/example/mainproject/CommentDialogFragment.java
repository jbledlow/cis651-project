package com.example.mainproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CommentDialogFragment extends DialogFragment {
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    DatabaseReference mRootReference = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mCommentsReference;
    AddCommentDialogListener listener;
    String key;

    public interface AddCommentDialogListener {
        public void onDialogPositiveClick(String key, String un, String comment);
        public void onDialogNegativeClick(String key, String un, String comment);
    }

    public CommentDialogFragment(String _key) {
        key = _key;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Add Comment");
        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_TEXT_VARIATION_LONG_MESSAGE);
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onDialogPositiveClick(key,user.getDisplayName(),input.getText().toString());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        return builder.show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (CommentDialogFragment.AddCommentDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()+" must implement DialogListener!");
        }
    }

}
