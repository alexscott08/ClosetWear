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

public class NewOutfitAdapter extends RecyclerView.Adapter<NewOutfitAdapter.ViewHolder> {
    private Context context;
    private List<ClothingPost> posts;

    public NewOutfitAdapter(Context context, List<ClothingPost> posts) {
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
    public NewOutfitAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_closet, parent, false);
        return new NewOutfitAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewOutfitAdapter.ViewHolder holder, int position) {
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
        }
    }
}
