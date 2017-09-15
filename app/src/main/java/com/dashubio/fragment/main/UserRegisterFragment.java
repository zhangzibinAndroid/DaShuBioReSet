package com.dashubio.fragment.main;


import android.content.DialogInterface;
import android.graphics.Color;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.lib.WheelView;
import com.bigkoo.pickerview.listener.CustomListener;
import com.dashubio.R;
import com.dashubio.base.BaseFragment;
import com.dashubio.bean.CameraCardBean;
import com.dashubio.bean.ErrorCodeBean;
import com.dashubio.constant.ErrorCode;
import com.dashubio.constant.InterfaceUrl;
import com.dashubio.db.DBManager;
import com.dashubio.gson.GsonParsing;
import com.dashubio.utils.CameraManager;
import com.dashubio.utils.FileUtil;
import com.dashubio.utils.HttpUtil;
import com.dashubio.utils.NetUtil;
import com.dashubio.view.ActionSheetDialog;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

import static com.dashubio.constant.CityArray.city;
import static com.dashubio.constant.CityArray.county;
import static com.dashubio.constant.CityArray.province;


/**
 * 作者： 张梓彬
 * 日期： 2017/7/13 0013
 * 时间： 上午 10:09
 * 描述： 用户注册
 */
public class UserRegisterFragment extends BaseFragment implements Callback {
    private CameraManager mCameraManager;
    private SurfaceView mSurfaceView;
    private ProgressBar pb;
    private ImageButton mShutter;
    private SurfaceHolder mSurfaceHolder;
    private String flashModel = Parameters.FLASH_MODE_OFF;
    private byte[] jpegData = null;
    private AlertDialog.Builder registerDialog;
    private AlertDialog dialog, dialogFolk;
    private ArrayAdapter<String> provinceAdapter = null;  //省级适配器
    private ArrayAdapter<String> cityAdapter = null;    //地级适配器
    private ArrayAdapter<String> countyAdapter = null;    //县级适配器
    private static int provincePosition = 3;

    public UserRegisterFragment() {

    }

