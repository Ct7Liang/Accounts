package android.develop.ct7liang.accounts.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.develop.ct7liang.accounts.BaseApp;
import android.develop.ct7liang.accounts.R;
import android.develop.ct7liang.accounts.bean.User;
import android.develop.ct7liang.accounts.utils.Base64Utils;
import android.develop.ct7liang.accounts.utils.FileUtils;
import android.develop.ct7liang.accounts.utils.SnackBarUtils;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.ct7liang.tangyuan.AppFolder;
import com.ct7liang.tangyuan.receiver.AppExitReceiver;
import com.ct7liang.tangyuan.utils.GlideHelper;
import com.ct7liang.tangyuan.utils.ScreenInfoUtil;
import com.ct7liang.tangyuan.utils.ToastUtils;
import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.TResult;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class EntryActivity extends TakePhotoActivity implements View.OnClickListener {

    private String passwordstr;
    private ImageView userImage;
    private ImageView image;
    private EditText password;
    private AppExitReceiver exitReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        if(Build.VERSION.SDK_INT >= 19) {
            Window window = this.getWindow();
            window.addFlags(67108864);
            View title = findViewById(R.id.title_back_ground);
            title.setBackgroundColor(Color.parseColor("#00000000"));
            title.setPadding(0, ScreenInfoUtil.getStatusHeight(this), 0, 0);
        }

        exitReceiver = new AppExitReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.ct7liang.tangyuan.receiver");
        registerReceiver(exitReceiver, filter);

        findViewById(R.id.left_image).setOnClickListener(this);
        userImage = (ImageView) findViewById(R.id.user_icon);
        userImage.setOnClickListener(this);
        findViewById(R.id.login).setOnClickListener(this);
        password = (EditText) findViewById(R.id.password);
        image = (ImageView) findViewById(R.id.bg_image);
        image.postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation animation = AnimationUtils.loadAnimation(EntryActivity.this, R.anim.login_bg_translate_anim);
                image.startAnimation(animation);
            }
        }, 500);

        File file = new File(AppFolder.get(), "/user.txt");
        File file1 = new File(AppFolder.get(), "user.jpg");
        if (file.exists()){
            String read = FileUtils.read(file);
            if (file1.exists()){
                file1.delete();
            }
            File file2 = Base64Utils.base64ToFile(read, file1);
            Glide.with(this).load(file2).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE)
                    .transform(new BitmapTransformation[]{GlideHelper.getInstance().getCircleTransform(this)}).into(userImage);
        }

        List<User> users = BaseApp.getDaoSession().getUserDao().loadAll();
        if (users.size()>0){
            passwordstr = Base64Utils.Base64ToString(users.get(0).getPassword());
        }else{
            startActivity(new Intent(EntryActivity.this, CreateEntryPwActivity.class));
            finish();
        }
    }

    private long lastTime;
    public void exitApp() {
        long currentTime = System.currentTimeMillis();
        if(currentTime - lastTime < 2000L) {
            lastTime = currentTime;
            Intent intent = new Intent();
            intent.setAction("com.ct7liang.tangyuan.receiver");
            sendBroadcast(intent);
            finish();
        } else {
            ToastUtils.showStatic(this, "再按一次退出");
            lastTime = currentTime;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.left_image:
                exitApp();
                break;
            case R.id.login:
                String pswd = password.getText().toString().trim();
                if (passwordstr.equals(pswd)){
                    startActivity(new Intent(EntryActivity.this, MainActivity.class));
                    finish();
                }else{
                    SnackBarUtils.show(findViewById(R.id.snack), "密码错误", "#66DD5044");
                }
                break;
            case R.id.user_icon:
                CropOptions cropOptions = new CropOptions.Builder().setAspectX(1).setAspectY(1).setWithOwnCrop(false).create();
                File file = new File(AppFolder.get(), "/user.jpg");
                if (file.exists()){
                    file.delete();
                }else{
                    try {
                        file.createNewFile();
                        Uri uri = Uri.fromFile(file);
                        getTakePhoto().onPickFromGalleryWithCrop(uri, cropOptions);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        exitApp();
    }

    @Override
    public void takeSuccess(TResult result) {
        String originalPath = result.getImage().getOriginalPath();
        File file = new File(originalPath);
        String s = Base64Utils.fileToBase64(file);
        File imageStr = new File(AppFolder.get(), "/user.txt");
        if (imageStr.exists()){
            imageStr.delete();
        }
        try {
            imageStr.createNewFile();
            FileUtils.write(imageStr.getPath(), s);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        Glide.with(this).load(file).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE)
                .transform(new BitmapTransformation[]{GlideHelper.getInstance().getCircleTransform(this)}).into(userImage);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(exitReceiver);
        File file = new File(AppFolder.get(), "/user.jpg");
        if (file.exists()){
            file.delete();
        }
    }
}