package gr.jvoyatz.foodrecipes.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.List;

import gr.jvoyatz.foodrecipes.models.Recipe;
import gr.jvoyatz.foodrecipes.requests.RecipeApiClient;

public class RecipeRepository {
    private static final String TAG = "RecipeRepository";

    private static RecipeRepository instance;
    private RecipeApiClient mRecipeApiClient;
    private String mQuery;
    private int mPageNumber;
    private MutableLiveData<Boolean> mIsQueryExhausted = new MutableLiveData<>();
    //acts as an mediator
    //used when you want to make a change a set of live data before it is returned
    private MediatorLiveData<List<Recipe>> mRecipes = new MediatorLiveData<>();

    public static RecipeRepository getInstance(){
        if(instance == null){
            instance = new RecipeRepository();
        }

        return instance;
    }

    private RecipeRepository(){
        mRecipeApiClient = RecipeApiClient.getInstance();
        initMediators();
    }

    public LiveData<List<Recipe>> getRecipes() {
        //return mRecipeApiClient.getRecipes();
        return mRecipes;
    }

    public LiveData<Recipe> getRecipe(){ return mRecipeApiClient.getRecipe();}

    public LiveData<Boolean> isRecipeRequestTimeout(){
        return mRecipeApiClient.isRecipeRequestTimeout();
    }

    private void initMediators(){
        final LiveData<List<Recipe>> recipeListApiSource = mRecipeApiClient.getRecipes();

        //in-between section
        mRecipes.addSource(recipeListApiSource, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> hits) {
                if(hits != null){
                    mRecipes.setValue(hits);
                    doneQuery(hits);
                }else{
                    //search database cache
                    doneQuery(null);
                }
            }
        });
    }

    private void doneQuery(List<Recipe> list) {
        //Log.d(TAG, "doneQuery: stasrt");
        if(list != null){
            if (list.size() % 30 != 0) {
                //Log.d(TAG, "doneQuery: % 10 != 0");
                mIsQueryExhausted.setValue(true);
            }
        }else{
            //Log.d(TAG, "doneQuery: null");
            mIsQueryExhausted.setValue(true);
        }
    }

    public LiveData<Boolean> isQueryExhausted(){
        return mIsQueryExhausted;
    }

    public void searchRecipesApi(String query, int pageNumber) {
        if (pageNumber == 0) {
            pageNumber = 1;
        }

        this.mQuery = query;
        mPageNumber = pageNumber;
        mIsQueryExhausted.setValue(false);
        mRecipeApiClient.searchRecipesApi(query, pageNumber);
    }

    public void searchNextPage(){
        searchRecipesApi(mQuery, mPageNumber + 1);
    }
    public void searchRecipeById(String recipeId){
        mRecipeApiClient.searchRecipeById(recipeId);
    }
    public void cancelRequest(){
        mRecipeApiClient.cancelRequest();
    }
}
