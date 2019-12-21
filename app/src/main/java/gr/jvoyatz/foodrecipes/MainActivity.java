package gr.jvoyatz.foodrecipes;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import gr.jvoyatz.foodrecipes.adapters.OnRecipeListener;
import gr.jvoyatz.foodrecipes.adapters.RecipeRecyclerAdapter;
import gr.jvoyatz.foodrecipes.models.Recipe;
import gr.jvoyatz.foodrecipes.util.VerticalSpacingItemDecorator;
import gr.jvoyatz.foodrecipes.viewmodels.RecipeListViewModel;

public class MainActivity extends AppCompatActivity implements OnRecipeListener {
    private static final String TAG = "MainActivity";

    //works by observing the changes of the data
    //anything happens to the data then changes are applied to the activity
    private RecipeListViewModel mRecipeListViewModel;
    private RecyclerView mRecyclerView;
    private RecipeRecyclerAdapter mRecipeRecyclerAdapter;
    private SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        mRecyclerView = findViewById(R.id.recipe_list);
        mRecipeListViewModel = ViewModelProviders.of(this).get(RecipeListViewModel.class);

        initRecyclerView();
        subsribeObservers();
        initSearchView();
        if(!mRecipeListViewModel.isViewingRecipes()){
            displaySearchCategories();
        }

        setSupportActionBar((Toolbar) findViewById(R.id.recipe_toolbar));
    }


    private void initRecyclerView(){
        mRecipeRecyclerAdapter = new RecipeRecyclerAdapter( this);
        mRecyclerView.setAdapter(mRecipeRecyclerAdapter);
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(30);
        mRecyclerView.addItemDecoration(itemDecorator);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if(!mRecyclerView.canScrollVertically(1)){
                    //search the next page
                    mRecipeListViewModel.searchNextPage();
                }
            }
        });

    }
    private void subsribeObservers(){
        mRecipeListViewModel.getRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> recipes) {
                if(recipes != null) {
                    if(mRecipeListViewModel.isViewingRecipes()) {
                        //Testing.printRecipes(recipes, "recipes test");
                        mRecipeListViewModel.setIsPerformingQuery(false);
                        mRecipeRecyclerAdapter.setRecipeHits(recipes);
                    }
                }
            }
        });

        mRecipeListViewModel.isQueryExhausted().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                Log.d(TAG, "onChanged: aBoolean exhausted " + aBoolean);
                if(aBoolean){
                    Log.d(TAG, "onChanged: exhausted" );
                    mRecipeRecyclerAdapter.setQueryExhausted();
                }
            }
        });
    }

    private void initSearchView(){
        mSearchView= findViewById(R.id.search_view);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mRecipeRecyclerAdapter.displayLoading();
                mRecipeListViewModel.searchRecipesApi(query, 1);
                mSearchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public void onRecipeClick(int position) {
        Intent intent = new Intent(this, RecipeActivity.class);
        intent.putExtra("recipe", mRecipeRecyclerAdapter.getSelectedRecipe(position));
        startActivity(intent);
    }

    @Override
    public void onCategoryClick(String category) {

        mRecipeRecyclerAdapter.displayLoading();
        mSearchView.clearFocus();
        mRecipeListViewModel.searchRecipesApi(category, 1);
    }

    private void displaySearchCategories(){
        mRecipeListViewModel.setIsViewingRecipes(false);
        mRecipeRecyclerAdapter.displaySearchCategories();
    }

    @Override
    public void onBackPressed() {
        if(mRecipeListViewModel.onBackPressed()){
            super.onBackPressed();
        }else {
            displaySearchCategories();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.recipe_search_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_categories){
            displaySearchCategories();
        }
        return super.onOptionsItemSelected(item);
    }
}
