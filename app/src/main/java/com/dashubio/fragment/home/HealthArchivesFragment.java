package com.dashubio.fragment.home;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.lib.WheelView;
import com.bigkoo.pickerview.listener.CustomListener;
import com.dashubio.R;
import com.dashubio.activity.HomeActivity;
import com.dashubio.adapter.viewadapter.BloodTransfusionAdapter;
import com.dashubio.adapter.viewadapter.ChildrenAdapter;
import com.dashubio.adapter.viewadapter.DiseaseAdapter;
import com.dashubio.adapter.viewadapter.FatherAdapter;
import com.dashubio.adapter.viewadapter.MotherAdapter;
import com.dashubio.adapter.viewadapter.SurgerListViewAdapter;
import com.dashubio.adapter.viewadapter.TraumaAdapter;
import com.dashubio.base.BaseFragment;
import com.dashubio.bean.ErrorCodeBean;
import com.dashubio.bean.HealthArchivesBean;
import com.dashubio.bean.viewbean.DiseaseBean;
import com.dashubio.bean.viewbean.SurgeryBean;
import com.dashubio.constant.ErrorCode;
import com.dashubio.constant.InterfaceUrl;
import com.dashubio.gson.GsonParsing;
import com.dashubio.utils.ViewUtils;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;

import static android.app.Activity.RESULT_OK;
import static com.dashubio.constant.CityArray.city;
import static com.dashubio.constant.CityArray.county;
import static com.dashubio.constant.CityArray.province;


/**
 * 作者： 张梓彬
 * 日期： 2017/7/13 0013
 * 时间： 上午 10:18
 * 描述： 健康档案
 */
