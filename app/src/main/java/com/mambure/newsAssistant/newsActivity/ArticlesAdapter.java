package com.mambure.newsAssistant.newsActivity;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mambure.newsAssistant.R;
import com.mambure.newsAssistant.data.models.Article;

import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArticlesAdapter extends
        RecyclerView.Adapter<ArticlesAdapter.ViewHolder> {

    private static final String TAG = ArticlesAdapter.class.getSimpleName();
    private static List<Article> articles;
    private OnItemClickListener onItemClickListener;
    private int currentItemPosition;

    public ArticlesAdapter(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        articles = new ArrayList<>();
    }

    void clearData() {
        articles.clear();
        notifyDataSetChanged();
    }

    public void addItems(List<Article> items) {
        if (items == null) {
            return;
        }
        if (articles.isEmpty()) {
            articles = items;
            notifyDataSetChanged();
        } else {
            articles.addAll(items.stream().
                    filter(source -> !articles.contains(source)).collect(Collectors.toList()));
            notifyItemRangeInserted(articles.size(), items.size());
        }
        Log.d(TAG, "Added " + items.size() + " to the adapter");
    }

    void removeItem(Article article) {
        int position = articles.indexOf(article);
        if(articles.remove(article)) notifyItemRemoved(position);
    }

    void addItem(Article article) {
        articles.add(currentItemPosition, article);
        notifyItemInserted(currentItemPosition);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        // Todo Butterknife bindings
        @BindView(R.id.txt_title) TextView title;
        @BindView(R.id.txt_description) TextView description;
        @BindView(R.id.txt_source) TextView source;
        @BindView(R.id.txt_date) TextView date;
        @BindView(R.id.img_article) ImageView image;
        @BindView(R.id.article_menu) ImageView menu;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(final Article model,
                  final OnItemClickListener listener) {
            itemView.setOnClickListener(v -> listener.onItemClick(v, articles.get(getLayoutPosition())));
            menu.setOnClickListener(v -> {
                currentItemPosition = getLayoutPosition();
                listener.onItemClick(v, articles.get(getLayoutPosition()));
            });
            title.setText(model.title);
            description.setText(model.description);
            source.setText(model.source.name);
            date.setText(formatTime(model.publishedAt));
            Glide.with(itemView).load(model.urlToImage).
                    placeholder(R.drawable.ic_placeholder_image_100dp).
                    centerCrop().override(100).into(image);
        }
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_article, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Article item = articles.get(position);
        holder.bind(item, onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, Article item);
    }

    private String formatTime(String time) {
        if (time == null || time.isEmpty()) return "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMM-dd HH:mm");

            Instant instant = time.contains("Z") ?
                    Instant.from(DateTimeFormatter.ISO_INSTANT.parse(time)) :
                    Instant.from(DateTimeFormatter.ISO_OFFSET_DATE_TIME.parse(time));
            LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
            return localDateTime.format(dateTimeFormatter);
        }
        return "";
    }
}