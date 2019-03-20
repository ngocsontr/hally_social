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
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.hally.influencerai.R;
import com.hally.influencerai.main.base.BaseView;
import com.hally.influencerai.main.pickImageBase.PickImagePresenter;
import com.hally.influencerai.managers.ProfileManager;
import com.hally.influencerai.managers.listeners.OnObjectChangedListenerSimple;
import com.hally.influencerai.managers.listeners.OnProfileCreatedListener;
import com.hally.influencerai.model.Profile;
import com.hally.influencerai.utils.ValidationUtil;

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
        ifViewAttached(BaseView::showProgress);
        profileManager.getProfileSingleValue(getCurrentUserId(), new OnObjectChangedListenerSimple<Profile>() {
            @Override
            public void onObjectChanged(Profile obj) {
                profile = obj;
                ifViewAttached(new ViewAction<V>() {
                    @Override
                    public void run(@NonNull V view) {
                        if (profile != null) {
                            view.setName(profile.getUsername());

                            if (profile.getPhotoUrl() != null) {
                                view.setProfilePhoto(profile.getPhotoUrl());
                            }
                        }

                        view.hideProgress();
                        view.setNameError(null);
                    }
                });
            }
        });
    }

    public void attemptCreateProfile(Uri imageUri) {
        if (checkInternetConnection()) {
            ifViewAttached(new ViewAction<V>() {
                @Override
                public void run(@NonNull V view) {
                    view.setNameError(null);

                    String name = view.getNameText().trim();
                    boolean cancel = false;

                    if (TextUtils.isEmpty(name)) {
                        view.setNameError(context.getString(R.string.error_field_required));
                        cancel = true;
                    } else if (!ValidationUtil.isNameValid(name)) {
                        view.setNameError(context.getString(R.string.error_profile_name_length));
                        cancel = true;
                    }

                    if (!cancel) {
                        view.showProgress();
                        profile.setUsername(name);
                        EditProfilePresenter.this.createOrUpdateProfile(imageUri);
                    }
                }
            });
        }
    }

    private void createOrUpdateProfile(Uri imageUri) {
        profileManager.createOrUpdateProfile(profile, imageUri, new OnProfileCreatedListener() {
            @Override
            public void onProfileCreated(boolean success) {
                EditProfilePresenter.this.ifViewAttached(new ViewAction<V>() {
                    @Override
                    public void run(@NonNull V view) {
                        view.hideProgress();
                        if (success) {
                            EditProfilePresenter.this.onProfileUpdatedSuccessfully();
                        } else {
                            view.showSnackBar(R.string.error_fail_create_profile);
                        }
                    }
                });
            }
        });
    }

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
