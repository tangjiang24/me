package com.tj.myandroid.recyclertest;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

@SuppressLint("ValidFragment")
public class ItemDialg extends AppCompatDialogFragment {
    private Context mContext;
    Dialog dialog;
    @SuppressLint("ValidFragment")
    public ItemDialg(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onStart() {
        super.onStart();
        dialog = getDialog();
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return initRootView();
    }

    private View initRootView() {
        LinearLayout linearLayout = new LinearLayout(mContext);
        linearLayout.removeAllViews();
        linearLayout.setOrientation(LinearLayout.VERTICAL);
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);
        if(dialog!=null){
            Window window = getDialog().getWindow();
            WindowManager.LayoutParams wmlp =window.getAttributes();
            wmlp.gravity = Gravity.LEFT ;
                    wmlp.x = offX;
                    wmlp.y = offY;
            Log.i(getTag(), "wmlp="+wmlp);
            window.setAttributes(wmlp);
        }
    }

    int offX = 0;
    int offY = 0;
    public void setPosition(int offX, int offY) {
        this.offX = offX;
        this.offY = offY;
    }
}
