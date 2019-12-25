package com.mambure.newsassistant.wakthroughActivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mambure.newsassistant.R;
import com.mambure.newsassistant.models.Tag;

import java.util.List;

public class TagsAdapter extends ArrayAdapter<Tag> {

    public TagsAdapter(@NonNull Context context, @NonNull List<Tag> categories) {
        super(context, 0, categories);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Tag tag = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).
                    inflate(R.layout.item_category_layout, parent, false);
        }

        TextView textView = convertView.findViewById(R.id.txt_category_title);
        View view = convertView.findViewById(R.id.view_category_selection_indicator);

        textView.setText(tag.name);
        if (tag.isSelected) {
            view.setBackground(getContext().getResources().getDrawable(R.drawable.ic_radio_button_checked_black_24dp));
        } else {
            view.setBackground(getContext().getResources().getDrawable(R.drawable.ic_radio_button_unchecked_black_24dp));
        }

        return convertView;
    }
}
