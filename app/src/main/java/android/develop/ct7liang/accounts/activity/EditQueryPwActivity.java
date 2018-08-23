package android.develop.ct7liang.accounts.activity;

import android.develop.ct7liang.accounts.BaseActivity;
import android.develop.ct7liang.accounts.BaseApp;
import android.develop.ct7liang.accounts.R;
import android.develop.ct7liang.accounts.bean.Query;
import android.develop.ct7liang.accounts.utils.Base64Utils;
import android.develop.ct7liang.accounts.utils.SnackBarUtils;
import android.develop.ct7liang.greendao.QueryDao;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;
import com.ct7liang.tangyuan.utils.ScreenInfoUtil;

import java.util.List;


public class EditQueryPwActivity extends BaseActivity {

    private boolean isRead;
    private PatternLockView patternLockView;
    private QueryDao queryDao;
    private Query query;
    private String pswd;

    @Override
    public int setLayout() {
        return R.layout.activity_update_query_pw;
    }

    @Override
    protected void setStatusBar() {
        View title = findViewById(R.id.title_back_ground);
        title.setBackgroundColor(Color.parseColor("#00000000"));
//        title.setBackgroundColor(Color.parseColor("#787772"));
        title.setPadding(0, ScreenInfoUtil.getStatusHeight(this), 0, 0);
    }

    @Override
    public void findView() {
        initStatusBar();
        ((TextView)findViewById(R.id.center_text)).setText("修改查询密码");
        findViewById(R.id.left_image).setOnClickListener(this);
        patternLockView = (PatternLockView) findViewById(R.id.pattern_lock_view);
        patternLockView.addPatternLockListener(new PatternLockViewListener() {
            @Override
            public void onStarted() {}
            @Override
            public void onProgress(List<PatternLockView.Dot> progressPattern) {}
            @Override
            public void onComplete(List<PatternLockView.Dot> pattern) {
                String s = PatternLockUtils.patternToString(patternLockView, pattern);
                if (!isRead){
                    if (!s.equals(Base64Utils.Base64ToString(query.getQueryPassword()))){
                        patternLockView.setViewMode(PatternLockView.PatternViewMode.WRONG);
                        SnackBarUtils.show(findViewById(R.id.snack), "密码错误", "#DD4E41", new Snackbar.Callback(){
                            @Override
                            public void onDismissed(Snackbar transientBottomBar, int event) {
                                patternLockView.clearPattern();
                            }
                        });
                    }else{
                        isRead = !isRead;
                        patternLockView.setViewMode(PatternLockView.PatternViewMode.CORRECT);
                        SnackBarUtils.show(findViewById(R.id.snack), "请输入新密码", "#82BF23", new Snackbar.Callback(){
                            @Override
                            public void onDismissed(Snackbar transientBottomBar, int event) {
                                patternLockView.clearPattern();
                            }
                        });
                    }
                }else{
                    if (pswd == null){
                        patternLockView.setViewMode(PatternLockView.PatternViewMode.CORRECT);
                        pswd = s;
                        SnackBarUtils.show(findViewById(R.id.snack), "请确认密码", "#82BF23", new Snackbar.Callback(){
                            @Override
                            public void onDismissed(Snackbar transientBottomBar, int event) {
                                patternLockView.clearPattern();
                            }
                        });
                    }else{
                        if (!s.equals(pswd)){
                            pswd = null;
                            patternLockView.setViewMode(PatternLockView.PatternViewMode.WRONG);
                            SnackBarUtils.show(findViewById(R.id.snack), "两次密码不一致, 请重新输入", "#DD4E41", new Snackbar.Callback(){
                                @Override
                                public void onDismissed(Snackbar transientBottomBar, int event) {
                                    patternLockView.clearPattern();
                                }
                            });
                        }else{
                            patternLockView.setViewMode(PatternLockView.PatternViewMode.CORRECT);
                            query.setQueryPassword(Base64Utils.StringToBase64(s));
                            queryDao.update(query);
                            SnackBarUtils.show(findViewById(R.id.snack), "查询密码修改成功", "#82BF23", new Snackbar.Callback(){
                                @Override
                                public void onDismissed(Snackbar transientBottomBar, int event) {
                                    finish();
                                }
                            });
                        }
                    }
                }
            }
            @Override
            public void onCleared() {}
        });
    }

    @Override
    public void initData() {
        queryDao = BaseApp.getDaoSession().getQueryDao();
        query = queryDao.loadAll().get(0);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initFinish() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.left_image:
                finish();
                break;
        }
    }
}