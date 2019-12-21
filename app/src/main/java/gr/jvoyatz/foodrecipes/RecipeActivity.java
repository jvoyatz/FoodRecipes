package gr.jvoyatz.foodrecipes;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import gr.jvoyatz.foodrecipes.models.Recipe;
import gr.jvoyatz.foodrecipes.requests.responses.Hit;
import gr.jvoyatz.foodrecipes.viewmodels.RecipeViewModel;

public class RecipeActivity extends BaseActivity {
    private static final String TAG = "RecipeActivity";
    private Hit hit;

    //ui
    private AppCompatImageView mRecipeImage;
    private TextView mRecipeTitle, mRecipeRank;
    private LinearLayout mRecipeIngredientsContainer;
    private ScrollView mScrollView;

    private RecipeViewModel mRecipeViewModel;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        mRecipeImage = findViewById(R.id.recipe_image);
        mRecipeTitle = findViewById(R.id.recipe_title);
        mRecipeRank = findViewById(R.id.recipe_social_score);
        mRecipeIngredientsContainer = findViewById(R.id.ingredients_container);
        mScrollView = findViewById(R.id.parent);


        mRecipeViewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);

       // subscribeObservers();
        showProgressBar(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getIncomingIntent();
            }
        }, 4000);

    }

    private void subscribeObservers(){
        mRecipeViewModel.getRecipe().observe(this, new Observer<Recipe>() {
            @Override
            public void onChanged(Recipe recipe) {
                if(recipe != null){
                    if(recipe.getLabel().equals(mRecipeViewModel.getmRecipeId()) ){
                        Log.d(TAG, "onChanged: eeeeeeeeeee");
                        //setRecipeProperties();
                        mRecipeViewModel.setRetrieveRecipe(true);
                    }
                }
            }
        });

        mRecipeViewModel.isRecipeRequestTimeout().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean && !mRecipeViewModel.didRetrieveRecipe()){
                    Log.d(TAG, "onChanged: eeeeeee...");
                }
            }
        });
    }
    private void getIncomingIntent(){
        if(getIntent().hasExtra("recipe")){
            hit = getIntent().getParcelableExtra("recipe");
            setRecipeProperties(hit);
            //displayErrorScreen("errrsdf");
        }
    }

    private void displayErrorScreen(String errorMessage){
        mRecipeTitle.setText("Error retrieving recipe");
        mRecipeRank.setText("");
        TextView textView = new TextView(this);
        if(!errorMessage.equals("")){
            textView.setText(errorMessage);
        }else{
            textView.setText("Error");
        }
        textView.setTextSize(15);
        textView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        mRecipeIngredientsContainer.addView(textView);

        RequestOptions requestOptions = new RequestOptions().placeholder(R.drawable.ic_launcher_background);

        Glide
                .with(this)
                .setDefaultRequestOptions(requestOptions)
                .load(R.drawable.ic_launcher_background)
                .into(mRecipeImage);
        showParent();
        showProgressBar(false);
    }
    private void setRecipeProperties(Hit hit){
        if(hit != null){
            RequestOptions requestOptions = new RequestOptions().placeholder(R.drawable.ic_launcher_background);

            Glide
                .with(this)
                .setDefaultRequestOptions(requestOptions)
                .load(hit.getRecipe().getImgUrl())
                .into(mRecipeImage);

            mRecipeTitle.setText(hit.getRecipe().getLabel());
            mRecipeRank.setText(Double.toString(hit.getRecipe().getYield()));

            mRecipeIngredientsContainer.removeAllViews();
            for (String ingredientLine : hit.getRecipe().getIngredientLines()) {
                TextView textView = new TextView(this);
                textView.setText(ingredientLine);
                textView.setTextSize(15);
                textView.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
                ));

                mRecipeIngredientsContainer.addView(textView);
            }
        }
        mScrollView.setVisibility(View.VISIBLE);
        showProgressBar(false);
    }

    private void showParent(){
        mScrollView.setVisibility(View.VISIBLE);
    }

}
