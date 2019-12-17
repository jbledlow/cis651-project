package com.example.mainproject;

import androidx.annotation.NonNull;

public class RecipeMaltItem {
    public String weight;
    public String name;

    @NonNull
    @Override
    public String toString() {
        return String.format("{\"name\": \"%s\", \"weight\": \"%s\"}",name, weight);
    }
}
