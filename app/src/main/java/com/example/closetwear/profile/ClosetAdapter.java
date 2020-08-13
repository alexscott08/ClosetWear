package com.example.closetwear.profile;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.closetwear.Navigation;
import com.example.closetwear.newoutfit.NewOutfitActivity;
import com.example.closetwear.parse.ClothingPost;
import com.example.closetwear.GlideApp;
import com.example.closetwear.R;
import com.parse.ParseFile;

import java.util.List;

/**
 * This class is the adapter to handle the content from {@link ClosetFragment}.
 */
public class ClosetAdapter extends RecyclerView.Adapter<ClosetAdapter.ViewHolder> {
    private Context context;
    private List<ClothingPost> posts;
    private FragmentManager fragmentManager;

    /**
     * This constructor creates a new instance of a ClosetAdapter.
     *
     * @param context         the current context of the Closet view
     * @param posts           a list of all the {@link ClothingPost} to be bound onto the screen
     * @param fragmentManager handles transactions between fragments
     */
    public ClosetAdapter(Context context, List<ClothingPost> posts, FragmentManager fragmentManager) {
        this.context = context;
        this.posts = posts;
        this.fragmentManager = fragmentManager;
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
        private TextView dateCreated;
        private TextView subcat;
        private String relativeDate;

        /**
         * {@link androidx.recyclerview.widget.RecyclerView.ViewHolder#ViewHolder(android.view.View)}
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImg = itemView.findViewById(R.id.itemImg);
            brand = itemView.findViewById(R.id.brand);
            itemName = itemView.findViewById(R.id.itemName);
            dateCreated = itemView.findViewById(R.id.dateCreated);
            subcat = itemView.findViewById(R.id.subcat);
        }

        /**
         * Binds each {@link ClothingPost} to the adapter along with its details. Also sets a
         * listener to navigate to {@link TaggedActivity} when an item in the closet is selected.
         *
         * @param post ClothingPost that needs to be bound
         */
        public void bind(final ClothingPost post) {
            // Bind the post data to the view elements
            brand.setText(post.getBrand());
            itemName.setText(post.getName());
            subcat.setText(post.getSubcategory());
            final ParseFile image = post.getImage();
            GlideApp.with(context).load(image.getUrl()).into(itemImg);
            relativeDate = DateUtils.getRelativeTimeSpanString(post.getCreatedAt().getTime()) + "";
            dateCreated.setText("Added " + relativeDate);
            itemImg.setOnClickListener(view -> Navigation.goTaggedActivity(context, post.getFit()));
        }
    }
}