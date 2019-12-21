package gr.jvoyatz.foodrecipes.requests.responses;

import android.os.Parcel;
import android.os.Parcelable;

import gr.jvoyatz.foodrecipes.models.Recipe;

public class Hit implements Parcelable {
    private Recipe recipe;
    private boolean bookmarked;
    private boolean bought;

    public Hit(Recipe recipe, boolean bookmarked, boolean bought) {
        this.recipe = recipe;
        this.bookmarked = bookmarked;
        this.bought = bought;
    }

    public Hit() {
    }

    protected Hit(Parcel in) {
        recipe = in.readParcelable(Recipe.class.getClassLoader());
        bookmarked = in.readByte() != 0;
        bought = in.readByte() != 0;
    }

    public static final Creator<Hit> CREATOR = new Creator<Hit>() {
        @Override
        public Hit createFromParcel(Parcel in) {
            return new Hit(in);
        }

        @Override
        public Hit[] newArray(int size) {
            return new Hit[size];
        }
    };

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public boolean isBookmarked() {
        return bookmarked;
    }

    public void setBookmarked(boolean bookmarked) {
        this.bookmarked = bookmarked;
    }

    public boolean isBought() {
        return bought;
    }

    public void setBought(boolean bought) {
        this.bought = bought;
    }

    @Override
    public String toString() {
        return "Hit{" +
                "recipe=" + recipe +
                ", bookmarked=" + bookmarked +
                ", bought=" + bought +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(recipe, flags);
        dest.writeByte((byte) (bookmarked ? 1 : 0));
        dest.writeByte((byte) (bought ? 1 : 0));
    }
}
