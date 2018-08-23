package android.develop.ct7liang.accounts.activity;

import android.develop.ct7liang.accounts.BaseActivity;
import android.develop.ct7liang.accounts.R;
import android.develop.ct7liang.accounts.utils.Constant;
import android.graphics.Color;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import com.ct7liang.tangyuan.utils.ScreenInfoUtil;
import com.ct7liang.tangyuan.utils.SpUtils;

public class AboutAppActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    private RadioButton a;
    private RadioButton b;

    @Override
    public int setLayout() {
        return R.layout.activity_about_app;
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
        ((TextView)findViewById(R.id.left_text)).setText("关于软件");
        a = (RadioButton) findViewById(R.id.menu_a);
        a.setOnCheckedChangeListener(this);
        b = (RadioButton) findViewById(R.id.menu_b);
        b.setOnCheckedChangeListener(this);
        boolean isMenuA = SpUtils.start().getBoolean(Constant.isTopMenu);
        if (isMenuA){
            a.setChecked(true);
        }else{
            b.setChecked(true);
        }
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
                finish();
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.menu_a:
                SpUtils.start().saveBoolean(Constant.isTopMenu, isChecked);
                break;
            case R.id.menu_b:
                SpUtils.start().saveBoolean(Constant.isTopMenu, !isChecked);
                break;
        }
    }
}