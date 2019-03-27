package com.hally.influencerai.helper.instagramSignIn;

import com.hally.influencerai.model.User;

/**
 * Callback operation to receive status of signin with instagram operation
 */

public interface InstagramResponse {

    /**
     * If instagram successfully login
     *
     * @param user user data from instagram
     */
    void onInstagramSignInSuccess(User user);

    /**
     * If error occurs while login to instagram
     *
     * @param error error message from {@link InstagramHelper}
     */
    void onInstagramSignInFail(String error);

    void onInstagramDismiss();
}
