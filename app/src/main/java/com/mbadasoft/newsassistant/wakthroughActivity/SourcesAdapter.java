package com.mbadasoft.newsassistant.wakthroughActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mbadasoft.newsassistant.R;
import com.mbadasoft.newsassistant.models.Source;

import java.util.ArrayList;
import java.util.List;

public class SourcesAdapter extends RecyclerView.Adapter<SourcesAdapter.SourcesViewHolder> {
    private List<Source> sources;
    private List<Source> filteredSources;
    private OnCheckBoxClickListener checkBoxClickListener;

    SourcesAdapter() {
        sources = new ArrayList<>();
        filteredSources = new ArrayList<>();
    }

    void addData(List<Source> sources) {
        this.sources.addAll(sources);
        getFilter().filter("");
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
        return filteredSources.size();
    }

    @Override
    public void onBindViewHolder(@NonNull SourcesViewHolder holder, int position) {
        Source source = filteredSources.get(position);
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

    class SourcesViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView description;
        TextView detail;
        CheckBox checkBox;

        SourcesViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.txt_item_source_title);
            description = itemView.findViewById(R.id.txt_item_source_description);
            detail = itemView.findViewById(R.id.txt_source_details);
            checkBox = itemView.findViewById(R.id.item_source_check);
        }
    }

    void setCheckBoxClickListener(OnCheckBoxClickListener checkBoxClickListener) {
        this.checkBoxClickListener = checkBoxClickListener;
    }

    public interface OnCheckBoxClickListener {
        void onCheckBoxClicked(CheckBox checkBox, Source source);
    }

    Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String filterString = constraint.toString();
                if (filterString.isEmpty()) {
                    filteredSources = sources;
                } else {
                    filteredSources = new ArrayList<>();
                    for (Source source : sources) {
                        if (source.name.toLowerCase().contains(filterString.toLowerCase())) {
                            filteredSources.add(source);
                        }
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredSources;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                    filteredSources = (ArrayList<Source>) results.values;
                    notifyDataSetChanged();
            }
        };
    }


}
