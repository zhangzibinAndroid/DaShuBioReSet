package com.dashubio.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Common Utils for the application
 */
public class Utils {

    public static boolean sDebug;
    public static String sLogTag;

    public static final String CACHE_ROOT_PATH = Environment.getExternalStorageDirectory().getPath()
            + "/Android/data/com.dashubio"; // 缓存根目录

    public static final String IMAGE_PATH = CACHE_ROOT_PATH + "/cache/image";// 图片数据路径

    public static final String IMAGE_CACHE_DIR = "images";// 图片缓存目录
    public static final String IMAGE_SUFFIX = ".jpg";// 图片缓存目录

    public static final String T_SESSION = "t_session_";

    public final static int FIRST_PAGE_INDEX = 1;//首页索引
    public final static int PAGE_SIZE = 10;//每页数目

    private static final String TAG = "Utils";

    // UTF-8 encoding
    private static final String ENCODING_UTF8 = "UTF-8";

    private static WeakReference<Calendar> calendar;

    public static final String SAVE_USER = "saveUserDashu"; //SharePreference保存的xml文件名

    public static final String TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";


    public static final String ARG_SECONDUSER = "arg_Seconduser";

    /**
     * <p>
     * Get UTF8 bytes from a string
     * </p>
     *
     * @param string String
     * @return UTF8 byte array, or null if failed to get UTF8 byte array
     */
    public static byte[] getUTF8Bytes(String string) {
        if (string == null)
            return new byte[0];

        try {
            return string.getBytes(ENCODING_UTF8);
        } catch (UnsupportedEncodingException e) {
            /*
			 * If system doesn't support UTF-8, use another way
			 */
            try {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                DataOutputStream dos = new DataOutputStream(bos);
                dos.writeUTF(string);
                byte[] jdata = bos.toByteArray();
                bos.close();
                dos.close();
                byte[] buff = new byte[jdata.length - 2];
                System.arraycopy(jdata, 2, buff, 0, buff.length);
                return buff;
            } catch (IOException ex) {
                return new byte[0];
            }
        }
    }

    /**
     * <p>
     * Get string in UTF-8 encoding
     * </p>
     *
     * @param b byte array
     * @return string in utf-8 encoding, or empty if the byte array is not
     * encoded with UTF-8
     */
    public static String getUTF8String(byte[] b) {
        if (b == null)
            return "";
        return getUTF8String(b, 0, b.length);
    }

    /**
     * <p>
     * Get string in UTF-8 encoding
     * </p>
     */
    public static String getUTF8String(byte[] b, int start, int length) {
        if (b == null) {
            return "";
        } else {
            try {
                return new String(b, start, length, ENCODING_UTF8);
            } catch (UnsupportedEncodingException e) {
                return "";
            }
        }
    }






