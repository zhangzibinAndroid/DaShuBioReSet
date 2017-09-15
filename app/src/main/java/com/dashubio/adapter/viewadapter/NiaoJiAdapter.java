package com.dashubio.adapter.viewadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.contec.jar.BC401.BC401_Struct;
import com.dashubio.R;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 张梓彬 on 2017/8/18 0018.
 */

public class NiaoJiAdapter extends RecyclerView.Adapter<NiaoJiAdapter.ViewHolder> {
    private Context context;
    private ArrayList<BC401_Struct> bcList = new ArrayList<>();

    public NiaoJiAdapter(Context context) {
        this.context = context;
    }

    public void addData(ArrayList<BC401_Struct> list) {
        this.bcList.clear();
        this.bcList.addAll(list);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_niaoji_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        AutoUtils.autoSize(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BC401_Struct mBC401Struct = bcList.get(position);
        String uro = "";
        switch (mBC401Struct.URO) {
            case 0:
                uro = "3.3umol/l";
                break;
            case 1:
                uro = "33umol/l";
                break;
            case 2:
                uro = "66umol/l";
                break;
            case 3:
                uro = "131umol/l";
                break;
        }
        holder.tvUnitUro.setText(uro);
        String bldStr = "";
        switch (mBC401Struct.BLD){
            case 0:
                bldStr = "-";
                break;

            case 1:
                bldStr = "10/ul";
                break;

            case 2:
                bldStr = "25/ul";
                break;

            case 3:
                bldStr = "50/ul";
                break;

            case 4:
                bldStr = "250/ul";
                break;
        }
        holder.tvUnitBld.setText(bldStr);

        String bilStr = "";
        switch (mBC401Struct.BIL){
            case 0:
                bilStr = "0umol/l";
                break;

            case 1:
                bilStr = "17umol/l";
                break;

            case 2:
                bilStr = "50umol/l";
                break;

            case 3:
                bilStr = "100umol/l";
                break;
        }
        holder.tvUnitBil.setText(bilStr);

        String ketStr = "";
        switch (mBC401Struct.KET){
            case 0:
                ketStr = "0mmol/l";
                break;

            case 1:
                ketStr = "1.5mmol/l";
                break;

            case 2:
                ketStr = "4.0mmol/l";
                break;

            case 3:
                ketStr = "8.0mmol/l";
                break;
        }
        holder.tvUnitKet.setText(ketStr);

        String gluStr = "";
        switch (mBC401Struct.GLU){
            case 0:
                gluStr = "0mmol/l";
                break;

            case 1:
                gluStr = "2.8mmol/l";
                break;

            case 2:
                gluStr = "5.5mmol/l";
                break;

            case 3:
                gluStr = "14mmol/l";

            case 4:
                gluStr = "28mmol/l";

            case 5:
                gluStr = "55mmol/l";
                break;
        }
        holder.tvUnitGlu.setText(gluStr);


        String proStr = "";
        switch (mBC401Struct.PRO){
            case 0:
                proStr = "0g/l";
                break;

            case 1:
                proStr = "0.15g/l";
                break;

            case 2:
                proStr = "0.3g/l";
                break;

            case 3:
                proStr = "1g/l";
                break;

            case 4:
                proStr = "3g/l";
                break;
        }
        holder.tvUnitPro.setText(proStr);


        String phStr = "";
        switch (mBC401Struct.PH){
            case 0:
                phStr = "5";
                break;

            case 1:
                phStr = "6";
                break;

            case 2:
                phStr = "7";
                break;

            case 3:
                phStr = "8";
                break;

            case 4:
                phStr = "9";
                break;
        }
        holder.tvUnitPh.setText(phStr);

        String nitStr = "";
        switch (mBC401Struct.NIT){
            case 0:
                nitStr = "-";
                break;

            case 1:
                nitStr = "18umol/l";
                break;
        }
        holder.tvUnitNit.setText(nitStr);

        //传统单位
        String leuStr = "";
        switch (mBC401Struct.LEU){
            case 0:
                leuStr = "-";
                break;

            case 1:
                leuStr = "15/ul";
                break;

            case 2:
                leuStr = "70/ul";
                break;

            case 3:
                leuStr = "125/ul";
                break;

            case 4:
                leuStr = "500/ul";
                break;
        }
        holder.tvUnitLeu.setText(leuStr);

        String sgStr = "";
        switch (mBC401Struct.SG){
            case 0:
                sgStr = "<=1.005";
                break;

            case 1:
                sgStr = "1.010";
                break;

            case 2:
                sgStr = "1.015";
                break;

            case 3:
                sgStr = "1.020";
                break;

            case 4:
                sgStr = "1.025";
                break;

            case 5:
                sgStr = ">=1.030";
                break;
        }
        holder.tvUnitSg.setText(sgStr);

        String vcStr = "";
        switch (mBC401Struct.VC){
            case 0:
                vcStr = "0mmol/l";
                break;

            case 1:
                vcStr = "0.6mmol/l";
                break;

            case 2:
                vcStr = "1.4mmol/l";
                break;

            case 3:
                vcStr = "2.8mmol/l";
                break;

            case 4:
                vcStr = "5.6mmol/l";
                break;
        }
        holder.tvUnitVc.setText(vcStr);

    }


    @Override
    public int getItemCount() {
        return bcList.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_unit_uro)
        TextView tvUnitUro;
        @BindView(R.id.tv_unit_bld)
        TextView tvUnitBld;
        @BindView(R.id.tv_unit_bil)
        TextView tvUnitBil;
        @BindView(R.id.tv_unit_ket)
        TextView tvUnitKet;
        @BindView(R.id.tv_unit_glu)
        TextView tvUnitGlu;
        @BindView(R.id.tv_unit_pro)
        TextView tvUnitPro;
        @BindView(R.id.tv_unit_ph)
        TextView tvUnitPh;
        @BindView(R.id.tv_unit_nit)
        TextView tvUnitNit;
        @BindView(R.id.tv_unit_leu)
        TextView tvUnitLeu;
        @BindView(R.id.tv_unit_sg)
        TextView tvUnitSg;
        @BindView(R.id.tv_unit_vc)
        TextView tvUnitVc;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
