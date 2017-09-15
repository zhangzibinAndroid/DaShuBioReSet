package com.dashubio.adapter.viewadapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dashubio.R;
import com.dashubio.base.MyBaseAdapter;
import com.dashubio.bean.viewbean.SurgeryBean;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者： 张梓彬
 * 日期： 2017/7/27 0027
 * 时间： 下午 12:28
 * 描述： 手术适配器
 */

public class SurgerListViewAdapter extends MyBaseAdapter<SurgeryBean> {
    private OnDelectClickListener onDelectClickListener;
    private OnWriteTimeClickListener onWriteTimeClickListener;

    public SurgerListViewAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_surgery, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final ViewHolder finalViewHolder = viewHolder;
        SurgeryBean bean = list.get(position);
        finalViewHolder.edtName.setText(bean.getName());
        finalViewHolder.tvTime.setText(bean.getTime());

        finalViewHolder.imgDelect.setTag(position);
        finalViewHolder.tvTime.setTag(viewHolder.tvTime);
        finalViewHolder.edtName.setTag(position);
        finalViewHolder.imgDelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDelectClickListener.OnDelectClick(v,(int) v.getTag());
            }
        });

        finalViewHolder.tvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onWriteTimeClickListener.OnWriteTimeClick(v,position, (TextView) finalViewHolder.tvTime.getTag());
            }
        });

        finalViewHolder.edtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                int position = (int) finalViewHolder.edtName.getTag();
                SurgeryBean bean = list.get(position);
                String content = s.toString();
                if (!TextUtils.isEmpty(content)) {
                    content.trim();
                }
                bean.setName(content);
            }
        });
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.img_delect)
        ImageView imgDelect;
        @BindView(R.id.edt_name)
        EditText edtName;
        @BindView(R.id.tv_time_surger)
        TextView tvTime;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


    public static interface OnDelectClickListener{
        void OnDelectClick(View v, int position);
    }

    public void setOnDelectClickListener(OnDelectClickListener onDelectClickListener){
        this.onDelectClickListener = onDelectClickListener;
    }



    public static interface OnWriteTimeClickListener{
        void OnWriteTimeClick(View v, int position, TextView tvTime);
    }

    public void setOnWriteTimeClickListener(OnWriteTimeClickListener onWriteTimeClickListener){
        this.onWriteTimeClickListener = onWriteTimeClickListener;
    }
}
