/*
 * Copyright 2018
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.hally.influencerai.main.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.hally.influencerai.R;
import com.hally.influencerai.helper.facebookSignIn.FacebookHelper;
import com.hally.influencerai.helper.facebookSignIn.FacebookResponse;
import com.hally.influencerai.helper.googleAuthSignin.GoogleAuthResponse;
import com.hally.influencerai.helper.googleAuthSignin.GoogleSignInHelper;
import com.hally.influencerai.helper.instagramSignIn.InstagramHelper;
import com.hally.influencerai.helper.instagramSignIn.InstagramResponse;
import com.hally.influencerai.helper.twitterSignIn.TwitterHelper;
import com.hally.influencerai.helper.twitterSignIn.TwitterResponse;
import com.hally.influencerai.main.base.BaseActivity;
import com.hally.influencerai.main.editProfile.EditProfileActivity;
import com.hally.influencerai.main.editProfile.createProfile.CreateProfileActivity;
import com.hally.influencerai.model.User;
import com.hally.influencerai.utils.LogUtil;
import com.twitter.sdk.android.core.TwitterException;

/**
 * Created by HallyTran on 3/22/2019.
 * transon97uet@gmail.com
 */
public class LoginActivity extends BaseActivity<LoginView, LoginPresenter> implements LoginView {
    private static final String TAG = LoginActivity.class.getSimpleName();

    private GoogleSignInHelper mGoogleAuthHelper;
    private InstagramHelper mInstagramHelper;
    private FacebookHelper mFacebookHelper;
    private TwitterHelper mTwitterHelper;

    private boolean mIsClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        initGoogleSignIn();
        initFacebookSignIn();
        initInstagramSignIn();
        initTwitterSignIn();
        initClickView();
        presenter.checkPermission();
    }

    private void initGoogleSignIn() {
        mGoogleAuthHelper = new GoogleSignInHelper(this, getString(R.string.google_web_client_id), new GoogleAuthResponse() {
            @Override
            public void onGoogleAuthSignIn(User googleUser) {
                LogUtil.logDebug(TAG, "Google:onSuccess: " + googleUser);
                presenter.handleSocialSignInResult(googleUser);
            }

            @Override
            public void onGoogleAuthSignInFailed() {
                LogUtil.logDebug(TAG, "Google:onGoogleAuthSignInFailed");
                showSnackBar(R.string.error_google_play_services);
                hideProgress();
            }

            @Override
            public void onGoogleAuthSignOut(boolean isSuccess) {

            }
        });

        findViewById(R.id.youtubeSignInButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mIsClicked) {
                    mIsClicked = true;
                    showProgress();
                    presenter.onGoogleSignInClick();
                }
            }
        });
    }

    private void initFacebookSignIn() {
        mFacebookHelper = new FacebookHelper(getString(R.string.facebook_request_field), this,
                new FacebookResponse() {
                    @Override
                    public void onFacebookSignInFail(Exception error) {
                        LogUtil.logError(TAG, "Facebook signIn:onError", error);
                        showSnackBar(error.getMessage());
                        hideProgress();
                    }

                    @Override
                    public void onFacebookSignInSuccess() {

                    }

                    @Override
                    public void onFacebookProfileReceived(User facebookUser) {
                        LogUtil.logDebug(TAG, "Facebook:onSuccess: " + facebookUser);
                        presenter.handleSocialSignInResult(facebookUser);
                    }

                    @Override
                    public void onFacebookSignOut() {

                    }
                }
        );

        findViewById(R.id.facebookSignInButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mIsClicked) {
                    mIsClicked = true;
                    showProgress();
                    presenter.onFacebookSignInClick();
                }
            }
        });
    }

    private void initInstagramSignIn() {
        //instagram initializer
        mInstagramHelper = new InstagramHelper(
                getResources().getString(R.string.instagram_client_id),
                getResources().getString(R.string.instagram_client_secret),
                getResources().getString(R.string.instagram_callback_url), this,
                new InstagramResponse() {
                    @Override
                    public void onInstagramSignInSuccess(User instagramUser) {
                        LogUtil.logDebug(TAG, "Instagram:onSuccess: " + instagramUser);
                        presenter.handleSocialSignInResult(instagramUser);
                    }

                    @Override
                    public void onInstagramSignInFail(String error) {
                        LogUtil.logError(TAG, "onInstagramSignInFail:onError " + error);
                        showSnackBar(error);
                    }

                    @Override
                    public void onInstagramDismiss() {
                        mIsClicked = false;
                    }
                });
        findViewById(R.id.instagramSignInButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mIsClicked) {
                    mIsClicked = true;
                    presenter.onInstagramSignInClick();
                }
            }
        });
    }

    private void initTwitterSignIn() {
        mTwitterHelper = new TwitterHelper(R.string.twitter_api_key,
                R.string.twitter_secret_key, this, new TwitterResponse() {
            @Override
            public void onTwitterError(TwitterException error) {
                LogUtil.logError(TAG, "onTwitterSignInFail:onError " + error);
//                showSnackBar(error.toString());
            }

            @Override
            public void onTwitterSignIn(@NonNull String userId, @NonNull String userName) {

            }

            @Override
            public void onTwitterProfileReceived(User instagramUser) {
                LogUtil.logDebug(TAG, "Twitter:onSuccess: " + instagramUser);
                presenter.handleSocialSignInResult(instagramUser);
            }
        }
        );

        findViewById(R.id.twitterSignInButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mIsClicked) {
                    mIsClicked = true;
                    presenter.onTwitterSignInClick();
                }
            }
        });
    }

    private void initClickView() {
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onViewBottomClick(view.getId());
            }
        };
        findViewById(R.id.bottomPrivacy).setOnClickListener(onClickListener);
        findViewById(R.id.bottomTermNCon).setOnClickListener(onClickListener);
        findViewById(R.id.bottomContactUs).setOnClickListener(onClickListener);
    }

    @NonNull
    @Override
    public LoginPresenter createPresenter() {
        if (presenter == null) {
            return new LoginPresenter(this);
        }
        return presenter;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mGoogleAuthHelper.onActivityResult(requestCode, resultCode, data);
        mFacebookHelper.onActivityResult(requestCode, resultCode, data);
        mTwitterHelper.onActivityResult(requestCode, resultCode, data);
        mIsClicked = false;
    }

    @Override
    public void startCreateProfileActivity(User user) {
        Intent intent = new Intent(LoginActivity.this, CreateProfileActivity.class);
        intent.putExtra(EditProfileActivity.SOCIAL_USER_EXTRA_KEY, user);
        startActivity(intent);
    }

    @Override
    public void signInWithGoogle() {
        mGoogleAuthHelper.performSignIn(this);
    }

    @Override
    public void signInWithFacebook() {
        mFacebookHelper.performSignIn(this);
    }

    @Override
    public void signInWithInstagram() {
        mInstagramHelper.performSignIn();
    }

    @Override
    public void signInWithTwitter() {
        mTwitterHelper.performSignIn();
    }
}

