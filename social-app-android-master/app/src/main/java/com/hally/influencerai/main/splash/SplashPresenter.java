
package com.hally.influencerai.main.splash;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;

import com.hally.influencerai.R;
import com.hally.influencerai.main.base.BasePresenter;
import com.hally.influencerai.main.base.BaseView;
import com.hally.influencerai.main.login.LoginActivity;
import com.hally.influencerai.managers.network.ApiUtils;
import com.hally.influencerai.model.LoginRes;
import com.hally.influencerai.utils.LogUtil;
import com.hally.influencerai.utils.SharePreUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by HallyTran on 3/22/2019.
 * transon97uet@gmail.com
 */
class SplashPresenter extends BasePresenter<SplashView> {

    SplashPresenter(Context context) {
        super(context);
    }

    public void routeToAppropriatePage() {
        ifViewAttached(new ViewAction<SplashView>() {
            @Override
            public void run(@NonNull SplashView view) {
                if (checkInternetConnection()) {
                    doAuthorization(view);
                }
            }
        });

    }

    private void doAuthorization(SplashView view) {
        if (!ApiUtils.login(context, new Callback<LoginRes>() {
            @Override
            public void onResponse(Call<LoginRes> call, Response<LoginRes> response) {
                if (response.errorBody() != null) {
                    LogUtil.logDebug(TAG, "onResponse:error " + response.message());
                    view.showSnackBar(R.string.error_authentication);
                    routeToLogin(view);
                    return;
                }
                LoginRes loginRes = response.body();
                LogUtil.logDebug(TAG, "onResponse:body() " + response.body());
                if (loginRes != null && loginRes.isAllowAccess()) {
                    view.showSnackBar(R.string.login_success);
                    if (loginRes.getUser().isRequireUpdateInfo()) {
                        view.startCreateProfileActivity(loginRes.getUser());
                    } else {
                        view.startMainActivity();
                    }
                    view.finish();
                }
            }

            @Override
            public void onFailure(Call<LoginRes> call, Throwable t) {
                LogUtil.logDebug(TAG, "onFailure:error " + t.getMessage());
                view.showSnackBar(R.string.error_authentication);
                routeToLogin(view);
            }
        })) {
            routeToLogin(view);
        }
    }

    private void routeToLogin(SplashView view) {
        SharePreUtil.clearLoginToken(context);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(context, LoginActivity.class);
                ActivityOptions options = ActivityOptions.
                        makeSceneTransitionAnimation((Activity) context,
                                new android.util.Pair<>(view.getBackground(),
                                        context.getString(R.string.post_image_transition_name)),
                                new android.util.Pair<>(view.getLogoView(),
                                        context.getString(R.string.post_logo_image_transition_name)));
                context.startActivity(intent, options.toBundle());
                ifViewAttached(BaseView::finish);
            }
        }, 300);
    }
}