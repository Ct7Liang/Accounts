package android.develop.ct7liang.accounts.activity;

import android.app.Dialog;
import android.develop.ct7liang.accounts.BaseActivity;
import android.develop.ct7liang.accounts.BaseApp;
import android.develop.ct7liang.accounts.R;
import android.develop.ct7liang.accounts.bean.Account;
import android.develop.ct7liang.accounts.bean.BackUp;
import android.develop.ct7liang.accounts.utils.Base64Utils;
import android.develop.ct7liang.accounts.utils.Constant;
import android.develop.ct7liang.accounts.utils.FileUtils;
import android.develop.ct7liang.accounts.utils.SnackBarUtils;
import android.develop.ct7liang.greendao.AccountDao;
import android.develop.ct7liang.greendao.QueryDao;
import android.develop.ct7liang.greendao.UserDao;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;
import com.ct7liang.tangyuan.AppFolder;
import com.ct7liang.tangyuan.utils.ScreenInfoUtil;
import com.ct7liang.tangyuan.utils.ToastUtils;
import com.ct7liang.tangyuan.utils.loading.LoadingDialog;
import com.ct7liang.tangyuan.utils.loading.ZddDialog;
import com.google.gson.Gson;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.List;


public class DataBackUpActivity extends BaseActivity {

