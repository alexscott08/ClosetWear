package com.example.closetwear.adapters;

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

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * This class is the adapter to handle the content from {@link com.example.closetwear.fragments.HomeFragment}
 * to bind onto the app's home page.
 */
public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
    private Context context;
    private List<OutfitPost> posts;

    /**
     * This constructor creates a new instance of a HomeAdapter.
     *
     * @param context the current context of the Home view
     * @param posts   a list of all the {@link OutfitPost} to be bound onto the screen
     */
    public HomeAdapter(Context context, List<OutfitPost> posts) {
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
        View view = LayoutInflater.from(context).inflate(R.layout.item_home, parent, false);
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
         * Binds each {@link OutfitPost} to the HomeAdapter. Also, adds a listener to connect each
         * post to its view in {@link com.example.closetwear.OutfitDetailsActivity}.
         *
         * @param post OutfitPost that needs to be bound
         */
        public void bind(final OutfitPost post) {
            // Bind the post data to the view elements
            final ParseFile image = post.getImage();
            int radius = 10;
            int margin = 30;
            GlideApp.with(context).load(image.getUrl()).transform(
                    new RoundedCornersTransformation(radius, margin))
                    .into(outfitImg);

            outfitImg.setOnClickListener(view -> Navigation.goOutfitDetailsActivity(context, post));
        }
    }
}
