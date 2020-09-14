package com.tj.myandroid.fullscresendialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.tj.myandroid.R;


public abstract class FullScreenDialog extends Dialog {
    public View contentView;
    public FullScreenDialog(Context context) {
        super(context, R.style.full_screen_common);
        contentView = LayoutInflater.from(getContext()).inflate(getContentView(),null);
        setContentView(contentView);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //        contentView = LayoutInflater.from(getContext()).inflate(getContentView(),null);
        //        setContentView(contentView);
        //        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
    }

    public abstract int getContentView();
}
