package gr.jvoyatz.foodrecipes.requests;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import gr.jvoyatz.foodrecipes.AppExecutors;
import gr.jvoyatz.foodrecipes.models.Recipe;
import gr.jvoyatz.foodrecipes.requests.responses.Hit;
import gr.jvoyatz.foodrecipes.requests.responses.RecipeSearchResponse;
import gr.jvoyatz.foodrecipes.util.Constants;
import retrofit2.Call;
import retrofit2.Response;

public class RecipeApiClient  {
    private static final String TAG = "RecipeApiClient";

    private static RecipeApiClient instance;

    private MutableLiveData<List<Hit>> mRecipes;
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

    public LiveData<List<Hit>> getRecipes(){
        return mRecipes;
    }

    public LiveData<Recipe> getRecipe(){
        return mRecipe;
    }

    public LiveData<Boolean> isRecipeRequestTimeout(){
        return mRecipeRequestTimeout;
    }

    public void searchRecipesApi(String query, int from, int to){
        if(mRetrieveRecipesRunnable != null){
            mRetrieveRecipesRunnable = null;
        }
        mRetrieveRecipesRunnable = new RetrieveRecipesRunnable(query, from, to);
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
        private int from;
        private int to;
        boolean cancelRequest;

        public RetrieveRecipesRunnable(String query, int from, int to) {
            this.query = query;
            this.from = from;
            this.to = to;
        }

        @Override
        public void run() {
            try {
                Response response = getRecipes(query, from, to == 0 ? 10:to).execute();

                if(cancelRequest){
                    return;
                }

                if(response.isSuccessful()){
                    RecipeSearchResponse recipeSearchResponse = (RecipeSearchResponse) response.body();
                    Log.d(TAG, "run: " + recipeSearchResponse.getRecipes().length);
                    ArrayList<Hit> recipes = new ArrayList<Hit>(Arrays.asList(recipeSearchResponse.getRecipes()));
                    if(from == 0) {
                        mRecipes.postValue(recipes);
                    }else{
                        List<Hit> currentHits = mRecipes.getValue();
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
        
        private Call<RecipeSearchResponse> getRecipes(String query, int from, int to){
            return ServiceGenerator.getRecipeApi().searchRecipe(
                    Constants.APP_ID,
                    Constants.API_KEY,
                    query,
                    String.valueOf(from),
                    String.valueOf(to)
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

        public RetrieveRecipeRunnable(String recipeId) {
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
                    Recipe recipeResponse = (Recipe) response.body();
                    mRecipe.postValue(recipeResponse);

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

        private Call<Recipe> getRecipe(String recipeId){
            return ServiceGenerator.getRecipeApi().getRecipe(
                    Constants.APP_ID,
                    Constants.API_KEY,
                    recipeId
            );
        }

        private void cancelRequest(){
            Log.d(TAG, "cancelRequest: cancelling the search request");
            cancelRequest = true;
        }
    }
}
