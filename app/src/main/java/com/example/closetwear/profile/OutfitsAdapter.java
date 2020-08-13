package com.example.closetwear.profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.closetwear.GlideApp;
import com.example.closetwear.Navigation;
import com.example.closetwear.parse.OutfitPost;
import com.example.closetwear.R;
import com.parse.ParseFile;

import java.util.List;

/**
 * This class is the adapter to handle the content from {@link OutfitsFragment}.
 */
public class OutfitsAdapter extends RecyclerView.Adapter<OutfitsAdapter.ViewHolder> {
    private Context context;
    private List<OutfitPost> posts;

    /**
     * This constructor creates a new instance of an OutfitsAdapter.
     *
     * @param context the current context of the Outfit view
     * @param posts   a list of all the {@link OutfitPost} to be bound onto the screen
     */
    public OutfitsAdapter(Context context, List<OutfitPost> posts) {
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
     * Adds a list of {@link OutfitPost} to the current list of items to be bound onto the
     * Recycler View
     *
     * @param list all {@link OutfitPost} instances to be added
     */
    public void addAll(List<OutfitPost> list) {
        posts.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * {@link androidx.recyclerview.widget.RecyclerView.Adapter#onCreateViewHolder(android.view.ViewGroup, int)}
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_outfits, parent, false);
        return new ViewHolder(view);
    }

    /**
     * {@link RecyclerView.Adapter#onBindViewHolder(androidx.recyclerview.widget.RecyclerView.ViewHolder, int)}
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OutfitPost post = posts.get(position);
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
        private ImageView outfitImg;

        /**
         * {@link androidx.recyclerview.widget.RecyclerView.ViewHolder#ViewHolder(android.view.View)}
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            outfitImg = itemView.findViewById(R.id.outfitImg);

        }

        /**
         * Binds each {@link OutfitPost} to the adapter along with its details. Also adds a
         * listener to navigate to OutfitDetailsActivity when an image is selected.
         *
         * @param post ClothingPost that needs to be bound
         */
        public void bind(final OutfitPost post) {
            // Bind the post data to the view elements
            final ParseFile image = post.getImage();
            GlideApp.with(context).load(image.getUrl()).into(outfitImg);
            outfitImg.setOnClickListener(view -> Navigation.goOutfitDetailsActivity(context, post));
        }
    }

}
