package com.dashubio.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.contec.jar.BC401.BC401_Struct;
import com.dashubio.bean.LoginBean;
import com.dashubio.bean.UserListBean;
import com.dashubio.bean.dbmanagerbean.BiochemicalBean;
import com.dashubio.bean.dbmanagerbean.BreathingBean;
import com.dashubio.bean.dbmanagerbean.LoginUserBean;
import com.dashubio.bean.dbmanagerbean.MultiDataBean;
import com.dashubio.constant.ErrorCode;
import com.dashubio.constant.InterfaceUrl;
import com.dashubio.db.DBManager;
import com.dashubio.gson.GsonParsing;
import com.dashubio.utils.NetUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

import static com.dashubio.constant.InterfaceUrl.code;
import static com.dashubio.constant.InterfaceUrl.t_session_code;
import static com.dashubio.constant.InterfaceUrl.zSesson;


/**
 * 作者： 张梓彬
 * 日期： 2017/8/21 0021
 * 时间： 下午 5:43
 * 描述： 网络广播
 */
public class NetBroadcastReceiver extends BroadcastReceiver {
    private DBManager dbManager;
    private static final String TAG = "NetBroadcastReceiver";
    private Context context;
    private String sessonWithCode;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        if (NetUtil.isNetworkConnectionActive(context)) {
            Log.e(TAG, "网络已连接" );
            dbManager = new DBManager(context);
            ArrayList<LoginUserBean> loginList = dbManager.searchLoginData();
            String phone = "";
            String pwds = "";
            try {
                LoginUserBean loginUserBean = loginList.get(0);
                phone = loginUserBean.getName();
                pwds = loginUserBean.getPwds();
                final String finalPhone = phone;
                final String finalPwds = pwds;
                equipmentLoginInterface(InterfaceUrl.LOGIN_URL, finalPhone, finalPwds);
            } catch (Exception e) {
                Log.e(TAG, "onReceive: " + e.getMessage());
            }
        } else {
            Log.e(TAG, "无网络连接");
        }
    }

    //用户登录接口
    private void equipmentLoginInterface(String loginUrl, String finalPhone, String finalPwds) {
        OkHttpUtils.post().url(loginUrl)
                .addParams("phone", finalPhone)
                .addParams("password", finalPwds)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.e(TAG, "调用登录接口失败" + e.getMessage());
            }

            @Override
            public void onResponse(String result, int id) {
                LoginBean loginBean = null;
                try {
                    loginBean = GsonParsing.getMessage(result);
                    code = loginBean.getCode();
                } catch (Exception e) {
                    Log.e(TAG, "连接超时，或非法请求");
                }

                try {
                    t_session_code = GsonParsing.gsonLoginSesson(result);
                } catch (JSONException e) {
                    Toast.makeText(context, "t_session_code解析失败", Toast.LENGTH_SHORT).show();
                }
                sessonWithCode = zSesson + code + "/" + t_session_code + "/uid/" + code;
                doActyion();
            }
        });
    }


    private void doActyion() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //注册离线用户
                ArrayList<UserListBean.UserListDataBean> userList = dbManager.searchUserListWithMidNull();
                for (UserListBean.UserListDataBean userListDataBean : userList) {
                    String name = userListDataBean.getName();
                    String phone = userListDataBean.getPhone();
                    String card_id = userListDataBean.getCard_id();
                    postUserRegister(name, phone, card_id);
                }

                //清空用户数据库
                dbManager.clearUserTable();//清空小用户列表

                //获取用户数据库重新储存
                getUserListInterface(-1);
            }
        }).start();


    }

    //恢复网络注册数据库中的用户
    private void postUserRegister(String name, String phone, String card_id) {
        Log.e(TAG, "url: " + InterfaceUrl.USER_REGISTER_URL + sessonWithCode);
        OkHttpUtils.post().url(InterfaceUrl.USER_REGISTER_URL + sessonWithCode)
                .addParams("card_id", card_id)
                .addParams("phone", phone)
                .addParams("name", name)
                .addParams("sex", "0")
                .addParams("birth", "2017-08-22")
                .addParams("nation", "汉族")
                .addParams("province", "——选择省——")
                .addParams("city", "——选择市——")
                .addParams("district", "——选择区——")
                .addParams("phone_contacts", "待修改")
                .addParams("address", "")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.e(TAG, "离线注册失败: " + e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                Log.e(TAG, "离线注册成功: " + response);
            }
        });
    }


    //获取用户列表接口
    private void getUserListInterface(int page) {
        OkHttpUtils.get().url(InterfaceUrl.USER_MESSAGE_URL + sessonWithCode)
                .addParams("p", String.valueOf(page))
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.e(TAG, "获取用户列表接口失败: " + e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                UserListBean userListBean = null;
                if (response.indexOf(ErrorCode.SUCCESS) > 0) {
                    try {
                        userListBean = GsonParsing.getUserListMessage(response);
                        List<UserListBean.UserListDataBean> userList = userListBean.getData();
                        List<UserListBean.UserListDataBean> dbUserList = dbManager.searchUserList();
                        String mResult = "";
                        for (UserListBean.UserListDataBean userListDataBean : dbUserList) {
                            mResult = mResult + String.valueOf(userListDataBean.card_id);
                            mResult = mResult + "\n" + "------------------------------------------" + "\n";
                        }

                        for (int i = userList.size() - 1; i > -1; i--) {
                            UserListBean.UserListDataBean users = userList.get(i);
                            String userName = users.getName();
                            String sex = users.getSex();
                            String userid = users.getId();
                            String card_id = users.getCard_id();
                            String phone = users.getPhone();
                            if (mResult.indexOf(card_id) != -1) {
                                //数据库有数据
                            } else {
                                dbManager.addUserData(userid, userName, sex, card_id, phone);
                            }
                        }

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                pushData();
                                Message msg = new Message();
                                msg.obj = "离线数据上传中...";
                                handler.sendMessage(msg);
                            }
                        }).start();


                    } catch (Exception e) {
                        Log.e(TAG, "获取用户列表接口解析失败: " + e.getMessage());
                    }


                }

            }
        });
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result = (String) msg.obj;
            Toast.makeText(context, result, Toast.LENGTH_SHORT).show();

        }
    };

    //上传数据
    private void pushData() {
        ArrayList<MultiDataBean> multiList = dbManager.searchMultiDataList();
        for (MultiDataBean multiDataBean : multiList) {
            String id = multiDataBean.getId();
            String date = multiDataBean.getDate();
            String rhythm = multiDataBean.getRhythm();
            String pulse = multiDataBean.getPulse();
            String sysdif = multiDataBean.getSysdif();
            String sys = multiDataBean.getSys();
            String dias = multiDataBean.getDias();
            String mean = multiDataBean.getMean();
            String oxygen = multiDataBean.getOxygen();
            String resp = multiDataBean.getResp();
            String st = multiDataBean.getSt();
            ArrayList<UserListBean.UserListDataBean> userList = dbManager.searchDataWithId(id);
            for (UserListBean.UserListDataBean userListDataBean : userList) {
                String mid = userListDataBean.getId();
                pushMultiData(mid, date, rhythm, pulse, sysdif, sys, dias, mean, oxygen, resp, st);
            }

        }
        //清空多功能参数检测仪数据表
        dbManager.clearMultiDataTable();

        ArrayList<BC401_Struct> bcList = dbManager.searchBCData();
        for (BC401_Struct bcBean : bcList) {
            String ID = bcBean.ID + "";
            String time = bcBean.Date + "";
            String URO = bcBean.URO + "";
            String BIL = bcBean.BIL + "";
            String GLU = bcBean.GLU + "";
            String PH = bcBean.PH + "";
            String LEU = bcBean.LEU + "";
            String BLD = bcBean.BLD + "";
            String KET = bcBean.KET + "";
            String PRO = bcBean.PRO + "";
            String NIT = bcBean.NIT + "";
            String SG = bcBean.SG + "";
            String VC = bcBean.VC + "";
            ArrayList<UserListBean.UserListDataBean> userList = dbManager.searchDataWithId(ID);
            for (UserListBean.UserListDataBean userListDataBean : userList) {
                String mid = userListDataBean.getId();
                pushBCData(mid,time, URO, BIL, GLU, PH, LEU, BLD, KET, PRO, NIT, SG, VC);
            }
        }
        //清空尿液检测仪数据表
        dbManager.clearBCDataTable();

        ArrayList<BiochemicalBean> bioList = dbManager.searchBiochemicalData();
        for (BiochemicalBean biochemicalBean : bioList) {
            String id = biochemicalBean.getId();
            String message = biochemicalBean.getMessage();
            ArrayList<UserListBean.UserListDataBean> userList = dbManager.searchDataWithId(id);
            for (UserListBean.UserListDataBean userListDataBean : userList) {
                String mid = userListDataBean.getId();
                pushBiochemicalData(mid, message);
            }
        }

        //清空干式生化仪数据表
        dbManager.clearBiochemicalTable();

        ArrayList<BreathingBean> breathList = dbManager.searchBreathingData();
        for (BreathingBean breathingBean : breathList) {
            String id = breathingBean.getId();
            String date = breathingBean.getDate();
            String tvPef = breathingBean.getTvPef();
            String tvFvc = breathingBean.getTvFvc();
            String tvFev1 = breathingBean.getTvFev1();
            ArrayList<UserListBean.UserListDataBean> userList = dbManager.searchDataWithId(id);
            for (UserListBean.UserListDataBean userListDataBean : userList) {
                String mid = userListDataBean.getId();
                pushBreathData(mid, date, tvPef, tvFvc, tvFev1);
            }
        }
        //清空呼吸机数据表
        dbManager.clearBreathingTable();

    }


    //上传多功能参数检测仪的数据
    private void pushMultiData(String mid, String date, String rhythm, String pulse, String sysdif, String sys, String dias, String mean, String oxygen, String resp, String st) {
        OkHttpUtils.post().url(InterfaceUrl.MULTIPARAMETERMONITORDATA_URL + sessonWithCode + "/mid/" + mid)
                .addParams("date", date)
                .addParams("rhythm", rhythm)
                .addParams("pulse", pulse)
                .addParams("sysdif", sysdif)
                .addParams("sys", sys)
                .addParams("dias", dias)
                .addParams("mean", mean)
                .addParams("oxygen", oxygen)
                .addParams("resp", resp)
                .addParams("st", st)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.e(TAG, "多功能参数检测仪离线上传数据失败: " + e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                if (response.indexOf(ErrorCode.SUCCESS) > 0) {
                    Log.e(TAG, "多功能参数检测仪离线上传数据成功: " + response);
                } else {
                    Log.e(TAG, "多功能参数检测仪离线上传数据异常: " + response);
                }
            }
        });
    }

    //上传尿液检测仪数据表中数据
    private void pushBCData(String mid,String time, String uro, String bil, String glu, String ph, String leu, String bld, String ket, String pro, String nit, String sg, String vc) {

        OkHttpUtils.post().url(InterfaceUrl.BCDATA_URL + sessonWithCode + "/m_id/" + mid)
                .addParams("addtime", time)
                .addParams("URO", uro)
                .addParams("BIL", bil)
                .addParams("GLU", glu)
                .addParams("PH", ph)
                .addParams("LEU", leu)
                .addParams("BLD", bld)
                .addParams("KET", ket)
                .addParams("PRO", pro)
                .addParams("NIT", nit)
                .addParams("SG", sg)
                .addParams("VC", vc)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.e(TAG, "上传尿液检测仪数据失败: " + e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                if (response.indexOf(ErrorCode.SUCCESS) > 0) {
                    Log.e(TAG, "上传尿液检测仪数据成功: " + response);
                } else {
                    Log.e(TAG, "上传尿液检测仪数据异常: " + response);
                }
            }
        });
    }

    //上传干式生化仪数据表中数据
    private void pushBiochemicalData(String mid, String message) {
        OkHttpUtils.post().url(InterfaceUrl.GANSHIDATA_URL + sessonWithCode + "/m_id/" + mid)
                .addParams("val", message)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.e(TAG, "干式生化仪上传数据失败: " + e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                if (response.indexOf(ErrorCode.SUCCESS) > 0) {
                    Log.e(TAG, "干式生化仪上传数据成功: " + response);
                } else {
                    Log.e(TAG, "干式生化仪上传数据异常: " + response);
                }
            }
        });
    }


    //上传呼吸数据表中的数据
    private void pushBreathData(String mid, String date, String tvPef, String tvFvc, String tvFev1) {
        OkHttpUtils.post().url(InterfaceUrl.BREATHDATA_URL + sessonWithCode + "/mid/" + mid)
                .addParams("date", date)
                .addParams("pef", tvPef)
                .addParams("fvc", tvFvc)
                .addParams("fev1", tvFev1)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.e(TAG, "上传呼吸数据表中的数据失败: " + e.getMessage());

            }

            @Override
            public void onResponse(String response, int id) {
                if (response.indexOf(ErrorCode.SUCCESS) > 0) {
                    Log.e(TAG, "上传呼吸数据表中的数据成功: " + response);

                } else {
                    Log.e(TAG, "上传呼吸数据表中的数据异常: " + response);

                }
            }
        });
    }
}
