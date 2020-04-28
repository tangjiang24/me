package com.tj.myandroid.greendao;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.tj.myandroid.R;
import com.tj.myandroid.greendao.dao.AnimalsDao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GreenDaoActivity extends AppCompatActivity {
    Button btnInsert;
    Button btnQuery;
    AnimalsDao animalsDao;
    IntentFilter intentFilter;
    PushReceiver pushReceiver;
    ImageView iv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_green_dao);
        registReceiver();
        iv = findViewById(R.id.iv);
        RequestOptions options = new RequestOptions();
        options.error(R.drawable.alarm);
//        String url = "assets://picture/icon_app_family_meal.png";
        String url = "file:///android_asset://picture/icon_app_family_meal.png";
        try {
            Glide.with(this).load(getAssets().open("icon_app_family_meal.png")).apply(options).into(iv);
        } catch (IOException e) {
            e.printStackTrace();
        }
        btnInsert = findViewById(R.id.btn_insert);
        btnQuery = findViewById(R.id.btn_query);
        animalsDao = GreenDaoManager.getDaoSession().getAnimalsDao();
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Animals> animals = new ArrayList<>();
                Animals cat = new Animals();
                cat.setId("cat");
                cat.setName("cat");
                cat.setHands(4);
                cat.setEatRou(true);
//                animals.add(cat);

                try {
//                    Animals target = animalsDao.load(null);
                    animalsDao.insert(null);
                    String name = null;
                                    List<Animals> target =
//                                    Animals target
                            animalsDao.queryBuilder().where(AnimalsDao.Properties.Hands.eq(name)).list();
                    if(target ==null){
                        Log.e("tj","target ==null");
                    }else {
                        Log.e("tj",target.toString());
                    }
                }catch (Exception e){
                    Log.e("tj",e.getMessage());
                }
            }
        });
    }


    /**
     * 总之： 若返回单个实体，则有可能返回null； 若返回list，则不会返回null，顶多返回空集合。
     *         并且其他所有的空指针都可以通过try catch 进行捕获；
     */


    //  animalsDao.insert(null);  空指针
//                Animals animals = new Animals();
//                animalsDao.insert(animals);    空指针

    //  animalsDao.insertOrReplaceInTx(null);  空指针 但可以插入空集合 ；若插入的集合元素没有ID，也会空指针
//    animalsDao.insert(cat); 若数据库中有元素，直接insert（）相同的元素会 UNIQUE constraint failed: ANIMALS.ID
//     animalsDao.delete(cat);  若cat为null，或者 cat没有ID 则都会报空指针
//    animalsDao.deletebykey("id");  若id为null，则会报空指针
//     animalsDao.update(cat);  若cat为null，或者 cat没有ID 则都会报空指针

//    String name ;
//        若name为null，则空指针 . 但返回的数据若查不出来 则会返回 空集合 而不是null;
//    List<Animals> target = animalsDao.queryBuilder().where(AnimalsDao.Properties.Name.eq(name)).list();

//    若name为null，则空指针 . 但返回的数据若查不出来 则会返回null;
//    Animals target = animalsDao.queryBuilder().where(AnimalsDao.Properties.Hands.eq(name)).unique();

//    若没有查出来，则返回null  且传入的ID 可以为null；
//    animalsDao.load("id");

    private void registReceiver() {
        intentFilter = new IntentFilter();
        intentFilter.addAction("com.tj24.msg.board");
        pushReceiver = new PushReceiver();
        registerReceiver(pushReceiver,intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(pushReceiver);
    }

    class PushReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context,"jtj34ldjflsjdf",Toast.LENGTH_LONG).show();
        }
    }
}
