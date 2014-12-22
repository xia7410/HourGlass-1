package com.muggins.poisearch;

import android.content.Context;
import android.widget.Toast;
/****************************
 *
 * @author zhaoqin
 * 功能：提示
 * 更新日期：2014-8-21
 *
 */
public class ToastUtil {

    public static void show(Context context, String info) {
        Toast.makeText(context, info, Toast.LENGTH_LONG).show();
    }

    public static void show(Context context, int info) {
        Toast.makeText(context, info, Toast.LENGTH_LONG).show();
    }
}

