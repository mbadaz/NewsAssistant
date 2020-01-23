package com.mambure.newsassistant.newsActivity;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mambure.newsassistant.R;
import com.mambure.newsassistant.models.Article;
import com.mambure.newsassistant.utils.DateParsingUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;

public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ArticlesViewHolder> {

    public interface ItemMenuClickListener {
        void onItemMenuClick(Article article);
    }

    List<Article> articles;
    ItemMenuClickListener listener;

    public ArticlesAdapter() {
        articles = new ArrayList<>();
    }

    public void setItemMenuClickListener(ItemMenuClickListener listener) {
        this.listener = listener;
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
        if (articles.isEmpty()) {
            articles.addAll(data);
            notifyDataSetChanged();
        } else {
            int count = articles.size();
            articles.addAll(data);
            notifyItemRangeInserted(count-1, data.size());
        }
    }

    public class ArticlesViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView description;
        TextView source;
        TextView publishedAt;
        ImageView image;
        ImageView menu;

        public ArticlesViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.txt_title);
            description = itemView.findViewById(R.id.txt_description);
            source = itemView.findViewById(R.id.txt_source);
            publishedAt = itemView.findViewById(R.id.txt_date);
            image = itemView.findViewById(R.id.img_article);
            menu = itemView.findViewById(R.id.article_item_menu);

        }

        public void bind(Article article) {
            title.setText(article.title);
            description.setText(article.description);
            source.setText(article.source.name);
            publishedAt.setText(DateParsingUtils.parseDate(article.publishedAt));
            if (article.urlToImage != null) {
                Glide.with(image).
                        load(article.urlToImage).
                        placeholder(image.getContext().getResources().getDrawable(R.drawable.circle_blue)).
                        into(image);
            } else {
                image.setImageDrawable(image.getContext().getResources().getDrawable(R.drawable.circle_blue));
            }

            menu.setOnClickListener(v -> {
                PopupMenu popupMenu = new PopupMenu(v.getContext(), v, Gravity.BOTTOM);
                popupMenu.inflate(R.menu.menu_article);
                popupMenu.setOnMenuItemClickListener(item -> {
                    switch (item.getItemId()) {
                        case R.id.articleMenuSave:
                            listener.onItemMenuClick(article);
                    }
                    return true;
                });

                popupMenu.show();
            });

        }

    }
}
