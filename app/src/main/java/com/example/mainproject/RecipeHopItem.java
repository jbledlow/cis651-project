package com.example.mainproject;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

public class RecipeHopItem {
    String name;
    String weight;
    String boilTime;

    @NonNull
    @Override
    public String toString() {
        return String.format("{\"name\": \"%s\", \"weight\": \"%s\", \"boilTime\": \"%s\"}",name, weight, boilTime);
    }

    public RecipeHopItem fromString(String s) {
        RecipeHopItem hop = new RecipeHopItem();
        try {
            JSONObject json = new JSONObject(s);
            hop.name = json.get("name").toString();
            hop.weight = json.get("weight").toString();
            hop.boilTime = json.get("boilTime").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return hop;
    }
}
