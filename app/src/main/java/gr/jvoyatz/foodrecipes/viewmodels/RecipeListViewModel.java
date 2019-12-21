package gr.jvoyatz.foodrecipes.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import gr.jvoyatz.foodrecipes.models.Recipe;
import gr.jvoyatz.foodrecipes.repositories.RecipeRepository;


/*
    responsible for getting/retrieving/holding/displaying recipes
    keeps an updated list of the activities
    uses live data
 */
public class RecipeListViewModel extends ViewModel {
    private static final String TAG = "RecipeListViewModel";

    private RecipeRepository mRecipeRepository;
    private boolean mIsViewingRecipes;
    private boolean mIsPerformingQuery;
    public RecipeListViewModel() {
        //mIsViewingRecipes = false;
        mIsPerformingQuery = false;
        mRecipeRepository = RecipeRepository.getInstance();
    }

    public LiveData<List<Recipe>> getRecipes() {
        return mRecipeRepository.getRecipes();
    }

    public void searchRecipesApi(String query, int pageNumber) {
        mIsViewingRecipes = true;
        mIsPerformingQuery = true;
        mRecipeRepository.searchRecipesApi(query, pageNumber);
    }

    public void searchNextPage(){
        if(!mIsPerformingQuery && mIsViewingRecipes && !isQueryExhausted().getValue()){
            mRecipeRepository.searchNextPage();
        }
    }
    public boolean isViewingRecipes() {
        return mIsViewingRecipes;
    }

    public void setIsViewingRecipes(boolean mIsViewingRecipes) {
        this.mIsViewingRecipes = mIsViewingRecipes;
    }

    public boolean onBackPressed(){
        if (mIsPerformingQuery){
            mRecipeRepository.cancelRequest();
            mIsPerformingQuery = false;
        }

        if(mIsViewingRecipes){
            mIsViewingRecipes = false;
            return false;
        }
        return true;
    }

    public boolean isPerformingQuery() {
        return mIsPerformingQuery;
    }

    public void setIsPerformingQuery(boolean mIsPerformingQuery) {
        this.mIsPerformingQuery = mIsPerformingQuery;
    }

    public LiveData<Boolean> isQueryExhausted(){
        return mRecipeRepository.isQueryExhausted();
    }
}
