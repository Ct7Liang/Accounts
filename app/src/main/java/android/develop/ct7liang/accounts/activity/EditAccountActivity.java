package android.develop.ct7liang.accounts.activity;

import android.content.Intent;
import android.develop.ct7liang.accounts.BaseActivity;
import android.develop.ct7liang.accounts.BaseApp;
import android.develop.ct7liang.accounts.R;
import android.develop.ct7liang.accounts.bean.Account;
import android.develop.ct7liang.accounts.utils.Base64Utils;
import android.develop.ct7liang.accounts.utils.SnackBarUtils;
import android.develop.ct7liang.greendao.AccountDao;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ct7liang.tangyuan.utils.ScreenInfoUtil;


public class EditAccountActivity extends BaseActivity {

    private EditText eT;
    private EditText eA;
    private EditText eP;
    private EditText eR;
    private Account account;
    private AccountDao accountDao;

    @Override
    public int setLayout() {
        return R.layout.activity_edit_account;
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
        ((TextView)findViewById(R.id.center_text)).setText("修改账号信息");
        findViewById(R.id.left_image).setOnClickListener(this);
        ((TextView)findViewById(R.id.right_text)).setText("确认");
        findViewById(R.id.right).setOnClickListener(this);
        eT = (EditText) findViewById(R.id.tag);
        eA = (EditText) findViewById(R.id.account);
        eP = (EditText) findViewById(R.id.password);
        eR = (EditText) findViewById(R.id.remark);
//        findViewById(R.id.confirm).setOnClickListener(this);
    }

    @Override
    public void initData() {
        account = (Account) getIntent().getSerializableExtra("bean");
        accountDao = BaseApp.getDaoSession().getAccountDao();
    }

    @Override
    public void initView() {
        eT.setText(Base64Utils.Base64ToString(account.getTag()));
        eA.setText(Base64Utils.Base64ToString(account.getAccount()));
        eP.setText(Base64Utils.Base64ToString(account.getPassword()));
        String remark = account.getRemark();
        if (remark!=null){
            eR.setText(Base64Utils.Base64ToString(account.getRemark()));
        }
    }

    @Override
    public void initFinish() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.left_image:
                Intent ii = new Intent();
                setResult(333, ii);
                finish();
                break;
            case R.id.right:
                String t = eT.getText().toString().trim();
                String a = eA.getText().toString().trim();
                String p = eP.getText().toString().trim();
                String r = eR.getText().toString().trim();
                if (TextUtils.isEmpty(t)||TextUtils.isEmpty(a)||TextUtils.isEmpty(p)){
                    SnackBarUtils.show(findViewById(R.id.snack), "请将内容输入完整", "#DD4E41");
                    return;
                }
                if (!TextUtils.isEmpty(r)){
                    account.setRemark(Base64Utils.StringToBase64(r));
                }else{
                    account.setRemark(Base64Utils.StringToBase64(""));
                }
                account.setTag(Base64Utils.StringToBase64(t));
                account.setAccount(Base64Utils.StringToBase64(a));
                account.setPassword(Base64Utils.StringToBase64(p));
                accountDao.update(account);
                Intent i = new Intent();
                i.putExtra("bean", account);
                setResult(222, i);
                finish();
                break;
        }
    }
}