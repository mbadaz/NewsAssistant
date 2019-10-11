package com.mbadasoft.newsassistant.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mbadasoft.newsassistant.R;
import com.mbadasoft.newsassistant.models.Article;
import com.mbadasoft.newsassistant.models.Source;
import com.mbadasoft.newsassistant.utils.DateParsingUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ArticlesViewHolder> {

    List<Article> articles;

    public ArticlesAdapter() {
        articles = new ArrayList<>();
    }

    @NonNull
    @Override
    public ArticlesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.item_article, parent, false);
        return new ArticlesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticlesViewHolder holder, int position) {
        holder.bind(articles.get(position));
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public void addData(List<Article> data) {
        articles.addAll(data);
        notifyDataSetChanged();
    }

    public class ArticlesViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView description;
        TextView source;
        TextView publishedAt;
        ImageView image;

        public ArticlesViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.txt_title);
            description = itemView.findViewById(R.id.txt_description);
            source = itemView.findViewById(R.id.txt_source);
            publishedAt = itemView.findViewById(R.id.txt_date);
            image = itemView.findViewById(R.id.img_article);
        }

        public void bind(Article article) {
            title.setText(article.title);
            description.setText(article.description);
            source.setText(article.source.name);
            publishedAt.setText(DateParsingUtils.parseDate(article.publishedAt));
            if (!article.urlToImage.isEmpty()) {
                Picasso.get().load(article.urlToImage).
                        placeholder(R.drawable.ic_launcher_background).into(image);
            }
        }
    }
}
