package template.entelect.co.za.template.presentation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.squareup.otto.Bus;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import template.entelect.co.za.template.MainApplication;
import template.entelect.co.za.template.R;
import template.entelect.co.za.template.common.log.Logger;
import template.entelect.co.za.template.common.util.BundleExtractor;

/**
 * Created by hennie.brink on 2017/03/02.
 */

public class BaseFragment  extends Fragment {

    private Bus eventBus;
    private Unbinder unbinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BundleExtractor.injectFromBundle(this, getArguments());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((MainApplication) context.getApplicationContext()).getFeather().injectFields(this);

        if (context instanceof BaseActivity) {
            eventBus = ((BaseActivity) context).getEventBus();
        } else {
            Logger.i("Activity does not inherit base activity so we will create a new event bus");
            eventBus = new Bus();
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        eventBus.register(this);
    }

    @Override
    public void onStop() {

        super.onStop();
        eventBus.unregister(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
    }

    @Override
    public void onDestroyView() {

        if (unbinder != null) {
            unbinder.unbind();
        }

        super.onDestroyView();
    }

    public Bus getEventBus() {
        return eventBus;
    }

    public BaseActivity getFragmentActivity() {
        return (BaseActivity) getActivity();
    }

    public boolean isTablet() {
        return getResources().getBoolean(R.bool.isTablet);
    }
}
