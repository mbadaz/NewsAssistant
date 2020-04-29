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

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArticlesAdapter extends
        RecyclerView.Adapter<ArticlesAdapter.ViewHolder> {

    private static final String TAG = ArticlesAdapter.class.getSimpleName();
    private static List<Article> list;
    private OnItemClickListener onItemClickListener;

    public ArticlesAdapter(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        list = new ArrayList<>();
    }

    public void addItems(List<Article> items) {
        if (items == null) {
            return;
        }
        if (list.isEmpty()) {
            list = items;
            notifyDataSetChanged();
        } else {
            list.addAll(items);
            notifyItemRangeInserted(list.size(), items.size());
        }
        Log.d(TAG, "Added " + items.size() + " to the adapter");

    }


    class ViewHolder extends RecyclerView.ViewHolder {
        // Todo Butterknife bindings
        @BindView(R.id.txt_title) TextView title;
        @BindView(R.id.txt_description) TextView description;
        @BindView(R.id.txt_source) TextView source;
        @BindView(R.id.txt_date) TextView date;
        @BindView(R.id.img_article) ImageView image;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(final Article model,
                  final OnItemClickListener listener) {
            itemView.setOnClickListener(v -> listener.onItemClick(getLayoutPosition()));
            title.setText(model.title);
            description.setText(model.description);
            source.setText(model.source.name);
            date.setText(formatTime(model.publishedAt));
            Glide.with(itemView).load(model.urlToImage).centerCrop().override(100).into(image);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_article, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Article item = list.get(position);
        holder.bind(item, onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private String formatTime(String time) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMM-dd HH:mm");
            Instant instant = Instant.from(DateTimeFormatter.ISO_INSTANT.parse(time));
            LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
            return localDateTime.format(dateTimeFormatter);
        }

        return "";
    }
}