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

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hally.influencerai.R;
import com.hally.influencerai.helper.facebookSignIn.FacebookHelper;
import com.hally.influencerai.helper.facebookSignIn.FacebookResponse;
import com.hally.influencerai.helper.instagramSignIn.InstagramHelper;
import com.hally.influencerai.helper.instagramSignIn.InstagramResponse;
import com.hally.influencerai.helper.twitterSignIn.TwitterHelper;
import com.hally.influencerai.helper.twitterSignIn.TwitterResponse;
import com.hally.influencerai.main.base.BaseActivity;
import com.hally.influencerai.main.editProfile.EditProfileActivity;
import com.hally.influencerai.main.editProfile.createProfile.CreateProfileActivity;
import com.hally.influencerai.model.SocialUser;
import com.hally.influencerai.utils.GoogleApiHelper;
import com.hally.influencerai.utils.LogUtil;
import com.hally.influencerai.utils.LogoutHelper;
import com.twitter.sdk.android.core.TwitterException;

public class LoginActivity extends BaseActivity<LoginView, LoginPresenter> implements LoginView, GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = LoginActivity.class.getSimpleName();
    private static final int SIGN_IN_GOOGLE = 9001;
    public static final int LOGIN_REQUEST_CODE = 10001;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private GoogleApiClient mGoogleApiClient;
    private InstagramHelper mInstagramHelper;
    private FacebookHelper mFacebookHelper;
    private TwitterHelper mTwitterHelper;

    private String profilePhotoUrlLarge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        initGoogleSignIn();
        initFirebaseAuth();
        initFacebookSignIn();
        initInstagramSignIn();
        initTwitterSignIn();
        initClickView();
    }

    private void initGoogleSignIn() {
        mGoogleApiClient = GoogleApiHelper.createGoogleApiClient(this);
        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.youtubeSignInButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onGoogleSignInClick();
            }
        });
    }

    private void initFirebaseAuth() {
        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            LogoutHelper.signOut(mGoogleApiClient, this);
        }

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // Profile is signed in
                    LogUtil.logDebug(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    presenter.checkIsProfileExist(user.getUid());
//                    LoginActivity.this.setResult(RESULT_OK);
                } else {
                    // Profile is signed out
                    LogUtil.logDebug(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    private void initFacebookSignIn() {
        mFacebookHelper = new FacebookHelper("id,name,email,gender,birthday,picture,cover", this,
                new FacebookResponse() {
                    @Override
                    public void onFacebookSignInFail(Exception error) {
                        LogUtil.logError(TAG, "facebook signIn:onError", error);
                        showSnackBar(error.getMessage());
                    }

                    @Override
                    public void onFacebookSignInSuccess() {

                    }

                    @Override
                    public void onFacebookProfileReceived(com.hally.influencerai.model.SocialUser facebookUser) {
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
                presenter.onFacebookSignInClick();
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
                    public void onInstagramSignInSuccess(com.hally.influencerai.model.SocialUser instagramUser) {
                        LogUtil.logDebug(TAG, "Instagram:onSuccess: " + instagramUser);
                        presenter.handleSocialSignInResult(instagramUser);
                    }

                    @Override
                    public void onInstagramSignInFail(String error) {
                        LogUtil.logError(TAG, "onInstagramSignInFail:onError " + error);
                        showSnackBar(error);
                    }
                });
        findViewById(R.id.instagramSignInButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onInstagramSignInClick();
            }
        });
    }

    private void initTwitterSignIn() {
        mTwitterHelper = new TwitterHelper(R.string.twitter_api_key,
                R.string.twitter_secret_key, this, new TwitterResponse() {
            @Override
            public void onTwitterError(TwitterException error) {
                LogUtil.logError(TAG, "onTwitterSignInFail:onError " + error);
                showSnackBar(error.toString());
            }

            @Override
            public void onTwitterSignIn(@NonNull String userId, @NonNull String userName) {

            }

            @Override
            public void onTwitterProfileReceived(SocialUser instagramUser) {
                LogUtil.logDebug(TAG, "Instagram:onSuccess: " + instagramUser);
                presenter.handleSocialSignInResult(instagramUser);

            }
        }
        );

        findViewById(R.id.twitterSignInButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onTwitterSignInClick();
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

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }

        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.stopAutoManage(this);
            mGoogleApiClient.disconnect();
        }
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
        mFacebookHelper.onActivityResult(requestCode, resultCode, data);
        mTwitterHelper.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == SIGN_IN_GOOGLE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            presenter.handleGoogleSignInResult(result);
        }
    }

    @Override
    public void startCreateProfileActivity() {
        Intent intent = new Intent(LoginActivity.this, CreateProfileActivity.class);
        intent.putExtra(CreateProfileActivity.LARGE_IMAGE_URL_EXTRA_KEY, profilePhotoUrlLarge);
        startActivity(intent);
    }

    @Override
    public void startCreateProfileActivity(com.hally.influencerai.model.SocialUser user) {
        Intent intent = new Intent(LoginActivity.this, CreateProfileActivity.class);
        intent.putExtra(EditProfileActivity.SOCIAL_USER_EXTRA_KEY, user);
        startActivity(intent);
    }

    @Override
    public void firebaseAuthWithCredentials(AuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        LogUtil.logDebug(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            presenter.handleAuthError(task);
                        }
                    }
                });
    }

    @Override
    public void setProfilePhotoUrl(String url) {
        profilePhotoUrlLarge = url;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        LogUtil.logDebug(TAG, "onConnectionFailed:" + connectionResult);
        showSnackBar(R.string.error_google_play_services);
        hideProgress();
    }

    @Override
    public void signInWithGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, SIGN_IN_GOOGLE);
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

