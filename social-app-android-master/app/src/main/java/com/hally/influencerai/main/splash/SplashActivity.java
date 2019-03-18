package com.hally.influencerai.main.splash;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.hally.influencerai.R;
import com.hally.influencerai.main.base.BaseActivity;
import com.hally.influencerai.utils.LogUtil;

public class SplashActivity extends BaseActivity<SplashView, SplashPresenter> implements SplashView {

    private static final String TAG = SplashActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        routeToAppropriateScreen();
        supportPostponeEnterTransition();
    }

    @NonNull
    @Override
    public SplashPresenter createPresenter() {
        if (presenter == null) {
            return new SplashPresenter(this);
        }
        return presenter;
    }

    @Override
    public void routeToAppropriateScreen() {
        LogUtil.logDebug(TAG, "routeToAppropriateScreen");
        presenter.routeToAppropriatePage();
    }

    @Override
    public View getLogoView() {
        return findViewById(R.id.app_logo);
    }
}
