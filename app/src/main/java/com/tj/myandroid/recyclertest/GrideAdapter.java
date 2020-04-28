package com.tj.myandroid.recyclertest;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tj.myandroid.R;

import java.util.List;

public class GrideAdapter extends BaseQuickAdapter<AppBean, BaseViewHolder> {
    public GrideAdapter(int layoutResId, @Nullable List<AppBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, AppBean item) {
        helper.setText(R.id.tv_name,item.getName())
                .setText(R.id.tv_des,item.getDescribe())
                .addOnClickListener(R.id.iv_iamge);
    }
}