    private QueryDao queryDao;
    private UserDao userDao;
    private AccountDao accountDao;
    private BackUp backUp; //导入数据时使用,用于记录需要导入的数据内容
    private List<Account> accounts;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    LoadingDialog.dismiss();
                    SnackBarUtils.show(findViewById(R.id.snack), "数据备份成功", "#82BF23");
                    break;
                case 1:
                    LoadingDialog.dismiss();
                    SnackBarUtils.show(findViewById(R.id.snack), "数据导入成功", "#82BF23");
                    break;
                case 2:
                    LoadingDialog.dismiss();
                    SnackBarUtils.show(findViewById(R.id.snack), "数据备份已取消", "#DD4E41");
                    break;
                case 3:
                    LoadingDialog.dismiss();
                    SnackBarUtils.show(findViewById(R.id.snack), "数据导入已取消", "#DD4E41");
                    break;
            }
        }
    };
    private Dialog entryDialog;
    private Dialog queryDialog;
    private EditText et_entry;
    private File dirs;  //同步数据文件夹
    private PatternLockView patternLockView;
    private boolean backupTag = true;
    private boolean intoTag = true;
    private File dis;
    private boolean isYuanShuJu;

    @Override
    public int setLayout() {
        return R.layout.activity_data_back_up;
    }

    @Override
    protected void setStatusBar() {
        View title = findViewById(R.id.title_back_ground);
        title.setBackgroundColor(Color.parseColor("#00000000"));
        title.setPadding(0, ScreenInfoUtil.getStatusHeight(this), 0, 0);
    }

    @Override
    public void findView() {
        initStatusBar();
        findViewById(R.id.left_image).setOnClickListener(this);
        ((TextView)findViewById(R.id.center_text)).setText("数据备份");
        findViewById(R.id.start).setOnClickListener(this);
        findViewById(R.id.into).setOnClickListener(this);
    }

    @Override
    public void initData() {
        File file = AppFolder.get();
        if (file==null){
            SnackBarUtils.show(findViewById(R.id.snack), "本地数据操作权限关闭,操作无法进行");
        }else{
            File dir = new File(file, "/backups");
            if (!dir.exists()){
                dir.mkdirs();
            }
            dirs = new File(file, "/fromBackups");
            if (!dirs.exists()){
                dirs.mkdirs();
            }
        }
        queryDao = BaseApp.getDaoSession().getQueryDao();
        userDao = BaseApp.getDaoSession().getUserDao();
        accountDao = BaseApp.getDaoSession().getAccountDao();
        backUp = new BackUp();
        accounts = accountDao.loadAll();
    }

    @Override
    public void initView() {}

    @Override
    public void initFinish() {}

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_image:
                //返回
                finish();
                break;
            case R.id.start:
                //点击开始备份
                backup();
                break;
            case R.id.into:
                //点击开始导入
                fromBackup();
                break;
            case R.id.cancel:
                //校验登录密码取消按钮
                entryDialog.dismiss();
                break;
            case R.id.confirm:
                //校验登录密码确定按钮
                String string = et_entry.getText().toString().trim();
                if (TextUtils.isEmpty(string)){
                    return;
                }
                if (Base64Utils.StringToBase64(string).equals(backUp.entryPswd)){
                    entryDialog.dismiss();
                    showCheckQueryPswd();
                }else{
                    ToastUtils.showStatic(mAct, "密码错误");
                }
                break;
            case R.id.close:
                //关闭校验查询密码窗口
                queryDialog.dismiss();
                break;
        }
    }

    /**
     * 数据备份
     * 1.检查是否授予读写SD卡权限
     * 2.检查是否有数据可以备份
     */
    private void backup() {
        backupTag = true;
        if (AppFolder.get()==null){
            SnackBarUtils.show(findViewById(R.id.snack), "读写SD卡权限关闭,操作无法进行");
            return;
        }
        if (accounts.size()==0){
            SnackBarUtils.show(findViewById(R.id.snack), "暂无数据可以备份");
            return;
        }
        LoadingDialog.show(this, false, R.layout.window_progress, false,
                ScreenInfoUtil.getScreenWH(this)[0] / 3,
                ScreenInfoUtil.getScreenWH(this)[0] / 3,
                new ZddDialog.OnBackPressed() {
                    @Override
                    public void onBackPressed() {
                        backupTag = false;
                    }
                }, null);
        new Thread(new BackUpData()){}.start();
    }

    /**
     * 导入数据前
     * 1.首先判断可供导入的数据以及所在文件夹是否存在
     * 2.校验是否是旧版本数据加密
     * 3.将数据记录至缓存中备用
     */
    private void fromBackup() {
        if (AppFolder.get()==null){
            SnackBarUtils.show(findViewById(R.id.snack), "本地数据操作权限关闭,操作无法进行");
            return;
        }
        if (dirs.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                if (pathname.getPath().endsWith(".7")){
                    return true;
                }
                return false;
            }
        }).length==0){
            SnackBarUtils.show(findViewById(R.id.snack), "目标文件夹无相关数据", "#DD4E41");
            return;
        }else{
            File[] files = dirs.listFiles();
            dis = null;
            for (File file : files) {
                if (file.getPath().endsWith(".7")) {
                    dis = file;
                    break;
                }
            }
            if (dis ==null){
                //没有备份文件可供数据导入,直接返回
                return;
            }
            String replace = FileUtils.read(dis);
            String content = replace.replace(Constant.HEADER, "");
            if (content.equals(replace)){
                //说明这是旧版本加密数据数据
                isYuanShuJu = true;
                //针对旧版本备份数据, 用旧版本解密方式进行解密
                backUp = new Gson().fromJson(Base64Utils.base642string(content), BackUp.class);
                //针对旧版本加密数据, 需要对密码进行解密和重新加密
                backUp.entryPswd = Base64Utils.StringToBase64(Base64Utils.base642string(backUp.entryPswd));
                backUp.queryPswd = Base64Utils.StringToBase64(Base64Utils.base642string(backUp.queryPswd));
            }else{
                backUp = new Gson().fromJson(Base64Utils.Base64ToString(content), BackUp.class);
            }
            showCheckEntryPswd();
        }
    }

    /**
     * 数据导入成功后删除备份文件
     */
    private void deleteFiles(File dir) {
        File[] files;
        while(true){
            files = dir.listFiles();
            if (files.length!=0){
                files[0].delete();
            }else{
                break;
            }
        }
    }

    /**
     * 校验登录密码
     */
    private void showCheckEntryPswd(){
        entryDialog = new Dialog(this);
        entryDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        entryDialog.setCanceledOnTouchOutside(false);
        View view = View.inflate(this, R.layout.window_check_entry_pswd, null);
        et_entry = (EditText) view.findViewById(R.id.entry_password);
        view.findViewById(R.id.confirm).setOnClickListener(this);
        view.findViewById(R.id.cancel).setOnClickListener(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ScreenInfoUtil.getScreenWH(this)[0]/6*5, ViewGroup.LayoutParams.WRAP_CONTENT);
        entryDialog.addContentView(view, layoutParams);
        entryDialog.show();
    }

    /**
     * 校验查询密码
     */
    private void showCheckQueryPswd(){
        queryDialog = new Dialog(this);
        queryDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        queryDialog.setCanceledOnTouchOutside(false);
        View view = View.inflate(this, R.layout.window_check_query_pswd, null);
        patternLockView = (PatternLockView) view.findViewById(R.id.pattern_lock_view);
        view.findViewById(R.id.close).setOnClickListener(this);
        patternLockView.addPatternLockListener(new PatternLockViewListener() {
            @Override
            public void onStarted() {}
            @Override
            public void onProgress(List<PatternLockView.Dot> progressPattern) {}
            @Override
            public void onComplete(List<PatternLockView.Dot> pattern) {
                String s = PatternLockUtils.patternToString(patternLockView, pattern);
                if (Base64Utils.StringToBase64(s).equals(backUp.queryPswd)){
                    queryDialog.dismiss();
                    doBackup();
                }else{
                    patternLockView.setViewMode(PatternLockView.PatternViewMode.WRONG);
                }
            }
            @Override
            public void onCleared() {}
        });
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ScreenInfoUtil.getScreenWH(this)[0]/7*6, ViewGroup.LayoutParams.WRAP_CONTENT);
        queryDialog.addContentView(view, layoutParams);
        queryDialog.show();
    }

    /**
     * 数据导入
     */
    private void doBackup() {
        intoTag = true;
        LoadingDialog.show(this, false, R.layout.window_progress, false,
                ScreenInfoUtil.getScreenWH(this)[0] / 3,
                ScreenInfoUtil.getScreenWH(this)[0] / 3,
                new ZddDialog.OnBackPressed() {
                    @Override
                    public void onBackPressed() {
                        intoTag = false;
                    }
                }, null);

        if (isYuanShuJu){
            new Thread(new IntoOldData()).start();
        }else{
            new Thread(new IntoNewData()).start();
        }
    }

    /**
     * 导入旧版本数据
     */
    private class IntoOldData implements Runnable {
        @Override
        public void run() {
            Account account;
            for (int i = 0; i < backUp.list.size(); i++) {
                if (!intoTag){
                    handler.sendEmptyMessage(3);
                    return;
                }
                account = new Account();
                account.setTag(Base64Utils.StringToBase64(Base64Utils.base642string(backUp.list.get(i).getTag())));
                account.setAccount(Base64Utils.StringToBase64(Base64Utils.base642string(backUp.list.get(i).getAccount())));
                account.setPassword(Base64Utils.StringToBase64(Base64Utils.base642string(backUp.list.get(i).getPassword())));
                String remark = backUp.list.get(i).getRemark();
                if (remark!=null){
                    account.setRemark(Base64Utils.StringToBase64(Base64Utils.base642string(remark)));
                }
                accountDao.insert(account);
            }
            dis.delete();
            SystemClock.sleep(500);
            handler.sendEmptyMessage(1);
        }
    }

    /**
     * 导入新版本数据
     */
    private class IntoNewData implements Runnable{
        @Override
        public void run() {
            Account account;
            for (int i = 0; i < backUp.list.size(); i++) {
                if (!intoTag){
                    handler.sendEmptyMessage(3);
                    return;
                }
                account = new Account();
                account.setTag(backUp.list.get(i).getTag());
                account.setAccount(backUp.list.get(i).getAccount());
                account.setPassword(backUp.list.get(i).getPassword());
                String remark = backUp.list.get(i).getRemark();
                if (remark!=null){
                    account.setRemark(remark);
                }
                accountDao.insert(account);
            }
            dis.delete();
            SystemClock.sleep(500);
            handler.sendEmptyMessage(1);
        }
    }

    /**
     * 备份数据
     */
    private class BackUpData implements Runnable{
        @Override
        public void run() {
            BackUp backUp1 = new BackUp();
            backUp1.entryPswd = userDao.loadAll().get(0).getPassword();
            backUp1.queryPswd = queryDao.loadAll().get(0).getQueryPassword();
            backUp1.list = accounts;
            String s = Constant.HEADER+Base64Utils.StringToBase64(new Gson().toJson(backUp1));
            File dir = new File(AppFolder.get(), "/backups");
            if (dir.listFiles().length>0){
                deleteFiles(dir);
            }
            File file = new File(dir, System.currentTimeMillis()+".txt");
            try {
                file.createNewFile();
                FileUtils.write(file.getPath(), s);
                file.renameTo(new File(dir, System.currentTimeMillis()+".7"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            SystemClock.sleep(500);
            if (!backupTag){
                deleteFiles(dir);
                handler.sendEmptyMessage(2);
                return;
            }
            handler.sendEmptyMessage(0);
        }
    }
}