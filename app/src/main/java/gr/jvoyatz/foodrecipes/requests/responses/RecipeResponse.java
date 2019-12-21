package gr.jvoyatz.foodrecipes.requests.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

import gr.jvoyatz.foodrecipes.models.Recipe;

public class RecipeResponse {

    @SerializedName("recipe")
    @Expose()
    private Recipe recipe;

    public Recipe getRecipe() {
        return recipe;
    }

    @NotNull
    @Override
    public String toString() {
        return "RecipeResponse{" +
                "recipe=" + recipe +
                '}';
    }
}