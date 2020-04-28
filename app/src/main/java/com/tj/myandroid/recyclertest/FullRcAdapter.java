package com.tj.myandroid.recyclertest;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tj.myandroid.R;

import java.util.List;

public class FullRcAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public FullRcAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, String item) {
        helper.setText(R.id.tv,item);
        switch (item){
            case "1":
                helper.setBackgroundColor(R.id.tv, Color.RED);
                break;
            case "2":
                helper.setBackgroundColor(R.id.tv, Color.YELLOW);
                break;
            case "3":
                helper.setBackgroundColor(R.id.tv, Color.GREEN);
                break;
            case "4":
                helper.setBackgroundColor(R.id.tv, Color.BLUE);
                break;
        }
    }
}
