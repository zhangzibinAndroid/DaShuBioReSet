package com.dashubio.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.contec.jar.BC401.BC401_Struct;
import com.dashubio.bean.UserListBean;
import com.dashubio.bean.dbmanagerbean.BiochemicalBean;
import com.dashubio.bean.dbmanagerbean.BreathingBean;
import com.dashubio.bean.dbmanagerbean.LoginUserBean;
import com.dashubio.bean.dbmanagerbean.MultiDataBean;

import java.util.ArrayList;

/**
 * 作者： 张梓彬
 * 日期： 2017/8/21 0021
 * 时间： 上午 10:34
 * 描述： DBManager是建立在DBHelper之上，封装了常用的业务方法
 */

public class DBManager {
    private DBHelper helper;
    private SQLiteDatabase db;

    public DBManager(Context context) {
        helper = new DBHelper(context);
        db = helper.getWritableDatabase();
    }

    //向login_table表中插入设备用户名和密码
    public void addLoginUser(String name, String pwds) {
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("pwds", pwds);
        db.insert(DBHelper.LOGIN_TABLE_NAME, null, cv);
    }


    //通过用户名密码查询设备登录表
    public ArrayList<LoginUserBean> searchLoginData(final String name, final String pwd) {
        String sql = "SELECT * FROM " + DBHelper.LOGIN_TABLE_NAME + " WHERE name =" + "'" + name + "'" + "and pwds =" + "'" + pwd + "'";
        return ExecSQLForLoginData(sql);
    }

    //无条件查询设备登录表
    public ArrayList<LoginUserBean> searchLoginData() {
        String sql = "SELECT * FROM " + DBHelper.LOGIN_TABLE_NAME;
        return ExecSQLForLoginData(sql);
    }


    //清空设备登录表
    public void clearLoginTable() {
        ExecSQL("DELETE FROM " + DBHelper.LOGIN_TABLE_NAME);
    }

    //搜寻设备登录
    private ArrayList<LoginUserBean> ExecSQLForLoginData(String sql) {
        ArrayList<LoginUserBean> list = new ArrayList<>();
        Cursor c = ExecSQLForCursor(sql);
        while (c.moveToNext()) {
            LoginUserBean loginUserBean = new LoginUserBean();
            loginUserBean._id = c.getInt(c.getColumnIndex("_id"));
            loginUserBean.name = c.getString(c.getColumnIndex("name"));
            loginUserBean.pwds = c.getString(c.getColumnIndex("pwds"));
            list.add(loginUserBean);
        }
        c.close();
        return list;
    }

    //向user_table表中插入小用户数据
    public void addUserData(String mid, String name, String sex, String card_no, String phone) {
        ContentValues cv = new ContentValues();
        cv.put("mid", mid);
        cv.put("name", name);
        cv.put("sex", sex);
        cv.put("card_no", card_no);
        cv.put("phone", phone);
        db.insert(DBHelper.USER_TABLE_NAME, null, cv);
    }

    //搜寻小用户
    public ArrayList<UserListBean.UserListDataBean> searchUserList() {
        String sql = "SELECT * FROM " + DBHelper.USER_TABLE_NAME;
        return ExecSQLForUserList(sql);
    }

    public ArrayList<UserListBean.UserListDataBean> searchUserListWithMidNull() {
        String sql = "SELECT * FROM " + DBHelper.USER_TABLE_NAME + " WHERE mid ISNULL";
        return ExecSQLForMidNull(sql);
    }

