package com.dashubio.adapter.viewadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dashubio.R;
import com.dashubio.bean.viewbean.DiseaseBean;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者： 张梓彬
 * 日期： 2017/7/26 0026
 * 时间： 上午 11:47
 * 描述： 疾病
 */

public class DiseaseAdapter extends RecyclerView.Adapter<DiseaseAdapter.ViewHolder> {
    private List<DiseaseBean> list = new ArrayList<>();
    private static final String TAG = "DiseaseAdapter";
    private Context context;
    private OnDelectClickListener onDelectClickListener;

    public DiseaseAdapter(Context context) {
        this.context = context;
    }

    public void addData(List<DiseaseBean> strList){
        list.clear();
        list.addAll(strList);
    }

    public void delectListData(int position){
        list.remove(position);
        notifyDataSetChanged();
    }

    public List getList(){
        return list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selected_disease_grid, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        AutoUtils.autoSize(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DiseaseBean diseaseBean = list.get(position);
        holder.diseaseNameTv.setText(diseaseBean.getName());
        holder.imgDelete.setTag(position);
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDelectClickListener.OnDelectClick(v, (Integer) v.getTag());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.disease_name_tv)
        TextView diseaseNameTv;
        @BindView(R.id.img_delete)
        ImageView imgDelete;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public static interface OnDelectClickListener{
        void OnDelectClick(View view, int position);
    }

    public void setOnDelectClickListener(OnDelectClickListener onDelectClickListener){
        this.onDelectClickListener = onDelectClickListener;
    }
}
