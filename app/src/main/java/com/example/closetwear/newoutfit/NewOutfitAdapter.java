package com.example.closetwear.newoutfit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.closetwear.parse.ClothingPost;
import com.example.closetwear.GlideApp;
import com.example.closetwear.R;
import com.parse.ParseFile;

import java.util.List;

/**
 * This class is the adapter to handle the content from {@link NewOutfitActivity}.
 */
public class NewOutfitAdapter extends RecyclerView.Adapter<NewOutfitAdapter.ViewHolder> {
    private Context context;
    private List<ClothingPost> posts;

    /**
            * This constructor creates a new instance of a NewOutfitAdapter.
            *
            * @param context the current context of the NewOutfit view
     * @param posts   a list of all the {@link ClothingPost} to be bound onto the screen
     */
    public NewOutfitAdapter(Context context, List<ClothingPost> posts) {
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
     * @param list all {@link ClothingPost} instances to be added
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
    public NewOutfitAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_closet, parent, false);
        return new NewOutfitAdapter.ViewHolder(view);
    }

    /**
     * {@link RecyclerView.Adapter#onBindViewHolder(androidx.recyclerview.widget.RecyclerView.ViewHolder, int)}
     */
    @Override
    public void onBindViewHolder(@NonNull NewOutfitAdapter.ViewHolder holder, int position) {
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
         * Binds each {@link ClothingPost} to the adapter along with its details.
         *
         * @param post ClothingPost that needs to be bound
         */
        public void bind(final ClothingPost post) {
            // Bind the post data to the view elements
            brand.setText(post.getBrand());
            itemName.setText(post.getName());
            final ParseFile image = post.getImage();
            GlideApp.with(context).load(image.getUrl()).into(itemImg);
        }
    }
}
