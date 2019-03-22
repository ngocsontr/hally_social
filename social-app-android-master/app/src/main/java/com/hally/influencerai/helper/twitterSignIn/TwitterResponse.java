package com.hally.influencerai.helper.twitterSignIn;

import android.support.annotation.NonNull;

import com.hally.influencerai.model.SocialUser;
import com.twitter.sdk.android.core.TwitterException;

/**
 * Created by HallyTran on 3/21/2019.
 * transon97uet@gmail.com
 */
public interface TwitterResponse {
    /**
     * This method will call when twitter sign in fails.
     *
     * @param exception
     */
    void onTwitterError(TwitterException exception);

    /**
     * This method will execute when twitter app is authorized by the user and access token is received.
     *
     * @param userId   twitter user id.
     * @param userName twitter user name
     */
    void onTwitterSignIn(@NonNull String userId, @NonNull String userName);

    /**
     * This method will execute when user profile is received.
     *
     * @param user {@link SocialUser} profile.
     */
    void onTwitterProfileReceived(SocialUser user);
}
