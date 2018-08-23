package android.develop.ct7liang.accounts.utils;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by Administrator on 2018-02-22.
 *
 */

public class SnackBarUtils {

    public static Snackbar show(View view, String text){
        Snackbar snackbar = Snackbar.make(view, text, Snackbar.LENGTH_SHORT);
        snackbar.getView().setBackgroundColor(Color.parseColor("#4C8BF5"));
        snackbar.show();
        return snackbar;
    }

    public static Snackbar show(View view, String text, String color){
        Snackbar snackbar = Snackbar.make(view, text, Snackbar.LENGTH_SHORT);
        snackbar.getView().setBackgroundColor(Color.parseColor(color));
        snackbar.show();
        return snackbar;
    }

    public static Snackbar show(View view, String text, Snackbar.Callback callback){
        Snackbar snackbar = Snackbar.make(view, text, Snackbar.LENGTH_SHORT);
        snackbar.getView().setBackgroundColor(Color.parseColor("#4C8BF5"));
        snackbar.setCallback(callback);
        snackbar.show();
        return snackbar;
    }

    public static Snackbar show(View view, String text, String color, Snackbar.Callback callback){
        Snackbar snackbar = Snackbar.make(view, text, Snackbar.LENGTH_SHORT);
        snackbar.getView().setBackgroundColor(Color.parseColor(color));
        snackbar.setCallback(callback);
        snackbar.show();
        return snackbar;
    }
}
