package android.develop.ct7liang.accounts.activity;

import android.content.Intent;
import android.develop.ct7liang.accounts.BaseActivity;
import android.develop.ct7liang.accounts.BaseApp;
import android.develop.ct7liang.accounts.R;
import android.develop.ct7liang.accounts.utils.Constant;
import android.develop.ct7liang.accounts.utils.SplashCountDown;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ct7liang.tangyuan.utils.SpUtils;

import java.io.File;

public class SplashActivity extends BaseActivity {

    public TextView time;
    public View countdown;
    private SplashCountDown splashCountDown;
    private ImageView image;
    public int isNewEntry;

    @Override
    public int setLayout() {
        return R.layout.activity_splash;
    }

    @Override
    protected void setStatusBar() {
        int flag= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        //设置当前窗体为全屏显示
        getWindow().setFlags(flag, flag);
    }

    @Override
    public void findView() {
        countdown = findViewById(R.id.count_down);
        time = (TextView) findViewById(R.id.time);
        image = (ImageView) findViewById(R.id.imageView);
    }

    @Override
    public void initData() {
        splashCountDown = new SplashCountDown(6000, 1000, this);
        isNewEntry = SpUtils.start().getInt(Constant.IsNewEntry, 0);
    }

    @Override
    public void initView() {
        countdown.setOnClickListener(this);
        File dir = BaseApp.getSplashFolder();
        if (dir!=null){
            File[] ls = dir.listFiles();
            if (ls.length != 0){
                Glide.with(this).load(ls[0]).into(image);
            }
        }
    }

    @Override
    public void initFinish() {
        splashCountDown.start();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.count_down:
                startActivity(new Intent(this, EntryActivity.class));
                splashCountDown.cancel();
                if (isNewEntry==0){
                    startActivity(new Intent(this, CreateEntryPwActivity.class));
                }else{
                    startActivity(new Intent(this, EntryActivity.class));
                }
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        splashCountDown.cancel();
    }
}