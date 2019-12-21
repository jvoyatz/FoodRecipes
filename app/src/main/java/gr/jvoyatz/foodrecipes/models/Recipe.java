package gr.jvoyatz.foodrecipes.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.HashMap;

public class Recipe implements Parcelable {
    private String uri;
    private String label;
    @SerializedName("image")
    @Expose
    private String imgUrl;
    private String source;
    @SerializedName("url")
    @Expose
    private String recipeUrl;
    @SerializedName("shareAs")
    @Expose
    private String shareAsUrl;
    private double yield;
    private String[] dietLabels;
    private String[] healthLabels;
    private String[] cautions;
    private String[] ingredientLines;
    private Ingredient[] ingredients;
    private double calories;
    private double totalWeight;
    private double totalTime;
    private HashMap<String, Nutrient> totalNutrients;

    public Recipe() {
    }

    public Recipe(String uri, String label, String imgUrl, String source, String recipeUrl, String shareAsUrl, double yield,
                  String[] dietLabels, String[] healthLabels, String[] cautions, String[] ingredientLines, Ingredient[] ingredients,
                  double calories, double totalWeight, double totalTime, HashMap<String, Nutrient> totalNutrients) {
        this.uri = uri;
        this.label = label;
        this.imgUrl = imgUrl;
        this.source = source;
        this.recipeUrl = recipeUrl;
        this.shareAsUrl = shareAsUrl;
        this.yield = yield;
        this.dietLabels = dietLabels;
        this.healthLabels = healthLabels;
        this.cautions = cautions;
        this.ingredientLines = ingredientLines;
        this.ingredients = ingredients;
        this.calories = calories;
        this.totalWeight = totalWeight;
        this.totalTime = totalTime;
        this.totalNutrients = totalNutrients;
    }

    protected Recipe(Parcel in) {
        uri = in.readString();
        label = in.readString();
        imgUrl = in.readString();
        source = in.readString();
        recipeUrl = in.readString();
        shareAsUrl = in.readString();
        yield = in.readDouble();
        dietLabels = in.createStringArray();
        healthLabels = in.createStringArray();
        cautions = in.createStringArray();
        ingredientLines = in.createStringArray();
        calories = in.readDouble();
        totalWeight = in.readDouble();
        totalTime = in.readDouble();
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getRecipeUrl() {
        return recipeUrl;
    }

    public void setRecipeUrl(String recipeUrl) {
        this.recipeUrl = recipeUrl;
    }

    public String getShareAsUrl() {
        return shareAsUrl;
    }

    public void setShareAsUrl(String shareAsUrl) {
        this.shareAsUrl = shareAsUrl;
    }

    public double getYield() {
        return yield;
    }

    public void setYield(double yield) {
        this.yield = yield;
    }

    public String[] getDietLabels() {
        return dietLabels;
    }

    public void setDietLabels(String[] dietLabels) {
        this.dietLabels = dietLabels;
    }

    public String[] getHealthLabels() {
        return healthLabels;
    }

    public void setHealthLabels(String[] healthLabels) {
        this.healthLabels = healthLabels;
    }

    public String[] getCautions() {
        return cautions;
    }

    public void setCautions(String[] cautions) {
        this.cautions = cautions;
    }

    public String[] getIngredientLines() {
        return ingredientLines;
    }

    public void setIngredientLines(String[] ingredientLines) {
        this.ingredientLines = ingredientLines;
    }

    public Ingredient[] getIngredients() {
        return ingredients;
    }

    public void setIngredients(Ingredient[] ingredients) {
        this.ingredients = ingredients;
    }

    public double getCalories() {
        return calories;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public double getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(double totalWeight) {
        this.totalWeight = totalWeight;
    }

    public double getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(double totalTime) {
        this.totalTime = totalTime;
    }

    public HashMap<String, Nutrient> getTotalNutrients() {
        return totalNutrients;
    }

    public void setTotalNutrients(HashMap<String, Nutrient> totalNutrients) {
        this.totalNutrients = totalNutrients;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "uri='" + uri + '\'' +
                ", label='" + label + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", source='" + source + '\'' +
                ", recipeUrl='" + recipeUrl + '\'' +
                ", shareAsUrl='" + shareAsUrl + '\'' +
                ", yield=" + yield +
                ", dietLabels=" + Arrays.toString(dietLabels) +
                ", healthLabels=" + Arrays.toString(healthLabels) +
                ", cautions=" + Arrays.toString(cautions) +
                ", ingredientLines=" + Arrays.toString(ingredientLines) +
                ", ingredients=" + Arrays.toString(ingredients) +
                ", calories=" + calories +
                ", totalWeight=" + totalWeight +
                ", totalTime=" + totalTime +
                ", totalNutrients=" + totalNutrients +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(uri);
        parcel.writeString(label);
        parcel.writeString(imgUrl);
        parcel.writeString(source);
        parcel.writeString(recipeUrl);
        parcel.writeString(shareAsUrl);
        parcel.writeDouble(yield);
        parcel.writeStringArray(dietLabels);
        parcel.writeStringArray(healthLabels);
        parcel.writeStringArray(cautions);
        parcel.writeStringArray(ingredientLines);
        parcel.writeDouble(calories);
        parcel.writeDouble(totalWeight);
        parcel.writeDouble(totalTime);
    }
}
