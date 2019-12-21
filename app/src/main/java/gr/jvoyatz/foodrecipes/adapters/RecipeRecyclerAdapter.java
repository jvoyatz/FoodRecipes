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
import gr.jvoyatz.foodrecipes.util.Constants;

public class RecipeRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "RecipeRecyclerAdapter";

    private static final int RECIPE_TYPE = 1;
    private static final int LOADING_TYPE = 2;
    private static final int CATEGORY_TYPE = 3;
    private static final int EXHAUSTED_TYPE = 4;

    private List<Recipe> mRecipeHits;
    private OnRecipeListener mOnRecipeListener;

    public RecipeRecyclerAdapter(OnRecipeListener mOnRecipeListener) {
        this.mOnRecipeListener = mOnRecipeListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
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
                    .load(mRecipeHits.get(position).getImage_url())
                    .into(((RecipeViewHolder) holder).image);

            ((RecipeViewHolder) holder).title.setText(mRecipeHits.get(position).getTitle());
            ((RecipeViewHolder) holder).publisher.setText(mRecipeHits.get(position).getPublisher());
            ((RecipeViewHolder) holder).dietLabels.setText(String.format("%s", mRecipeHits.get(position).getSocial_rank()));

        }else if(itemViewType == CATEGORY_TYPE){
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .error(R.drawable.ic_launcher_background);

            Uri path = Uri.parse("android.resource://gr.jvoyatz.foodrecipes/drawable/" + mRecipeHits.get(position).getImage_url());
            //Uri path = getUriToDrawable(holder.itemView.getContext(), R.drawable.breakfast);
            Glide.with(((CategoryViewHolder) holder).itemView.getContext())
                    .setDefaultRequestOptions(options)
                    .load(path)
                    .into(((CategoryViewHolder)holder).categoryImage);


            ((CategoryViewHolder) holder).categoryTitle.setText(mRecipeHits.get(position).getTitle());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mRecipeHits.get(position).getSocial_rank() == -1) {
            return CATEGORY_TYPE;
        } else if (mRecipeHits.get(position).getTitle().equals("LOADING...")) {
            return LOADING_TYPE;
        } else if (mRecipeHits.get(position).getTitle().equals("EXHAUSTED...")) {
            return EXHAUSTED_TYPE;
        } else if (mRecipeHits != null && position == mRecipeHits.size() - 1 && position != 0 && !mRecipeHits.get(position).getTitle().equals("EXHAUSTED...")) {
            return LOADING_TYPE;
        }else{
            return RECIPE_TYPE;
        }
    }
    public void setQueryExhausted(){
        hideLoading();
        Recipe exhaustedRecipe = new Recipe();
        exhaustedRecipe.setTitle("EXHAUSTED...");
        //mRecipeHits.clear();
        mRecipeHits.add(exhaustedRecipe);
        notifyDataSetChanged();
    }
    private void hideLoading(){
        if(isLoading()){
            for (Recipe mRecipeHit : mRecipeHits) {
                if (mRecipeHit.getTitle().equals("LOADING...") || mRecipeHit.getTitle().equals("EXHAUSTED...")) {
                    mRecipeHits.remove(mRecipeHit);
                }
            }
            notifyDataSetChanged();
        }
    }

    public void displayLoading(){
        if(!isLoading()){
            Recipe recipe = new Recipe();
            recipe.setTitle("LOADING...");
            List<Recipe> hitList = new ArrayList<>();
            hitList.add(recipe);
            mRecipeHits = hitList;
            notifyDataSetChanged();
        }
    }

    public void displaySearchCategories(){
        List<Recipe> hits = new ArrayList<>();
        for (int i = 0; i < Constants.DEFAULT_SEARCH_CATEGORY_IMAGES.length; i++) {
            Recipe recipe = new Recipe();
            recipe.setSocial_rank(-1);
            recipe.setTitle(Constants.DEFAULT_SEARCH_CATEGORIES[i]);
            recipe.setImage_url(Constants.DEFAULT_SEARCH_CATEGORY_IMAGES[i]);
            hits.add(recipe);
        }
        mRecipeHits = hits;
        notifyDataSetChanged();
    }
    private boolean isLoading(){
        if(mRecipeHits != null && mRecipeHits.size() > 0){
            return mRecipeHits.get(mRecipeHits.size() - 1).getTitle().equals("LOADING...");
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

    public void setRecipeHits(List<Recipe> hits) {
        this.mRecipeHits = hits;
        notifyDataSetChanged();
    }

    public Recipe getSelectedRecipe(int position) {
        if(mRecipeHits != null){
            if(mRecipeHits.size() > 0){
                return mRecipeHits.get(position);
            }
        }
        return null;
    }

}
