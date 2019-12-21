package gr.jvoyatz.foodrecipes.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.List;

import gr.jvoyatz.foodrecipes.models.Recipe;
import gr.jvoyatz.foodrecipes.requests.RecipeApiClient;
import gr.jvoyatz.foodrecipes.requests.responses.Hit;

public class RecipeRepository {
    private static final String TAG = "RecipeRepository";

    private static RecipeRepository instance;
    private RecipeApiClient mRecipeApiClient;
    private String mQuery;
    private int mFrom, mTo;
    private MutableLiveData<Boolean> mIsQueryExhausted = new MutableLiveData<>();
    //acts as an mediator
    //used when you want to make a change a set of live data before it is returned
    private MediatorLiveData<List<Hit>> mRecipes = new MediatorLiveData<>();

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

    public LiveData<List<Hit>> getRecipes(){
        //return mRecipeApiClient.getRecipes();
        return mRecipes;
    }

    public LiveData<Recipe> getRecipe(){ return mRecipeApiClient.getRecipe();}

    public LiveData<Boolean> isRecipeRequestTimeout(){
        return mRecipeApiClient.isRecipeRequestTimeout();
    }

    private void initMediators(){
        final LiveData<List<Hit>> recipeListApiSource = mRecipeApiClient.getRecipes();

        //in-between section
        mRecipes.addSource(recipeListApiSource, new Observer<List<Hit>>() {
            @Override
            public void onChanged(List<Hit> hits) {
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

    public void doneQuery(List<Hit> list){
        //Log.d(TAG, "doneQuery: stasrt");
        if(list != null){
            if(list.size() % 10 != 0){
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
    public void searchRecipesApi(String query, int from , int to){
        mRecipeApiClient.searchRecipesApi(query, from, to);
        this.mQuery = query;
        this.mFrom = from;
        this.mTo = to == 0? 10: to;
        mIsQueryExhausted.setValue(false);
    }


    public void searchNextPage(){
        searchRecipesApi(mQuery, mTo + 1, mTo + 10);
    }

    public void searchRecipeById(String recipeId){
        mRecipeApiClient.searchRecipeById(recipeId);
    }
    public void cancelRequest(){
        mRecipeApiClient.cancelRequest();
    }
}
