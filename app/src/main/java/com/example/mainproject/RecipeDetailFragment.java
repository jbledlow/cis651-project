package com.example.mainproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RecipeDetailFragment extends Fragment {
    TextView name;
    TextView volume;
    TextView type;
    LinearLayout mash;
    LinearLayout hops;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        View view = inflater.inflate(R.layout.recipe_viewer_fragment, container, false);
        type = view.findViewById(R.id.recipe_type);
        type.setText(args.getString("type"));
        volume = view.findViewById(R.id.recipe_volume);
        volume.setText(args.getString("volume"));
        name = view.findViewById(R.id.recipe_name);
        name.setText(args.getString("name"));
        mash = view.findViewById(R.id.mash_recycler);
        hops = view.findViewById(R.id.hop_recycler);

        String hopListText = args.getString("hops");
        Pattern p = Pattern.compile("\\{.*?\\}");
        Matcher m = p.matcher(hopListText);
        while (m.find()) {
            try {
                JSONObject j = new JSONObject(m.group(0));
                //Toast.makeText(getContext(),j.get("name").toString(), Toast.LENGTH_LONG).show();
                View hopView = inflater.inflate(R.layout.layout_recipe_hop_item,hops,false);
                TextView name = hopView.findViewById(R.id.hop_name);
                TextView weight = hopView.findViewById(R.id.hop_weight);
                TextView boilTime = hopView.findViewById(R.id.boil_time);
                name.setText(j.getString("name"));
                weight.setText(j.getString("weight"));
                boilTime.setText(j.getString("boilTime"));
                hops.addView(hopView);
            } catch (JSONException e) {}
        }

        String mashListText = args.getString("mash");
        Pattern p2 = Pattern.compile("\\{.*?\\}");
        Matcher m2 = p2.matcher(mashListText);
        while (m2.find()) {
            try {
                JSONObject j = new JSONObject(m2.group(0));
                //Toast.makeText(getContext(),j.get("name").toString(), Toast.LENGTH_LONG).show();
                View mashView = inflater.inflate(R.layout.layout_recipe_malt_item,mash,false);
                TextView name = mashView.findViewById(R.id.malt_name);
                TextView weight = mashView.findViewById(R.id.malt_weight);
                name.setText(j.getString("name"));
                weight.setText(j.getString("weight"));
                mash.addView(mashView);
            } catch (JSONException e) {}
        }





        return view;
    }
}
