package com.tj.myandroid.newfunctionalguide;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tj.myandroid.R;

public class TransparentActivity extends AppCompatActivity {
    private TextView tvTitle;
    private TextView tvContent;
    private LinearLayout radioGroup;
    private ViewPager viewPager;
    private ImageView ivCancle;
    private ViewPagerAdapter viewPagerAdapter;

    private Drawable mSelectedDrawable;
    private Drawable mUnselectedDrawable;

    private static final String[] titles = new String[]{"首页","定时器","添加食材","蓝牙电话"};
    private static final String[] contents = new String[]{
            "长按拖动可以更改模块顺序", "同时最多可以预约五个场景",
            "点击拖动食材添加到不同温区","手机配对成功，冰箱接电话"};
    private static final int [] images = new int[]{
            R.drawable.gif_main_new_function_guide,
            R.drawable.gif_timmer_new_function_guide,
            R.drawable.gif_addfood_new_function_guide,
            R.drawable.gif_bloothphone_new_function_guide};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transparent);
        initView();
        initData();
    }

    private void initData() {
        mSelectedDrawable = generateDefaultDrawable(Color.parseColor("#B6B6B6"));
        mUnselectedDrawable = generateDefaultDrawable(Color.parseColor("#e9e9e9"));
        createIndicators();
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                tvTitle.setText(titles[i]);
                tvContent.setText(contents[i]);
                switchIndicator(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }


    private void initView() {
        tvTitle = findViewById(R.id.guide_fuction);
        tvContent = findViewById(R.id.guide_describ);
        viewPager = findViewById(R.id.vp_guide);
        radioGroup =findViewById(R.id.rg_guide);
        ivCancle = findViewById(R.id.iv_cancle);
        ivCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });
    }

    private void createIndicators( ) {
        radioGroup.removeAllViews();
        for (int i = 0; i < titles.length; i++) {
            AppCompatImageView img = new AppCompatImageView(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.rightMargin = 25;
            img.setImageDrawable(i == 0 ? mSelectedDrawable : mUnselectedDrawable);
            radioGroup.addView(img, lp);
        }
    }

    /**
     * 改变导航的指示点
     */
    private void switchIndicator(int position) {
        if (radioGroup != null && radioGroup.getChildCount() > 0) {
            for (int i = 0; i < radioGroup.getChildCount(); i++) {
                ((AppCompatImageView) radioGroup.getChildAt(i)).setImageDrawable(
                        i == position ? mSelectedDrawable : mUnselectedDrawable);
            }
        }
    }

    private GradientDrawable generateDefaultDrawable(int color) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setSize(12, 12);
        gradientDrawable.setCornerRadius(12);
        gradientDrawable.setColor(color);
        return gradientDrawable;
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return new NewFunctionalGuideFragment(images[position]);
        }

        @Override
        public int getCount() {
            return titles.length;
        }
    }

}
