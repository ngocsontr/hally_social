
package com.hally.influencerai.main.splash;

import android.view.View;

import com.hally.influencerai.main.base.BaseView;
import com.hally.influencerai.model.User;

public interface SplashView extends BaseView {

    void routeToAppropriateScreen();

    View getLogoView();

    View getBackground();

    void startCreateProfileActivity(User user);
}
