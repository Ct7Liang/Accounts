package android.develop.ct7liang.accounts.activity;

import android.app.Dialog;
import android.content.Intent;
import android.develop.ct7liang.accounts.BaseActivity;
import android.develop.ct7liang.accounts.BaseApp;
import android.develop.ct7liang.accounts.R;
import android.develop.ct7liang.accounts.bean.Account;
import android.develop.ct7liang.accounts.utils.Base64Utils;
import android.develop.ct7liang.accounts.utils.SnackBarUtils;
import android.develop.ct7liang.greendao.AccountDao;
import android.develop.ct7liang.greendao.QueryDao;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;
import com.ct7liang.tangyuan.utils.ScreenInfoUtil;

import java.util.List;


public class AccountDetailActivity extends BaseActivity {

    private Account account;
    private View a;
    private View b;
    private TextView tvA;
    private TextView tvP;
    private TextView tvR;
    private PatternLockView patternLockView;
    private AccountDao accountDao;
    private QueryDao queryDao;
    private Dialog d;

    @Override
    public int setLayout() {
        return R.layout.activity_account_detail;
    }

    @Override
    protected void setStatusBar() {
        View title = findViewById(R.id.title_back_ground);
        title.setBackgroundColor(Color.parseColor("#00000000"));
//        title.setBackgroundColor(Color.parseColor("#335860"));
        title.setPadding(0, ScreenInfoUtil.getStatusHeight(this), 0, 0);
    }

    @Override
    public void findView() {
        initStatusBar();
        findViewById(R.id.left_image).setOnClickListener(this);
        findViewById(R.id.delete).setOnClickListener(this);
        findViewById(R.id.update).setOnClickListener(this);
        a = findViewById(R.id.a);
        b = findViewById(R.id.b);
        tvA = (TextView) findViewById(R.id.account);
        tvP = (TextView) findViewById(R.id.password);
        tvR = (TextView) findViewById(R.id.remark);
        patternLockView = (PatternLockView) findViewById(R.id.pattern_lock_view);
        patternLockView.addPatternLockListener(new PatternLockViewListener() {
            @Override
            public void onStarted() {}
            @Override
            public void onProgress(List<PatternLockView.Dot> progressPattern) {}
            @Override
            public void onComplete(List<PatternLockView.Dot> pattern) {
                String s = PatternLockUtils.patternToString(patternLockView, pattern);
                if (s.equals(Base64Utils.Base64ToString(queryDao.loadAll().get(0).getQueryPassword()))){
                    a.setVisibility(View.GONE);
                    b.setVisibility(View.VISIBLE);
                }else{
                    patternLockView.setViewMode(PatternLockView.PatternViewMode.WRONG);
                    SnackBarUtils.show(findViewById(R.id.snack), "查询密码错误", "#DD4E41", new Snackbar.Callback(){
                        @Override
                        public void onDismissed(Snackbar transientBottomBar, int event) {
                            patternLockView.clearPattern();
                        }
                    });
                }
            }
            @Override
            public void onCleared() {}
        });
    }

    @Override
    public void initData() {
        account = (Account) getIntent().getSerializableExtra("bean");
        if (account==null){
            finish();
        }
        accountDao = BaseApp.getDaoSession().getAccountDao();
        queryDao = BaseApp.getDaoSession().getQueryDao();
    }

    @Override
    public void initView() {
        a.setVisibility(View.VISIBLE);
        b.setVisibility(View.GONE);
        ((TextView)findViewById(R.id.center_text)).setText(Base64Utils.Base64ToString(account.getTag()));
        tvA.setText("账号: " + Base64Utils.Base64ToString(account.getAccount()));
        tvP.setText("密码: " + Base64Utils.Base64ToString(account.getPassword()));
        String remark = account.getRemark();
        if (remark!=null){
            tvR.setText("备注: " + Base64Utils.Base64ToString(remark));
        }
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
            case R.id.delete:
                showDeleteWindow();
                //删除
                break;
            case R.id.update:
                //修改
                Intent i = new Intent(this, EditAccountActivity.class);
                i.putExtra("bean", account);
                startActivityForResult(i, 111);
                break;
            case R.id.cancel:
                d.dismiss();
                break;
            case R.id.confirm:
                accountDao.delete(account);
                d.dismiss();
                SnackBarUtils.show(findViewById(R.id.snack), "删除成功", "#82BF23", new Snackbar.Callback(){
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        finish();
                    }
                });
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 222){
            account = (Account) data.getSerializableExtra("bean");
            ((TextView)findViewById(R.id.center_text)).setText(Base64Utils.Base64ToString(account.getTag()));
            tvA.setText("账号: " + Base64Utils.Base64ToString(account.getAccount()));
            tvP.setText("密码: " + Base64Utils.Base64ToString(account.getPassword()));
            tvR.setText("备注: " + Base64Utils.Base64ToString(account.getRemark()));
            SnackBarUtils.show(findViewById(R.id.snack), "修改成功", "#82BF23");
        }
        if (resultCode == 333){
            SnackBarUtils.show(findViewById(R.id.snack), "修改操作已取消", "#82BF23");
        }
    }

    private void showDeleteWindow() {
        d = new Dialog(this);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.setCanceledOnTouchOutside(false);
        View view = View.inflate(this, R.layout.window_delete, null);
        view.findViewById(R.id.cancel).setOnClickListener(this);
        view.findViewById(R.id.confirm).setOnClickListener(this);
        LinearLayout.LayoutParams l = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        d.addContentView(view, l);
        d.show();
    }
}
