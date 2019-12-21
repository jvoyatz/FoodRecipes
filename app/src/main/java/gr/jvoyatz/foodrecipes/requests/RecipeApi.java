package gr.jvoyatz.foodrecipes.requests;

import gr.jvoyatz.foodrecipes.requests.responses.RecipeResponse;
import gr.jvoyatz.foodrecipes.requests.responses.RecipeSearchResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RecipeApi {

    //search
    @GET("api/search")
    Call<RecipeSearchResponse> searchRecipe(@Query("key") String appKey, @Query("q") String query, @Query("page") String page);

    @GET("api/get")
    Call<RecipeResponse> getRecipe(@Query("key") String appKey, @Query("rId") String recipeId);
}
