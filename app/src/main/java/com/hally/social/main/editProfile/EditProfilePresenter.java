/*
 * Copyright 2018 Rozdoum
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

package com.hally.social.main.editProfile;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.hally.social.R;
import com.hally.social.main.base.BaseView;
import com.hally.social.main.pickImageBase.PickImagePresenter;
import com.hally.social.managers.ProfileManager;
import com.hally.social.managers.listeners.OnObjectChangedListenerSimple;
import com.hally.social.managers.listeners.OnProfileCreatedListener;
import com.hally.social.model.Profile;
import com.hally.social.utils.ValidationUtil;

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
        ifViewAttached(BaseView::finish);
    }

}
