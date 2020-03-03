package com.peruzal.newsassistant.wakthroughActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.peruzal.newsassistant.R;
import com.peruzal.newsassistant.models.Source;

import java.util.ArrayList;
import java.util.List;

public class SourcesAdapter extends RecyclerView.Adapter<SourcesAdapter.SourceViewHolder> {

    private List<Source> sources;
    private OnListItemClickListener listItemClick;

    public SourcesAdapter(OnListItemClickListener listener) {
        sources = new ArrayList<>();
        listItemClick = listener;
    }

    public void addData(List<Source> sources) {
        this.sources = sources;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SourceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.item_source, parent, false);

        return new SourceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SourceViewHolder holder, int position) {
        Source source = sources.get(position);
        holder.title.setText(source.name);
        holder.description.setText(source.description);
        holder.source.setText(source.id);
        holder.layout.setOnClickListener(v -> listItemClick.onItemClick(source.id));
    }

    @Override
    public int getItemCount() {
        if (sources == null) {
            return 0;
        }
        return sources.size();
    }

    public class SourceViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView source;
        TextView description;
        CheckBox checkBox;
        View layout;

        public SourceViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView;
            title = itemView.findViewById(R.id.txt_item_source_title);
            source = itemView.findViewById(R.id.txt_source_details);
            description = itemView.findViewById(R.id.txt_item_source_description);
            checkBox = itemView.findViewById(R.id.item_source_check);
        }
    }

    public interface OnListItemClickListener {
        void onItemClick(String sourceId);
    }


}
