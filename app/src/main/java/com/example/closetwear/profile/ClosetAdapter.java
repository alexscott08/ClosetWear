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
import com.example.closetwear.parse.ClothingPost;
import com.example.closetwear.GlideApp;
import com.example.closetwear.R;
import com.parse.ParseFile;

import java.util.List;

public class ClosetAdapter extends RecyclerView.Adapter<ClosetAdapter.ViewHolder> {
    private Context context;
    private List<ClothingPost> posts;
    private FragmentManager fragmentManager;

    public ClosetAdapter(Context context, List<ClothingPost> posts, FragmentManager fragmentManager) {
        this.context = context;
        this.posts = posts;
        this.fragmentManager = fragmentManager;
    }

    // Clean all elements of the recycler
    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

    // Add a list of OutfitPost
    public void addAll(List<ClothingPost> list) {
        posts.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_closet, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ClothingPost post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView itemImg;
        private TextView brand;
        private TextView itemName;
        private TextView dateCreated;
        private TextView subcat;
        private String relativeDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImg = itemView.findViewById(R.id.itemImg);
            brand = itemView.findViewById(R.id.brand);
            itemName = itemView.findViewById(R.id.itemName);
            dateCreated = itemView.findViewById(R.id.dateCreated);
            subcat = itemView.findViewById(R.id.subcat);
        }
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