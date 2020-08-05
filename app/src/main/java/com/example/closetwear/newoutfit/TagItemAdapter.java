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

public class TagItemAdapter extends RecyclerView.Adapter<TagItemAdapter.ViewHolder> {
    private Context context;
    private List<ClothingPost> posts;
    private OutfitPost outfitPost;

    public TagItemAdapter(Context context, List<ClothingPost> posts, OutfitPost outfitPost) {
        this.context = context;
        this.posts = posts;
        this.outfitPost = outfitPost;
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
    public TagItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_closet, parent, false);
        return new TagItemAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TagItemAdapter.ViewHolder holder, int position) {
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
            itemImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
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
                }
            });
        }
    }
}
