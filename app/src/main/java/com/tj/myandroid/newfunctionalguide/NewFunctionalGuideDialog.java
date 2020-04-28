package com.tj.myandroid.newfunctionalguide;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatImageView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tj.myandroid.R;

@SuppressLint("ValidFragment")
public class NewFunctionalGuideDialog extends DialogFragment {
    private Context mContext;
    private TextView tvTitle;
    private TextView tvContent;
    private LinearLayout radioGroup;
    private ViewPager viewPager;
    private ImageView ivCancle;
    private ViewPagerAdapter viewPagerAdapter;
    Dialog dialog;

    private Drawable mSelectedDrawable;
    private Drawable mUnselectedDrawable;

    private static final String[] titles = new String[]{"1","2","3","4"};
    private static final String[] contents = new String[]{"hello1","hello2","hello3","hello4"};
    private static final int [] images = new int[]{R.drawable.a1,R.drawable.a2,R.drawable.a3,R.drawable.a4};

    public NewFunctionalGuideDialog(Context mContext) {
        this.mContext = mContext;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_new_function_guide,null);
        init(view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        dialog = getDialog();
        if (dialog != null) {
            dialog.setCanceledOnTouchOutside(false);
            DisplayMetrics dm = new DisplayMetrics();
            Window window = dialog.getWindow();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            window.setLayout((int) (dm.widthPixels * 0.8),ViewGroup.LayoutParams.WRAP_CONTENT);
//            window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

//            window.setLayout(1020,ViewGroup.LayoutParams.WRAP_CONTENT);

            //            dialog.getWindow().setLayout((int) (dm.widthPixels * 0.95), ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    private void init(View view) {
        tvTitle = view.findViewById(R.id.guide_fuction);
        tvContent = view.findViewById(R.id.guide_describ);
        viewPager = view.findViewById(R.id.vp_guide);
        radioGroup = view.findViewById(R.id.rg_guide);
        ivCancle = view.findViewById(R.id.iv_cancle);
        ivCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dialog!=null){
                    dialog.dismiss();
                }
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSelectedDrawable = generateDefaultDrawable(Color.parseColor("#C0C0C0"));
        mUnselectedDrawable = generateDefaultDrawable(Color.parseColor("#ffffff"));
        createIndicators();
        viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
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

    private void createIndicators( ) {
        radioGroup.removeAllViews();
        for (int i = 0; i < titles.length; i++) {
            AppCompatImageView img = new AppCompatImageView(mContext);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.rightMargin = 14;
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

    /**
     * 默认指示器是一系列直径为6dp的小圆点
     */
    private GradientDrawable generateDefaultDrawable(int color) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setSize(14, 14);
        gradientDrawable.setCornerRadius(14);
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
