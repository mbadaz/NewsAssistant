package com.peruzal.newsassistant.newsActivity;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class NewsActivityViewModel extends AndroidViewModel {
    private static final String TAG = NewsActivityViewModel.class.getSimpleName();

    public NewsActivityViewModel(@NonNull Application application) {
        super(application);
    }

}
