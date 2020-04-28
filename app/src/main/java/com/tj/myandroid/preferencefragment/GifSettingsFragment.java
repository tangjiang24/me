package com.tj.myandroid.preferencefragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.tj.myandroid.R;

public class GifSettingsFragment extends PreferenceFragmentCompat {
    private Context mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity =  context;
    }
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.gif_preferences);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((SettingsActivity)mActivity).setTitle("gif设置");
    }
}
