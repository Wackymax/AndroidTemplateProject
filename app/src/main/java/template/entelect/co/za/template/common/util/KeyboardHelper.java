package template.entelect.co.za.template.common.util;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import template.entelect.co.za.template.presentation.BaseActivity;

/**
 * Created by hennie.brink on 2017/03/02.
 */

public class KeyboardHelper {

    public static void hideKeyboard(Context ctx, Object source) {

        if (ctx == null)
            return;

        InputMethodManager inputManager = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);

        View v;
        if (source instanceof BaseActivity) {
            v = ((BaseActivity) source).getCurrentFocus();
        } else if (source instanceof Fragment) {
            View baseView = ((Fragment) source).getView();
            v = baseView == null ? null : baseView.findFocus();
        }
        else throw new RuntimeException("Invalid source (" + source.getClass().getSimpleName() + ") needs to be either BaseActivity or Fragment");

        if (v == null || ! inputManager.isAcceptingText())
            return;

        v.clearFocus();
        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }
}
