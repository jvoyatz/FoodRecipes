package gr.jvoyatz.foodrecipes.requests.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import gr.jvoyatz.foodrecipes.models.Recipe;

public class RecipeSearchResponse {

    @SerializedName("count")
    @Expose()
    private int count;

    @SerializedName("recipes")
    @Expose()
    private List<Recipe> recipes;

    public int getCount() {
        return count;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    @NotNull
    @Override
    public String toString() {
        return "RecipeSearchResponse{" +
                "count=" + count +
                ", recipes=" + recipes +
                '}';
    }
}