public class HealthArchivesFragment extends BaseFragment {
    private static final String TAG = "HealthArchivesFragment";
    @BindView(R.id.img_card)
    ImageView imgCard;
    @BindView(R.id.edt_name)
    EditText edtName;
    @BindView(R.id.tv_man)
    TextView tvMan;
    @BindView(R.id.tv_woman)
    TextView tvWoman;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.sp_folk)
    Spinner spFolk;
    @BindView(R.id.edt_card_id)
    EditText edtCardId;
    @BindView(R.id.sp_province)
    Spinner spProvince;
    @BindView(R.id.sp_city)
    Spinner spCity;
    @BindView(R.id.sp_district)
    Spinner spDistrict;
    @BindView(R.id.tv_household_registration)
    TextView tvHouseholdRegistration;
    @BindView(R.id.tv_unhousehold_registration)
    TextView tvUnhouseholdRegistration;
    @BindView(R.id.edt_address)
    EditText edtAddress;
    @BindView(R.id.edt_company)
    EditText edtCompany;
    @BindView(R.id.edt_my_phone)
    EditText edtMyPhone;
    @BindView(R.id.edt_friend_phone)
    EditText edtFriendPhone;
    @BindView(R.id.sp_blood_type)
    Spinner spBloodType;
    @BindView(R.id.sp_rh)
    Spinner spRh;
    @BindView(R.id.sp_education_degree)
    Spinner spEducationDegree;
    @BindView(R.id.sp_profession)
    Spinner spProfession;
    @BindView(R.id.sp_marriage)
    Spinner spMarriage;
    @BindView(R.id.sp_pay_style)
    Spinner spPayStyle;
    @BindView(R.id.sp_medicine)
    Spinner spMedicine;
    @BindView(R.id.sp_expose)
    Spinner spExpose;
    @BindView(R.id.imgBtn_add)
    ImageButton imgBtnAdd;
    @BindView(R.id.imgBtn_add_surgery)
    ImageButton imgBtnAddSurgery;
    @BindView(R.id.imgBtn_add_trauma)
    ImageButton imgBtnAddTrauma;
    @BindView(R.id.imgBtn_add_blood_transfusion)
    ImageButton imgBtnAddBloodTransfusion;
    @BindView(R.id.imgBtn_add_father)
    ImageButton imgBtnAddFather;
    @BindView(R.id.imgBtn_add_mother)
    ImageButton imgBtnAddMother;
    @BindView(R.id.imgBtn_add_children)
    ImageButton imgBtnAddChildren;
    @BindView(R.id.et_genetic_history)
    EditText etGeneticHistory;
    @BindView(R.id.sp_canji)
    Spinner spCanji;
    @BindView(R.id.sp_exhaust)
    Spinner spExhaust;
    @BindView(R.id.sp_fuel)
    Spinner spFuel;
    @BindView(R.id.sp_water)
    Spinner spWater;
    @BindView(R.id.sp_wc)
    Spinner spWc;
    @BindView(R.id.sp_qcl)
    Spinner spQcl;
    @BindView(R.id.recyView_disease)
    RecyclerView recyViewDisease;//疾病
    @BindView(R.id.lv_surgery)
    ListView lv_surgery;//手术
    @BindView(R.id.lv_trauma)
    ListView lv_trauma;//外伤
    @BindView(R.id.lv_blood_transfusion)
    ListView lv_blood_transfusion;//输血
    @BindView(R.id.recyView_father)
    RecyclerView recyViewFather;//父亲
    @BindView(R.id.recyView_mother)
    RecyclerView recyViewMother;//母亲
    @BindView(R.id.recyView_children)
    RecyclerView recyViewChildren;//子女
    @BindView(R.id.lay_surgery)
    AutoLinearLayout laySurgery;
    private Unbinder unbinder;
    private ArrayAdapter spFolkAdapter = null;
    private ArrayAdapter<String> provinceAdapter = null;  //省级适配器
    private ArrayAdapter<String> cityAdapter = null;    //地级适配器
    private ArrayAdapter<String> countyAdapter = null;    //县级适配器
    private ArrayAdapter<String> spBloodTypeAdapter = null;
    private ArrayAdapter<String> spRhTypeAdapter = null;
    private ArrayAdapter<String> spEducationDegreeAdapter = null;
    private ArrayAdapter<String> spProfessionAdapter = null;
    private ArrayAdapter<String> spMarriageAdapter = null;
    private ArrayAdapter<String> spPayStyleAdapter = null;
    private ArrayAdapter<String> spMedicineAdapter = null;
    private ArrayAdapter<String> spCanjiAdapter = null;
    private ArrayAdapter<String> spExposeAdapter = null;
    private ArrayAdapter<String> spExhaustAdapter = null;
    private ArrayAdapter<String> spFuelAdapter = null;
    private ArrayAdapter<String> spWaterAdapter = null;
    private ArrayAdapter<String> spWcAdapter = null;
    private ArrayAdapter<String> spQclAdapter = null;
    private static int provincePosition = 3;
    private DiseaseAdapter diseaseAdapter;
    private FatherAdapter fatherAdapter;
    private MotherAdapter motherAdapter;
    private SurgerListViewAdapter surgeryAdapter;
    private TraumaAdapter traumaAdapter;
    private BloodTransfusionAdapter bloodTransfusionAdapter;
    private ChildrenAdapter childrenAdapter;
    private ViewUtils viewUtils;
    private Runnable runnable;
    private Bitmap bitmap;

    public HealthArchivesFragment() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
    private DisplayImageOptions options;




    //网络获取各个控件的值
    private void setUserMessage(final HealthArchivesBean.UserMessageDataBean userMessageDataBean) {
        ViewUtils viewUtils = new ViewUtils();
        /*new Thread(runnable = new Runnable() {
            @Override
            public void run() {
                //耗时操作
                byte[] map = HttpUtils.getInstance().getByteArrayFromWeb(InterfaceUrl.BITMAP_URL + userMessageDataBean.getCard_img());
                bitmap = BitmapFactory.decodeByteArray(map, 0, map.length);
                getActivity().runOnUiThread(runnable =new Runnable() {
                    @Override
                    public void run() {
                        imgCard.setImageBitmap(bitmap);
                    }
                });

            }
        }).start();*/

        ImageLoader.getInstance().displayImage(InterfaceUrl.BITMAP_URL + userMessageDataBean.getCard_img(), imgCard,options);

        edtName.setText(userMessageDataBean.getName());
        if (userMessageDataBean.getSex().equals("1")) {
            tvMan.setSelected(true);
            tvWoman.setSelected(false);
            tvMan.setBackground(getResources().getDrawable(R.drawable.textbg_shape_border_left_all));
            tvWoman.setBackground(getResources().getDrawable(R.drawable.textbg_shape_border_right));
        } else if (userMessageDataBean.getSex().equals("2")) {
            tvMan.setSelected(false);
            tvWoman.setSelected(true);
            tvMan.setBackground(getResources().getDrawable(R.drawable.textbg_shape_border_left));
            tvWoman.setBackground(getResources().getDrawable(R.drawable.textbg_shape_border_right_all));
        } else {
            tvMan.setSelected(false);
            tvWoman.setSelected(false);
            tvMan.setBackground(getResources().getDrawable(R.drawable.textbg_shape_border_left));
            tvWoman.setBackground(getResources().getDrawable(R.drawable.textbg_shape_border_right));
        }

        tvDate.setText(userMessageDataBean.getBirth());
        //设置民族
        String[] folk = getResources().getStringArray(R.array.nationResArray);
        for (int i = 0; i < folk.length; i++) {
            if (folk[i].equals(userMessageDataBean.getNation())) {
                spFolk.setSelection(i);
            }
        }

        //设置身份证号码
        edtCardId.setText(userMessageDataBean.getCard_id());
        //三级联动
        for (int i = 0; i < province.length; i++) {
            if (province[i].equals(userMessageDataBean.getProvince())) {
                spProvince.setSelection(i, true);
                for (int j = 0; j < city[i].length; j++) {
                    if (city[i][j].equals(userMessageDataBean.getCity())) {
                        cityAdapter = new ArrayAdapter<String>(getActivity(), R.layout.textview_healtarchves, city[i]);
                        spCity.setAdapter(cityAdapter);
                        spCity.setSelection(j, true);
                        for (int k = 0; k < county[i][j].length; k++) {
                            if (county[i][j][k].equals(userMessageDataBean.getDistrict())) {
                                countyAdapter = new ArrayAdapter<String>(getActivity(), R.layout.textview_healtarchves, county[i][j]);
                                spDistrict.setAdapter(countyAdapter);
                                spDistrict.setSelection(k, true);
                                spProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        cityAdapter = new ArrayAdapter<String>(getActivity(), R.layout.textview_healtarchves, city[position]);
                                        spCity.setAdapter(cityAdapter);
                                        provincePosition = position;    //记录当前省级序号，留给下面修改县级适配器时用
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });

                                spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        countyAdapter = new ArrayAdapter<String>(getActivity(), R.layout.textview_healtarchves, county[provincePosition][position]);
                                        spDistrict.setAdapter(countyAdapter);
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            }
                        }
                    }
                }
            }
        }

        //户籍
        if (userMessageDataBean.getResident().equals("0")) {
            tvHouseholdRegistration.setSelected(true);
            tvUnhouseholdRegistration.setSelected(false);
            tvHouseholdRegistration.setBackground(getResources().getDrawable(R.drawable.textbg_shape_border_left_all));
            tvUnhouseholdRegistration.setBackground(getResources().getDrawable(R.drawable.textbg_shape_border_right));
        } else if (userMessageDataBean.getResident().equals("1")) {
            tvHouseholdRegistration.setSelected(false);
            tvUnhouseholdRegistration.setSelected(true);
            tvHouseholdRegistration.setBackground(getResources().getDrawable(R.drawable.textbg_shape_border_left));
            tvUnhouseholdRegistration.setBackground(getResources().getDrawable(R.drawable.textbg_shape_border_right_all));
        } else {
            tvHouseholdRegistration.setSelected(false);
            tvUnhouseholdRegistration.setSelected(false);
            tvHouseholdRegistration.setBackground(getResources().getDrawable(R.drawable.textbg_shape_border_left));
            tvUnhouseholdRegistration.setBackground(getResources().getDrawable(R.drawable.textbg_shape_border_right));
        }
        //地址
        edtAddress.setText(userMessageDataBean.getAddress());
        //工作单位
        edtCompany.setText(userMessageDataBean.getWork());
        //本人电话
        edtMyPhone.setText(userMessageDataBean.getPhone());
        //常用联系人电话
        edtFriendPhone.setText(userMessageDataBean.getPhone_contacts());
        //血型
        spBloodType.setSelection(Integer.parseInt(userMessageDataBean.getBlood()) - 1);
        //RH阴性
        spRh.setSelection(Integer.parseInt(userMessageDataBean.getBlood_hr()) - 1);
        //文化程度
        spEducationDegree.setSelection(Integer.parseInt(userMessageDataBean.getEdu()) - 1);
        //职业
        spProfession.setSelection(Integer.parseInt(userMessageDataBean.getOcc()) - 1);
        //婚姻状态
        spMarriage.setSelection(Integer.parseInt(userMessageDataBean.getMarr()) - 1);
        //医疗费用支付方式
        spPayStyle.setSelection(Integer.parseInt(userMessageDataBean.getPay_type()) - 1);
        //药物过敏史
        spMedicine.setSelection(Integer.parseInt(userMessageDataBean.getAllergor()) - 1);
        //遗传病史
        etGeneticHistory.setText(userMessageDataBean.getInheri());
        //暴露史
        spExpose.setSelection(Integer.parseInt(userMessageDataBean.getExpose()) - 1);


        List<HealthArchivesBean.UserMessageDataBean.PDisBean> disList = userMessageDataBean.getP_dis();
        diseaseList.clear();
        for (int i = 0; i < disList.size(); i++) {
            HealthArchivesBean.UserMessageDataBean.PDisBean pdBean = disList.get(i);
            DiseaseBean diseaseBean = new DiseaseBean(pdBean.getName(), pdBean.getId());
            diseaseList.add(diseaseBean);
        }
        diseaseAdapter.addData(diseaseList);
        recyViewDisease.setAdapter(diseaseAdapter);
        diseaseAdapter.notifyDataSetChanged();
        int num = diseaseList.size();
        num = num % 3 == 0 ? num / 3 : num / 3 + 1;//进一法
        ViewUtils.setLayoutHeight(recyViewDisease, num * getActivity().getResources().getDimensionPixelSize(R.dimen.px38));

        List<HealthArchivesBean.UserMessageDataBean.POperaBean> poperaList = userMessageDataBean.getP_opera();

        if (poperaList.size() > 0) {
            for (int i = 0; i < poperaList.size(); i++) {
                HealthArchivesBean.UserMessageDataBean.POperaBean poPeraBean = poperaList.get(i);
                SurgeryBean surgeryBean = new SurgeryBean(poPeraBean.getName(), poPeraBean.getDateTime());
                surgeryAdapter.addDATA(surgeryBean);
            }
        } else {
            SurgeryBean surgeryBean = new SurgeryBean();
            surgeryAdapter.addDATA(surgeryBean);
        }
        viewUtils.setListViewHeightBasedOnChildren(lv_surgery);

        List<HealthArchivesBean.UserMessageDataBean.PTrauBean> trauList = userMessageDataBean.getP_trau();
        if (trauList.size() > 0) {
            for (int i = 0; i < trauList.size(); i++) {
                HealthArchivesBean.UserMessageDataBean.PTrauBean ptrauBean = trauList.get(i);
                SurgeryBean surgeryBean = new SurgeryBean(ptrauBean.getName(), ptrauBean.getDateTime());
                traumaAdapter.addDATA(surgeryBean);
            }
        } else {
            SurgeryBean surgeryBean = new SurgeryBean();
            traumaAdapter.addDATA(surgeryBean);
        }
        viewUtils.setListViewHeightBasedOnChildren(lv_trauma);


        List<HealthArchivesBean.UserMessageDataBean.PTransBean> transList = userMessageDataBean.getP_trans();
        if (transList.size() > 0) {
            for (int i = 0; i < transList.size(); i++) {
                HealthArchivesBean.UserMessageDataBean.PTransBean ptTransBean = transList.get(i);
                SurgeryBean surgeryBean = new SurgeryBean(ptTransBean.getName(), ptTransBean.getDateTime());
                bloodTransfusionAdapter.addDATA(surgeryBean);
            }
        } else {
            SurgeryBean surgeryBean = new SurgeryBean();
            bloodTransfusionAdapter.addDATA(surgeryBean);
        }
        viewUtils.setListViewHeightBasedOnChildren(lv_blood_transfusion);


        List<HealthArchivesBean.UserMessageDataBean.FPBean> f_pList = userMessageDataBean.getF_p();
        fatherList.clear();
        for (int i = 0; i < f_pList.size(); i++) {
            HealthArchivesBean.UserMessageDataBean.FPBean fpBean = f_pList.get(i);
            DiseaseBean diseaseBean = new DiseaseBean(fpBean.getName(), fpBean.getId());
            fatherList.add(diseaseBean);
        }
        fatherAdapter.addData(fatherList);
        recyViewFather.setAdapter(fatherAdapter);
        fatherAdapter.notifyDataSetChanged();
        int numfa = fatherList.size();
        numfa = numfa % 3 == 0 ? numfa / 3 : numfa / 3 + 1;//进一法
        ViewUtils.setLayoutHeight(recyViewFather, numfa * getActivity().getResources().getDimensionPixelSize(R.dimen.px38));

        List<HealthArchivesBean.UserMessageDataBean.FMBean> f_mList = userMessageDataBean.getF_m();
        motherList.clear();
        for (int i = 0; i < f_mList.size(); i++) {
            HealthArchivesBean.UserMessageDataBean.FMBean fmBean = f_mList.get(i);
            DiseaseBean diseaseBean = new DiseaseBean(fmBean.getName(), fmBean.getId());
            motherList.add(diseaseBean);
        }
        motherAdapter.addData(motherList);
        recyViewMother.setAdapter(motherAdapter);
        motherAdapter.notifyDataSetChanged();
        int nummo = motherList.size();
        nummo = nummo % 3 == 0 ? nummo / 3 : nummo / 3 + 1;//进一法
        ViewUtils.setLayoutHeight(recyViewMother, nummo * getActivity().getResources().getDimensionPixelSize(R.dimen.px38));

        List<HealthArchivesBean.UserMessageDataBean.FChiBean> f_chiList = userMessageDataBean.getF_chi();
        childrenList.clear();
        for (int i = 0; i < f_chiList.size(); i++) {
            HealthArchivesBean.UserMessageDataBean.FChiBean fChiBean = f_chiList.get(i);
            DiseaseBean diseaseBean = new DiseaseBean(fChiBean.getName(), fChiBean.getId());
            childrenList.add(diseaseBean);
        }
        childrenAdapter.addData(childrenList);
        recyViewChildren.setAdapter(childrenAdapter);
        childrenAdapter.notifyDataSetChanged();
        int numchild = childrenList.size();
        numchild = numchild % 3 == 0 ? numchild / 3 : numchild / 3 + 1;//进一法
        ViewUtils.setLayoutHeight(recyViewChildren, numchild * getActivity().getResources().getDimensionPixelSize(R.dimen.px38));

        //残疾情况
        spCanji.setSelection(Integer.parseInt(userMessageDataBean.getDeformity()) - 1);
        //厨房排风设施
        spExhaust.setSelection(Integer.parseInt(userMessageDataBean.getExha()) - 1);
        //燃料类型
        spFuel.setSelection(Integer.parseInt(userMessageDataBean.getFuel()) - 1);
        //饮水
        spWater.setSelection(Integer.parseInt(userMessageDataBean.getWater()) - 1);
        //厕所
        spWc.setSelection(Integer.parseInt(userMessageDataBean.getWc()) - 1);
        //禽畜栏
        spQcl.setSelection(Integer.parseInt(userMessageDataBean.getPoultry()) - 1);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_health_archives, container, false);
        unbinder = ButterKnife.bind(this, view);
        options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.ic_empty)//设置图片Uri为空或是错误的时候显示的图片
                .cacheInMemory()//设置下载的图片缓存在内存中
                .cacheOnDisc()//设置下载的图片缓存在SD卡中
                .displayer(new RoundedBitmapDisplayer(5))
                .build();
        initView();
        return view;
    }

    //初始化各个控件
    private void initView() {
        File file = new File(Environment.getExternalStorageDirectory() + "/idCard");
        if (!file.exists()) {
            file.mkdir();
        }
        diseaseAdapter = new DiseaseAdapter(getActivity());
        surgeryAdapter = new SurgerListViewAdapter(getActivity());
        traumaAdapter = new TraumaAdapter(getActivity());
        bloodTransfusionAdapter = new BloodTransfusionAdapter(getActivity());
        fatherAdapter = new FatherAdapter(getActivity());
        motherAdapter = new MotherAdapter(getActivity());
        childrenAdapter = new ChildrenAdapter(getActivity());

        GridLayoutManager mgr = new GridLayoutManager(getActivity(), 3);
        GridLayoutManager mgrFather = new GridLayoutManager(getActivity(), 3);
        GridLayoutManager mgrMother = new GridLayoutManager(getActivity(), 3);
        GridLayoutManager mgrChildren = new GridLayoutManager(getActivity(), 3);
        recyViewDisease.setLayoutManager(mgr);
        recyViewFather.setLayoutManager(mgrFather);
        recyViewMother.setLayoutManager(mgrMother);
        recyViewChildren.setLayoutManager(mgrChildren);

        lv_surgery.setAdapter(surgeryAdapter);
        lv_trauma.setAdapter(traumaAdapter);
        lv_blood_transfusion.setAdapter(bloodTransfusionAdapter);

        bloodTransfusionAdapter.setOnDelectClickListener(new BloodTransfusionAdapter.OnDelectClickListener() {
            @Override
            public void OnDelectClick(View v, int position) {
                if (bloodTransfusionAdapter.getList().size() > 1) {
                    bloodTransfusionAdapter.delectData(position);
                    ViewUtils viewUtils = new ViewUtils();
                    viewUtils.setListViewHeightBasedOnChildren(lv_blood_transfusion);
                }
            }
        });

        traumaAdapter.setOnDelectClickListener(new TraumaAdapter.OnDelectClickListener() {
            @Override
            public void OnDelectClick(View v, int position) {
                if (traumaAdapter.getList().size() > 1) {
                    traumaAdapter.delectData(position);
                    ViewUtils viewUtils = new ViewUtils();
                    viewUtils.setListViewHeightBasedOnChildren(lv_trauma);
                }
            }
        });

        surgeryAdapter.setOnDelectClickListener(new SurgerListViewAdapter.OnDelectClickListener() {
            @Override
            public void OnDelectClick(View v, int position) {
                if (surgeryAdapter.getList().size() > 1) {
                    surgeryAdapter.delectData(position);
                    ViewUtils viewUtils = new ViewUtils();
                    viewUtils.setListViewHeightBasedOnChildren(lv_surgery);
                }
            }
        });

        bloodTransfusionAdapter.setOnWriteTimeClickListener(new BloodTransfusionAdapter.OnWriteTimeClickListener() {
            @Override
            public void OnWriteTimeClick(View v, int position, TextView tvTime) {
                initBloodTransfusionCustomTimePicker(tvTime, position);
                timePickerView.show();
            }
        });

        traumaAdapter.setOnWriteTimeClickListener(new TraumaAdapter.OnWriteTimeClickListener() {
            @Override
            public void OnWriteTimeClick(View v, int position, TextView tvTime) {
                initTraumaCustomTimePicker(tvTime, position);
                timePickerView.show();

            }
        });

        surgeryAdapter.setOnWriteTimeClickListener(new SurgerListViewAdapter.OnWriteTimeClickListener() {
            @Override
            public void OnWriteTimeClick(View v, int position, TextView tvTime) {
                initSurgeryCustomTimePicker(tvTime, position);
                timePickerView.show();

            }
        });

        diseaseAdapter.setOnDelectClickListener(new DiseaseAdapter.OnDelectClickListener() {
            @Override
            public void OnDelectClick(View view, int position) {
                diseaseAdapter.delectListData(position);
                int num = diseaseAdapter.getList().size();
                num = num % 3 == 0 ? num / 3 : num / 3 + 1;//进一法
                ViewUtils.setLayoutHeight(recyViewDisease, num * getActivity().getResources().getDimensionPixelSize(R.dimen.px38));
            }
        });
        initCustomTimePicker(tvDate);
        spFolkAdapter = new ArrayAdapter<String>(getActivity(), R.layout.textview_healtarchves, getResources().getStringArray(R.array.nationResArray));
        spFolk.setAdapter(spFolkAdapter);
        spFolk.setSelection(0, true);  //设置默认选中项，此处为默认选中第1个值

        //三级联动
        provinceAdapter = new ArrayAdapter<String>(getActivity(), R.layout.textview_healtarchves, province);
        spProvince.setAdapter(provinceAdapter);
        spProvince.setSelection(0, true);  //设置默认选中项，此处为默认选中第1个值

        //血型
        spBloodTypeAdapter = new ArrayAdapter<String>(getActivity(), R.layout.textview_healtarchves, getResources().getStringArray(R.array.bloods));
        spBloodType.setAdapter(spBloodTypeAdapter);
        spBloodType.setSelection(0, true);
        //RH阴性
        spRhTypeAdapter = new ArrayAdapter<String>(getActivity(), R.layout.textview_healtarchves, getResources().getStringArray(R.array.rhNegative));
        spRh.setAdapter(spRhTypeAdapter);
        spRh.setSelection(0, true);
        //文化程度
        spEducationDegreeAdapter = new ArrayAdapter<String>(getActivity(), R.layout.textview_healtarchves, getResources().getStringArray(R.array.educationDegrees));
        spEducationDegree.setAdapter(spEducationDegreeAdapter);
        spEducationDegree.setSelection(0, true);
        //职业
        spProfessionAdapter = new ArrayAdapter<String>(getActivity(), R.layout.textview_healtarchves, getResources().getStringArray(R.array.professions));
        spProfession.setAdapter(spProfessionAdapter);
        spProfession.setSelection(0, true);
        //婚姻状态
        spMarriageAdapter = new ArrayAdapter<String>(getActivity(), R.layout.textview_healtarchves, getResources().getStringArray(R.array.marriage));
        spMarriage.setAdapter(spMarriageAdapter);
        spMarriage.setSelection(0, true);
        //医疗费用支付方式
        spPayStyleAdapter = new ArrayAdapter<String>(getActivity(), R.layout.textview_healtarchves, getResources().getStringArray(R.array.payStyles));
        spPayStyle.setAdapter(spPayStyleAdapter);
        spPayStyle.setSelection(0, true);
        //药物过敏史
        spMedicineAdapter = new ArrayAdapter<String>(getActivity(), R.layout.textview_healtarchves, getResources().getStringArray(R.array.medicines));
        spMedicine.setAdapter(spMedicineAdapter);
        spMedicine.setSelection(0, true);
        //暴露史
        spExposeAdapter = new ArrayAdapter<String>(getActivity(), R.layout.textview_healtarchves, getResources().getStringArray(R.array.exposes));
        spExpose.setAdapter(spExposeAdapter);
        spExpose.setSelection(0, true);
        //残疾情况
        spCanjiAdapter = new ArrayAdapter<String>(getActivity(), R.layout.textview_healtarchves, getResources().getStringArray(R.array.canjis));
        spCanji.setAdapter(spCanjiAdapter);
        spCanji.setSelection(0, true);
        //厨房排风设施
        spExhaustAdapter = new ArrayAdapter<String>(getActivity(), R.layout.textview_healtarchves, getResources().getStringArray(R.array.exhausts));
        spExhaust.setAdapter(spExhaustAdapter);
        spExhaust.setSelection(0, true);
        //燃料类型
        spFuelAdapter = new ArrayAdapter<String>(getActivity(), R.layout.textview_healtarchves, getResources().getStringArray(R.array.fuels));
        spFuel.setAdapter(spFuelAdapter);
        spFuel.setSelection(0, true);
        //饮水
        spWaterAdapter = new ArrayAdapter<String>(getActivity(), R.layout.textview_healtarchves, getResources().getStringArray(R.array.waters));
        spWater.setAdapter(spWaterAdapter);
        spWater.setSelection(0, true);
        //厕所
        spWcAdapter = new ArrayAdapter<String>(getActivity(), R.layout.textview_healtarchves, getResources().getStringArray(R.array.wcs));
        spWc.setAdapter(spWcAdapter);
        spWc.setSelection(0, true);
        //禽畜栏
        spQclAdapter = new ArrayAdapter<String>(getActivity(), R.layout.textview_healtarchves, getResources().getStringArray(R.array.qcls));
        spQcl.setAdapter(spQclAdapter);
        spQcl.setSelection(0, true);
        /*setOnUrlCallBack(new OnUrlCallBack() {
            @Override
            public void getUrlCallBack(String url) {
                new Thread(new R).start();
                byte[] map = HttpUtils.getInstance().getByteArrayFromWeb(InterfaceUrl.BITMAP_URL + url);
                bitmap = BitmapFactory.decodeByteArray(map, 0, map.length);
                imgCard.setImageBitmap(bitmap);
            }
        });*/

        new Thread(new Runnable() {
            @Override
            public void run() {
                getHealthArchivesInterface();
            }
        }).start();
    }

    private void getHealthArchivesInterface() {
        OkHttpUtils.get().url(InterfaceUrl.HEALTH_ARCHIVES_URL + sessonWithCode)
                .addParams("m_id", HomeActivity.mid)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
//                toastOnUi(getResources().getString(R.string.network_exception_please_try_again_later));
            }

            @Override
            public void onResponse(String response, int id) {
                Message msg = new Message();
                msg.obj = response;
                healthArchivesHandler.sendMessage(msg);

            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @OnClick({R.id.btn_camera, R.id.tv_man, R.id.tv_woman, R.id.tv_date, R.id.tv_household_registration, R.id.tv_unhousehold_registration, R.id.imgBtn_add, R.id.imgBtn_add_surgery, R.id.imgBtn_add_trauma, R.id.imgBtn_add_blood_transfusion, R.id.imgBtn_add_father, R.id.imgBtn_add_mother, R.id.imgBtn_add_children, R.id.btn_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_camera://拍照
                getCamera();
                break;
            case R.id.tv_man://性别：男
                tvMan.setSelected(true);
                tvWoman.setSelected(false);
                tvMan.setBackground(getResources().getDrawable(R.drawable.textbg_shape_border_left_all));
                tvWoman.setBackground(getResources().getDrawable(R.drawable.textbg_shape_border_right));
                break;
            case R.id.tv_woman://性别：女
                tvMan.setSelected(false);
                tvWoman.setSelected(true);
                tvMan.setBackground(getResources().getDrawable(R.drawable.textbg_shape_border_left));
                tvWoman.setBackground(getResources().getDrawable(R.drawable.textbg_shape_border_right_all));
                break;
            case R.id.tv_date://出生日期
                timePickerView.show();
                break;
            case R.id.tv_household_registration://户籍
                tvHouseholdRegistration.setBackground(getResources().getDrawable(R.drawable.textbg_shape_border_left_all));
                tvUnhouseholdRegistration.setBackground(getResources().getDrawable(R.drawable.textbg_shape_border_right));
                break;
            case R.id.tv_unhousehold_registration://非户籍
                tvHouseholdRegistration.setBackground(getResources().getDrawable(R.drawable.textbg_shape_border_left));
                tvUnhouseholdRegistration.setBackground(getResources().getDrawable(R.drawable.textbg_shape_border_right_all));
                break;
            case R.id.imgBtn_add://增加疾病
                showDiseaseDialog();
                break;
            case R.id.imgBtn_add_surgery://手术
                if (surgeryAdapter.getList().size() < 5) {
                    SurgeryBean surgeryBean = new SurgeryBean();
                    surgeryAdapter.addDATA(surgeryBean);
                    ViewUtils viewUtils = new ViewUtils();
                    viewUtils.setListViewHeightBasedOnChildren(lv_surgery);
                    surgeryAdapter.notifyDataSetChanged();
                }
                break;
            case R.id.imgBtn_add_trauma://外伤
                if (traumaAdapter.getList().size() < 5) {
                    SurgeryBean surgeryBean = new SurgeryBean();
                    traumaAdapter.addDATA(surgeryBean);
                    ViewUtils viewUtils = new ViewUtils();
                    viewUtils.setListViewHeightBasedOnChildren(lv_trauma);
                    traumaAdapter.notifyDataSetChanged();
                }
                break;
            case R.id.imgBtn_add_blood_transfusion://输血
                if (bloodTransfusionAdapter.getList().size() < 5) {
                    SurgeryBean surgeryBean = new SurgeryBean();
                    bloodTransfusionAdapter.addDATA(surgeryBean);
                    ViewUtils viewUtils = new ViewUtils();
                    viewUtils.setListViewHeightBasedOnChildren(lv_blood_transfusion);
                    bloodTransfusionAdapter.notifyDataSetChanged();
                }
                break;
            case R.id.imgBtn_add_father://家族史：父亲
                showFatherDialog();
                break;
            case R.id.imgBtn_add_mother://母亲
                showMotherDialog();
                break;
            case R.id.imgBtn_add_children://子女
                showChildrenDialog();
                break;
            case R.id.btn_save://保存
                restartRegister();

                break;
        }
    }

    protected static Uri tempUri;

    //相机拍照
    private void getCamera() {
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File idCardFile = new File(Environment.getExternalStorageDirectory() + "/idCard", HomeActivity.mid+"_id_card.jpg");

        tempUri = Uri.fromFile(idCardFile);
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
        startActivityForResult(openCameraIntent, 520);
    }

    private void restartRegister() {
        if (edtName.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "姓名不能为空", Toast.LENGTH_SHORT).show();
            return;
        } else if (edtCardId.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "身份证号码不能为空", Toast.LENGTH_SHORT).show();
            return;
        } else if (edtMyPhone.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "电话号码不能为空", Toast.LENGTH_SHORT).show();
            return;
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    restartRegisterInterface();
                }
            }).start();
        }
    }

    //重新注册接口
    private void restartRegisterInterface() {
        int sex = 0;
        if (tvMan.isSelected() == true && tvWoman.isSelected() == false) {
            sex = 1;
        } else if (tvMan.isSelected() == false && tvWoman.isSelected() == true) {
            sex = 2;
        } else {
            sex = 0;
        }

        int resident = 0;
        if (tvHouseholdRegistration.isSelected() == true && tvUnhouseholdRegistration.isSelected() == false) {
            resident = 0;
        } else if (tvHouseholdRegistration.isSelected() == false && tvUnhouseholdRegistration.isSelected() == true) {
            resident = 1;
        }

        Gson gson = new Gson();
        String disJson = gson.toJson(diseaseAdapter.getList());
        String operaJson = gson.toJson(surgeryAdapter.getList());
        String trauJson = gson.toJson(traumaAdapter.getList());
        String transJson = gson.toJson(bloodTransfusionAdapter.getList());
        String fatherJson = gson.toJson(fatherAdapter.getList());
        String motherJson = gson.toJson(motherAdapter.getList());
        String childrenJson = gson.toJson(childrenAdapter.getList());
        File file = new File(Environment.getExternalStorageDirectory()+ "/idCard/" + HomeActivity.mid+"_id_card.jpg");
        if (file.exists()) {
            OkHttpUtils.post().url(InterfaceUrl.USER_REGISTER_URL + sessonWithCode + "/m_id/" + HomeActivity.mid)
                    .addParams("name", edtName.getText().toString())
                    .addParams("sex", String.valueOf(sex))
                    .addParams("birth", tvDate.getText().toString())
                    .addParams("nation", spFolk.getSelectedItem().toString())
                    .addParams("card_id", edtCardId.getText().toString())
                    .addParams("province", spProvince.getSelectedItem().toString())
                    .addParams("city", spCity.getSelectedItem().toString())
                    .addParams("district", spDistrict.getSelectedItem().toString())
                    .addParams("resident", String.valueOf(resident))
                    .addParams("address", edtAddress.getText().toString())
                    .addParams("work", edtCompany.getText().toString())
                    .addParams("phone", edtMyPhone.getText().toString())
                    .addParams("phone_contacts", edtFriendPhone.getText().toString())
                    .addParams("blood", spBloodType.getSelectedItem().toString())
                    .addParams("blood_hr", spRh.getSelectedItem().toString())
                    .addParams("edu", spEducationDegree.getSelectedItem().toString())
                    .addParams("occ", spProfession.getSelectedItem().toString())
                    .addParams("marr", spMarriage.getSelectedItem().toString())
                    .addParams("pay_type", spPayStyle.getSelectedItem().toString())
                    .addParams("allergor", spMedicine.getSelectedItem().toString())
                    .addParams("expose", spExpose.getSelectedItem().toString())
                    .addParams("p_dis", disJson)
                    .addParams("p_opera", operaJson)
                    .addParams("p_trau", trauJson)
                    .addParams("p_trans", transJson)
                    .addParams("f_p", fatherJson)
                    .addParams("f_m", motherJson)
                    .addParams("f_chi", childrenJson)
                    .addParams("inheri", etGeneticHistory.getText().toString())
                    .addParams("deformity", spCanji.getSelectedItem().toString())
                    .addParams("exha", spExhaust.getSelectedItem().toString())
                    .addParams("fuel", spFuel.getSelectedItem().toString())
                    .addParams("water", spWater.getSelectedItem().toString())
                    .addParams("wc", spWc.getSelectedItem().toString())
                    .addParams("poultry", spQcl.getSelectedItem().toString())
                    .addFile("card_img","id_card.jpg",file)
                    .build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    Log.e(TAG, "onError: "+e.getMessage() );
                    toastOnUi(getResources().getString(R.string.network_exception_please_try_again_later));

                }

                @Override
                public void onResponse(String response, int id) {
                    Message msg = new Message();
                    msg.obj = response;
                    restartRegisterHandler.sendMessage(msg);

                }
            });
        }else {
            OkHttpUtils.post().url(InterfaceUrl.USER_REGISTER_URL + sessonWithCode + "/m_id/" + HomeActivity.mid)
                    .addParams("name", edtName.getText().toString())
                    .addParams("sex", String.valueOf(sex))
                    .addParams("birth", tvDate.getText().toString())
                    .addParams("nation", spFolk.getSelectedItem().toString())
                    .addParams("card_id", edtCardId.getText().toString())
                    .addParams("province", spProvince.getSelectedItem().toString())
                    .addParams("city", spCity.getSelectedItem().toString())
                    .addParams("district", spDistrict.getSelectedItem().toString())
                    .addParams("resident", String.valueOf(resident))
                    .addParams("address", edtAddress.getText().toString())
                    .addParams("work", edtCompany.getText().toString())
                    .addParams("phone", edtMyPhone.getText().toString())
                    .addParams("phone_contacts", edtFriendPhone.getText().toString())
                    .addParams("blood", spBloodType.getSelectedItem().toString())
                    .addParams("blood_hr", spRh.getSelectedItem().toString())
                    .addParams("edu", spEducationDegree.getSelectedItem().toString())
                    .addParams("occ", spProfession.getSelectedItem().toString())
                    .addParams("marr", spMarriage.getSelectedItem().toString())
                    .addParams("pay_type", spPayStyle.getSelectedItem().toString())
                    .addParams("allergor", spMedicine.getSelectedItem().toString())
                    .addParams("expose", spExpose.getSelectedItem().toString())
                    .addParams("p_dis", disJson)
                    .addParams("p_opera", operaJson)
                    .addParams("p_trau", trauJson)
                    .addParams("p_trans", transJson)
                    .addParams("f_p", fatherJson)
                    .addParams("f_m", motherJson)
                    .addParams("f_chi", childrenJson)
                    .addParams("inheri", etGeneticHistory.getText().toString())
                    .addParams("deformity", spCanji.getSelectedItem().toString())
                    .addParams("exha", spExhaust.getSelectedItem().toString())
                    .addParams("fuel", spFuel.getSelectedItem().toString())
                    .addParams("water", spWater.getSelectedItem().toString())
                    .addParams("wc", spWc.getSelectedItem().toString())
                    .addParams("poultry", spQcl.getSelectedItem().toString())
                    .build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    Log.e(TAG, "onError: "+e.getMessage() );
                    toastOnUi(getResources().getString(R.string.network_exception_please_try_again_later));

                }

                @Override
                public void onResponse(String response, int id) {
                    Message msg = new Message();
                    msg.obj = response;
                    restartRegisterHandler.sendMessage(msg);

                }
            });
        }


    }


    private boolean[] mDiseaseMulChoice;
    private List<DiseaseBean> diseaseList = new ArrayList<>();
    private List<DiseaseBean> fatherList = new ArrayList<>();
    private List<DiseaseBean> motherList = new ArrayList<>();
    private List<DiseaseBean> childrenList = new ArrayList<>();

    private void showDiseaseDialog() {
        mDiseaseMulChoice = new boolean[getResources().getStringArray(R.array.diseaseResArray).length];
        AlertDialog.Builder diseaseDialog = new AlertDialog.Builder(getActivity());
        diseaseDialog.setTitle("请选择");
        diseaseDialog.setMultiChoiceItems(getResources().getStringArray(R.array.diseaseResArray), mDiseaseMulChoice, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                mDiseaseMulChoice[which] = isChecked;
            }
        });

        diseaseDialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                diseaseList.clear();
                for (int i = 0; i < mDiseaseMulChoice.length; i++) {
                    if (mDiseaseMulChoice[i]) {
                        DiseaseBean diseaseBean = new DiseaseBean(getResources().getStringArray(R.array.diseaseResArray)[i], String.valueOf(i + 1));
                        diseaseList.add(diseaseBean);
                        diseaseAdapter.addData(diseaseList);
                        recyViewDisease.setAdapter(diseaseAdapter);
                    }
                }
                int num = diseaseList.size();
                num = num % 3 == 0 ? num / 3 : num / 3 + 1;//进一法
                ViewUtils.setLayoutHeight(recyViewDisease, num * getActivity().getResources().getDimensionPixelSize(R.dimen.px38));
                diseaseAdapter.notifyDataSetChanged();
            }
        });
        diseaseDialog.show();
    }

    private void showFatherDialog() {
        mDiseaseMulChoice = new boolean[getResources().getStringArray(R.array.diseaseResArray).length];
        AlertDialog.Builder diseaseDialog = new AlertDialog.Builder(getActivity());
        diseaseDialog.setTitle("请选择");
        diseaseDialog.setMultiChoiceItems(getResources().getStringArray(R.array.diseaseResArray), mDiseaseMulChoice, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                mDiseaseMulChoice[which] = isChecked;
            }
        });

        diseaseDialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                fatherList.clear();
                for (int i = 0; i < mDiseaseMulChoice.length; i++) {
                    if (mDiseaseMulChoice[i]) {
                        DiseaseBean diseaseBean = new DiseaseBean(getResources().getStringArray(R.array.diseaseResArray)[i], String.valueOf(i + 1));
                        fatherList.add(diseaseBean);
                        fatherAdapter.addData(fatherList);
                        recyViewFather.setAdapter(fatherAdapter);
                    }
                }
                int num = fatherList.size();
                num = num % 3 == 0 ? num / 3 : num / 3 + 1;//进一法
                ViewUtils.setLayoutHeight(recyViewFather, num * getActivity().getResources().getDimensionPixelSize(R.dimen.px38));
                fatherAdapter.notifyDataSetChanged();
            }
        });
        diseaseDialog.show();
    }

    private void showMotherDialog() {
        mDiseaseMulChoice = new boolean[getResources().getStringArray(R.array.diseaseResArray).length];
        AlertDialog.Builder diseaseDialog = new AlertDialog.Builder(getActivity());
        diseaseDialog.setTitle("请选择");
        diseaseDialog.setMultiChoiceItems(getResources().getStringArray(R.array.diseaseResArray), mDiseaseMulChoice, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                mDiseaseMulChoice[which] = isChecked;
            }
        });

        diseaseDialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                motherList.clear();
                for (int i = 0; i < mDiseaseMulChoice.length; i++) {
                    if (mDiseaseMulChoice[i]) {
                        DiseaseBean diseaseBean = new DiseaseBean(getResources().getStringArray(R.array.diseaseResArray)[i], String.valueOf(i + 1));
                        motherList.add(diseaseBean);
                        motherAdapter.addData(motherList);
                        recyViewMother.setAdapter(motherAdapter);
                    }
                }
                int num = motherList.size();
                num = num % 3 == 0 ? num / 3 : num / 3 + 1;//进一法
                ViewUtils.setLayoutHeight(recyViewMother, num * getActivity().getResources().getDimensionPixelSize(R.dimen.px38));
                motherAdapter.notifyDataSetChanged();
            }
        });
        diseaseDialog.show();
    }

    private void showChildrenDialog() {
        mDiseaseMulChoice = new boolean[getResources().getStringArray(R.array.diseaseResArray).length];
        AlertDialog.Builder diseaseDialog = new AlertDialog.Builder(getActivity());
        diseaseDialog.setTitle("请选择");
        diseaseDialog.setMultiChoiceItems(getResources().getStringArray(R.array.diseaseResArray), mDiseaseMulChoice, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                mDiseaseMulChoice[which] = isChecked;
            }
        });

        diseaseDialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                childrenList.clear();
                for (int i = 0; i < mDiseaseMulChoice.length; i++) {
                    if (mDiseaseMulChoice[i]) {
                        DiseaseBean diseaseBean = new DiseaseBean(getResources().getStringArray(R.array.diseaseResArray)[i], String.valueOf(i + 1));
                        childrenList.add(diseaseBean);
                        childrenAdapter.addData(childrenList);
                        recyViewChildren.setAdapter(childrenAdapter);
                    }
                }
                int num = childrenList.size();
                num = num % 3 == 0 ? num / 3 : num / 3 + 1;//进一法
                ViewUtils.setLayoutHeight(recyViewChildren, num * getActivity().getResources().getDimensionPixelSize(R.dimen.px38));
                childrenAdapter.notifyDataSetChanged();
            }
        });
        diseaseDialog.show();

    }

    //出错模式下设置，目前有BUG
    private void initCitySelect() {
        provinceAdapter = new ArrayAdapter<String>(getActivity(), R.layout.textview_healtarchves, province);
        spProvince.setAdapter(provinceAdapter);
        spProvince.setSelection(0, true);  //设置默认选中项，此处为默认选中第1个值

        cityAdapter = new ArrayAdapter<String>(getActivity(), R.layout.textview_healtarchves, city[0]);
        spCity.setAdapter(cityAdapter);
        spCity.setSelection(0, true);  //默认选中第0个
        countyAdapter = new ArrayAdapter<String>(getActivity(), R.layout.textview_healtarchves, county[0][0]);
        spDistrict.setAdapter(countyAdapter);
        spDistrict.setSelection(0, true);
        spProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cityAdapter = new ArrayAdapter<String>(getActivity(), R.layout.textview_healtarchves, city[position]);
                spCity.setAdapter(cityAdapter);
                provincePosition = position;    //记录当前省级序号，留给下面修改县级适配器时用
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                countyAdapter = new ArrayAdapter<String>(getActivity(), R.layout.textview_healtarchves, county[provincePosition][position]);
                spDistrict.setAdapter(countyAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private Handler healthArchivesHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result = (String) msg.obj;
            if (result.indexOf(ErrorCode.SUCCESS) > 0) {
                try {
                    HealthArchivesBean healthArchivesBean = GsonParsing.getHealthArchivesMessageJson(result);
                    HealthArchivesBean.UserMessageDataBean userMessageDataBean = healthArchivesBean.getData();
                    setUserMessage(userMessageDataBean);
                } catch (Exception e) {
                    Log.e(TAG, "healthArchivesHandlerException: " + e);
//                    initCitySelect();
                }

            } else {
                //解析
                ErrorCodeBean errorCodeBean = null;
                try {
                    errorCodeBean = GsonParsing.sendCodeError(result);
                    judge(errorCodeBean.getCode() + "");
                } catch (Exception e) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_timeout_or_illegal_request), Toast.LENGTH_SHORT).show();
//                    initCitySelect();
                }
            }

        }
    };


    private Handler restartRegisterHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result = (String) msg.obj;
            if (result.indexOf(ErrorCode.SUCCESS) > 0) {
                Toast.makeText(getActivity(), "保存成功", Toast.LENGTH_SHORT).show();
            } else {
                //解析
                ErrorCodeBean errorCodeBean = null;
                try {
                    errorCodeBean = GsonParsing.sendCodeError(result);
                    judge(errorCodeBean.getCode() + "");
                } catch (Exception e) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_timeout_or_illegal_request), Toast.LENGTH_SHORT).show();
