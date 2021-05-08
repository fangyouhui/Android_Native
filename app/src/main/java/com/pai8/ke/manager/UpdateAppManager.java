package com.pai8.ke.manager;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentActivity;

import com.pai8.ke.R;
import com.pai8.ke.app.MyApp;
import com.pai8.ke.base.BaseEvent;
import com.pai8.ke.entity.event.DownStatusEvent;
import com.pai8.ke.entity.resp.VersionResp;
import com.pai8.ke.fragment.DownLoadProgressDialogFragment;
import com.pai8.ke.global.EventCode;
import com.pai8.ke.utils.AppUtils;
import com.pai8.ke.utils.EventBusUtils;
import com.pai8.ke.utils.FileUtils;
import com.pai8.ke.utils.ResUtils;
import com.pai8.ke.utils.StringUtils;
import com.pai8.ke.utils.ToastUtils;
import com.pai8.ke.widget.CommonDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 更新/下载Apk管理类
 * Created by gh on 2020/12/20.
 */
public class UpdateAppManager extends IntentService {

    private Context mContext;

    private NotificationManager mNotifiManager;
    private NotificationCompat.Builder mNotifiBuilder;

    private Map<Integer, NotificationCompat.Builder> mNotifiBuildMap;

    private int mNotifiId = 0xff99;
    private int mProgress;

    private String mApkUrl;
    private String mApkFilePath;
    private boolean isShowProgressdialog;

    public static final String CHANNEL_ID = MyApp.getMyApp().getPackageName();

    public UpdateAppManager() {
        super("UpdateAppManager");
    }

