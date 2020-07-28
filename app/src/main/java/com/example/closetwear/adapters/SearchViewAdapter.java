package com.example.closetwear.adapters;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.closetwear.GlideApp;
import com.example.closetwear.OutfitDetailsActivity;
import com.example.closetwear.OutfitPost;
import com.example.closetwear.R;
import com.parse.ParseFile;

import org.parceler.Parcels;

import java.util.List;

public class SearchViewAdapter extends RecyclerView.Adapter<SearchViewAdapter.ViewHolder> {
        private Context context;
        private List<OutfitPost> posts;

    public SearchViewAdapter(Context context, List<OutfitPost> posts) {
            this.context = context;
            this.posts = posts;
        }

        // Clean all elements of the recycler
        public void clear() {
            posts.clear();
            notifyDataSetChanged();
        }

        // Add a list of OutfitPost
        public void addAll(List<OutfitPost> list) {
            posts.addAll(list);
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public SearchViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_outfits, parent, false);
            return new SearchViewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            OutfitPost post = posts.get(position);
            holder.bind(post);
        }

        @Override
        public int getItemCount() {
            return posts.size();
        }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView outfitImg;
        private String relativeDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            outfitImg = itemView.findViewById(R.id.outfitImg);
        }

        public void bind(final OutfitPost post) {
            // Bind the post data to the view elements
            final ParseFile image = post.getImage();
            GlideApp.with(context).load(image.getUrl()).into(outfitImg);
            relativeDate = DateUtils.getRelativeTimeSpanString(post.getCreatedAt().getTime()) + "";
            outfitImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, OutfitDetailsActivity.class);
                    intent.putExtra("KEY_CREATED_KEY", relativeDate);
                    intent.putExtra("KEY_USER", Parcels.wrap(post.getUser()));
                    intent.putExtra("KEY_COMMENTS", post.getCommentsCount());
                    intent.putExtra("KEY_CAPTION", post.getCaption());
                    intent.putExtra("KEY_LIKES", post.getLikesCount());
                    intent.putExtra("KEY_IMAGE", Parcels.wrap(image));
                    intent.putExtra("KEY_ITEMS", Parcels.wrap(post.getFitItems()));
                    context.startActivity(intent);
                }
            });
        }
    }
}
