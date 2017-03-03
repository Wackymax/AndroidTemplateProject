package template.entelect.co.za.template.presentation;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.transition.Slide;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.otto.Bus;
import com.squareup.otto.DeadEvent;
import com.squareup.otto.Subscribe;
import com.squareup.otto.ThreadEnforcer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import template.entelect.co.za.template.MainApplication;
import template.entelect.co.za.template.R;
import template.entelect.co.za.template.common.log.Logger;
import template.entelect.co.za.template.common.util.BundleExtractor;
import template.entelect.co.za.template.common.util.KeyboardHelper;
import template.entelect.co.za.template.common.util.MainThreadBus;
import template.entelect.co.za.template.presentation.event.PermissionResult;
import template.entelect.co.za.template.presentation.event.RequestPermissionEvent;

/**
 * Created by hennie.brink on 2017/03/02.
 */

public class BaseActivity  extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener {

    public static final String EVENT_DATA = "EVENT_DATA";
    public static final int EVENT_RESULT = 1024;
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 1000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 1000;

    private final BaseActivityEvents baseActivityEvents = new BaseActivityEvents();
    private final Bus bus = new MainThreadBus(ThreadEnforcer.MAIN, getClass().getSimpleName());

    List<Object> postponedEvents = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        super.onCreate(savedInstanceState);
        ((MainApplication) getApplication()).getFeather().injectFields(this);
        bus.register(this);
        bus.register(baseActivityEvents);
        BundleExtractor.injectFromBundle(this, getIntent().getExtras());

        if(overrideOrientation()) {
            if (isTablet()) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            } else {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        }

        getSupportFragmentManager().addOnBackStackChangedListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected boolean applyLoginCheck() {
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getSupportFragmentManager().removeOnBackStackChangedListener(this);
        bus.unregister(baseActivityEvents);
        bus.unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        List<Object> postponedEventsToRemove = new ArrayList<>();
        for (Object postponedEvent : postponedEvents) {
            bus.post(postponedEvent);
            postponedEventsToRemove.add(postponedEvent);
        }
        postponedEvents.removeAll(postponedEventsToRemove);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        initButterKnife();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        initButterKnife();
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        initButterKnife();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case EVENT_RESULT:
                Serializable serializableExtra = data.getSerializableExtra(EVENT_DATA);
                postponedEvents.add(serializableExtra);
                break;
        }

    }

    public void replaceFragment(int container, Fragment fragment) {
        replaceFragment(container, fragment, true);
    }

    public void replaceFragment(int container, Fragment fragment, Map<View, String> sharedElementTransitions) {
        replaceFragment(container, fragment, true, sharedElementTransitions);
    }

    public void popFragment() {

        KeyboardHelper.hideKeyboard(this, this);
        getSupportFragmentManager().popBackStack();
    }

    public void replaceFragment(int container, Fragment fragment, boolean addToBackStack) {

        replaceFragment(container, fragment, addToBackStack, null);
    }

    public void replaceFragment(int container, Fragment fragment, boolean addToBackStack, Map<View, String> sharedElementTransitions) {
        String tag = fragment.getClass().getCanonicalName();

        getSupportFragmentManager().executePendingTransactions();
        Fragment fragmentByTag = getSupportFragmentManager().findFragmentByTag(tag);

        if (fragmentByTag != null) {

            getSupportFragmentManager().popBackStack();
        }

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction()
                .replace(container, fragment, tag);

        if (addToBackStack) fragmentTransaction.addToBackStack(tag);
        else fragmentTransaction.disallowAddToBackStack();

        if (sharedElementTransitions != null && !sharedElementTransitions.isEmpty()) {
            for (Map.Entry<View, String> entry : sharedElementTransitions.entrySet()) {

                fragmentTransaction.addSharedElement(entry.getKey(), entry.getValue());
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && isTablet()) {
            fragment.setEnterTransition(new Slide(Gravity.LEFT));
        }
        fragmentTransaction.commit();

        KeyboardHelper.hideKeyboard(this, this);
    }

    public void startActivityForEvent(Intent intent) {

        startActivityForResult(intent, EVENT_RESULT);
    }

    public void finishActivityWithEvent(Serializable event) {

        Intent data = new Intent();
        data.putExtra(EVENT_DATA, event);
        setResult(Activity.RESULT_OK, data);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        } else {
            finish();
        }
    }

    public void finishActivity() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        } else {
            finish();
        }
    }

    private void initButterKnife() {
        ButterKnife.bind(this);
    }

    public Bus getEventBus() {
        return bus;
    }

    public boolean isTablet() {
        return getResources().getBoolean(R.bool.isTablet);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0) {
            getEventBus().post(new PermissionResult(grantResults[0] == PackageManager.PERMISSION_GRANTED, RequestPermissionEvent.AppPermission.findPermissionByCode(requestCode)));
        }
    }

    @Override
    public void onBackStackChanged() {

        KeyboardHelper.hideKeyboard(this, this);
    }

    protected boolean overrideOrientation(){
        return true;
    }

    private class BaseActivityEvents {

        @Subscribe
        public void onRequestPermissionEvent(RequestPermissionEvent event) {
            // Here, thisActivity is the current activity
            if (ContextCompat.checkSelfPermission(BaseActivity.this, event.appPermission.permission) != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(BaseActivity.this, event.appPermission.permission)) {

                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(BaseActivity.this, new String[]{event.appPermission.permission}, event.appPermission.permissionCode);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            } else {
                getEventBus().post(new PermissionResult(true, event.appPermission));
            }
        }

        @Subscribe
        public void onDeadEvent(DeadEvent event){

            Logger.w("Dead Event Detected.  This might be normal and expected, but log this so we can be sure: " + event.event.toString());
        }
    }
}
