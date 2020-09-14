package com.tj.myandroid;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tj.myandroid.accesbilityservice.AccessbilityActivity;
import com.tj.myandroid.bindservice.BindServiceActivity;
import com.tj.myandroid.clearcache.ClearCacheActivity;
import com.tj.myandroid.download.DownLoadActivity;
import com.tj.myandroid.fullscresendialog.FullScreenDialogActivity;
import com.tj.myandroid.greendao.GreenDaoActivity;
import com.tj.myandroid.kotlin.KotlinActivity;
import com.tj.myandroid.newfunctionalguide.GuideActivity;
import com.tj.myandroid.preferencefragment.SettingsActivity;
import com.tj.myandroid.recyclertest.RecyclerviewActivity;
import com.tj.myandroid.screenprotect.ProtectActivity;
import com.tj.myandroid.screenprotect.ProtectService;
import com.tj.myandroid.shareelement.ShareElementActivity;
import com.tj.myandroid.telphone.TelActivity;
import com.tj.myandroid.timmer.TimmerActivity;
import com.tj.myandroid.video.VideoPlayActivity;
import com.tj.myandroid.wifinew.wifi.WIFIActivity;

public class MainActivity extends AppCompatActivity {
    private Context context;
    private ImageButton menu;
    private Button btnTest;
    private Button btnTest2;
    private GuideViewEasy guideView;
    private GuideViewEasy guideView3;
    private GuideViewEasy guideView2;

    private Button btnTimmer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        getCacheDir();
        ProtectService.initScreenProtect(this);
        menu = (ImageButton) findViewById(R.id.ib_menu);
        btnTest = (Button) findViewById(R.id.btn_test);
        btnTest2 = (Button) findViewById(R.id.btn_test2);
        btnTimmer = findViewById(R.id.btn_timmer);
        btnTimmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, TimmerActivity.class));
            }
        });
        findViewById(R.id.btn_guide).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, GuideActivity.class));
            }
        });
        findViewById(R.id.btn_bind).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, BindServiceActivity.class));
            }
        });
        findViewById(R.id.btn_recycler).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, RecyclerviewActivity.class));
            }
        });
        findViewById(R.id.btn_shareElement).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, ShareElementActivity.class));
            }
        });
        findViewById(R.id.btn_tel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, TelActivity.class));
            }
        });
        findViewById(R.id.btn_accesbility).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, AccessbilityActivity.class));
            }
        });
        findViewById(R.id.btn_settings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, SettingsActivity.class));
            }
        });
        findViewById(R.id.btn_protect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, ProtectActivity.class));
            }
        });
        findViewById(R.id.btn_greendao).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, GreenDaoActivity.class));
            }
        });
        findViewById(R.id.btn_fullscreen_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, FullScreenDialogActivity.class));
            }
        });
        findViewById(R.id.btn_video).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, VideoPlayActivity.class));
            }
        });
        findViewById(R.id.btn_downLoad).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, DownLoadActivity.class));
            }
        });
        findViewById(R.id.btn_kotlin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, KotlinActivity.class));
            }
        });
        findViewById(R.id.btn_wifi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, WIFIActivity.class));
            }
        });
        findViewById(R.id.btn_cache).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, ClearCacheActivity.class));
            }
        });
        findViewById(R.id.btn_setview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, ClearCacheActivity.class));
            }
        });

    }

    private void setGuideView() {

        // 使用图片
        final ImageView iv = new ImageView(this);
        iv.setImageResource(R.drawable.img_new_task_guide);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        iv.setLayoutParams(params);

        // 使用文字
        TextView tv = new TextView(this);
        tv.setText("欢迎使用");
        tv.setTextColor(getResources().getColor(R.color.white));
        tv.setTextSize(30);
        tv.setGravity(Gravity.CENTER);

        // 使用文字
        final TextView tv2 = new TextView(this);
        tv2.setText("欢迎使用2");
        tv2.setTextColor(getResources().getColor(R.color.white));
        tv2.setTextSize(30);
        tv2.setGravity(Gravity.CENTER);


        guideView = GuideViewEasy.Builder
                .newInstance(this)
                .setTargetView(menu)//设置目标
                .setCustomGuideView(iv)
                .setDirction(GuideViewEasy.Direction.LEFT_BOTTOM)
                .setShape(GuideViewEasy.MyShape.CIRCULAR)   // 设置圆形显示区域，
                .setBgColor(getResources().getColor(R.color.shadow))
                .setOnclickListener(new GuideViewEasy.OnClickCallback() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onClickedGuideView() {
                        guideView.hide();
                        guideView2.show();
                    }
                })
                .build();


        guideView2 = GuideViewEasy.Builder
                .newInstance(this)
                .setTargetView(btnTest)
                .setCustomGuideView(tv)
                .setDirction(GuideViewEasy.Direction.LEFT_BOTTOM)
                .setShape(GuideViewEasy.MyShape.ELLIPSE)   // 设置椭圆形显示区域，
                .setBgColor(getResources().getColor(R.color.shadow))
                .setOnclickListener(new GuideViewEasy.OnClickCallback() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onClickedGuideView() {
                        guideView2.hide();
                        guideView3.show();
                    }
                })
                .build();


        guideView3 = GuideViewEasy.Builder
                .newInstance(this)
                .setTargetView(btnTest2)
                .setCustomGuideView(tv2)
                .setDirction(GuideViewEasy.Direction.LEFT_BOTTOM)
                .setShape(GuideViewEasy.MyShape.RECTANGULAR)   // 设置矩形显示区域，
                .setRadius(80)          // 设置圆形或矩形透明区域半径，默认是targetView的显示矩形的半径，如果是矩形，这里是设置矩形圆角大小
                .setBgColor(getResources().getColor(R.color.shadow))
                .setOnclickListener(new GuideViewEasy.OnClickCallback() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onClickedGuideView() {
                        guideView3.hide();
                        guideView.show();
                    }
                })
                .build();

        guideView.show();
    }

    public void hello(){
        Toast.makeText(this,"hello",Toast.LENGTH_SHORT).show();
        Log.e("tj","hello");
    }
    @Override
    protected void onResume() {
        super.onResume();
//        setGuideView();
}

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }
}
