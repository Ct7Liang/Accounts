package android.develop.ct7liang.accounts;

import android.app.Application;
import android.develop.ct7liang.crashlog.CrashHandler;
import android.develop.ct7liang.greendao.DaoMaster;
import android.develop.ct7liang.greendao.DaoSession;
import com.ct7liang.tangyuan.AppFolder;
import com.ct7liang.tangyuan.utils.LogUtils;
import com.ct7liang.tangyuan.utils.SpUtils;
import com.ct7liang.tangyuan.utils.ToastUtils;
import org.greenrobot.greendao.database.Database;
import java.io.File;

/**
 * Created by Administrator on 2018-02-22.
 *
 */
public class BaseApp extends Application {

    private static DaoSession dao;

    @Override
    public void onCreate() {
        super.onCreate();

        SpUtils.init(this);
        ToastUtils.setIsShowTestEnable(true);
        LogUtils.setLogEnable(true);
        LogUtils.setShowLocationEnable(false);
        LogUtils.setTag("Account");
        AppFolder.createAppFolder("Accounts");
        createSplashFolder();

        File crashFolder = new File(AppFolder.get()+"crash_log");
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext(), new File(AppFolder.get()+"/crash_log"));

        initDataBase();
    }

    private void initDataBase() {
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(this, "account_safer");
        Database db = devOpenHelper.getWritableDb();
        DaoMaster daoMaster = new DaoMaster(db);
        dao = daoMaster.newSession();
    }

    public static DaoSession getDaoSession(){
        return dao;
    }

    private void createSplashFolder(){
        File dir = AppFolder.get();
        if (dir.exists()){
            File dis_s = new File(dir, "/splash");
            if (!dis_s.exists()){
                dis_s.mkdirs();
            }
        }
    }

    public static File getSplashFolder(){
        File dir = AppFolder.get();
        if (!dir.exists()){
            return null;
        }else{
            return new File(dir, "/splash");
        }
    }
}
