package gr.jvoyatz.foodrecipes.requests;

import gr.jvoyatz.foodrecipes.models.Recipe;
import gr.jvoyatz.foodrecipes.requests.responses.RecipeSearchResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RecipeApi {

    //search
    @GET("/search")
    Call<RecipeSearchResponse> searchRecipe(@Query("app_id") String appId, @Query("app_key") String appKey, @Query("q")  String query, @Query("from") String from, @Query("to") String to);

    Call<Recipe> getRecipe(@Query("app_id") String appId, @Query("app_key") String appKey, String recipeId);
}
