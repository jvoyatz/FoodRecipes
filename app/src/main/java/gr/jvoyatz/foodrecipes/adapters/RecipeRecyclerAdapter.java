package gr.jvoyatz.foodrecipes.adapters;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import gr.jvoyatz.foodrecipes.R;
import gr.jvoyatz.foodrecipes.models.Recipe;
import gr.jvoyatz.foodrecipes.requests.responses.Hit;
import gr.jvoyatz.foodrecipes.util.Constants;

public class RecipeRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "RecipeRecyclerAdapter";

    private static final int RECIPE_TYPE = 1;
    private static final int LOADING_TYPE = 2;
    private static final int CATEGORY_TYPE = 3;
    private static final int EXHAUSTED_TYPE = 4;

    private List<Hit> mRecipeHits;
    private OnRecipeListener mOnRecipeListener;

    public RecipeRecyclerAdapter(OnRecipeListener mOnRecipeListener) {
        this.mOnRecipeListener = mOnRecipeListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType){
            default:
            case RECIPE_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recipe_list_item, parent, false);
                return new RecipeViewHolder(view, mOnRecipeListener);
            case LOADING_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_loading_list_item, parent, false);
                return new LoadingViewHolder(view);

            case CATEGORY_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_category_list_item, parent, false);
                return new CategoryViewHolder(view, mOnRecipeListener);
            case EXHAUSTED_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_search_exhausted, parent, false);
                return new SearchExhaustedViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int itemViewType = getItemViewType(position);
        if(itemViewType == RECIPE_TYPE) {
            RequestOptions requestOptions = new RequestOptions().placeholder(R.drawable.ic_launcher_background);

            Glide.with(holder.itemView.getContext())
                    .setDefaultRequestOptions(requestOptions)
                    .load(mRecipeHits.get(position).getRecipe().getImgUrl())
                    .into(((RecipeViewHolder) holder).image);

            ((RecipeViewHolder) holder).title.setText(mRecipeHits.get(position).getRecipe().getLabel());
            ((RecipeViewHolder) holder).publisher.setText(mRecipeHits.get(position).getRecipe().getSource());
            if (mRecipeHits.get(position).getRecipe().getDietLabels() != null && mRecipeHits.get(position).getRecipe().getDietLabels().length > 0) {
                ((RecipeViewHolder) holder).dietLabels.setText(mRecipeHits.get(position).getRecipe().getDietLabels()[0]);
            }
        }else if(itemViewType == CATEGORY_TYPE){
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .error(R.drawable.ic_launcher_background);

            Uri path = Uri.parse("android.resource://gr.jvoyatz.foodrecipes/drawable/" + mRecipeHits.get(position).getRecipe().getImgUrl());
            //Uri path = getUriToDrawable(holder.itemView.getContext(), R.drawable.breakfast);
            Glide.with(((CategoryViewHolder)holder).itemView)
                    .setDefaultRequestOptions(options)
                    .load(path)
                    .into(((CategoryViewHolder)holder).categoryImage);


            ((CategoryViewHolder)holder).categoryTitle.setText(mRecipeHits.get(position).getRecipe().getLabel());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(mRecipeHits.get(position).getRecipe().getYield() == -1){
            return CATEGORY_TYPE;
        }else if(mRecipeHits.get(position).getRecipe().getLabel().equals("LOADING...")){
            return LOADING_TYPE;
        }else if(mRecipeHits.get(position).getRecipe().getLabel().equals("EXHAUSTED...")){
            return EXHAUSTED_TYPE;
        }else if(mRecipeHits != null && position == mRecipeHits.size() - 1 && position != 0 && !mRecipeHits.get(position).getRecipe().getLabel().equals("EXHAUSTED...")){
            return LOADING_TYPE;
        }else{
            return RECIPE_TYPE;
        }
    }
    public void setQueryExhausted(){
        hideLoading();
        Recipe exhaustedRecipe = new Recipe();
        exhaustedRecipe.setLabel("EXHAUSTED...");
        Hit hit = new Hit();
        hit.setRecipe(exhaustedRecipe);
        //mRecipeHits.clear();
        mRecipeHits.add(hit);
        notifyDataSetChanged();
    }
    private void hideLoading(){
        if(isLoading()){
            for (Hit mRecipeHit : mRecipeHits) {
                if(mRecipeHit.getRecipe().getLabel().equals("LOADING...") || mRecipeHit.getRecipe().getLabel().equals("EXHAUSTED...")){
                    mRecipeHits.remove(mRecipeHit);
                }
            }
            notifyDataSetChanged();
        }
    }

    public void displayLoading(){
        if(!isLoading()){
            Recipe recipe = new Recipe();
            recipe.setLabel("LOADING...");
            Hit hit = new Hit();
            hit.setRecipe(recipe);
            List<Hit> hitList = new ArrayList<>();
            hitList.add(hit);
            mRecipeHits = hitList;
            notifyDataSetChanged();
        }
    }

    public void displaySearchCategories(){
        List<Hit> hits = new ArrayList<>();
        for (int i = 0; i < Constants.DEFAULT_SEARCH_CATEGORY_IMAGES.length; i++) {
            Recipe recipe = new Recipe();
            recipe.setYield( -1);
            recipe.setLabel(Constants.DEFAULT_SEARCH_CATEGORIES[i]);
            recipe.setImgUrl(Constants.DEFAULT_SEARCH_CATEGORY_IMAGES[i]);
            Hit hit = new Hit();
            hit.setRecipe(recipe);
            hits.add(hit);
        }
        mRecipeHits = hits;
        notifyDataSetChanged();
    }
    private boolean isLoading(){
        if(mRecipeHits != null && mRecipeHits.size() > 0){
            if(mRecipeHits.get(mRecipeHits.size() - 1).getRecipe().getLabel().equals("LOADING...")){
                return true;
            }
        }
        return false;
    }

    @Override
    public int getItemCount() {
        if(mRecipeHits != null){
            return mRecipeHits.size();
        }
        return 0;
    }

    public void setRecipeHits(List<Hit> hits){
        this.mRecipeHits = hits;
        notifyDataSetChanged();
    }

    public Hit getSelectedRecipe(int position){
        if(mRecipeHits != null){
            if(mRecipeHits.size() > 0){
                return mRecipeHits.get(position);
            }
        }
        return null;
    }

}
