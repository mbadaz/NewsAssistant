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

public class WalkthroughLanguageSelectionFragment extends Fragment implements AdapterView.OnItemClickListener {
    private static final String TAG = WalkthroughLanguageSelectionFragment.class.getSimpleName();


    private WalkthroughActivityViewModel viewModel;
    @Inject
    public ViewModelsFactory viewModelsFactory;
    @BindView(R.id.tableLayout) TableLayout tableLayout;
    @BindView(R.id.list_walkthrough_category) ListView languagesList;
    private TableRow currentRow = null;
    private TableRow nextRow = null;
    private TagsAdapter adapter;
    private List<Tag> languages = new ArrayList<>();
    private String[] languageCodes;
    private Map<String, TagViewHolder> viewMap = new HashMap<>();

    public WalkthroughLanguageSelectionFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context context = getContext();

        if (context != null) {
            String[] languages = context.getResources().getStringArray(R.array.languages);
            for (String cat : languages) {
                Tag tag = new Tag();
                tag.name = cat;
                tag.isSelected = false;
                this.languages.add(tag);
            }
            adapter = new TagsAdapter(context, this.languages);
        }

        languageCodes = context.getResources().getStringArray(R.array.language_codes);

        ((MyApplication)getActivity().getApplication()).getAppComponent().inject(this);

        viewModel = ViewModelProviders.of(getActivity(), viewModelsFactory).get(WalkthroughActivityViewModel.class);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.languages_fragment_walkthrough, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        languagesList.setAdapter(adapter);
        languagesList.setOnItemClickListener(this);


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Tag tag = adapter.getItem(position);

        if (tag.isSelected) {
            tag.isSelected = false;
            viewModel.removeLanguageFromSelection(languageCodes[position]);
            adapter.notifyDataSetChanged();
            removeItemFromTable(tag.name);
        } else {
            tag.isSelected = true;
            viewModel.addLanguageToSelection(languageCodes[position]);
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


