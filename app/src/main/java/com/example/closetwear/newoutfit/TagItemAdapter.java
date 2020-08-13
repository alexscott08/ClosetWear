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
import com.example.closetwear.Navigation;
import com.example.closetwear.parse.OutfitPost;
import com.example.closetwear.R;
import com.parse.ParseException;
import com.parse.ParseFile;

import java.util.List;

/**
 * This class is the adapter to handle the content from {@link TagItemActivity}.
 */
public class TagItemAdapter extends RecyclerView.Adapter<TagItemAdapter.ViewHolder> {
    private Context context;
    private List<ClothingPost> posts;
    private OutfitPost outfitPost;

    /**
     * This constructor creates a new instance of a TagItemAdapter.
     *
     * @param context the current context of the TagItem view
     * @param posts   a list of all the {@link ClothingPost} to be bound onto the screen
     * @param outfitPost the object that a ClothingPost instance will be tagged to
     */
    public TagItemAdapter(Context context, List<ClothingPost> posts, OutfitPost outfitPost) {
        this.context = context;
        this.posts = posts;
        this.outfitPost = outfitPost;
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
    public TagItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_closet, parent, false);
        return new TagItemAdapter.ViewHolder(view);
    }

    /**
     * {@link RecyclerView.Adapter#onBindViewHolder(androidx.recyclerview.widget.RecyclerView.ViewHolder, int)}
     */
    @Override
    public void onBindViewHolder(@NonNull TagItemAdapter.ViewHolder holder, int position) {
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
         * Binds each {@link ClothingPost} to the adapter along with its details. Also sets a listener
         * to navigate back to {@link NewOutfitActivity} when image is selected.
         *
         * @param post ClothingPost that needs to be bound
         */
        public void bind(final ClothingPost post) {
            // Bind the post data to the view elements
            brand.setText(post.getBrand());
            itemName.setText(post.getName());
            final ParseFile image = post.getImage();
            GlideApp.with(context).load(image.getUrl()).into(itemImg);
            itemImg.setOnClickListener(view -> {
                outfitPost.add("fitItems", post.getObjectId());
                post.setFit(outfitPost.getObjectId());
                try {
                    outfitPost.save();
                    outfitPost.fetch();
                    post.save();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Navigation.goNewOutfitActivity(context, post, outfitPost);
            });
        }
    }
}
