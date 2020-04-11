package com.peruzal.newsassistant.wakthroughActivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.peruzal.newsassistant.R;
import com.peruzal.newsassistant.data.models.Source;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SourcesAdapter extends
        RecyclerView.Adapter<SourcesAdapter.ViewHolder> {

    private static final String TAG = SourcesAdapter.class.getSimpleName();

    private static List<Source> list;
    private OnItemClickListener onItemClickListener;

    public SourcesAdapter(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        list = new ArrayList<>();
    }

    void addData(List<Source> sources) {
        list.addAll(sources);
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_item_source_title)
        TextView title;
        @BindView(R.id.txt_item_source_description)
        TextView description;
        @BindView(R.id.txt_source_details)
        TextView id;
        @BindView(R.id.item_source_check)
        CheckBox checkBox;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(final Source model, final OnItemClickListener listener) {
            itemView.setOnClickListener(v -> {
                model.setChecked(!model.isChecked());
                listener.onItemClick(model);
                checkBox.setChecked(!checkBox.isChecked());
            });
            title.setText(model.name);
            description.setText(model.description);
            id.setText(new Locale(model.language, model.country).getDisplayName());
            checkBox.setChecked(model.isChecked());
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_source, parent, false);
        ButterKnife.bind(this, view);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Source item = list.get(position);
        holder.bind(item, onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnItemClickListener {
        void onItemClick(Source source);
    }

}