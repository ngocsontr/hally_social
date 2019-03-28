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

package com.hally.influencerai.main.editProfile;

import android.content.Context;
import android.support.annotation.NonNull;

import com.hally.influencerai.R;
import com.hally.influencerai.main.base.BaseView;
import com.hally.influencerai.main.pickImageBase.PickImagePresenter;
import com.hally.influencerai.managers.ProfileManager;
import com.hally.influencerai.managers.network.ApiUtils;
import com.hally.influencerai.model.Profile;
import com.hally.influencerai.model.UpdateUserRes;
import com.hally.influencerai.model.User;
import com.hally.influencerai.utils.LogUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Alexey on 03.05.18.
 */

public class EditProfilePresenter<V extends EditProfileView> extends PickImagePresenter<V> {

    protected Profile profile;
    protected ProfileManager profileManager;

    protected EditProfilePresenter(Context context) {
        super(context);
        profileManager = ProfileManager.getInstance(context.getApplicationContext());

    }

    public void loadProfile() {
//        ifViewAttached(BaseView::showProgress);
//        profileManager.getProfileSingleValue(getCurrentUserId(), new OnObjectChangedListenerSimple<Profile>() {
//            @Override
//            public void onObjectChanged(Profile obj) {
//                profile = obj;
//                ifViewAttached(new ViewAction<V>() {
//                    @Override
//                    public void run(@NonNull V view) {
//                        if (profile != null) {
//                            view.setName(profile.getUsername());
//
//                            if (profile.getPhotoUrl() != null) {
//                                view.setProfilePhoto(profile.getPhotoUrl());
//                            }
//                        }
//
//                        view.hideProgress();
//                        view.setNameError(null);
//                    }
//                });
//            }
//        });
    }

//    public void attemptCreateProfileemptCreateProfile(Uri imageUri) {
//        if (checkInternetConnection()) {
//            ifViewAttached(new ViewAction<V>() {
//                @Override
//                public void run(@NonNull V view) {
//                    view.setNameError(null);
//
//                    String name = view.getNameText().trim();
//                    boolean cancel = false;
//
//                    if (TextUtils.isEmpty(name)) {
//                        view.setNameError(context.getString(R.string.error_field_required));
//                        cancel = true;
//                    } else if (!ValidationUtil.isNameValid(name)) {
//                        view.setNameError(context.getString(R.string.error_profile_name_length));
//                        cancel = true;
//                    }
//
//                    if (!cancel) {
//                        view.showProgress();
//                        profile.setUsername(name);
//                        EditProfilePresenter.this.createOrUpdateProfile(imageUri);
//                    }
//                }
//            });
//        }
//    }


    public void attemptCreateProfile(User socialUser) {
        if (checkInternetConnection()) {
            ifViewAttached(new ViewAction<V>() {
                @Override
                public void run(@NonNull V view) {
                    User user = new User();
                    user.setSocialId(socialUser.getSocialId());
                    user.setUsername(socialUser.getUsername());
                    user.setEmail(socialUser.getEmail());
                    user.setAvatar(socialUser.getAvatar());
                    user.setDescription(socialUser.getDescription());
                    user.setSnsAccessToken(socialUser.getSnsAccessToken());
                    user.setSocialType(socialUser.getSocialType());

                    ApiUtils.updateUser(context, user, new Callback<UpdateUserRes>() {
                        @Override
                        public void onResponse(Call<UpdateUserRes> call, Response<UpdateUserRes> response) {
                            if (response.errorBody() != null) {
                                LogUtil.logDebug(TAG, "onResponse:error " + response.errorBody());
                                view.showSnackBar(R.string.error_fail_update_profile);
                                return;
                            }
                            UpdateUserRes updateUserRes = response.body();
                            if (updateUserRes != null && updateUserRes.isSuccess())
                                onProfileUpdatedSuccessfully();
                        }

                        @Override
                        public void onFailure(Call<UpdateUserRes> call, Throwable t) {
                            view.showSnackBar(R.string.error_fail_create_profile);
                        }
                    });
                }
            });
        }
    }

//    private void createOrUpdateProfile(Uri imageUri) {
//        profileManager.createOrUpdateProfile(profile, imageUri, new OnProfileCreatedListener() {
//            @Override
//            public void onProfileCreated(boolean success) {
//                EditProfilePresenter.this.ifViewAttached(new ViewAction<V>() {
//                    @Override
//                    public void run(@NonNull V view) {
//                        view.hideProgress();
//                        if (success) {
//                            EditProfilePresenter.this.onProfileUpdatedSuccessfully();
//                        } else {
//                            view.showSnackBar(R.string.error_fail_create_profile);
//                        }
//                    }
//                });
//            }
//        });
//    }

    protected void onProfileUpdatedSuccessfully() {
        ifViewAttached(BaseView::startMainActivity);
        ifViewAttached(BaseView::finish);
    }

    public void initProfessionalList() {
        ifViewAttached(new ViewAction<V>() {
            @Override
            public void run(@NonNull V view) {
                view.createProfessionalList();
            }
        });
    }

}
