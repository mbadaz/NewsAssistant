package com.mbadasoft.newsassistant.wakthroughActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import com.mbadasoft.newsassistant.R;
import com.mbadasoft.newsassistant.models.Category;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WalkthroughFragment2 extends Fragment implements AdapterView.OnItemClickListener {
    private static final String TAG = WalkthroughFragment2.class.getSimpleName();
    private WalkthroughActivityViewModel viewModel;
    @BindView(R.id.tableLayout) TableLayout tableLayout;
    @BindView(R.id.list_walkthrough_category) ListView categoriesList;
    @BindView(R.id.linearLayout_row1) LinearLayout row1LinearLayout;
    @BindView(R.id.linearLayout_row2) LinearLayout row2LinearLayout;
    @BindView(R.id.linearLayout_row3) LinearLayout row3LinearLayout;
    private TableRow currentRow = null;
    private TableRow nextRow = null;
    private int rowPosition = 0;
    private CategoriesAdapter adapter;
    private List<Category> categories = new ArrayList<>();
    private Map<String, CategoryTagViewHolder> viewMap = new HashMap<>();

    public WalkthroughFragment2() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context context = getContext();

        if (context != null) {
            String[] cats = context.getResources().getStringArray(R.array.categories);
            for (String cat : cats) {
                Category category = new Category();
                category.title = cat;
                category.isSelected = false;
                categories.add(category);
            }
            adapter = new CategoriesAdapter(context, categories);
        }

        viewModel = ViewModelProviders.of(getActivity()).get(WalkthroughActivityViewModel.class);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_walkthrough2, container, false);

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
        Category category = adapter.getItem(position);

        if (category.isSelected) {
            category.isSelected = false;
            viewModel.removeCategoryToSelected(category);
            adapter.notifyDataSetChanged();
            removeItemFromTable(category.title);
        } else {
            category.isSelected = true;
            viewModel.addCategoryToSelection(category);
            adapter.notifyDataSetChanged();
            if (!viewMap.containsKey(category.title)) {
                addItemToTable(view, category.title);
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
        viewMap.put(item, new CategoryTagViewHolder(child, currentRow));
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

        CategoryTagViewHolder tagViewHolder = viewMap.remove(item);
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


