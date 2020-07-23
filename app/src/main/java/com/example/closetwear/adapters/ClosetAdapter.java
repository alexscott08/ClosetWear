package com.example.closetwear.adapters;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.closetwear.ClothingPost;
import com.example.closetwear.GlideApp;
import com.example.closetwear.R;
import com.example.closetwear.fragments.OutfitsFragment;
import com.parse.ParseFile;

import org.parceler.Parcels;

import java.util.List;

public class ClosetAdapter extends RecyclerView.Adapter<ClosetAdapter.ViewHolder> {
    private Context context;
    private List<ClothingPost> posts;

    public ClosetAdapter(Context context, List<ClothingPost> posts) {
        this.context = context;
        this.posts = posts;
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
        private String relativeDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImg = itemView.findViewById(R.id.itemImg);
            brand = itemView.findViewById(R.id.brand);
            itemName = itemView.findViewById(R.id.itemName);
        }
        public void bind(final ClothingPost post) {
            // Bind the post data to the view elements
            brand.setText(post.getBrand());
            itemName.setText(post.getName());
            final ParseFile image = post.getImage();
            GlideApp.with(context).load(image.getUrl()).into(itemImg);
            relativeDate = DateUtils.getRelativeTimeSpanString(post.getCreatedAt().getTime()) + "";

            itemImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, OutfitsFragment.class);
                    intent.putExtra("KEY_CREATED_KEY", relativeDate);
                    intent.putExtra("KEY_USER", Parcels.wrap(post.getUser()));
                    intent.putExtra("KEY_CATEGORY", post.getCategory());
                    intent.putExtra("KEY_SUBCATEGORY", post.getSubcategory());
                    intent.putExtra("KEY_SIZE", post.getSize());
                    intent.putExtra("KEY_BRAND", post.getBrand());
                    intent.putExtra("KEY_COLOR", post.getColor());
                    intent.putExtra("KEY_NAME", post.getName());
                    intent.putExtra("KEY_IMAGE", Parcels.wrap(image));
                    context.startActivity(intent);
                }
            });
        }
    }
}