package gr.jvoyatz.foodrecipes.models;

public class Ingredient {
    private String text;
    private String weight;

    public Ingredient(String text, String weight) {
        this.text = text;
        this.weight = weight;
    }

    public Ingredient() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "text='" + text + '\'' +
                ", weight='" + weight + '\'' +
                '}';
    }
}
