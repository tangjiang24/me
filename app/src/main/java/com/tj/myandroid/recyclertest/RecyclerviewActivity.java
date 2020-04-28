package com.tj.myandroid.recyclertest;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.PopupWindowCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tj.myandroid.R;

import java.util.ArrayList;
import java.util.List;

public class RecyclerviewActivity extends AppCompatActivity {
    private static final String TAG = "RecyclerviewActivity";
    Button btnChange;
    Button btnScroll;
    RecyclerView recyclerView;
    LinearAdapter linearAdapter;
    GrideAdapter grideAdapter;
    List<AppBean> appBeans = new ArrayList<>();
    boolean isLinear = true;
    Toolbar  toolbar;
    AppEditPopup popup;
    View shadow;
    int showType = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview);
        recyclerView = findViewById(R.id.rc);
        btnChange = findViewById(R.id.btn_change);
        toolbar = findViewById(R.id.toolbar);
        shadow = findViewById(R.id.shadow);
        btnScroll = findViewById(R.id.btn_scroll);

        setSupportActionBar(toolbar);
        setTitle("recycler");
        appBeans.add(new AppBean("tengxun","聊天的"));
        appBeans.add(new AppBean("wechat","熟人聊天的"));
        appBeans.add(new AppBean("抖音","看视频的"));
        appBeans.add(new AppBean("头条","看新闻的"));
        appBeans.add(new AppBean("斗鱼","看直播的"));
        appBeans.add(new AppBean("tengxun","聊天的"));
        appBeans.add(new AppBean("wechat","熟人聊天的"));
        appBeans.add(new AppBean("抖音","看视频的"));
        appBeans.add(new AppBean("头条","看新闻的"));
        appBeans.add(new AppBean("斗鱼","看直播的"));
        appBeans.add(new AppBean("tengxun","聊天的"));
        appBeans.add(new AppBean("wechat","熟人聊天的"));
        appBeans.add(new AppBean("抖音","看视频的"));
        appBeans.add(new AppBean("头条","看新闻的"));
        appBeans.add(new AppBean("斗鱼","看直播的"));
        appBeans.add(new AppBean("tengxun","聊天的"));
        appBeans.add(new AppBean("wechat","熟人聊天的"));
        appBeans.add(new AppBean("抖音","看视频的"));
        appBeans.add(new AppBean("头条","看新闻的"));
        appBeans.add(new AppBean("斗鱼","看直播的"));
        appBeans.add(new AppBean("tengxun","聊天的"));
        appBeans.add(new AppBean("wechat","熟人聊天的"));
        appBeans.add(new AppBean("抖音","看视频的"));
        appBeans.add(new AppBean("头条","看新闻的"));
        appBeans.add(new AppBean("斗鱼","看直播的"));
        appBeans.add(new AppBean("tengxun","聊天的"));
        appBeans.add(new AppBean("wechat","熟人聊天的"));
        appBeans.add(new AppBean("抖音","看视频的"));
        appBeans.add(new AppBean("头条","看新闻的"));
        appBeans.add(new AppBean("斗鱼","看直播的"));
        linearAdapter = new LinearAdapter(R.layout.rv_linear_item,appBeans);
        linearAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(RecyclerviewActivity.this,"linearItemClick",Toast.LENGTH_SHORT).show();
            }
        });

        linearAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, final View view, int position) {
                popup = new AppEditPopup(RecyclerviewActivity.this);
                View contentView = popup.getContentView();
                //需要先测量，PopupWindow还未弹出时，宽高为0
                contentView.measure(makeDropDownMeasureSpec(popup.getWidth()),
                        makeDropDownMeasureSpec(popup.getHeight()));
                int width = contentView.getMeasuredWidth();
                int height = contentView.getMeasuredHeight();
                Log.e(TAG,"width="+width+"----height="+height);
                Log.e(TAG,"POPUP H:"+view.getHeight());
                Log.e(TAG,"ITEM H:"+view.getBottom());
                Log.e(TAG,"recyclerViewH:"+ recyclerView.getBottom());
                int[] location = new int[2];
                view.getLocationOnScreen(location);
                Log.e(TAG,"ITEM H:"+"locationX="+location[0]+"----lacationY="+location[1]);

                int[] rv = new int[2];
                recyclerView.getLocationOnScreen(rv);
                Log.e(TAG,"rv :"+"rvX="+rv[0]+"----rVY="+rv[1]+"RVh="+recyclerView.getHeight());

                int offX = (int) x;
                int offY = 0;
                if(location[1] + view.getHeight() + +height > rv[1]+recyclerView.getHeight()){
                    offY = -(view.getHeight()+height);
                }
                if(x + width > view.getWidth()){
                    offX = (int) (x - width);
                }
                PopupWindowCompat.showAsDropDown(popup,view,offX,offY, Gravity.START);
                shadow.setVisibility(View.VISIBLE);
                view.setBackgroundColor(getColor(R.color.trans));
                view.setElevation(4F);
//                popup.setmAlpha(0.7F);
//                popup.showBackgroundAnimator();

                popup.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        shadow.setVisibility(View.GONE);
                        view.setBackgroundColor(getColor(R.color.white));
                        view.setElevation(0f);
                    }
                });


