package gr.jvoyatz.foodrecipes.util;

import android.util.Log;

import java.util.List;

import gr.jvoyatz.foodrecipes.requests.responses.Hit;

public class Testing {
    public static void printRecipes (List<Hit> hits, String tag){
        try {
            for (Hit hit : hits) {
                Log.d(tag, "printRecipes: " + hit.getRecipe());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
