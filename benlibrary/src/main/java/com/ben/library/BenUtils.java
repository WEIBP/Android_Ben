package com.ben.library;

import android.annotation.SuppressLint;
import android.content.Context;

import com.ben.library.log.L;

import androidx.annotation.NonNull;

public class BenUtils {

    @SuppressLint("StaticFieldLeak")
    private static Context context;

    private BenUtils(){

    }

    public static void init(@NonNull Context context){
        BenUtils.context = context.getApplicationContext();
    }

    public static Context getContext(){
        if (context == null){
            L.object("please init first!");
            throw new NullPointerException("BenUtils should init first!");
        }
        return context;
    }

}