    private Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Toast.makeText(getActivity(), "拍照失败", Toast.LENGTH_SHORT).show();
                    mCameraManager.initPreView();
                    break;
                case 1:
                    jpegData = (byte[]) msg.obj;
                    if (jpegData != null && jpegData.length > 0) {
                        pb.setVisibility(View.VISIBLE);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                if ((jpegData.length > (1000 * 1024 * 5))) {
                                    mHandler.sendMessage(mHandler.obtainMessage(3, getResources().getString(R.string.photo_too_lage)));
                                    return;
                                }
                                String result = null;
                                boolean isavilable = NetUtil.isNetworkConnectionActive(getActivity());
                                if (isavilable) {
                                    result = Scan(UserLoginFragment.action, jpegData, "jpg");
                                    if (result.equals("-2")) {
                                        result = "连接超时！";
                                        mHandler.sendMessage(mHandler.obtainMessage(3, result));
                                    } else if (HttpUtil.connFail.equals(result)) {
                                        mHandler.sendMessage(mHandler.obtainMessage(3, result));
                                    } else {
                                        mHandler.sendMessage(mHandler.obtainMessage(4, result));
                                    }
                                } else {
                                    mHandler.sendMessage(mHandler.obtainMessage(3, "无网络，请确定网络是否连接!"));
                                }
                            }
                        }).start();
                    }
                    break;
                case 3:
                    pb.setVisibility(View.GONE);
                    String str = msg.obj + "";
                    Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
                    mCameraManager.initPreView();
                    mShutter.setEnabled(true);
                    break;
                case 4:
                    mShutter.setEnabled(true);
                    pb.setVisibility(View.GONE);
                    String result = msg.obj + "";
                    CameraCardBean cameraCardBean = null;
                    try {
                        cameraCardBean = GsonParsing.getCardMessageJson(result);
                        CameraCardBean.DataBean dataBean = cameraCardBean.getData();
                        CameraCardBean.DataBean.ItemBean itemBean = dataBean.getItem();
                        String name = itemBean.getName();
                        String id_number = itemBean.getCardno();
                        String sex = itemBean.getSex();
                        String folk = itemBean.getFolk();
                        String birth = itemBean.getBirthday();
                        String address = itemBean.getAddress();
                        showDialog(name,id_number,sex,folk,birth,address);
                        Toast.makeText(getActivity(), "操作成功", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), "身份证识别失败", Toast.LENGTH_SHORT).show();
                    }

                    break;
                case 5:
                    String filePath = msg.obj + "";
                    byte[] data = FileUtil.getByteFromPath(filePath);
                    if (data != null && data.length > 0) {
                        mHandler.sendMessage(mHandler.obtainMessage(1, data));
                    } else {
                        mHandler.sendMessage(mHandler.obtainMessage(0));
                    }
                    break;
                case 6:
                    Toast.makeText(getActivity(), "请插入存储卡！", Toast.LENGTH_SHORT).show();
                    mCameraManager.initPreView();
                    break;
            }
        }

        ;
    };




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_register, container, false);
        dbManager = new DBManager(getActivity());
        mCameraManager = new CameraManager(getActivity(), mHandler);
        initViews(view);

        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(getActivity(), "请插入存储卡", Toast.LENGTH_LONG).show();
        }

        File dir = new File(CameraManager.strDir);
        if (!dir.exists()) {
            dir.mkdir();
        }
        return view;
    }


    private void initViews(View view) {
        pb = (ProgressBar) view.findViewById(R.id.reco_recognize_bar);
        mSurfaceView = (SurfaceView) view.findViewById(R.id.camera_preview);
        mShutter = (ImageButton) view.findViewById(R.id.camera_shutter);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mShutter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mCameraManager.requestFocuse();
                mShutter.setEnabled(false);
            }
        });
        Button btn_register = (Button) view.findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog("","","","","","");
            }
        });
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            mCameraManager.openCamera(mSurfaceHolder);
            if (flashModel == null || !mCameraManager.isSupportFlash(flashModel)) {
                flashModel = mCameraManager.getDefaultFlashMode();
            }
            mCameraManager.setCameraFlashMode(flashModel);
        } catch (RuntimeException e) {
            Toast.makeText(getActivity(), R.string.camera_open_error, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(getActivity(), R.string.camera_open_error, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (width > height) {
            mCameraManager.setPreviewSize(width, height);
        } else {
            mCameraManager.setPreviewSize(height, width);
        }
        mCameraManager.initPreView();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mCameraManager.closeCamera();
    }

    public static String Scan(String type, byte[] file, String ext) {
        String xml = HttpUtil.getSendXML(type, ext);
        return HttpUtil.send(xml, file);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    private void showDialog(String name, String id_number, String sex, String folk, String birth, String address) {
        registerDialog = new AlertDialog.Builder(getActivity());
        View dialogView = View.inflate(getActivity(), R.layout.dialog_user_register, null);


        final ViewHolder viewHolder = new ViewHolder(dialogView);
        viewHolder.dialogTvUserName.setText(name);
        viewHolder.dialogTvUserIdNumber.setText(id_number);
        viewHolder.dialogTvUserSex.setText(sex);
        viewHolder.dialogTvUserFolk.setText(folk);
        viewHolder.dialogTvUserBirth.setText(birth);
        viewHolder.dialogTvUserAddress.setText(address);

        //绑定适配器和值
        provinceAdapter = new ArrayAdapter<String>(getActivity(), R.layout.item_city_text_select, province);
        viewHolder.tvProvince.setAdapter(provinceAdapter);
        viewHolder.tvProvince.setSelection(0, true);  //设置默认选中项，此处为默认选中第1个值

        cityAdapter = new ArrayAdapter<String>(getActivity(), R.layout.item_city_text_select, city[0]);
        viewHolder.tvCity.setAdapter(cityAdapter);
        viewHolder.tvCity.setSelection(0, true);  //默认选中第0个

        countyAdapter = new ArrayAdapter<String>(getActivity(), R.layout.item_city_text_select, county[0][0]);
        viewHolder.tvDistrict.setAdapter(countyAdapter);
        viewHolder.tvDistrict.setSelection(0, true);

        if (!NetUtil.isNetworkConnectionActive(getActivity())){
            viewHolder.lay_dialog_sex.setVisibility(View.GONE);
            viewHolder.lay_dialog_folk.setVisibility(View.GONE);
            viewHolder.lay_dialog_birth.setVisibility(View.GONE);
            viewHolder.lay_dialog_area.setVisibility(View.GONE);
            viewHolder.lay_dialog_address.setVisibility(View.GONE);
            viewHolder.lay_dialog_mphone.setVisibility(View.GONE);
            viewHolder.my_view.setVisibility(View.VISIBLE);
        }else {
            viewHolder.my_view.setVisibility(View.GONE);
        }
        viewHolder.tvProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cityAdapter = new ArrayAdapter<String>(getActivity(), R.layout.item_city_text_select, city[position]);
                viewHolder.tvCity.setAdapter(cityAdapter);
                provincePosition = position;    //记录当前省级序号，留给下面修改县级适配器时用
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        viewHolder.tvCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                countyAdapter = new ArrayAdapter<String>(getActivity(), R.layout.item_city_text_select, county[provincePosition][position]);
                viewHolder.tvDistrict.setAdapter(countyAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        viewHolder.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = viewHolder.dialogTvUserName.getText().toString();
                String sex = viewHolder.dialogTvUserSex.getText().toString();
                String sexNo = "0";
                if (sex.equals(getString(R.string.male))) {
                    sexNo = "1";
                } else if (sex.equals(getString(R.string.female))) {
                    sexNo = "2";
                }
                final String finalSexNo = sexNo;
                final String folk = viewHolder.dialogTvUserFolk.getText().toString();
                String birth = viewHolder.dialogTvUserBirth.getText().toString();
                if (!TextUtils.isEmpty(birth)) {
                    birth = birth.replace("年", "-");
                    birth = birth.replace("月", "-");
                    birth = birth.replace("日", "");
                }
                final String finalBirth = birth;
                final String province = viewHolder.tvProvince.getSelectedItem().toString();
                final String city = viewHolder.tvCity.getSelectedItem().toString();
                final String district = viewHolder.tvDistrict.getSelectedItem().toString();
                final String address = viewHolder.dialogTvUserAddress.getText().toString();
                final String id_number = viewHolder.dialogTvUserIdNumber.getText().toString();
                final String phone_number = viewHolder.dialogTvUserPhoneNumber.getText().toString();
                final String user_Frequent_contacts = viewHolder.dialogTvUserFrequentContacts.getText().toString();
                if (name.equals("")) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.username_cannot_empty), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (id_number.equals("")) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.user_id_card_cannot_empty), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (phone_number.equals("")) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.phone_cannot_empty), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (NetUtil.isNetworkConnectionActive(getActivity())){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            userRegisterInterface(name, finalSexNo, folk, finalBirth,
                                    province, city, district, address, id_number,
                                    phone_number, user_Frequent_contacts);
                        }
                    }).start();
                }else {
                    //如果网络不可用，则储存在本地数据库中
                    dialog.dismiss();
                    dbManager.addUserData(null,name,finalSexNo,id_number,phone_number);
                    Toast.makeText(getActivity(), "离线注册成功，已存入本地数据库", Toast.LENGTH_SHORT).show();
                }



            }
        });
        viewHolder.btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        registerDialog.setView(dialogView);
        dialog = registerDialog.show();
        viewHolder.dialogTvUserName.clearFocus();
        viewHolder.dialogTvUserSex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ActionSheetDialog(getActivity())
                        .builder()
                        .setCancelable(false)
                        .setCanceledOnTouchOutside(true)
                        .addSheetItem(getString(R.string.male), ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        viewHolder.dialogTvUserSex.setText(R.string.male);
                                    }
                                })
                        .addSheetItem(getString(R.string.female), ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        viewHolder.dialogTvUserSex.setText(R.string.female);
                                    }
                                }).show();
            }
        });


        viewHolder.dialogTvUserFolk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //选择名族
                showFolkDialog(viewHolder.dialogTvUserFolk);
            }
        });

        initCustomTimePicker(viewHolder.dialogTvUserBirth);
        viewHolder.dialogTvUserBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerView.show();
            }
        });

        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    //新用户注册
    private void userRegisterInterface(String name, String sex, String folk, String birth, String province,
                                       String city, String district, String address, String id_number,
                                       String phone_number, String user_frequent_contacts) {
        OkHttpUtils.post().url(InterfaceUrl.USER_REGISTER_URL + sessonWithCode)
                .addParams("card_id", id_number)
                .addParams("phone", phone_number)
                .addParams("name", name)
                .addParams("sex", sex)
                .addParams("birth", birth)
                .addParams("nation", folk)
                .addParams("province", province)
                .addParams("city", city)
                .addParams("district", district)
                .addParams("phone_contacts", user_frequent_contacts)
                .addParams("address", address)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                toastOnUi("注册异常，请检查网络");
            }

            @Override
            public void onResponse(String response, int id) {
                Message msg = new Message();
                msg.obj = response;
                userRegisterHandler.sendMessage(msg);
            }
        });

    }


    int itemSelectedNum = 0;

    private void showFolkDialog(final TextView textView) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.choice_nation)
                .setSingleChoiceItems(R.array.nationResArray, itemSelectedNum,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                itemSelectedNum = which;
                            }
                        })
                .setPositiveButton(R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                String[] nationList = getResources().getStringArray(R.array.nationResArray);
                                textView.setText(nationList[itemSelectedNum]);
                            }
                        })
                .setNegativeButton(R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialogFolk.dismiss();
                            }
                        });
        dialogFolk = builder.show();

    }

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
                SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
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


    static class ViewHolder {
        @BindView(R.id.my_view)
        View my_view;

        @BindView(R.id.lay_dialog_sex)
        AutoLinearLayout lay_dialog_sex;
        @BindView(R.id.lay_dialog_folk)
        AutoLinearLayout lay_dialog_folk;
        @BindView(R.id.lay_dialog_birth)
        AutoLinearLayout lay_dialog_birth;
        @BindView(R.id.lay_dialog_area)
        AutoLinearLayout lay_dialog_area;
        @BindView(R.id.lay_dialog_address)
        AutoLinearLayout lay_dialog_address;
        @BindView(R.id.lay_dialog_mphone)
        AutoLinearLayout lay_dialog_mphone;



        @BindView(R.id.dialog_tv_user_name)
        EditText dialogTvUserName;
        @BindView(R.id.dialog_tv_user_sex)
        TextView dialogTvUserSex;
        @BindView(R.id.dialog_tv_user_folk)
        TextView dialogTvUserFolk;
        @BindView(R.id.dialog_tv_user_birth)
        TextView dialogTvUserBirth;
        @BindView(R.id.tv_province)
        Spinner tvProvince;
        @BindView(R.id.tv_city)
        Spinner tvCity;
        @BindView(R.id.tv_district)
        Spinner tvDistrict;
        @BindView(R.id.dialog_tv_user_address)
        EditText dialogTvUserAddress;
        @BindView(R.id.dialog_tv_user_id_number)
        EditText dialogTvUserIdNumber;
        @BindView(R.id.dialog_tv_user_phone_number)
        EditText dialogTvUserPhoneNumber;
        @BindView(R.id.dialog_tv_user_Frequent_contacts)
        EditText dialogTvUserFrequentContacts;
        @BindView(R.id.btn_register)
        Button btnRegister;
        @BindView(R.id.btn_cancle)
        Button btnCancle;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


    private Handler userRegisterHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result = (String) msg.obj;
            if (result.indexOf(ErrorCode.SUCCESS) > 0) {
                dialog.dismiss();
                Toast.makeText(getActivity(), "用户注册成功", Toast.LENGTH_SHORT).show();
            } else {
                //解析
                ErrorCodeBean errorCodeBean = null;
                try {
                    errorCodeBean = GsonParsing.sendCodeError(result);
                    judge(errorCodeBean.getCode() + "");
                } catch (Exception e) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_timeout_or_illegal_request), Toast.LENGTH_SHORT).show();
                }
            }
        }
    };
}
