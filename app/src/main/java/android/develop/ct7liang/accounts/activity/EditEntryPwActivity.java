package android.develop.ct7liang.accounts.activity;

import android.develop.ct7liang.accounts.BaseActivity;
import android.develop.ct7liang.accounts.BaseApp;
import android.develop.ct7liang.accounts.R;
import android.develop.ct7liang.accounts.bean.User;
import android.develop.ct7liang.accounts.utils.Base64Utils;
import android.develop.ct7liang.accounts.utils.SnackBarUtils;
import android.develop.ct7liang.greendao.UserDao;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ct7liang.tangyuan.utils.ScreenInfoUtil;


public class EditEntryPwActivity extends BaseActivity {

    private EditText ep;
    private EditText epn;
    private EditText epc;
    private UserDao userDao;
    private User user;

    @Override
    public int setLayout() {
        return R.layout.activity_update_login_pw;
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
        ((TextView)findViewById(R.id.center_text)).setText("修改登录密码");
        findViewById(R.id.left_image).setOnClickListener(this);
        ((TextView)findViewById(R.id.right_text)).setText("确认");
        findViewById(R.id.right).setOnClickListener(this);
        ep = (EditText) findViewById(R.id.password);
        epn = (EditText) findViewById(R.id.password_new);
        epc = (EditText) findViewById(R.id.password_confirm);
    }

    @Override
    public void initData() {
        userDao = BaseApp.getDaoSession().getUserDao();
        user = userDao.loadAll().get(0);
    }

    @Override
    public void initView() {}

    @Override
    public void initFinish() {}

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.left_image:
                finish();
                break;
            case R.id.right:
                String pswd = ep.getText().toString().trim();
                String pswd1 = epn.getText().toString().trim();
                String pswd2 = epc.getText().toString().trim();
                if (TextUtils.isEmpty(pswd)||TextUtils.isEmpty(pswd1)||TextUtils.isEmpty(pswd2)){
                    SnackBarUtils.show(findViewById(R.id.snack), "密码不能为空", "#DD4E41");
                    return;
                }
                if (!pswd.equals(Base64Utils.Base64ToString(user.getPassword()))){
                    SnackBarUtils.show(findViewById(R.id.snack), "密码错误", "#DD4E41");
                    return;
                }
                if (!pswd1.equals(pswd2)){
                    SnackBarUtils.show(findViewById(R.id.snack), "密码不一致", "#DD4E41");
                    return;
                }
                user.setPassword(Base64Utils.StringToBase64(pswd1));
                userDao.update(user);
                SnackBarUtils.show(findViewById(R.id.snack), "密码修改成功", "#82BF23", new Snackbar.Callback(){
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        finish();
                    }
                });
                break;
        }
    }
}
