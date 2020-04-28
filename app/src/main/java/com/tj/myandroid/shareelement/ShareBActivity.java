package com.tj.myandroid.shareelement;

import android.annotation.TargetApi;
import android.app.SharedElementCallback;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.tj.myandroid.R;

import java.util.List;

public class ShareBActivity extends AppCompatActivity {
    Toolbar toolbar;
    SearchView searchView;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_b);

        initToolBar();
        initTransition();

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        toolbar.setBackgroundColor(ContextCompat.getColor(this,R.color.colorPrimary));
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("shareB");
        int a = toolbar.getChildCount();
        for(int i = 0 ;i<a;i++){
            View view = toolbar.getChildAt(i);
            Log.e("ddd",view.getId()+"bb");
            if(i==1){
                view.setTransitionName("search");
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finishAfterTransition();
        }
        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAfterTransition();
    }

    private void initTransition() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            setEnterSharedElementCallback(new SharedElementCallback() {
                @Override
                public void onSharedElementStart(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
                    super.onSharedElementStart(sharedElementNames, sharedElements, sharedElementSnapshots);
                    if(sharedElements!=null && sharedElements.size()>0){
                        View search = sharedElements.get(0);
                        if(search.getId() == R.id.action_search){
                            int centerX = (search.getLeft()+search.getRight())/2;
                            //                            val hideResults = TransitionUtils.findTransition(window.returnTransition as TransitionSet, CircularReveal::class.java, R.id.resultsContainer) as CircularReveal?
                            //                            hideResults?.setCenter(Point(centerX, 0))
                        }
                    }
                }
            });
            getWindow().getEnterTransition().addListener(new Transition.TransitionListener() {
                @Override
                public void onTransitionStart(Transition transition) {

                }

                @Override
                public void onTransitionEnd(Transition transition) {
                    View viewById = toolbar.findViewById(R.id.action_search);
                    viewById.performClick();
                }

                @Override
                public void onTransitionCancel(Transition transition) {

                }

                @Override
                public void onTransitionPause(Transition transition) {

                }

                @Override
                public void onTransitionResume(Transition transition) {

                }
            });
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share_b_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        //通过MenuItem得到SearchView
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        return super.onCreateOptionsMenu(menu);
    }
}
