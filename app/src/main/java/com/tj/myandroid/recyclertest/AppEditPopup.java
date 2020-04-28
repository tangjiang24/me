package com.tj.myandroid.recyclertest;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tj.myandroid.R;

public class AppEditPopup extends PopupWindow {
    private Context mContext;

    public void setmAlpha(float mAlpha) {
        this.mAlpha = mAlpha;
    }

    private float mAlpha = 1f; //背景灰度  0-1  1表示全透明
    @TargetApi(Build.VERSION_CODES.M)
    public AppEditPopup(Context context) {
        super(context);
        this.mContext = context;
        View rootView = initRootView();
        setContentView(rootView);
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        setOutsideTouchable(false);
//        setOnDismissListener(new OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                mAlpha = 1;
//                setWindowBackgroundAlpha(mAlpha);
//            }
//        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private View initRootView() {
        LinearLayout linearLayout = new LinearLayout(mContext);
        linearLayout.removeAllViews();
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setBackgroundColor(mContext.getColor(R.color.white));
//        linearLayout.setElevation(4);
        for(int i = 0; i<5;i++){
            TextView textView = new TextView(mContext);
            textView.setText("menu:"+i);
            textView.setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            textView.setTextColor(Color.RED);
            textView.setTextSize(25);
            linearLayout.addView(textView);
        }
        return linearLayout;
    }


    /**
     * 控制窗口背景的不透明度
     */
    private void setWindowBackgroundAlpha(float alpha) {
        if (mContext == null) return;
        if (mContext instanceof Activity) {
            Window window = ((Activity) mContext).getWindow();
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.alpha = alpha;
            window.setAttributes(layoutParams);
        }
    }

    /**
     * 窗口显示，窗口背景透明度渐变动画
     */
    public void showBackgroundAnimator() {
        if (mAlpha >= 1f) return;
        ValueAnimator animator = ValueAnimator.ofFloat(1.0f, mAlpha);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float alpha = (float) animation.getAnimatedValue();
                setWindowBackgroundAlpha(alpha);
            }
        });
        animator.setDuration(360);
        animator.start();
    }
}
