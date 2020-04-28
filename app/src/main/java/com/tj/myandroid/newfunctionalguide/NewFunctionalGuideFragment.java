package com.tj.myandroid.newfunctionalguide;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tj.myandroid.R;

@SuppressLint("ValidFragment")
public class NewFunctionalGuideFragment extends Fragment {
    ImageView ivGuide;

    private int res;

    public NewFunctionalGuideFragment(int res) {
        this.res = res;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_guide_function,null);
        ivGuide = view.findViewById(R.id.iv_guide_content);
        ivGuide.setImageResource(res);
        return view;
    }
}
