package com.hally.influencerai.helper.facebookSignIn;

import com.hally.influencerai.model.SocialUser;

/**
 * Created by HallyTran on 3/22/2019.
 * transon97uet@gmail.com
 */
public interface FacebookResponse {
    void onFacebookSignInFail(Exception e);

    void onFacebookSignInSuccess();

    void onFacebookProfileReceived(SocialUser socialUser);

    void onFacebookSignOut();
}
