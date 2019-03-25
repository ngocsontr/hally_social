package com.hally.influencerai.helper.googleAuthSignin;

import com.hally.influencerai.model.SocialUser;

/**
 * Created by HallyTran on 3/22/2019.
 * transon97uet@gmail.com
 */
public interface GoogleAuthResponse {

    void onGoogleAuthSignIn(SocialUser user);

    void onGoogleAuthSignInFailed();

    void onGoogleAuthSignOut(boolean isSuccess);
}
