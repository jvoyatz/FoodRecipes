package gr.jvoyatz.foodrecipes.adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;
import gr.jvoyatz.foodrecipes.R;

public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    OnRecipeListener listener;
    CircleImageView categoryImage;
    TextView categoryTitle;

    public CategoryViewHolder(@NonNull View itemView, OnRecipeListener listener) {
        super(itemView);

        this.listener = listener;
        categoryImage = itemView.findViewById(R.id.category_image);
        categoryTitle = itemView.findViewById(R.id.category_title);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        listener.onCategoryClick(categoryTitle.getText().toString());
    }
}
