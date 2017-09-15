package com.dashubio.utils;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

/**
 * 作者： 张梓彬
 * 日期： 2017/7/26 0026
 * 时间： 下午 2:57
 * 描述： View的工具类
 */

public class ViewUtils {
    private static final String TAG = "ViewUtils";

    /**
     * 設置View的高度（像素），若設置爲自適應則應該傳入MarginLayoutParams.WRAP_CONTENT
     */
    public static void setLayoutHeight(View view, int height) {

        if (view.getParent() instanceof FrameLayout) {
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) view.getLayoutParams();
            lp.height = height;
            view.setLayoutParams(lp);
            view.requestLayout();
            Log.d(TAG, "FrameLayout: ");
        } else if (view.getParent() instanceof RelativeLayout) {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) view.getLayoutParams();
            lp.height = height;
            view.setLayoutParams(lp);
            view.requestLayout();
            Log.d(TAG, "RelativeLayout: ");

        } else if (view.getParent() instanceof LinearLayout) {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) view.getLayoutParams();
            lp.height = height;
            view.setLayoutParams(lp);
            view.requestLayout();
            Log.d(TAG, "LinearLayout: ");

        }
    }

    //设置ListView的高度
    public void setListViewHeightBasedOnChildren(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();

        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();

        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));


        listView.setLayoutParams(params);
    }


}