    public void clearUserTable() {
        String sql = "DELETE FROM "+DBHelper.USER_TABLE_NAME;
        try {
            db.execSQL(sql);
            revertSeq(db);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void revertSeq(SQLiteDatabase db) {
        String sql = "UPDATE sqlite_sequence SET seq = 0 WHERE name = 'user_table'";
        db.execSQL(sql);
    }


    //搜寻小用户
    private ArrayList<UserListBean.UserListDataBean> ExecSQLForUserList(String sql) {
        ArrayList<UserListBean.UserListDataBean> list = new ArrayList<UserListBean.UserListDataBean>();
        Cursor c = ExecSQLForCursor(sql);
        while (c.moveToNext()) {
            UserListBean.UserListDataBean userListDataBean = new UserListBean.UserListDataBean();
            userListDataBean.card_id = c.getString(c.getColumnIndex("card_no"));
            userListDataBean.id = c.getString(c.getColumnIndex("mid"));
            userListDataBean.sex = c.getString(c.getColumnIndex("sex"));
            userListDataBean.name = c.getString(c.getColumnIndex("name"));
            list.add(userListDataBean);
        }
        c.close();
        return list;
    }

    private ArrayList<UserListBean.UserListDataBean> ExecSQLForMidNull(String sql) {
        ArrayList<UserListBean.UserListDataBean> list = new ArrayList<>();
        Cursor c = ExecSQLForCursor(sql);
        while (c.moveToNext()) {
            UserListBean.UserListDataBean userListDataBean = new UserListBean.UserListDataBean();
            userListDataBean.name = c.getString(c.getColumnIndex("name"));
            userListDataBean.phone = c.getString(c.getColumnIndex("phone"));
            userListDataBean.card_id = c.getString(c.getColumnIndex("card_no"));
            list.add(userListDataBean);
        }
        c.close();
        return list;
    }


    //向multi_table表中插入多参数检测仪数据
    public void addMultiData(String id, String date, String rhythm, String pulse, String sysdif, String sys, String dias, String mean, String oxygen, String resp, String st) {
        ContentValues cv = new ContentValues();
        cv.put("_id", id);
        cv.put("date", date);
        cv.put("rhythm", rhythm);
        cv.put("pulse", pulse);
        cv.put("sysdif", sysdif);
        cv.put("sys", sys);
        cv.put("dias", dias);
        cv.put("mean", mean);
        cv.put("oxygen", oxygen);
        cv.put("resp", resp);
        cv.put("st", st);
        db.insert(DBHelper.MULTI_TABLE_NAME, null, cv);
    }

    //搜寻多功能参数检测仪数据表
    public ArrayList<MultiDataBean> searchMultiDataList() {
        String sql = "SELECT * FROM " + DBHelper.MULTI_TABLE_NAME;
        return ExecSQLForMultiDataBean(sql);
    }

    private ArrayList<MultiDataBean> ExecSQLForMultiDataBean(String sql) {
        ArrayList<MultiDataBean> list = new ArrayList<>();
        Cursor c = ExecSQLForCursor(sql);
        while (c.moveToNext()) {
            MultiDataBean multiDataBean = new MultiDataBean();
            multiDataBean.id = c.getString(c.getColumnIndex("_id"));
            multiDataBean.date = c.getString(c.getColumnIndex("date"));
            multiDataBean.rhythm = c.getString(c.getColumnIndex("rhythm"));
            multiDataBean.pulse = c.getString(c.getColumnIndex("pulse"));
            multiDataBean.sysdif = c.getString(c.getColumnIndex("sysdif"));
            multiDataBean.sys = c.getString(c.getColumnIndex("sys"));
            multiDataBean.dias = c.getString(c.getColumnIndex("dias"));
            multiDataBean.mean = c.getString(c.getColumnIndex("mean"));
            multiDataBean.oxygen = c.getString(c.getColumnIndex("oxygen"));
            multiDataBean.resp = c.getString(c.getColumnIndex("resp"));
            multiDataBean.st = c.getString(c.getColumnIndex("st"));
            list.add(multiDataBean);
        }
        c.close();
        return list;
    }

    //通过其他表的_id查询user_table表的mid
    public ArrayList<UserListBean.UserListDataBean> searchDataWithId(String id) {
        String sql = "SELECT * FROM " + DBHelper.USER_TABLE_NAME + " WHERE _id =" + "'" + id + "'";
        return ExecSQLForMid(sql);
    }


    private ArrayList<UserListBean.UserListDataBean> ExecSQLForMid(String sql) {
        ArrayList<UserListBean.UserListDataBean> list = new ArrayList<>();
        Cursor c = ExecSQLForCursor(sql);
        while (c.moveToNext()) {
            UserListBean.UserListDataBean userListDataBean = new UserListBean.UserListDataBean();
            userListDataBean.id = c.getString(c.getColumnIndex("mid"));
            list.add(userListDataBean);
        }
        c.close();
        return list;
    }


    //清空多功能参数检测仪数据表
    public void clearMultiDataTable() {
        ExecSQL("DELETE FROM " + DBHelper.MULTI_TABLE_NAME);
    }

    //向bc_table表中插入尿液检测仪数据
    public void addBCData(String id,String time, String URO, String BLD, String BIL, String KET, String GLU, String PRO, String PH, String NIT, String LEU, String SG, String VC) {
        ContentValues cv = new ContentValues();
        cv.put("time", time);
        cv.put("_id", id);
        cv.put("URO", URO);
        cv.put("BLD", BLD);
        cv.put("BIL", BIL);
        cv.put("KET", KET);
        cv.put("GLU", GLU);
        cv.put("PRO", PRO);
        cv.put("PH", PH);
        cv.put("NIT", NIT);
        cv.put("LEU", LEU);
        cv.put("SG", SG);
        cv.put("VC", VC);
        db.insert(DBHelper.BC_TABLE_NAME, null, cv);
    }


    //搜索尿液数据表
    public ArrayList<BC401_Struct> searchBCData() {
        String sql = "SELECT * FROM " + DBHelper.BC_TABLE_NAME;
        return ExecSQLForBCData(sql);
    }

    private ArrayList<BC401_Struct> ExecSQLForBCData(String sql) {
        ArrayList<BC401_Struct> list = new ArrayList<>();
        Cursor c = ExecSQLForCursor(sql);
        while (c.moveToNext()) {
            BC401_Struct bcBean = new BC401_Struct();
            bcBean.ID = Integer.valueOf(c.getString(c.getColumnIndex("_id")));
            bcBean.Date = Integer.valueOf(c.getString(c.getColumnIndex("time")));
            bcBean.URO = Byte.valueOf(c.getString(c.getColumnIndex("URO")));
            bcBean.BLD = Byte.valueOf(c.getString(c.getColumnIndex("BLD")));
            bcBean.BIL = Byte.valueOf(c.getString(c.getColumnIndex("BIL")));
            bcBean.KET = Byte.valueOf(c.getString(c.getColumnIndex("KET")));
            bcBean.GLU = Byte.valueOf(c.getString(c.getColumnIndex("GLU")));
            bcBean.PRO = Byte.valueOf(c.getString(c.getColumnIndex("PRO")));
            bcBean.PH = Byte.valueOf(c.getString(c.getColumnIndex("PH")));
            bcBean.NIT = Byte.valueOf(c.getString(c.getColumnIndex("NIT")));
            bcBean.LEU = Byte.valueOf(c.getString(c.getColumnIndex("LEU")));
            bcBean.SG = Byte.valueOf(c.getString(c.getColumnIndex("SG")));
            bcBean.VC = Byte.valueOf(c.getString(c.getColumnIndex("VC")));
            list.add(bcBean);
        }
        c.close();
        return list;
    }

    //清空尿液检测仪数据表
    public void clearBCDataTable() {
        ExecSQL("DELETE FROM " + DBHelper.BC_TABLE_NAME);
    }

    //向biochemical_table表中插入干式生化仪检测仪数据
    public void addBiochemicalData(String id, String message) {
        ContentValues cv = new ContentValues();
        cv.put("_id", id);
        cv.put("message", message);
        db.insert(DBHelper.BIOCHEMICAL_TABLE_NAME, null, cv);
    }

    //搜索干式生化以数据表
    public ArrayList<BiochemicalBean> searchBiochemicalData() {
        String sql = "SELECT * FROM " + DBHelper.BIOCHEMICAL_TABLE_NAME;
        return ExecSQLForBiochemicalData(sql);
    }


    private ArrayList<BiochemicalBean> ExecSQLForBiochemicalData(String sql) {
        ArrayList<BiochemicalBean> list = new ArrayList<>();
        Cursor c = ExecSQLForCursor(sql);
        while (c.moveToNext()) {
            BiochemicalBean biochemicalBean = new BiochemicalBean();
            biochemicalBean.id = c.getString(c.getColumnIndex("_id"));
            biochemicalBean.message = c.getString(c.getColumnIndex("message"));
            list.add(biochemicalBean);
        }
        c.close();
        return list;
    }

    //清空多功能参数检测仪数据表
    public void clearBiochemicalTable() {
        ExecSQL("DELETE FROM " + DBHelper.BIOCHEMICAL_TABLE_NAME);
    }


    //向breathing_table表中插入干式生化仪检测仪数据
    public void addBreathingData(String id, String date, String tvPef, String tvFvc, String tvFev1) {
        ContentValues cv = new ContentValues();
        cv.put("_id", id);
        cv.put("date", date);
        cv.put("tvPef", tvPef);
        cv.put("tvFvc", tvFvc);
        cv.put("tvFev1", tvFev1);
        db.insert(DBHelper.BREATHING_TABLE_NAME, null, cv);
    }

    //搜索干式生化以数据表
    public ArrayList<BreathingBean> searchBreathingData() {
        String sql = "SELECT * FROM " + DBHelper.BREATHING_TABLE_NAME;
        return ExecSQLForBreathingData(sql);
    }

    private ArrayList<BreathingBean> ExecSQLForBreathingData(String sql) {
        ArrayList<BreathingBean> list = new ArrayList<>();
        Cursor c = ExecSQLForCursor(sql);
        while (c.moveToNext()) {
            BreathingBean breathingBean = new BreathingBean();
            breathingBean.id = c.getString(c.getColumnIndex("_id"));
            breathingBean.date = c.getString(c.getColumnIndex("date"));
            breathingBean.tvPef = c.getString(c.getColumnIndex("tvPef"));
            breathingBean.tvFvc = c.getString(c.getColumnIndex("tvFvc"));
            breathingBean.tvFev1 = c.getString(c.getColumnIndex("tvFev1"));
            list.add(breathingBean);
        }
        c.close();
        return list;
    }

    //清空多功能参数检测仪数据表
    public void clearBreathingTable() {
        ExecSQL("DELETE FROM " + DBHelper.BREATHING_TABLE_NAME);
    }

    //执行SQL，返回一个游标
    private Cursor ExecSQLForCursor(String sql) {
        Cursor c = db.rawQuery(sql, null);
        return c;
    }


    public void closeDB() {
        db.close();
    }

    private void ExecSQL(String sql) {
        try {
            db.execSQL(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
