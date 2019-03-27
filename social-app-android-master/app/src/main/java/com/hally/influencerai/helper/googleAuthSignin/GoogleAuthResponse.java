package com.hally.influencerai.helper.googleAuthSignin;

import com.hally.influencerai.model.User;

/**
 * Created by HallyTran on 3/22/2019.
 * transon97uet@gmail.com
 */
public interface GoogleAuthResponse {

    void onGoogleAuthSignIn(User user);

    void onGoogleAuthSignInFailed();

    void onGoogleAuthSignOut(boolean isSuccess);
}