//                ItemDialg dialg = new ItemDialg(RecyclerviewActivity.this);
//                dialg.setPosition(offX,offY);
//                dialg.show(getSupportFragmentManager(),"");
                return true;
            }
        });
        grideAdapter = new GrideAdapter(R.layout.rv_linear_item,appBeans);
        grideAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(RecyclerviewActivity.this,"grideItemClick",Toast.LENGTH_SHORT).show();
            }
        });

        shadow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isShowing()){
                    popup.dismiss();
                }
            }
        });

      shadow.setOnTouchListener(new View.OnTouchListener() {
          float x = 0;
          float y = 0;
          long time = 0;
          @Override
          public boolean onTouch(View v, MotionEvent event) {
              switch (event.getAction()){
                 case MotionEvent.ACTION_DOWN:
                     x=event.getX();
                     y = event.getY();
                     time = System.currentTimeMillis();
                     return false;
                     case MotionEvent.ACTION_UP:
                         Log.e(TAG,"x="+(x - event.getX())+"=======y="+(y-event.getY())+"time="+(System.currentTimeMillis() - time));
                         if (Math.abs(x - event.getX())<2 && Math.abs(y-event.getY())<2 && System.currentTimeMillis() - time<300) {
                             return false;
                         }else {
                             return true;
                         }
              }
              return false;
          }
      });
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLinear = !isLinear;
                initLayoutManager();
            }
        });

        btnScroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.scrollBy(0,30);
            }
        });

        findViewById(R.id.btn_go).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RecyclerviewActivity.this,FullItemActivity.class));
            }
        });
        initLayoutManager();
    }

    float x;
    float y;
    float rawX;
    float rawY;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                x = event.getX();
                y = event.getY();
                rawX = event.getRawX();
                rawY = event.getRawY();
                Log.e(TAG,"X="+x+"-----y="+y);
                Log.e(TAG,"rawX="+rawX+"-----rawy="+rawY);
        }
        return super.dispatchTouchEvent(event);
    }


    private void initLayoutManager() {
        if(isLinear){
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(linearAdapter);
        }else {
            recyclerView.setLayoutManager(new GridLayoutManager(this,2));
            recyclerView.setAdapter(grideAdapter);
        }
    }


    @SuppressWarnings("ResourceType")
    private static int makeDropDownMeasureSpec(int measureSpec) {
        int mode;
        if (measureSpec == ViewGroup.LayoutParams.WRAP_CONTENT) {
            mode = View.MeasureSpec.UNSPECIFIED;
        } else {
            mode = View.MeasureSpec.EXACTLY;
        }
        return View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(measureSpec), mode);
    }

    public boolean isShowing(){
        if(popup!=null && popup.isShowing()){
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if(isShowing()){
            popup.dismiss();
        }else {
            super.onBackPressed();
        }
    }
}
