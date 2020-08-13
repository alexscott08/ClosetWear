package com.example.closetwear.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.closetwear.GlideApp;
import com.example.closetwear.parse.ClothingPost;
import com.example.closetwear.R;
import com.example.closetwear.parse.OutfitPost;
import com.parse.ParseFile;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * This class is the adapter to handle the content from {@link com.example.closetwear.OutfitDetailsActivity}
 * to show the fit breakdown of an {@link com.example.closetwear.parse.OutfitPost}.
 *
 */
public class OutfitDetailsAdapter extends RecyclerView.Adapter<OutfitDetailsAdapter.ViewHolder> {
    private Context context;
    private List<ClothingPost> posts;

    /**
     * This constructor creates a new instance of an OutfitDetailsAdapter.
     *
     * @param  context  the current context of the OutfitDetails view
     * @param  posts a list of all the {@link ClothingPost} to be bound onto the bottom screen
     */
    public OutfitDetailsAdapter(Context context, List<ClothingPost> posts) {
        this.context = context;
        this.posts = posts;
    }

    /**
     * Clears all elements currently attached to the Recycler View
     */
    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

    /**
     * Adds a list of {@link ClothingPost} to the current list of items to be bound onto the
     * Recycler View
     *
     * @param  list all {@link ClothingPost} instances to be added
     */
    public void addAll(List<ClothingPost> list) {
        posts.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * {@link androidx.recyclerview.widget.RecyclerView.Adapter#onCreateViewHolder(android.view.ViewGroup, int)}
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_closet, parent, false);
        return new ViewHolder(view);
    }

    /**
     * {@link RecyclerView.Adapter#onBindViewHolder(androidx.recyclerview.widget.RecyclerView.ViewHolder, int)}
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ClothingPost post = posts.get(position);
        holder.bind(post);
    }

    /**
     * {@link RecyclerView.Adapter#getItemCount()}
     */
    @Override
    public int getItemCount() {
        return posts.size();
    }

    /**
     * {@link RecyclerView.ViewHolder}
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView itemImg;
        private TextView brand;
        private TextView itemName;

        /**
         * {@link androidx.recyclerview.widget.RecyclerView.ViewHolder#ViewHolder(android.view.View)}
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImg = itemView.findViewById(R.id.itemImg);
            brand = itemView.findViewById(R.id.brand);
            itemName = itemView.findViewById(R.id.itemName);

        }

        /**
         * Binds each {@link ClothingPost} to the adapter.
         *
         * @param  post  ClothingPost that needs to be bound
         */
        public void bind(final ClothingPost post) {
            // Bind the post data to the view elements
            brand.setText(post.getBrand());
            itemName.setText(post.getName());
            final ParseFile image = post.getImage();
            int radius = 10;
            int margin = 30;
            GlideApp.with(context).load(image.getUrl()).transform(
                    new RoundedCornersTransformation(radius, margin)).into(itemImg);
        }
    }
}