//                    initCitySelect();
                }
            }

        }
    };

    private TimePickerView timePickerView;//时间选择器

    private void initCustomTimePicker(final TextView textView) {
        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        Calendar startDate = Calendar.getInstance();//开始时间
        Calendar endDate = Calendar.getInstance();//结束时间
        startDate.set(1850, 1, 1);
        endDate.set(2200, 1, 28);
        timePickerView = new TimePickerView.Builder(getActivity(), new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                //可根据需要自行截取数据显示在控件上  yyyy-MM-dd HH:mm:ss  或yyyy-MM-dd
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                String time = format.format(date);
                textView.setText(time);
            }
        })
                .setType(TimePickerView.Type.YEAR_MONTH_DAY)
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setTextColorCenter(Color.parseColor("#FF4081"))//设置选中文字的颜色#64AE4A
                .setTextColorOut(Color.parseColor("#717171"))//设置选中项以外的颜色#64AE4A
                .setLineSpacingMultiplier(2.4f)//设置两横线之间的间隔倍数
                .setDividerColor(Color.parseColor("#24E1E4"))//设置分割线的颜色
                .setDividerType(WheelView.DividerType.WRAP)//设置分割线的类型
                .setBgColor(Color.parseColor("#FFFFFF"))//背景颜色(必须是16进制) Night mode#2AA2BC
                .gravity(Gravity.CENTER)//设置控件显示位置 default is center*/
                .isDialog(true)//设置显示位置
                .setLayoutRes(R.layout.pickerview_custom_time, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        TextView tvCancel = (TextView) v.findViewById(R.id.iv_cancel);
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                timePickerView.returnData();
                            }
                        });
                        tvCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                timePickerView.dismiss();
                            }
                        });
                    }
                })
                .build();
    }

    private void initSurgeryCustomTimePicker(final TextView textView, final int position) {
        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        Calendar startDate = Calendar.getInstance();//开始时间
        Calendar endDate = Calendar.getInstance();//结束时间
        startDate.set(1850, 1, 1);
        endDate.set(2200, 1, 28);
        timePickerView = new TimePickerView.Builder(getActivity(), new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                //可根据需要自行截取数据显示在控件上  yyyy-MM-dd HH:mm:ss  或yyyy-MM-dd
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                String time = format.format(date);
                textView.setText(time);
            }
        })
                .setType(TimePickerView.Type.YEAR_MONTH_DAY)
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setTextColorCenter(Color.parseColor("#FF4081"))//设置选中文字的颜色#64AE4A
                .setTextColorOut(Color.parseColor("#717171"))//设置选中项以外的颜色#64AE4A
                .setLineSpacingMultiplier(2.4f)//设置两横线之间的间隔倍数
                .setDividerColor(Color.parseColor("#24E1E4"))//设置分割线的颜色
                .setDividerType(WheelView.DividerType.WRAP)//设置分割线的类型
                .setBgColor(Color.parseColor("#FFFFFF"))//背景颜色(必须是16进制) Night mode#2AA2BC
                .gravity(Gravity.CENTER)//设置控件显示位置 default is center*/
                .isDialog(true)//设置显示位置
                .setLayoutRes(R.layout.pickerview_custom_time, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        TextView tvCancel = (TextView) v.findViewById(R.id.iv_cancel);
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                timePickerView.returnData();
                                SurgeryBean bean = surgeryAdapter.getList().get(position);
                                bean.setTime(textView.getText().toString());
                            }
                        });
                        tvCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                timePickerView.dismiss();
                            }
                        });
                    }
                })
                .build();
    }

    private void initTraumaCustomTimePicker(final TextView textView, final int position) {
        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        Calendar startDate = Calendar.getInstance();//开始时间
        Calendar endDate = Calendar.getInstance();//结束时间
        startDate.set(1850, 1, 1);
        endDate.set(2200, 1, 28);
        timePickerView = new TimePickerView.Builder(getActivity(), new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                //可根据需要自行截取数据显示在控件上  yyyy-MM-dd HH:mm:ss  或yyyy-MM-dd
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                String time = format.format(date);
                textView.setText(time);
            }
        })
                .setType(TimePickerView.Type.YEAR_MONTH_DAY)
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setTextColorCenter(Color.parseColor("#FF4081"))//设置选中文字的颜色#64AE4A
                .setTextColorOut(Color.parseColor("#717171"))//设置选中项以外的颜色#64AE4A
                .setLineSpacingMultiplier(2.4f)//设置两横线之间的间隔倍数
                .setDividerColor(Color.parseColor("#24E1E4"))//设置分割线的颜色
                .setDividerType(WheelView.DividerType.WRAP)//设置分割线的类型
                .setBgColor(Color.parseColor("#FFFFFF"))//背景颜色(必须是16进制) Night mode#2AA2BC
                .gravity(Gravity.CENTER)//设置控件显示位置 default is center*/
                .isDialog(true)//设置显示位置
                .setLayoutRes(R.layout.pickerview_custom_time, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        TextView tvCancel = (TextView) v.findViewById(R.id.iv_cancel);
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                timePickerView.returnData();
                                SurgeryBean bean = traumaAdapter.getList().get(position);
                                bean.setTime(textView.getText().toString());
                            }
                        });
                        tvCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                timePickerView.dismiss();
                            }
                        });
                    }
                })
                .build();
    }

    private void initBloodTransfusionCustomTimePicker(final TextView textView, final int position) {
        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        Calendar startDate = Calendar.getInstance();//开始时间
        Calendar endDate = Calendar.getInstance();//结束时间
        startDate.set(1850, 1, 1);
        endDate.set(2200, 1, 28);
        timePickerView = new TimePickerView.Builder(getActivity(), new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                //可根据需要自行截取数据显示在控件上  yyyy-MM-dd HH:mm:ss  或yyyy-MM-dd
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                String time = format.format(date);
                textView.setText(time);
            }
        })
                .setType(TimePickerView.Type.YEAR_MONTH_DAY)
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setTextColorCenter(Color.parseColor("#FF4081"))//设置选中文字的颜色#64AE4A
                .setTextColorOut(Color.parseColor("#717171"))//设置选中项以外的颜色#64AE4A
                .setLineSpacingMultiplier(2.4f)//设置两横线之间的间隔倍数
                .setDividerColor(Color.parseColor("#24E1E4"))//设置分割线的颜色
                .setDividerType(WheelView.DividerType.WRAP)//设置分割线的类型
                .setBgColor(Color.parseColor("#FFFFFF"))//背景颜色(必须是16进制) Night mode#2AA2BC
                .gravity(Gravity.CENTER)//设置控件显示位置 default is center*/
                .isDialog(true)//设置显示位置
                .setLayoutRes(R.layout.pickerview_custom_time, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        TextView tvCancel = (TextView) v.findViewById(R.id.iv_cancel);
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                timePickerView.returnData();
                                SurgeryBean bean = bloodTransfusionAdapter.getList().get(position);
                                bean.setTime(textView.getText().toString());
                            }
                        });
                        tvCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                timePickerView.dismiss();
                            }
                        });
                    }
                })
                .build();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) { // 如果返回码是可以用的
            imgCard.setImageURI(tempUri);
        }
    }


}
