package com.app.weather.utils;

import android.app.ProgressDialog;
import android.content.Context;

public class ProgressDialogUtils {

    private static ProgressDialog progressDialog;

    /**
     * 显示ProgressDialog
     * @param context 上下文对象
     */
    public static void showProgressDialog(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("加载中,请稍后......");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // 设置进度条为圆形旋转样式
        progressDialog.setCancelable(false); // 设置对话框不可通过Back键取消
        progressDialog.setCanceledOnTouchOutside(false); // 设置对话框在触摸外部时不取消
        progressDialog.show();
    }

    /**
     * 隐藏ProgressDialog
     */
    public static void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
