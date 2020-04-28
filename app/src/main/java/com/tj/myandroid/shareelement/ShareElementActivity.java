package com.tj.myandroid.shareelement;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.tj.myandroid.R;

public class ShareElementActivity extends AppCompatActivity {
    private static final int REQUEST_SEARCH = 100;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_element);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("shareElement");
        toolbar.setBackgroundColor(ContextCompat.getColor(this,R.color.colorPrimary));
        toolbar.setNavigationIcon(R.drawable.alarm);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.action_search:
                        jump();
                        break;
                }
                return true;
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share_element_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void jump() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { // Android 5.0版本启用transition动画会存在一些效果上的异常，因此这里只在Android 5.1以上启用此动画
            View search = toolbar.findViewById(R.id.action_search);
            Bundle options = ActivityOptions.makeSceneTransitionAnimation(this, search,
                   "search").toBundle();
            startActivityForResult(new Intent(this, ShareBActivity.class), REQUEST_SEARCH, options);
        } else {
            startActivityForResult(new Intent(this, ShareBActivity.class), REQUEST_SEARCH);
        }
    }
}