    /**
     * String转int
     *
     * @param intstr
     * @return
     */
    public static int string2Int(String intstr) {
        int result = -1;
        try {
            result = Integer.valueOf(intstr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 十六进制String转int
     *
     * @param hexStr
     * @return
     */
    public static int hexStr2Int(String hexStr) {
        int result = -1;
        try {
            result = Integer.parseInt(hexStr, 16);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static long string2Long(String longstr) {
        long result = -1;
        try {
            result = Long.valueOf(longstr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * <p>
     * Parse int value from string
     * </p>
     *
     * @param value string
     * @return int value
     */
    public static int getInt(String value) {
        if (TextUtils.isEmpty(value)) {
            return 0;
        }

        try {
            return Integer.parseInt(value.trim(), 10);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * <p>
     * Parse float value from string
     * </p>
     *
     * @param value string
     * @return float value
     */
    public static float getFloat(String value) {
        if (value == null)
            return 0f;

        try {
            return Float.parseFloat(value.trim());
        } catch (NumberFormatException e) {
            return 0f;
        }
    }

    /**
     * <p>
     * Parse long value from string
     * </p>
     *
     * @param value string
     * @return long value
     */
    public static long getLong(String value) {
        if (value == null)
            return 0L;

        try {
            return Long.parseLong(value.trim());
        } catch (NumberFormatException e) {
            return 0L;
        }
    }

    public static void V(String msg) {
        if (sDebug) {
            Log.v(sLogTag, msg);
        }
    }

    public static void V(String msg, Throwable e) {
        if (sDebug) {
            Log.v(sLogTag, msg, e);
        }
    }

    public static void D(String msg) {
        if (sDebug) {
            Log.d(sLogTag, msg);
        }
    }

    public static void D(String msg, Throwable e) {
        if (sDebug) {
            Log.d(sLogTag, msg, e);
        }
    }

    public static void I(String msg) {
        if (sDebug) {
            Log.i(sLogTag, msg);
        }
    }

    public static void I(String msg, Throwable e) {
        if (sDebug) {
            Log.i(sLogTag, msg, e);
        }
    }

    public static void W(String msg) {
        if (sDebug) {
            Log.w(sLogTag, msg);
        }
    }

    public static void W(String msg, Throwable e) {
        if (sDebug) {
            Log.w(sLogTag, msg, e);
        }
    }

    public static void E(String msg) {
        if (sDebug) {
            Log.e(sLogTag, msg);
        }
    }

    public static void E(String msg, Throwable e) {
        if (sDebug) {
            Log.e(sLogTag, msg, e);
        }
    }

    @SuppressLint("SimpleDateFormat")
    public static String formatDate(long time) {
        if (calendar == null || calendar.get() == null) {
            calendar = new WeakReference<Calendar>(Calendar.getInstance());
        }
        Calendar target = calendar.get();
        target.setTimeInMillis(time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(target.getTime());
    }

    @SuppressLint("SimpleDateFormat")
    public static String getTodayDate() {
        if (calendar == null || calendar.get() == null) {
            calendar = new WeakReference<Calendar>(Calendar.getInstance());
        }
        Calendar today = calendar.get();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(today.getTime());
    }

    @SuppressLint("SimpleDateFormat")
    public static String getImageName() {
        if (calendar == null || calendar.get() == null) {
            calendar = new WeakReference<Calendar>(Calendar.getInstance());
        }
        Calendar today = calendar.get();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(today.getTime());
    }

    @SuppressLint("SimpleDateFormat")
    public static String getTodayDate2() {
        if (calendar == null || calendar.get() == null) {
            calendar = new WeakReference<Calendar>(Calendar.getInstance());
        }
        Calendar today = calendar.get();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.format(today.getTime());
    }

    @SuppressLint("SimpleDateFormat")
    public static String getTodayDateWithoutHour() {
        if (calendar == null || calendar.get() == null) {
            calendar = new WeakReference<Calendar>(Calendar.getInstance());
        }
        Calendar today = calendar.get();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(today.getTime());
    }

    // 获取指定多少天后的日期     apponit = 1 即为第二天的日期   如果要前一天  apponit = -1 即可
    @SuppressLint("SimpleDateFormat")
    public static String getAppointDate(int apponit) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, apponit);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(calendar.getTime());

    }

    /**
     * 妫�鏌D鍗℃槸鍚﹀瓨鍦�
     */
    public static boolean checkSdCard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED))
            return true;
        else
            return false;
    }


    /**
     * 灏嗛暱鏃堕棿鏍煎紡瀛楃涓茶浆鎹负鏃堕棿 yyyy-MM-dd
     *
     * @param strDate
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static Date strToDate(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }

    /**
     * Returns whether the network is available
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            Log.w(TAG, "couldn't get connectivity manager");
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0, length = info.length; i < length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Returns whether the network is roaming
     */
    public static boolean isNetworkRoaming(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            // Log.w(Constants.TAG, "couldn't get connectivity manager");
        } else {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null
                    && info.getType() == ConnectivityManager.TYPE_MOBILE) {
            } else {
            }
        }
        return false;
    }

    /**
     * 鏍煎紡鍖栨椂闂达紙Format锛歽yyy-MM-dd HH:mm锛�
     *
     * @param timeInMillis
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String formatTime(long timeInMillis) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(new Date(timeInMillis));
    }

    /**
     * 鏂囦欢鎷疯礉宸ュ叿绫�
     *
     * @param in 婧愭枃浠�
     * @param dst 鐩爣鏂囦欢
     * @throws IOException
     */
    public static void copyFile(InputStream in, FileOutputStream dst)
            throws IOException {
        byte[] buffer = new byte[8192];
        int len = 0;
        while ((len = in.read(buffer)) > 0) {
            dst.write(buffer, 0, len);
        }
        in.close();
        dst.close();
    }




    /**
     * 鐣岄潰鍒囨崲鍔ㄧ敾
     *
     * @return
     */
    public static LayoutAnimationController getLayoutAnimation() {
        AnimationSet set = new AnimationSet(true);

        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(50);
        set.addAnimation(animation);

        animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        animation.setDuration(100);
        set.addAnimation(animation);

        LayoutAnimationController controller = new LayoutAnimationController(
                set, 0.5f);
        return controller;
    }

    /**
     * 姣旇緝涓や釜鏂囦欢鐨勭鍚嶆槸鍚︿竴鑷�
     */
    public static boolean compareFileWithSignature(String path1, String path2) {

        long start = System.currentTimeMillis();
        if (TextUtils.isEmpty(path1) || TextUtils.isEmpty(path2)) {
            return false;
        }

        String signature1 = getFileSignatureMd5(path1);
        String signature2 = getFileSignatureMd5(path2);

        V("compareFileWithSignature total time is "
                + (System.currentTimeMillis() - start));
        if (!TextUtils.isEmpty(signature1) && signature1.equals(signature2)) {
            return true;
        }
        return false;
    }

    /**
     * 鑾峰彇搴旂敤绛惧悕MD5
     */
    public static String getFileSignatureMd5(String targetFile) {

        try {
            JarFile jarFile = new JarFile(targetFile);
            // 鍙朢SA鍏挜
            JarEntry jarEntry = jarFile.getJarEntry("AndroidManifest.xml");

            if (jarEntry != null) {
                InputStream is = jarFile.getInputStream(jarEntry);
                byte[] buffer = new byte[8192];
                while (is.read(buffer) > 0) {
                    // do nothing
                }
                is.close();
                Certificate[] certs = jarEntry == null ? null : jarEntry
                        .getCertificates();
                if (certs != null && certs.length > 0) {
                    String rsaPublicKey = String.valueOf(certs[0]
                            .getPublicKey());
                    return getMD5(rsaPublicKey);
                }
            }
        } catch (IOException e) {
            W("occur IOException when get file signature", e);
        }
        return "";
    }

    /**
     * 鑾峰彇MD5鐮�
     */
    public static String getMD5(String text) {
        try {
            byte[] byteArray = text.getBytes("utf8");
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(byteArray, 0, byteArray.length);
            return convertToHex(md.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 杞崲涓�16杩涘埗
     */
    private static String convertToHex(byte[] data) {
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            int halfbyte = (data[i] >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                if ((0 <= halfbyte) && (halfbyte <= 9))
                    buf.append((char) ('0' + halfbyte));
                else
                    buf.append((char) ('a' + (halfbyte - 10)));
                halfbyte = data[i] & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }


    public static boolean isSdcardAvailable() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    // 鑾峰彇SD鍗¤矾寰�
    public static String getSDPath() {
        String sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED); // 鍒ゆ柇sd鍗℃槸鍚﹀瓨鍦�
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory().toString();// 鑾峰彇璺熺洰褰�    鍙栧緱澶栭儴鐨勫瓨鍌ㄧ洰褰�
        } else {
            sdDir = Environment.getDataDirectory().getPath();// 鑾峰彇璺熺洰褰�
        }
        return sdDir;
    }

    /**
     * 妫�鏌D鍗℃槸鍚﹀彲璇�
     */
    public static boolean isSdcardReadable() {
        final String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)
                || Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /**
     * 妫�鏌D鍗℃槸鍚﹀彲鍐�
     */
    public static boolean isSdcardWritable() {
        final String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    // 鍒ゆ柇瀵嗙爜鏍煎紡鏄惁姝ｇ‘
    // 闀垮害鍦�6~18涔嬮棿锛屽彧鑳藉寘鍚瓧绗︺�佹暟瀛楀拰涓嬪垝绾裤��
    public static boolean isPassWd(String pass) {
        Pattern p = Pattern.compile("^[0-9a-zA-Z_]{6,18}");
        Matcher m = p.matcher(pass);
        return m.matches();
    }



    public static void clearCache(Context context) {
        File file = Environment.getDownloadCacheDirectory();
        File[] files = file.listFiles();
        if (files != null) {
            for (File f : files) {
                f.delete();
            }
        }
        file = context.getCacheDir();
        files = file.listFiles();
        if (files != null) {
            for (File f : files) {
                f.delete();
            }
        }
    }


    @SuppressLint("SimpleDateFormat")
    public static String getHourAndMin(long time) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(new Date(time));
    }


    @SuppressLint("SimpleDateFormat")
    public static String getTime(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd HH:mm");
        return format.format(new Date(time));
    }

    /**
     * 寮�鍚疺iew闂儊鏁堟灉
     */
    public static void startFlick(View view) {
        if (null == view) {
            return;
        }
        Animation alphaAnimation = new AlphaAnimation(1, 0);
        alphaAnimation.setDuration(300);
        alphaAnimation.setInterpolator(new LinearInterpolator());
        alphaAnimation.setRepeatCount(Animation.INFINITE);
        alphaAnimation.setRepeatMode(Animation.REVERSE);
        view.startAnimation(alphaAnimation);
    }

    /**
     * 鍙栨秷View闂儊鏁堟灉
     */
    public static void stopFlick(View view) {
        if (null == view) {
            return;
        }
        view.clearAnimation();
    }

    // 鍒ゆ柇鎵嬫満鏍煎紡鏄惁姝ｇ‘
    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern
                .compile("^((13[0-9])|(15[^4,\\D])|(18[0,1-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);

        return m.matches();
    }

    // 鍒ゆ柇email鏍煎紡鏄惁姝ｇ‘
    public boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);

        return m.matches();
    }

    /*
     * To convert the InputStream to String we use the BufferedReader.readLine()
     * method. We iterate until the BufferedReader return null which means
     * there's no more data to read. Each line will appended to a StringBuilder
     * and returned as String.
     */
    public static String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }

    /*
     * listView 涓祵濂� listview 鑾峰彇鍏堕珮搴︼紝璁╁叾瀹屽叏鏄剧ず 鐢ㄦ硶锛氬湪璁剧疆ListView鐨凙dapter涔嬪悗杩愯杩欎釜鏂规硶
     */
    public static void setListViewHeight(ListView listView) {

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
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }


    /**
     * 鑾峰彇鏂囦欢鍚庣紑鍚�
     *
     * @param fileName
     * @return 鏂囦欢鍚庣紑鍚�
     */
    public static String getFileType(String fileName) {
        if (fileName != null) {
            int typeIndex = fileName.lastIndexOf(".");
            if (typeIndex != -1) {
                String fileType = fileName.substring(typeIndex + 1)
                        .toLowerCase();
                Log.i("fileType", fileType);
                return fileType;
            }
        }
        return "";
    }

    /*
     * 鍥剧墖鍘嬬缉 鍒嗕袱姝� 1銆佽幏鍙栨枃浠惰矾寰� do something 2銆佽皟鐢╟ompressImage锛堬級 鍘嬬缉鍥剧墖
     */
    public static Bitmap getimage(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 寮�濮嬭鍏ュ浘鐗囷紝姝ゆ椂鎶妎ptions.inJustDecodeBounds 璁惧洖true浜�
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 姝ゆ椂杩斿洖bm涓虹┖

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;

        float hh = 800f;// 杩欓噷璁剧疆楂樺害涓�1280f
        float ww = 400f;// 杩欓噷璁剧疆瀹藉害涓�720f
        // 缂╂斁姣斻�傜敱浜庢槸鍥哄畾姣斾緥缂╂斁锛屽彧鐢ㄩ珮鎴栬�呭鍏朵腑涓�涓暟鎹繘琛岃绠楀嵆鍙�
        int be = 1;// be=1琛ㄧず涓嶇缉鏀�
        if (w > h && w > ww) {// 濡傛灉瀹藉害澶х殑璇濇牴鎹搴﹀浐瀹氬ぇ灏忕缉鏀�
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 濡傛灉楂樺害楂樼殑璇濇牴鎹搴﹀浐瀹氬ぇ灏忕缉鏀�
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 璁剧疆缂╂斁姣斾緥
        // 閲嶆柊璇诲叆鍥剧墖锛屾敞鎰忔鏃跺凡缁忔妸options.inJustDecodeBounds 璁惧洖false浜�
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return compressImage(bitmap);// 鍘嬬缉濂芥瘮渚嬪ぇ灏忓悗鍐嶈繘琛岃川閲忓帇缂�
    }

    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 璐ㄩ噺鍘嬬缉鏂规硶锛岃繖閲�100琛ㄧず涓嶅帇缂╋紝鎶婂帇缂╁悗鐨勬暟鎹瓨鏀惧埌baos涓�
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) { // 寰幆鍒ゆ柇濡傛灉鍘嬬缉鍚庡浘鐗囨槸鍚﹀ぇ浜�100kb,澶т簬缁х画鍘嬬缉
            options -= 10;// 姣忔閮藉噺灏�10
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 杩欓噷鍘嬬缉options%锛屾妸鍘嬬缉鍚庣殑鏁版嵁瀛樻斁鍒癰aos涓�
            baos.reset();// 閲嶇疆baos鍗虫竻绌篵aos
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 鎶婂帇缂╁悗鐨勬暟鎹産aos瀛樻斁鍒癇yteArrayInputStream涓�
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 鎶夿yteArrayInputStream鏁版嵁鐢熸垚鍥剧墖
        return bitmap;
    }

    /**
     * @param root         鏈�澶栧眰甯冨眬锛岄渶瑕佽皟鏁寸殑甯冨眬
     * @param scrollToView 琚敭鐩橀伄鎸＄殑scrollToView锛屾粴鍔╮oot,浣縮crollToView鍦╮oot鍙鍖哄煙鐨勫簳閮�
     */
    public static void controlKeyboardLayout(final View root, final View scrollToView) {
        root.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                //鑾峰彇root鍦ㄧ獥浣撶殑鍙鍖哄煙  
                root.getWindowVisibleDisplayFrame(rect);
                //鑾峰彇root鍦ㄧ獥浣撶殑涓嶅彲瑙嗗尯鍩熼珮搴�(琚叾浠朧iew閬尅鐨勫尯鍩熼珮搴�)  
                int rootInvisibleHeight = root.getRootView().getHeight() - rect.bottom;
                //鑻ヤ笉鍙鍖哄煙楂樺害澶т簬100锛屽垯閿洏鏄剧ず  
                if (rootInvisibleHeight > 100) {
                    int[] location = new int[2];
                    //鑾峰彇scrollToView鍦ㄧ獥浣撶殑鍧愭爣  
                    scrollToView.getLocationInWindow(location);
                    //璁＄畻root婊氬姩楂樺害锛屼娇scrollToView鍦ㄥ彲瑙佸尯鍩�  
                    int srollHeight = (location[1] + scrollToView.getHeight()) - rect.bottom;
                    root.scrollTo(0, srollHeight);
                } else {
                    //閿洏闅愯棌  
                    root.scrollTo(0, 0);
                }
            }
        });
    }

