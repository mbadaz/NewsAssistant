package com.mambure.newsAssistant.dependencyInjection;

import android.content.SharedPreferences;

import androidx.annotation.Nullable;

import java.util.Map;
import java.util.Set;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class SharedPreferenceModule {
    @Provides
    @Singleton
    SharedPreferences providesSharedPreferences() {
        return new SharedPreferences() {
            @Override
            public Map<String, ?> getAll() {
                return null;
            }

            @Nullable
            @Override
            public String getString(String key, @Nullable String defValue) {
                return null;
            }

            @Nullable
            @Override
            public Set<String> getStringSet(String key, @Nullable Set<String> defValues) {
                return null;
            }

            @Override
            public int getInt(String key, int defValue) {
                return 0;
            }

            @Override
            public long getLong(String key, long defValue) {
                return 0;
            }

            @Override
            public float getFloat(String key, float defValue) {
                return 0;
            }

            @Override
            public boolean getBoolean(String key, boolean defValue) {
                return true;
            }

            @Override
            public boolean contains(String key) {
                return false;
            }

            @Override
            public Editor edit() {
                return new Editor() {
                    @Override
                    public Editor putString(String key, @Nullable String value) {
                        return this;
                    }

                    @Override
                    public Editor putStringSet(String key, @Nullable Set<String> values) {
                        return this;
                    }

                    @Override
                    public Editor putInt(String key, int value) {
                        return this;
                    }

                    @Override
                    public Editor putLong(String key, long value) {
                        return this;
                    }

                    @Override
                    public Editor putFloat(String key, float value) {
                        return this;
                    }

                    @Override
                    public Editor putBoolean(String key, boolean value) {
                        return this;
                    }

                    @Override
                    public Editor remove(String key) {
                        return this;
                    }

                    @Override
                    public Editor clear() {
                        return this;
                    }

                    @Override
                    public boolean commit() {
                        return false;
                    }

                    @Override
                    public void apply() {

                    }
                };
            }

            @Override
            public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {

            }

            @Override
            public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {

            }
        };
    }
}
