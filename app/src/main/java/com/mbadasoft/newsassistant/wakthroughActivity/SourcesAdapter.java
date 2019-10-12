package com.mbadasoft.newsassistant.wakthroughActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mbadasoft.newsassistant.R;
import com.mbadasoft.newsassistant.models.Source;

import java.util.ArrayList;
import java.util.List;

public class SourcesAdapter extends RecyclerView.Adapter<SourcesAdapter.SourcesViewHolder> {
    private List<Source> sources;
    private OnCheckBoxClickListener checkBoxClickListener;

    public SourcesAdapter() {
        sources = new ArrayList<>();
    }

    public void addData(List<Source> sources) {
        this.sources.addAll(sources);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SourcesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.item_source, parent, false);
        return new SourcesViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return sources.size();
    }

    @Override
    public void onBindViewHolder(@NonNull SourcesViewHolder holder, int position) {
        Source source = sources.get(position);
        String detail = source.country.concat(" | ").
                concat(source.language).concat(" | ").concat(source.category);
        holder.title.setText(source.name);
        holder.description.setText(source.description);
        holder.detail.setText(detail);
        holder.checkBox.setOnClickListener(v -> {
            checkBoxClickListener.onCheckBoxClicked((CheckBox)v, source);
        });
        holder.checkBox.setChecked(source.isChecked());

    }

    public class SourcesViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView description;
        public TextView detail;
        public CheckBox checkBox;

        public SourcesViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.txt_item_source_title);
            description = itemView.findViewById(R.id.txt_item_source_description);
            detail = itemView.findViewById(R.id.txt_source_details);
            checkBox = itemView.findViewById(R.id.item_source_check);
        }
    }

    public void setCheckBoxClickListener(OnCheckBoxClickListener checkBoxClickListener) {
        this.checkBoxClickListener = checkBoxClickListener;
    }

    public interface OnCheckBoxClickListener {
        void onCheckBoxClicked(CheckBox checkBox, Source source);
    }


}
