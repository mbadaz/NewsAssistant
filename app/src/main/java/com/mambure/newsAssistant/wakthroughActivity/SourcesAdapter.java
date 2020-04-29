package com.mambure.newsAssistant.wakthroughActivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mambure.newsAssistant.R;
import com.mambure.newsAssistant.data.models.Source;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SourcesAdapter extends
        RecyclerView.Adapter<SourcesAdapter.ViewHolder> {

    private static final String TAG = SourcesAdapter.class.getSimpleName();

    private static List<Source> unFilteredSourcesList;
    private OnItemClickListener onItemClickListener;
    private static Filterable filterable;
    private List<Source> filteredSourcesList;

    SourcesAdapter(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        unFilteredSourcesList = new ArrayList<>();
        filterable = createFilterable();
    }

    void addData(List<Source> sources) {
        unFilteredSourcesList.addAll(sources);
        getItemsFilter().filter("");
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
                checkBox.setChecked(!checkBox.isChecked());
                listener.onItemClick(model);
            });
            title.setText(model.name);
            description.setText(model.description);
            id.setText(new Locale(model.language, model.country).getDisplayName());
            checkBox.setChecked(model.isChecked());
        }
    }

    @NotNull
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
        Source item = filteredSourcesList.get(position);
        holder.bind(item, onItemClickListener);

    }

    @Override
    public int getItemCount() {
        return filteredSourcesList == null ? 0 : filteredSourcesList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(Source source);
    }

    @SuppressWarnings("unchecked")
    private Filterable createFilterable() {
        return () -> new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Source> filteringList;
                if (constraint.length() == 0) {
                    filteringList = unFilteredSourcesList;
                }else {
                    filteringList = unFilteredSourcesList.stream().
                            filter(source -> source.name.contains(constraint)).
                            collect(Collectors.toList());
                }
                FilterResults results = new FilterResults();
                results.count = filteringList.size();
                results.values = filteringList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredSourcesList = (List<Source>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    Filter getItemsFilter() {
        return filterable.getFilter();
    }

}