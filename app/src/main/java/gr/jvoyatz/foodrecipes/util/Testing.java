package gr.jvoyatz.foodrecipes.util;

import android.util.Log;

import java.util.List;

import gr.jvoyatz.foodrecipes.models.Recipe;

public class Testing {
    public static void printRecipes(List<Recipe> recipes, String tag) {
        try {
            for (Recipe rec : recipes) {
                Log.d(tag, "printRecipes: " + rec);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
