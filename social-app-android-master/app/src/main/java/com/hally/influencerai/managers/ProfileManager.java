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

package com.hally.influencerai.managers;

import android.content.Context;
import android.net.Uri;

import com.hally.influencerai.enums.ProfileStatus;
import com.hally.influencerai.main.interactors.ProfileInteractor;
import com.hally.influencerai.managers.listeners.OnDataChangedListener;
import com.hally.influencerai.managers.listeners.OnObjectChangedListener;
import com.hally.influencerai.managers.listeners.OnObjectExistListener;
import com.hally.influencerai.managers.listeners.OnProfileCreatedListener;
import com.hally.influencerai.model.Profile;
import com.hally.influencerai.utils.SharePreUtil;

/**
 * Created by HallyTran on 10/28/16.
 */

public class ProfileManager extends FirebaseListenersManager {

    private static final String TAG = ProfileManager.class.getSimpleName();
    private static ProfileManager instance;

    private Context context;
    private ProfileInteractor profileInteractor;


    public static ProfileManager getInstance(Context context) {
        if (instance == null) {
            instance = new ProfileManager(context);
        }

        return instance;
    }

    private ProfileManager(Context context) {
        this.context = context;
        profileInteractor = ProfileInteractor.getInstance(context);
    }
//
//    public Profile buildProfile(FirebaseUser firebaseUser, String largeAvatarURL) {
//        Profile profile = new Profile(firebaseUser.getUid());
//        profile.setEmail(firebaseUser.getEmail());
//        profile.setUsername(firebaseUser.getDisplayName());
//        profile.setPhotoUrl(largeAvatarURL != null ? largeAvatarURL : firebaseUser.getPhotoUrl().toString());
//        return profile;
//    }

    public void isProfileExist(String id, final OnObjectExistListener<Profile> onObjectExistListener) {
        profileInteractor.isProfileExist(id, onObjectExistListener);
    }

    public void createOrUpdateProfile(Profile profile, OnProfileCreatedListener onProfileCreatedListener) {
        createOrUpdateProfile(profile, null, onProfileCreatedListener);
    }

    public void createOrUpdateProfile(final Profile profile, Uri imageUri, final OnProfileCreatedListener onProfileCreatedListener) {
        if (imageUri == null) {
            profileInteractor.createOrUpdateProfile(profile, onProfileCreatedListener);
        } else {
            profileInteractor.createOrUpdateProfileWithImage(profile, imageUri, onProfileCreatedListener);
        }
    }

    public void getProfileValue(Context activityContext, String id, final OnObjectChangedListener<Profile> listener) {
//        ValueEventListener valueEventListener = profileInteractor.getProfile(id, listener);
//        addListenerToMap(activityContext, valueEventListener);
    }

    public void getProfileSingleValue(String id, final OnObjectChangedListener<Profile> listener) {
        profileInteractor.getProfileSingleValue(id, listener);
    }

    public ProfileStatus checkProfile() {
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Object user = null;
        if (user == null) {
            return ProfileStatus.NOT_AUTHORIZED;
        } else if (!SharePreUtil.isProfileCreated(context)) {
            return ProfileStatus.NO_PROFILE;
        } else {
            return ProfileStatus.PROFILE_CREATED;
        }
    }

    public void search(String searchText, OnDataChangedListener<Profile> onDataChangedListener) {
        closeListeners(context);
//        ValueEventListener valueEventListener = profileInteractor.searchProfiles(searchText, onDataChangedListener);
//        addListenerToMap(context, valueEventListener);
    }

    public void addRegistrationToken(String token, String userId) {
        profileInteractor.addRegistrationToken(token, userId);
    }
}
