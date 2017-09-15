package com.dashubio.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.callback.IFooterCallBack;
import com.dashubio.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 作者： 张梓彬
 * 日期： 2017/7/14 0014
 * 时间： 下午 5:32
 * 描述： xrefresh刷新头部布局
 */

public class ViewFooter extends LinearLayout implements IFooterCallBack {
    private ImageView img_refresh;
    private TextView tv_pullLoad;
    private TextView tv_refreshTime;
    private Context context;

    public ViewFooter(Context context) {
        super(context);
        this.context = context;
        setBackgroundColor(Color.parseColor("#f3f3f3"));
        initView(context);
    }

    /**
     * @param context
     * @param attrs
     */
    public ViewFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.head_view_refresh, this);
        img_refresh = (ImageView) findViewById(R.id.img_refresh);
        tv_pullLoad = (TextView) findViewById(R.id.tv_pullLoad);
        tv_refreshTime = (TextView) findViewById(R.id.tv_refreshTime);
        Animation rotate = AnimationUtils.loadAnimation(context, R.anim.rotateanimation);
        img_refresh.setAnimation(rotate);
        long currentTime = System.currentTimeMillis();
        setListRefreshTime(currentTime);
    }





    public void setListRefreshTime(long time){
            try {
                String data = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(time));
                tv_refreshTime.setText(data);
            } catch (Exception e) {
                Toast.makeText(context, "获取刷新时间失败", Toast.LENGTH_SHORT).show();
                Log.e("TAG", "ParseException: ==="+e );
            }
    }




    @Override
    public void callWhenNotAutoLoadMore(XRefreshView xRefreshView) {

    }

    /**
     * 正常状态
     */
    @Override
    public void onStateReady() {
        tv_pullLoad.setText(R.string.xrefreshview_header_hint_normal);
    }

    /**
     * 正在刷新
     */
    @Override
    public void onStateRefreshing() {
        tv_pullLoad.setText(R.string.xrefreshview_header_hint_refreshing);
        //重新调用，否则时间不刷新
        long currentTime = System.currentTimeMillis();
        setListRefreshTime(currentTime);
    }

    @Override
    public void onReleaseToLoadMore() {
        tv_pullLoad.setText(R.string.xrefreshview_header_hint_ready);


    }


    /**
     * 刷新结束
     * <p>
     * 是否刷新成功 success参数由XRefreshView.stopRefresh(boolean)传入
     */
    @Override
    public void onStateFinish(boolean success) {
        tv_pullLoad.setText(success ? R.string.xrefreshview_header_hint_loaded : R.string.xrefreshview_header_hint_loaded_fail);
    }

    @Override
    public void onStateComplete() {
        setVisibility(View.GONE);
    }

    @Override
    public void show(boolean show) {
        Log.e("ZZZ", "show: "+show );
//        if (show){
            setVisibility(View.VISIBLE);
//        }else {
//        }
    }

    @Override
    public boolean isShowing() {
        return false;
    }

    @Override
    public int getFooterHeight() {
        return getMeasuredHeight();
    }
}
