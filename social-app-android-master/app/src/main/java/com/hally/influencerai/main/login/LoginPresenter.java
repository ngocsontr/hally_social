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

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;
import com.hally.influencerai.Constants;
import com.hally.influencerai.R;
import com.hally.influencerai.main.base.BasePresenter;
import com.hally.influencerai.main.interactors.ProfileInteractor;
import com.hally.influencerai.managers.ProfileManager;
import com.hally.influencerai.managers.listeners.OnObjectExistListener;
import com.hally.influencerai.managers.network.ApiUtils;
import com.hally.influencerai.model.Profile;
import com.hally.influencerai.model.RegisterUserRes;
import com.hally.influencerai.model.User;
import com.hally.influencerai.utils.LogUtil;
import com.hally.influencerai.utils.PreferencesUtil;
import com.hally.influencerai.utils.Utils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class LoginPresenter extends BasePresenter<LoginView> {

    LoginPresenter(Context context) {
        super(context);
    }

    public void checkIsProfileExist(final String userId) {
        ProfileManager.getInstance(context).isProfileExist(userId, new OnObjectExistListener<Profile>() {
            @Override
            public void onDataChanged(boolean exist) {
                LoginPresenter.this.ifViewAttached(new ViewAction<LoginView>() {
                    @Override
                    public void run(@NonNull LoginView view) {
                        if (!exist) {
//                            view.startCreateProfileActivity();
                        } else {
                            PreferencesUtil.setProfileCreated(context, true);
                            ProfileInteractor.getInstance(context.getApplicationContext())
                                    .addRegistrationToken(FirebaseInstanceId.getInstance().getToken(), userId);
                            view.startMainActivity();
                        }

                        view.hideProgress();
                        view.finish();
                    }
                });
            }
        });
    }

    public void onGoogleSignInClick() {
        if (checkInternetConnection()) {
            ifViewAttached(LoginView::signInWithGoogle);
        }
    }

    public void onFacebookSignInClick() {
        if (checkInternetConnection()) {
            ifViewAttached(LoginView::signInWithFacebook);
        }
    }

    public void onInstagramSignInClick() {
        if (checkInternetConnection()) {
            ifViewAttached(LoginView::signInWithInstagram);
        }
    }

    public void onTwitterSignInClick() {
        if (checkInternetConnection()) {
            ifViewAttached(LoginView::signInWithTwitter);
        }
    }

    public void handleGoogleSignInResult(GoogleSignInResult result) {
        ifViewAttached(new ViewAction<LoginView>() {
            @Override
            public void run(@NonNull LoginView view) {
                if (result.isSuccess()) {
                    view.showProgress();

                    GoogleSignInAccount account = result.getSignInAccount();

//                    view.setProfilePhotoUrl(LoginPresenter.this.buildGooglePhotoUrl(account.getPhotoUrl()));

                    AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
//                    view.firebaseAuthWithCredentials(credential);

                    LogUtil.logDebug(TAG, "firebaseAuthWithGoogle:" + account.getId());

                } else {
                    LogUtil.logDebug(TAG, "SIGN_IN_GOOGLE failed :" + result);
                    view.hideProgress();
                }
            }
        });
    }

    public void handleFacebookSignInResult(LoginResult loginResult) {
        ifViewAttached(new ViewAction<LoginView>() {
            @Override
            public void run(@NonNull LoginView view) {
                LogUtil.logDebug(TAG, "handleFacebookSignInResult: " + loginResult);
//                view.setProfilePhotoUrl(LoginPresenter.this.buildFacebookPhotoUrl(loginResult.getSnsAccessToken().getUserId()));
                view.showProgress();
                AuthCredential credential = FacebookAuthProvider.getCredential(loginResult.getAccessToken().getToken());
//                view.firebaseAuthWithCredentials(credential);
            }
        });
    }

    public void handleSocialSignInResult(User user) {
        ifViewAttached(new ViewAction<LoginView>() {
            @Override
            public void run(@NonNull LoginView view) {
//                view.startCreateProfileActivity(user);
                attemptCreateProfile(user);
                view.hideProgress();
            }
        });
    }

    public void attemptCreateProfile(User socialUser) {
        if (checkInternetConnection()) {
            ifViewAttached(new ViewAction<LoginView>() {
                @Override
                public void run(@NonNull LoginView view) {
                    User user = new User();
                    user.setSocialId(socialUser.getSocialId());
                    user.setSocialType(socialUser.getSocialType());
                    if (!TextUtils.isEmpty(socialUser.getUsername()))
                        user.setUsername(socialUser.getUsername());
                    if (!TextUtils.isEmpty(socialUser.getEmail()))
                        user.setEmail(user.getEmail());
                    if (!TextUtils.isEmpty(socialUser.getAvatar()))
                        user.setAvatar(socialUser.getAvatar());
                    if (!TextUtils.isEmpty(socialUser.getDescription()))
                        user.setDescription(socialUser.getDescription());
                    if (!TextUtils.isEmpty(socialUser.getSnsAccessToken()))
                        user.setSnsAccessToken(user.getSnsAccessToken());

                    ApiUtils.registerUser(context, user, new Callback<List<RegisterUserRes>>() {
                        @Override
                        public void onResponse(Call<List<RegisterUserRes>> call, Response<List<RegisterUserRes>> response) {
                            LogUtil.logDebug(TAG, "onResponse: Call: " + call);
                            List<RegisterUserRes> users = response.body();
                            String token = users.get(0).getToken();
                            User resUser = users.get(1).getUser();
                            LogUtil.logDebug(TAG, "onResponse: token: " + token);
                            LogUtil.logDebug(TAG, "onResponse: User: " + resUser);
                            if (resUser != null) {
                                if (resUser.isRequireUpdateInfo()) {
                                    view.startCreateProfileActivity(resUser);
                                } else {
                                    view.startMainActivity();
                                }
                            } else {
                                view.showSnackBar(R.string.error_fail_create_profile);
//                                view.startCreateProfileActivity(socialUser);
                            }
                        }

                        @Override
                        public void onFailure(Call<List<RegisterUserRes>> call, Throwable t) {
                            LogUtil.logDebug(TAG, "onFailure: Message: " + t.getMessage());
                            view.showSnackBar(R.string.error_fail_create_profile);
//                            view.startCreateProfileActivity(socialUser);
                        }
                    });
                }
            });
        }
    }


    private String buildGooglePhotoUrl(Uri photoUrl) {
        return String.format(context.getString(R.string.google_large_image_url_pattern),
                photoUrl, Constants.Profile.MAX_AVATAR_SIZE);
    }

    private String buildFacebookPhotoUrl(String userId) {
        return String.format(context.getString(R.string.facebook_large_image_url_pattern),
                userId);
    }

    public void handleAuthError(Task<AuthResult> task) {
        Exception exception = task.getException();
        LogUtil.logError(TAG, "signInWithCredential", exception);

        ifViewAttached(new ViewAction<LoginView>() {
            @Override
            public void run(@NonNull LoginView view) {
                if (exception != null) {
                    view.showWarningDialog(exception.getMessage());
                } else {
                    view.showSnackBar(R.string.error_authentication);
                }

                view.hideProgress();
            }
        });
    }

    public void onViewBottomClick(int id) {
        String url = "http://www.google.com";
        switch (id) {
            case R.id.bottomPrivacy:
            case R.id.bottomTermNCon:
            case R.id.bottomContactUs:
                // TODO: 3/21/2019 add reflect url after

        }
        Utils.openBrower(context, url);
    }

}
