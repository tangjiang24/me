package com.tj.myandroid.preferencefragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.tj.myandroid.R;

public class SettingsActivity extends AppCompatActivity {

    public static final int SETTING_TYPE_MAIN = 0;
    public static final int SETTING_TYPE_GIF = 1;
    public static final String INTENT_SETTING_TYPE = "intent.setting.type";
    int settingTypes;
    private Fragment fragment;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setupToolbar();
        settingTypes = getIntent().getIntExtra(INTENT_SETTING_TYPE,SETTING_TYPE_MAIN);
        gotoSettings(settingTypes);
    }

    protected void setupToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void gotoSettings(int settingTypes) {
        if(settingTypes == SETTING_TYPE_MAIN){
            fragment = new MainSettingsFragment();
        }else if(settingTypes == SETTING_TYPE_GIF){
            fragment = new GifSettingsFragment();
        }
        if(fragment!=null){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.settingsFragmentLayout,fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        int fragmentCounts = getSupportFragmentManager().getBackStackEntryCount();
        if(fragmentCounts==1){
           finish();
        }else {
            if (fragmentCounts > 1) {
                getSupportFragmentManager().popBackStack();
            } else {
                finish();
            }
        }
    }

}
