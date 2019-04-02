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
import android.text.TextUtils;

import com.hally.influencerai.R;
import com.hally.influencerai.main.base.BasePresenter;
import com.hally.influencerai.managers.network.ApiUtils;
import com.hally.influencerai.model.RegisterUserRes;
import com.hally.influencerai.model.User;
import com.hally.influencerai.utils.LogUtil;
import com.hally.influencerai.utils.SharePreUtil;
import com.hally.influencerai.utils.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by HallyTran on 3/22/2019.
 * transon97uet@gmail.com
 */
class LoginPresenter extends BasePresenter<LoginView> {

    LoginPresenter(Context context) {
        super(context);
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

    public void handleSocialSignInResult(User user) {
        ifViewAttached(view -> attemptCreateProfile(view, user));
    }

    public void attemptCreateProfile(LoginView view, User socialUser) {
        if (checkInternetConnection()) {
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

            ApiUtils.registerUser(context, user, new Callback<RegisterUserRes>() {
                @Override
                public void onResponse(Call<RegisterUserRes> call, Response<RegisterUserRes> response) {
                    LogUtil.logDebug(TAG, "onResponse: Call: " + call);
                    RegisterUserRes user = response.body();
                    if (user == null) {
                        view.showSnackBar(R.string.error_fail_create_profile);
                        return;
                    }
                    String token = user.getToken();
                    User resUser = user.getUser();
                    LogUtil.logDebug(TAG, "onResponse: token: " + token);
                    LogUtil.logDebug(TAG, "onResponse: User: " + resUser);

                    if (!TextUtils.isEmpty(token))
                        SharePreUtil.saveLoginToken(context, token);
                    if (resUser != null) {
                        if (resUser.isRequireUpdateInfo()) {
                            view.startCreateProfileActivity(resUser);
                        } else {
                            view.startMainActivity();
                        }
                    } else {
                        view.showSnackBar(R.string.error_fail_create_profile);
                    }
                    view.hideProgress();
                }

                @Override
                public void onFailure(Call<RegisterUserRes> call, Throwable t) {
                    LogUtil.logDebug(TAG, "onFailure: Message: " + t.getMessage());
                    view.showSnackBar(R.string.error_fail_create_profile);
                    view.hideProgress();
                }
            });
        }
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
