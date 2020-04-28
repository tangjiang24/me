package com.tj.myandroid.preferencefragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.Log;
import android.view.View;

import com.tj.myandroid.R;

public class MainSettingsFragment extends PreferenceFragmentCompat {
    private Context mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity =  context;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.main_preferences);

        Preference pf_gif_play = findPreference("pf_gif_play");
        pf_gif_play.setIcon(null);
        pf_gif_play.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if(mActivity instanceof SettingsActivity){
                    ((SettingsActivity)mActivity).gotoSettings(SettingsActivity.SETTING_TYPE_GIF);
                }
                return false;
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e("ddd","onviewcreated");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((SettingsActivity)mActivity).setTitle("设置");
        Log.e("ddd","onactivitycreated");
    }

    @Override
    public void setDivider(Drawable divider) {
        super.setDivider(new ColorDrawable(Color.BLACK));
    }

    @Override
    public void setDividerHeight(int height) {
        super.setDividerHeight(4);
    }
}
