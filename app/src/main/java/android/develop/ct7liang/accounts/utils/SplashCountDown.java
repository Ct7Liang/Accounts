package android.develop.ct7liang.accounts.utils;

import android.content.Intent;
import android.develop.ct7liang.accounts.activity.CreateEntryPwActivity;
import android.develop.ct7liang.accounts.activity.EntryActivity;
import android.develop.ct7liang.accounts.activity.SplashActivity;
import android.os.CountDownTimer;
import android.view.View;
import java.lang.ref.WeakReference;

/**
 * Created by Administrator on 2018-02-22.
 *
 */
public class SplashCountDown extends CountDownTimer {
    /**
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receive
     *                          {@link #onTick(long)} callbacks.
     */
    private WeakReference<SplashActivity> mWeakReference;

    public SplashCountDown(long millisInFuture, long countDownInterval, SplashActivity activity) {
        super(millisInFuture, countDownInterval);
        mWeakReference = new WeakReference<>(activity);
    }

    @Override
    public void onTick(long millisUntilFinished) {
        SplashActivity activity = mWeakReference.get();
        if (activity!=null){
            activity.time.setText(millisUntilFinished/1000-1 + "s");
            if (millisUntilFinished/1000-1==0){
                activity.countdown.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onFinish() {
        SplashActivity activity = mWeakReference.get();
        if (activity!=null){
            if (activity.isNewEntry==0){
                activity.startActivity(new Intent(activity, CreateEntryPwActivity.class));
            }else{
                activity.startActivity(new Intent(activity, EntryActivity.class));
            }
            activity.finish();
        }
    }
}
