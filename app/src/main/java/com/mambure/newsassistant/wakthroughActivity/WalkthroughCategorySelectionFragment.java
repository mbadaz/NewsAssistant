package com.mambure.newsassistant.wakthroughActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.mambure.newsassistant.MyApplication;
import com.mambure.newsassistant.R;
import com.mambure.newsassistant.dependencyInjection.ViewModelsFactory;
import com.mambure.newsassistant.models.Tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WalkthroughCategorySelectionFragment extends Fragment implements AdapterView.OnItemClickListener {
    private static final String TAG = WalkthroughCategorySelectionFragment.class.getSimpleName();


    public WalkthroughActivityViewModel viewModel;
    @Inject
    public ViewModelsFactory viewModelsFactory;
    @BindView(R.id.tableLayout) TableLayout tableLayout;
    @BindView(R.id.list_walkthrough_category) ListView categoriesList;
    private TableRow currentRow = null;
    private TableRow nextRow = null;
    private TagsAdapter adapter;
    private List<Tag> categories = new ArrayList<>();
    private Map<String, TagViewHolder> viewMap = new HashMap<>();

    public WalkthroughCategorySelectionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context context = getContext();

        if (context != null) {
            String[] cats = context.getResources().getStringArray(R.array.categories);
            for (String cat : cats) {
                Tag tag = new Tag();
                tag.name = cat;
                tag.isSelected = false;
                categories.add(tag);
            }
            adapter = new TagsAdapter(context, categories);
        }

        ((MyApplication)getActivity().getApplication()).getAppComponent().inject(this);

        viewModel = ViewModelProviders.of(getActivity(), viewModelsFactory).get(WalkthroughActivityViewModel.class);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.categories_fragment_walkthrough, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        categoriesList.setAdapter(adapter);
        categoriesList.setOnItemClickListener(this);


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Tag tag = adapter.getItem(position);

        if (tag.isSelected) {
            tag.isSelected = false;
            viewModel.removeCategoryToSelected(tag);
            adapter.notifyDataSetChanged();
            removeItemFromTable(tag.name);
        } else {
            tag.isSelected = true;
            viewModel.addCategoryToSelection(tag);
            adapter.notifyDataSetChanged();
            if (!viewMap.containsKey(tag.name)) {
                addItemToTable(view, tag.name);
            }
        }

    }

    private void addItemToTable(View view, String item) {

        if (currentRow == null || currentRow.getChildCount()== 3) {

            if (nextRow == null || nextRow.getChildCount() == 3) {
                currentRow = new TableRow(view.getContext());
                currentRow.setMeasureWithLargestChildEnabled(false);
                tableLayout.addView(currentRow);
            } else {
                currentRow = nextRow;
                nextRow = null;
            }
        }

        TextView child = new TextView(view.getContext());
        child.setText(item);
        viewMap.put(item, new TagViewHolder(child, currentRow));
        TableRow.LayoutParams params = new TableRow.LayoutParams();
        params.setMarginEnd(8);
        params.setMarginStart(8);
        params.setMargins(8, 8, 8, 8);
        params.gravity = Gravity.CENTER;
        params.width = TableRow.LayoutParams.WRAP_CONTENT;
        child.setLayoutParams(params);
        child.setBackground(getResources().getDrawable(R.drawable.item_category_background));
        child.setTextColor(getResources().getColor(R.color.colorBackground_light));
        currentRow.addView(child);
    }

    private void removeItemFromTable(String item) {

        TagViewHolder tagViewHolder = viewMap.remove(item);
        tagViewHolder.tableRow.removeView(tagViewHolder.view);

        if (tagViewHolder.tableRow.getChildCount() == 0) {
            tableLayout.removeView(tagViewHolder.tableRow);
            currentRow = (TableRow) tableLayout.getChildAt(tableLayout.getChildCount()-1);
        } else {
            nextRow = currentRow;
            currentRow = tagViewHolder.tableRow;
        }

    }

}


