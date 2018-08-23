package android.develop.ct7liang.accounts.activity;

import android.content.Intent;
import android.develop.ct7liang.accounts.BaseActivity;
import android.develop.ct7liang.accounts.BaseApp;
import android.develop.ct7liang.accounts.R;
import android.develop.ct7liang.accounts.bean.User;
import android.develop.ct7liang.accounts.utils.Base64Utils;
import android.develop.ct7liang.accounts.utils.SnackBarUtils;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ct7liang.tangyuan.utils.ScreenInfoUtil;

public class CreateEntryPwActivity extends BaseActivity {

    private EditText editText01;
    private EditText editText02;

    @Override
    public int setLayout() {
        return R.layout.activity_entry_password_setting;
    }

    @Override
    protected void setStatusBar() {
        View title = findViewById(R.id.title_back_ground);
        title.setBackgroundColor(Color.parseColor("#00000000"));
//        title.setBackgroundColor(Color.parseColor("#787772"));
        title.setPadding(0, ScreenInfoUtil.getStatusHeight(mAct), 0, 0);
    }

    @Override
    public void findView() {
        initStatusBar();
        findViewById(R.id.left_image).setOnClickListener(this);
        ((TextView)findViewById(R.id.center_text)).setText("设置登录密码");
        ((TextView)findViewById(R.id.right_text)).setText("确认");
        findViewById(R.id.right).setOnClickListener(this);
        editText01 = (EditText) findViewById(R.id.password);
        editText02 = (EditText) findViewById(R.id.password_confirm);
    }

    @Override
    public void initData() {

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
                exitApp();
                break;
            case R.id.right:
                String pw01 = editText01.getText().toString().trim();
                String pw02 = editText02.getText().toString().trim();
                if (TextUtils.isEmpty(pw01)||TextUtils.isEmpty(pw02)){
                    SnackBarUtils.show(findViewById(R.id.snack), "密码不能为空", "#DD4E41");
                    return;
                }
                if (!pw01.equals(pw02)){
                    SnackBarUtils.show(findViewById(R.id.snack), "密码不一致", "#DD4E41");
                    return;
                }
                BaseApp.getDaoSession().getUserDao().deleteAll();
                User user = new User(null, Base64Utils.StringToBase64(pw01));
                BaseApp.getDaoSession().insert(user);
                startActivity(new Intent(this, CreateQueryPwActivity.class));
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        exitApp();
    }
}