package gr.jvoyatz.foodrecipes.requests;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import gr.jvoyatz.foodrecipes.AppExecutors;
import gr.jvoyatz.foodrecipes.models.Recipe;
import gr.jvoyatz.foodrecipes.requests.responses.RecipeResponse;
import gr.jvoyatz.foodrecipes.requests.responses.RecipeSearchResponse;
import gr.jvoyatz.foodrecipes.util.Constants;
import retrofit2.Call;
import retrofit2.Response;

public class RecipeApiClient  {
    private static final String TAG = "RecipeApiClient";

    private static RecipeApiClient instance;

    private MutableLiveData<List<Recipe>> mRecipes;
    private MutableLiveData<Recipe> mRecipe;
    private MutableLiveData<Boolean> mRecipeRequestTimeout;
    private RetrieveRecipesRunnable mRetrieveRecipesRunnable;
    private RetrieveRecipeRunnable mRetrieveRecipeRunnable;

    public static RecipeApiClient getInstance(){
        if(instance == null) {
            instance = new RecipeApiClient();
        }

        return instance;
    }

    private RecipeApiClient(){
        mRecipes = new MutableLiveData<>();
        mRecipe = new MutableLiveData<>();
        mRecipeRequestTimeout = new MutableLiveData<>();
    }

    public LiveData<List<Recipe>> getRecipes() {
        return mRecipes;
    }

    public LiveData<Recipe> getRecipe(){
        return mRecipe;
    }

    public LiveData<Boolean> isRecipeRequestTimeout(){
        return mRecipeRequestTimeout;
    }

    public void searchRecipesApi(String query, int pageNumber) {
        if(mRetrieveRecipesRunnable != null){
            mRetrieveRecipesRunnable = null;
        }
        mRetrieveRecipesRunnable = new RetrieveRecipesRunnable(query, pageNumber);
        final Future handler = AppExecutors.getInstance().networkIO().submit(mRetrieveRecipesRunnable);

        AppExecutors.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                //let the user know it is timed out
                handler.cancel(true);
            }
        }, Constants.NETWORK_TIMEOUT, TimeUnit.MILLISECONDS);
    }

    public void searchRecipeById(String recipeID){
        Log.d(TAG, "searchRecipeById: " + recipeID);
        if(mRetrieveRecipeRunnable != null){
            mRetrieveRecipeRunnable = null;
        }

        mRecipeRequestTimeout.setValue(false);
        mRetrieveRecipeRunnable = new RetrieveRecipeRunnable(recipeID);
        final Future handler = AppExecutors.getInstance().networkIO().submit(mRetrieveRecipeRunnable);

        AppExecutors.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                //let the user know it is timed out
                mRecipeRequestTimeout.postValue(true);
                handler.cancel(true);
            }
        }, Constants.NETWORK_TIMEOUT, TimeUnit.MILLISECONDS );
    }

    public void cancelRequest(){
        if(mRetrieveRecipesRunnable != null){
            mRetrieveRecipesRunnable.cancelRequest();
        }
        if(mRetrieveRecipeRunnable != null){
            mRetrieveRecipeRunnable.cancelRequest();
        }

    }
    private class RetrieveRecipesRunnable implements Runnable{
        private String query;
        private int pageNumber;
        boolean cancelRequest;

        RetrieveRecipesRunnable(String query, int pageNumber) {
            this.query = query;
            this.pageNumber = pageNumber;
        }

        @Override
        public void run() {
            try {
                Response response = getRecipes(query, pageNumber).execute();
                if(cancelRequest){
                    return;
                }
                if(response.isSuccessful()){
                    RecipeSearchResponse recipeSearchResponse = (RecipeSearchResponse) response.body();
                    List<Recipe> recipes = new ArrayList<Recipe>(recipeSearchResponse.getRecipes());
                    if (pageNumber == 1) {
                        mRecipes.postValue(recipes);
                    }else{
                        List<Recipe> currentHits = mRecipes.getValue();
                        currentHits.addAll(recipes);
                        mRecipes.postValue(currentHits);
                    }
                }else{
                    String error = response.errorBody().string();
                    Log.e(TAG, "run: " + error);
                    mRecipes.postValue(null);
                    response.errorBody().close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                mRecipes.postValue(null);
            }
        }

        private Call<RecipeSearchResponse> getRecipes(String query, int pageNumber) {
            return ServiceGenerator.getRecipeApi().searchRecipe(
                    Constants.API_KEY,
                    query,
                    String.valueOf(pageNumber)
            );
        }
        
        private void cancelRequest(){
            Log.d(TAG, "cancelRequest: cancelling the search request");
            cancelRequest = true;
        }
    }

    private class RetrieveRecipeRunnable implements Runnable{
        private String recipeId;
        boolean cancelRequest;

        RetrieveRecipeRunnable(String recipeId) {
            this.recipeId = recipeId;
        }

        @Override
        public void run() {
            try {
                Response response = getRecipe(recipeId).execute();
                if(cancelRequest){
                    return;
                }

                if(response.isSuccessful()){
                    RecipeResponse recipeResponse = (RecipeResponse) response.body();
                    mRecipe.postValue(recipeResponse.getRecipe());
                }else{
                    String error = response.errorBody().string();
                    Log.e(TAG, "run: " + error);
                    mRecipe.postValue(null);
                }
            } catch (IOException e) {
                e.printStackTrace();
                mRecipe.postValue(null);
            }
        }

        private Call<RecipeResponse> getRecipe(String recipeId) {
            Log.d(TAG, "getRecipe: " + recipeId);
            return ServiceGenerator.getRecipeApi().getRecipe(
                    Constants.API_KEY,
                    recipeId
            );
        }

        private void cancelRequest(){
            Log.d(TAG, "cancelRequest: cancelling the recipe get request");
            cancelRequest = true;
        }
    }
}