    //鑾峰緱cache璺緞
    @SuppressLint("NewApi")
    public static File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }

    //鑾风殑搴旂敤鐨勭増鏈彿
    public static int getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

    public static String hashKeyForDisk(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    public static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    public static String byteToHex(byte b) {
        String result = Integer.toHexString(b & 0xFF);
        if (result.length() == 1) {
            result = '0' + result;
        }
        return result;
    }


    //鑾峰彇鏂囦欢鍚�
    public static String getFileName(String pathandname) {

        int start = pathandname.lastIndexOf("/");
        int end = pathandname.lastIndexOf(".");
        if (start != -1 && end != -1) {
            return pathandname.substring(start + 1, end);
        } else {
            return null;
        }

    }


    /**
     * 閫掑綊鍒犻櫎鏂囦欢鍜屾枃浠跺す
     *
     * @param file 瑕佸垹闄ょ殑鏍圭洰褰�
     */
    public static void RecursionDeleteFile(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }
        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                file.delete();
                return;
            }
            for (File f : childFile) {
                RecursionDeleteFile(f);
            }
            file.delete();
        }
    }

    //鍒ゆ柇鏂囦欢澶у皬   true:鍥剧墖灏忎簬100K false:鍥剧墖澶т簬100K
    public static boolean isFileSize(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if (baos.toByteArray().length / 1024 > 100) { //鍥剧墖澶т簬100K
            return false;
        }
        return true;
    }

    /**
     * 鐢ㄦ椂闂存埑鐢熸垚鐓х墖鍚嶇О
     *
     * @return
     */
    public static String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "'IMG'_yyyyMMdd_HHmmss");
        return dateFormat.format(date);
    }

    /**
     * Math.pow(...) is very expensive, so avoid calling it and create it
     * yourself.
     */
    private static final int POW_10[] = {
            1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000, 1000000000
    };

    /**
     * Formats the given number to the given number of decimals, and returns the
     * number as a string, maximum 35 characters.
     *
     * @param number
     * @param digitCount
     * @param separateThousands set this to true to separate thousands values
     * @return
     */
    public static String formatNumber(float number, int digitCount, boolean separateThousands) {

        char[] out = new char[35];

        boolean neg = false;
        if (number == 0) {
            return "0";
        }

        boolean zero = false;
        if (number < 1 && number > -1) {
            zero = true;
        }

        if (number < 0) {
            neg = true;
            number = -number;
        }

        if (digitCount > POW_10.length) {
            digitCount = POW_10.length - 1;
        }

        number *= POW_10[digitCount];
        long lval = Math.round(number);
        int ind = out.length - 1;
        int charCount = 0;
        boolean decimalPointAdded = false;

        while (lval != 0 || charCount < (digitCount + 1)) {
            int digit = (int) (lval % 10);
            lval = lval / 10;
            out[ind--] = (char) (digit + '0');
            charCount++;

            // add decimal point
            if (charCount == digitCount) {
                out[ind--] = ',';
                charCount++;
                decimalPointAdded = true;

                // add thousand separators
            } else if (separateThousands && lval != 0 && charCount > digitCount) {

                if (decimalPointAdded) {

                    if ((charCount - digitCount) % 4 == 0) {
                        out[ind--] = '.';
                        charCount++;
                    }

                } else {

                    if ((charCount - digitCount) % 4 == 3) {
                        out[ind--] = '.';
                        charCount++;
                    }
                }
            }
        }

        // if number around zero (between 1 and -1)
        if (zero) {
            out[ind--] = '0';
            charCount += 1;
        }

        // if the number is negative
        if (neg) {
            out[ind--] = '-';
            charCount += 1;
        }

        int start = out.length - charCount;

        // use this instead of "new String(...)" because of issue < Android 4.0
        return String.valueOf(out, start, out.length - start);
    }

    /* 鍥剧墖褰㈢姸鍙樻垚鍦嗗舰 */
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = bitmap.getWidth() / 2;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }



    //读取本地JSON文件  返回字符串
    public static String readLocalJson(Context context, String fileName) {
        String resultString = "";
        try {
            InputStream inputStream = context.getResources().getAssets().open(fileName);
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            resultString = new String(buffer, "UTF-8");
        } catch (Exception e) {
            // TODO: handle exception
        }
        return resultString;
    }

    /**
     * ��URI�л�ȡ�ļ���
     */
    public static String getFileNameFromUrl(final String url) {
        if (TextUtils.isEmpty(url)) {
            return "";
        }
        return url.substring(url.lastIndexOf("/") + 1);
    }

    /**
     * 隐藏软键盘
     * @param activity
     */
    public static void hideSoftKeyboard(Activity activity, View view) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        // 隐藏软键盘
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    public static String getFormatedDateTime(String pattern, long dateTime) {
        SimpleDateFormat sDateFormat = new SimpleDateFormat(pattern);
        return sDateFormat.format(new Date(dateTime + 0));
    }

    /**
     * 字符串转换unicode
     */
    public static String string2Unicode(String string) {

        StringBuffer unicode = new StringBuffer();

        for (int i = 0; i < string.length(); i++) {

            // 取出每一个字符
            char c = string.charAt(i);

            // 转换为unicode
            unicode.append("\\u" + Integer.toHexString(c));
        }

        return unicode.toString();
    }


    /**
     * 判断当前设备是手机还是平板，代码来自 Google I/O App for Android
     * @param context
     * @return 平板返回 True，手机返回 False
     */
    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }


    /*
     * 将时间戳转换为时间
     */
    public static String stampToDate(String s){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    /**
     * 設置View的寬度（像素），若設置爲自適應則應該傳入MarginLayoutParams.WRAP_CONTENT
     *
     * @param view
     * @param width
     */
    public static void setLayoutWidth(View view, int width) {
       /* MarginLayoutParams margin=new MarginLayoutParams(view.getLayoutParams());
        margin.setMargins(margin.leftMargin,y, margin.rightMargin, y+margin.height);
        //RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(margin);
        //view.setLayoutParams(layoutParams);
        ViewGroup.MarginLayoutParams  layoutParams =newLayParms(view, margin);
        //RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(margin);
        view.setLayoutParams(layoutParams);
        view.requestLayout();*/
        if (view.getParent() instanceof FrameLayout) {
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) view.getLayoutParams();
            lp.width = width;
            view.setLayoutParams(lp);
            //view.setX(x);
            view.requestLayout();
        } else if (view.getParent() instanceof RelativeLayout) {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) view.getLayoutParams();
            lp.width = width;
            view.setLayoutParams(lp);
            //view.setX(x);
            view.requestLayout();
        } else if (view.getParent() instanceof LinearLayout) {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) view.getLayoutParams();
            lp.width = width;
            view.setLayoutParams(lp);
            //view.setX(x);
            view.requestLayout();
        }
    }

    /**
     * 設置View的高度（像素），若設置爲自適應則應該傳入MarginLayoutParams.WRAP_CONTENT
     *
     * @param view
     * @param height
     */
    public static void setLayoutHeight(View view, int height) {
       /* MarginLayoutParams margin=new MarginLayoutParams(view.getLayoutParams());
        margin.setMargins(margin.leftMargin,y, margin.rightMargin, y+margin.height);
        //RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(margin);
        //view.setLayoutParams(layoutParams);
        ViewGroup.MarginLayoutParams  layoutParams =newLayParms(view, margin);
        //RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(margin);
        view.setLayoutParams(layoutParams);
        view.requestLayout();*/
        if (view.getParent() instanceof FrameLayout) {
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) view.getLayoutParams();
            lp.height = height;
            view.setLayoutParams(lp);
            //view.setX(x);
            view.requestLayout();
        } else if (view.getParent() instanceof RelativeLayout) {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) view.getLayoutParams();
            lp.height = height;
            view.setLayoutParams(lp);
            //view.setX(x);
            view.requestLayout();
        } else if (view.getParent() instanceof LinearLayout) {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) view.getLayoutParams();
            lp.height = height;
            view.setLayoutParams(lp);
            //view.setX(x);
            view.requestLayout();
        }
    }

}