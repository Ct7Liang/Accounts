package android.develop.ct7liang.accounts.activity;

import android.develop.ct7liang.accounts.BaseActivity;
import android.develop.ct7liang.accounts.BaseApp;
import android.develop.ct7liang.accounts.R;
import android.develop.ct7liang.accounts.bean.Account;
import android.develop.ct7liang.accounts.utils.Base64Utils;
import android.develop.ct7liang.accounts.utils.SnackBarUtils;
import android.develop.ct7liang.greendao.AccountDao;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.ct7liang.tangyuan.utils.ScreenInfoUtil;
import java.util.List;


public class AddAccountActivity extends BaseActivity {

    private EditText eTag;
    private EditText eAccount;
    private EditText ePassword;
    private EditText eDescription;
    private AccountDao accountDao;
    private Account account;
    private List<Account> accounts;

    @Override
    public int setLayout() {
        return R.layout.activity_add_account;
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
        ((TextView)findViewById(R.id.center_text)).setText("添加新账号");
        ((TextView)findViewById(R.id.right_text)).setText("完成");
        findViewById(R.id.right).setOnClickListener(this);
        eTag = (EditText) findViewById(R.id.tag);
        eAccount = (EditText) findViewById(R.id.account);
        ePassword = (EditText) findViewById(R.id.password);
        eDescription = (EditText) findViewById(R.id.description);
    }

    @Override
    public void initData() {
        accountDao = BaseApp.getDaoSession().getAccountDao();
        account = new Account();
        accounts = accountDao.loadAll();
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
            case R.id.right:
                String tag = eTag.getText().toString().trim();
                String accountstr = eAccount.getText().toString().trim();
                String password = ePassword.getText().toString().trim();
                String description = eDescription.getText().toString().trim();
                if (TextUtils.isEmpty(tag)||TextUtils.isEmpty(accountstr)||TextUtils.isEmpty(password)){
                    SnackBarUtils.show(findViewById(R.id.snack), "请将必填项输入完整", "#DD4E41");
                    return;
                }
                for (int i = 0; i < accounts.size(); i++) {
                    if (Base64Utils.StringToBase64(tag).equals(accounts.get(i).getTag())){
                        SnackBarUtils.show(findViewById(R.id.snack), "该标签已存在", "#DD4E41");
                        return;
                    }
                }
                account.setTag(Base64Utils.StringToBase64(tag));
                account.setAccount(Base64Utils.StringToBase64(accountstr));
                account.setPassword(Base64Utils.StringToBase64(password));
                if (!TextUtils.isEmpty(description)){
                    account.setRemark(Base64Utils.StringToBase64(description));
                }else{
                    account.setRemark(Base64Utils.StringToBase64(""));
                }
                accountDao.insert(account);
                SnackBarUtils.show(findViewById(R.id.snack), "数据录入成功", "#82BF23", new Snackbar.Callback(){
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        finish();
                    }
                });
                break;
        }
    }
}
