package gr.jvoyatz.foodrecipes.requests.responses;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

public class RecipeSearchResponse {

    @SerializedName("q")
    private String q;
    @SerializedName("from")
    private int from;
    @SerializedName("to")
    private int to;
    @SerializedName("more")
    private boolean more;
    @SerializedName("count")
    private int count;
    @SerializedName("hits")
    private Hit[] recipes;

    public RecipeSearchResponse() {
    }

    public RecipeSearchResponse(String q, int from, int to, boolean more, int count, Hit[] hits) {
        this.q = q;
        this.from = from;
        this.to = to;
        this.more = more;
        this.count = count;
        this.recipes = hits;
    }

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public boolean isMore() {
        return more;
    }

    public void setMore(boolean more) {
        this.more = more;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Hit[] getRecipes() {
        return recipes;
    }

    public void setRecipes(Hit[] recipes) {
        this.recipes = recipes;
    }

    @Override
    public String toString() {
        return "RecipeSearchResponse{" +
                "q='" + q + '\'' +
                ", from=" + from +
                ", to=" + to +
                ", more=" + more +
                ", count=" + count +
                ", recipes=" + Arrays.toString(recipes) +
                '}';
    }


}
