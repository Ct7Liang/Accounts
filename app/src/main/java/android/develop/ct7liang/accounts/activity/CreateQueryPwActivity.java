package android.develop.ct7liang.accounts.activity;

import android.content.Intent;
import android.develop.ct7liang.accounts.BaseActivity;
import android.develop.ct7liang.accounts.BaseApp;
import android.develop.ct7liang.accounts.R;
import android.develop.ct7liang.accounts.bean.Query;
import android.develop.ct7liang.accounts.utils.Base64Utils;
import android.develop.ct7liang.accounts.utils.Constant;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;
import com.ct7liang.tangyuan.utils.ScreenInfoUtil;
import com.ct7liang.tangyuan.utils.SpUtils;
import com.ct7liang.tangyuan.utils.ToastUtils;

import java.util.List;

public class CreateQueryPwActivity extends BaseActivity {

    private PatternLockView mPatternLockView;
    private String tempPassword;

    @Override
    public int setLayout() {
        return R.layout.activity_query_password_setting;
    }

    @Override
    protected void setStatusBar() {
        View title = findViewById(R.id.title_back_ground);
        title.setBackgroundColor(Color.parseColor("#00000000"));
//        title.setBackgroundColor(Color.parseColor("#787772"));
        title.setPadding(0, ScreenInfoUtil.getStatusHeight(mAct),0 ,0);
    }

    @Override
    public void findView() {
        initStatusBar();
        ((TextView)findViewById(R.id.center_text)).setText("设置查询密码");
        findViewById(R.id.left_image).setOnClickListener(this);
        mPatternLockView = (PatternLockView) findViewById(R.id.pattern_lock_view);
        mPatternLockView.addPatternLockListener(new PatternLockViewListener() {
            @Override
            public void onStarted() {}
            @Override
            public void onProgress(List<PatternLockView.Dot> progressPattern) {}
            @Override
            public void onComplete(List<PatternLockView.Dot> pattern) {
                if (tempPassword ==null){
                    tempPassword = PatternLockUtils.patternToString(mPatternLockView, pattern);
                    if (tempPassword.length()<4){
                        ToastUtils.showStatic(mAct, "密码长度过短");
                        mPatternLockView.clearPattern();
                        tempPassword = null;
                        return;
                    }
                    mPatternLockView.clearPattern();
                    ToastUtils.showStatic(mAct, "请再次确认密码");
                }else{
                    String s = PatternLockUtils.patternToString(mPatternLockView, pattern);
                    if (s.equals(tempPassword)){
                        BaseApp.getDaoSession().getQueryDao().deleteAll();
                        Query query = new Query(null, Base64Utils.StringToBase64(s));
                        BaseApp.getDaoSession().getQueryDao().insert(query);
                        SpUtils.start().saveInt(Constant.IsNewEntry, 1);
                        startActivity(new Intent(mAct, EntryActivity.class));
                        finish();
                    }else{
                        tempPassword = null;
                        mPatternLockView.clearPattern();
                        ToastUtils.showStatic(mAct, "密码设置不一致, 请重新设置");
                    }
                }
            }
            @Override
            public void onCleared() {}
        });
    }

    @Override
    public void initData() {}

    @Override
    public void initView() {}

    @Override
    public void initFinish() {}

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.left_image:
                exitApp();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        exitApp();
    }
}