    public static void startDownLoadService(Context context, String url, boolean isShowProgressDialog) {
        boolean serviceRunning = AppUtils.isServiceRunning(context, UpdateAppManager.class.getName());
        if (serviceRunning) {
            ToastUtils.showShort(context, "已经在下载App中...");
            return;
        }
        ToastUtils.showShort(context, "开始下载...");
        Intent serviceIntent = new Intent(context, UpdateAppManager.class);
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        bundle.putBoolean("showprogressdialog", isShowProgressDialog);
        serviceIntent.putExtras(bundle);
        context.startService(serviceIntent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        Bundle bundle = intent.getExtras();
        mApkUrl = bundle.getString("url");
        isShowProgressdialog = bundle.getBoolean("showprogressdialog");

        mContext = MyApp.getMyApp().getApplicationContext();
        mNotifiManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //适配android 8.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_ID, NotificationManager
                    .IMPORTANCE_DEFAULT);
            channel.setDescription(CHANNEL_ID);
            mNotifiManager.createNotificationChannel(channel);
        }
        mNotifiBuildMap = new HashMap<>();
        showNotification();
        //同步下载
        try {
            Response response = new OkHttpClient().newCall(new Request.Builder().url(mApkUrl).build()).execute();
            if (!response.isSuccessful()) {
                updateError();
                return;
            }
            if (!writeRespBodyToDisk(response.body())) {
                updateError();
                return;
            }
            installApk();
        } catch (Exception e) {
            e.printStackTrace();
            updateError();
        }

    }

    /**
     * 下载更新失败
     */
    private void updateError() {
        cancel(mNotifiId);
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                ToastUtils.showShort(mContext, "下载更新失败");
            }
        });
    }

    /**
     * 显示通知
     */
    public void showNotification() {
        if (mNotifiBuildMap.containsKey(mNotifiId)) {
            return;
        }
        try {
            ToastUtils.showShort(mContext, "开始下载...");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (isShowProgressdialog) {
            DownStatusEvent downStatusEvent = new DownStatusEvent();
            downStatusEvent.setStatus(DownStatusEvent.DOWN_START);
            EventBusUtils.sendEvent(new BaseEvent(EventCode.EVENT_UPDATE, downStatusEvent));
        }
        mProgress = 0;
        mNotifiBuilder = new NotificationCompat.Builder(this, CHANNEL_ID);
        mNotifiBuilder.setContentTitle(ResUtils.getText(R.string.app_name) + "更新中...");
        mNotifiBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mNotifiBuilder.setWhen(System.currentTimeMillis());
        mNotifiBuilder.setOnlyAlertOnce(true);
        mNotifiBuilder.setAutoCancel(true);
        mNotifiBuilder.setProgress(100, 0, false);
        mNotifiManager.notify(mNotifiId, mNotifiBuilder.build());
        mNotifiBuildMap.put(mNotifiId, mNotifiBuilder);
    }

    /**
     * 取消通知操作
     *
     * @param notificationId
     */
    public void cancel(int notificationId) {
        mNotifiManager.cancel(notificationId);
        mNotifiBuildMap.remove(notificationId);
        if (isShowProgressdialog) {
            DownStatusEvent downStatusEvent = new DownStatusEvent();
            downStatusEvent.setStatus(DownStatusEvent.DOWN_COMPLETE);
            EventBusUtils.sendEvent(new BaseEvent(EventCode.EVENT_UPDATE, downStatusEvent));
        }
    }

    /**
     * 显示进度
     *
     * @param progress
     */
    public void updateProgress(int progress) {
        this.mProgress = progress;
        NotificationCompat.Builder notifyBuild = mNotifiBuildMap.get(mNotifiId);
        if (notifyBuild == null) {
            return;
        }
        mNotifiBuilder.setProgress(100, progress, false);
        mNotifiManager.notify(mNotifiId, notifyBuild.build());
        if (isShowProgressdialog) {
            DownStatusEvent downStatusEvent = new DownStatusEvent();
            downStatusEvent.setProgress(progress);
            downStatusEvent.setStatus(DownStatusEvent.DOWN_LODING);
            EventBusUtils.sendEvent(new BaseEvent(EventCode.EVENT_UPDATE, downStatusEvent));
        }
    }

    /**
     * 安装apk
     */
    public void installApk() {
        cancel(mNotifiId);
        Intent install = new Intent(Intent.ACTION_INSTALL_PACKAGE);
        if (StringUtils.isEmpty(mApkFilePath)) {
            return;
        }
        if (Build.VERSION.SDK_INT >= 24) { // 适配安卓7.0
            install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri apkFileUri = FileProvider.getUriForFile(mContext, mContext.getPackageName() + ".util" +
                    ".MyProvider", new File(mApkFilePath));
            install.addCategory("android.intent.category.DEFAULT");
            install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加Flag 表示我们需要什么权限

            install.setDataAndType(apkFileUri, "application/vnd.android.package-archive");
        } else {
            install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            install.setAction(Intent.ACTION_VIEW);
            install.setDataAndType(Uri.parse("file://" + mApkFilePath), "application/vnd.android" +
                    ".package-archive"); // File.toString()会返回路径信息
        }
        mContext.startActivity(install);
        mNotifiBuilder.setContentIntent(PendingIntent.getActivity(mContext, mNotifiId, install,
                PendingIntent.FLAG_UPDATE_CURRENT));
    }

    /**
     * 写入到sd卡
     *
     * @param body
     * @return
     */
    private boolean writeRespBodyToDisk(ResponseBody body) {
        File file = FileUtils.createApkFile(mApkUrl.substring(mApkUrl.lastIndexOf("/") + 1) + ".apk");
        mApkFilePath = file.getAbsolutePath();
        try {
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                byte[] fileReader = new byte[4096];
                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;
                inputStream = body.byteStream();
                outputStream = new FileOutputStream(file);
                while (true) {
                    int read = inputStream.read(fileReader);
                    if (read == -1) {
                        break;
                    }
                    outputStream.write(fileReader, 0, read);
                    fileSizeDownloaded += read;
                    int pro = (int) (fileSizeDownloaded * 100 / fileSize);
                    if (pro > mProgress) {
                        mProgress = pro;
                        updateProgress(pro);
                    }
                }
                outputStream.flush();
                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }


    /**
     * 显示更新对话框
     *
     * @param context
     * @param data
     */
    public static void showUpdateDialog(final Context context, final VersionResp data) {
        if (data == null || data.getUpgrade() == 0) return;
        //0-无更新 1-普通更新 2-强制更新
        View view = View.inflate(context, R.layout.view_dialog_upgrade, null);
        TextView tvTip = view.findViewById(R.id.tv_tip);
        TextView tvVersion = view.findViewById(R.id.tv_version);
        TextView tvContent = view.findViewById(R.id.tv_content);
        Button btnUpgrade = view.findViewById(R.id.btn_upgrade);
        ImageView ivBtnClose = view.findViewById(R.id.iv_btn_close);
        tvVersion.setText("v" + data.getCurrent_version());
        CommonDialog commonDialog = new CommonDialog(context, view);

        if (StringUtils.isNotEmpty(data.getContent())) {
            tvContent.setText(data.getContent());
        }

        btnUpgrade.setOnClickListener(view1 -> {
            if (data.getUpgrade() == 1) {
                commonDialog.dismiss();
                startDownLoadService(context, data.getUpgrade_url(), false);
            } else {
                startDownLoadService(context, data.getUpgrade_url(), true);
                if (context instanceof FragmentActivity) {
                    FragmentActivity activity = (FragmentActivity) context;
                    DownLoadProgressDialogFragment loadProgressDialogFragment = new DownLoadProgressDialogFragment();
                    loadProgressDialogFragment.show(activity.getSupportFragmentManager(), "loadProgressDialogFragment");
                }
            }
            commonDialog.dismiss();
        });
        commonDialog.show();
        if (data.getUpgrade() == 1) {
            commonDialog.setCancelable(true);
            ivBtnClose.setVisibility(View.VISIBLE);
        } else {
            commonDialog.setCancelable(false);
            ivBtnClose.setVisibility(View.INVISIBLE);
        }
        commonDialog.setIsCanceledOnTouchOutside(false);
    }


}
