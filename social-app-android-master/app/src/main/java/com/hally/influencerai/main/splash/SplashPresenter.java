
package com.hally.influencerai.main.splash;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.widget.ImageView;

import com.hally.influencerai.R;
import com.hally.influencerai.main.base.BasePresenter;
import com.hally.influencerai.main.base.BaseView;
import com.hally.influencerai.main.login.LoginActivity;
import com.hally.influencerai.utils.GlideApp;
import com.hally.influencerai.utils.ImageUtil;

class SplashPresenter extends BasePresenter<SplashView> {

    SplashPresenter(Context context) {
        super(context);
    }


    public void loadLogo(ImageView logiImageView) {
        ImageUtil.loadLocalImage(GlideApp.with(context), R.drawable.influencer_logo, logiImageView, null);
    }

    public void routeToAppropriatePage() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (checkInternetConnection()) {
                    if (hasAuthorization()) {
                        ifViewAttached(BaseView::startMainActivity);
                        ifViewAttached(BaseView::finish);
                    } else {
                        ifViewAttached((SplashView view) -> {
                            Intent intent = new Intent(context, LoginActivity.class);
                            ActivityOptions options = ActivityOptions.
                                    makeSceneTransitionAnimation((Activity) context, new android.util.Pair<>(
                                            view.getLogoView(),
                                            context.getString(R.string.post_logo_image_transition_name)));
                            context.startActivity(intent, options.toBundle());
                        });

                    }
                }

            }
        }, 500);
    }

}
