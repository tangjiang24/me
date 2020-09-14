package com.example.aartest;

import android.content.Context;
import android.widget.Toast;

import com.aigestudio.wheelpicker.model.City;

/**
 * Describtion  :
 * Author  : 19018090 2020/8/27 17:21
 * version : 4.2
 */
public class Hello {
    public static void sayHello(Context context){
        City city = new City();
        city.setName("西安");
        Toast.makeText(context,"hello"+city.getName(),Toast.LENGTH_SHORT).show();
    }
}
