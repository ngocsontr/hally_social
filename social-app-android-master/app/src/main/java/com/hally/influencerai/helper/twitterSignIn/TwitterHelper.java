package com.hally.influencerai.helper.twitterSignIn;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.util.Log;

import com.hally.influencerai.enums.UserType;
import com.hally.influencerai.model.SocialUser;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.models.User;
import com.twitter.sdk.android.core.services.AccountService;

import retrofit2.Call;

/**
 * Created by HallyTran on 3/21/2019.
 * transon97uet@gmail.com
 */
public class TwitterHelper {
    private static final String TAG = "TwitterHelper";
    @NonNull
    private final Activity mActivity;
    @NonNull
    private final TwitterResponse mListener;
    private TwitterAuthClient mAuthClient;

    /**
     * Public constructor. This will initialize twitter sdk.
     *
     * @param twitterApiKey     twitter api key
     * @param twitterSecreteKey twitter secrete key
     * @param context           instance of the caller.
     * @param response          {@link TwitterResponse} response listener.
     */
    public TwitterHelper(@StringRes final int twitterApiKey,
                         @StringRes final int twitterSecreteKey,
                         @NonNull Activity context, @NonNull TwitterResponse response) {

        //noinspection ConstantConditions
        if (response == null) throw new IllegalArgumentException("TwitterResponse cannot be null.");

        mActivity = context;
        mListener = response;

        //initialize sdk
        TwitterConfig authConfig = new TwitterConfig.Builder(context)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(
                        new TwitterAuthConfig(context.getResources().getString(twitterApiKey),
                        context.getResources().getString(twitterSecreteKey)))
                .debug(true)
                .build();
        Twitter.initialize(authConfig);

        mAuthClient = new TwitterAuthClient();
    }

    /**
     * Perform twitter sign in. Call this method when user clicks on "Login with Twitter" button.
     */
    public void performSignIn() {
        mAuthClient.authorize(mActivity, new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                TwitterSession session = result.data;
                mListener.onTwitterSignIn(session.getUserName(), session.getUserId() + " ");

                //load user data.
                getUserData();
            }

            @Override
            public void failure(TwitterException exception) {
                mListener.onTwitterError(exception);
            }
        });
    }

    /**
     * This method handles onActivityResult callbacks from fragment or activity.
     *
     * @param requestCode request code received.
     * @param resultCode  result code received.
     * @param data        Data intent.
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mAuthClient != null)
            mAuthClient.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Load twitter user profile.
     */
    private void getUserData() {
        TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
        AccountService statusesService = twitterApiClient.getAccountService();
        Call<User> call = statusesService.verifyCredentials(true, true, true);
        call.enqueue(new Callback<User>() {
            @Override
            public void success(Result<User> userResult) {
                //parse the response
                SocialUser user = new SocialUser();
                user.setUserType(UserType.TWITTER);
                user.setUsername(userResult.data.name);
                user.setEmail(userResult.data.email);
                user.setDescription(userResult.data.description);
                user.setLocation(userResult.data.location);
                user.setAvatar(userResult.data.profileImageUrl);
                user.setCoverPicUrl(userResult.data.profileBannerUrl);
                user.setId(userResult.data.idStr);

                mListener.onTwitterProfileReceived(user);
            }

            public void failure(TwitterException exception) {
                mListener.onTwitterError(exception);
            }
        });
    }
}

