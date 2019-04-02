/*
 *
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
 *
 */

package com.hally.influencerai.main.interactors;

import android.content.Context;
import android.net.Uri;

import com.hally.influencerai.ApplicationHelper;
import com.hally.influencerai.enums.UploadImagePrefix;
import com.hally.influencerai.managers.DatabaseHelper;
import com.hally.influencerai.managers.listeners.OnObjectChangedListener;
import com.hally.influencerai.managers.listeners.OnObjectExistListener;
import com.hally.influencerai.managers.listeners.OnProfileCreatedListener;
import com.hally.influencerai.model.Post;
import com.hally.influencerai.model.Profile;
import com.hally.influencerai.utils.ImageUtil;

/**
 * Created by HallyTran on 05.06.18.
 */

public class ProfileInteractor {

    private static final String TAG = ProfileInteractor.class.getSimpleName();
    private static ProfileInteractor instance;

    private DatabaseHelper databaseHelper;
    private Context context;

    public static ProfileInteractor getInstance(Context context) {
        if (instance == null) {
            instance = new ProfileInteractor(context);
        }

        return instance;
    }

    private ProfileInteractor(Context context) {
        this.context = context;
        databaseHelper = ApplicationHelper.getDatabaseHelper();
    }

    public void createOrUpdateProfile(final Profile profile, final OnProfileCreatedListener onProfileCreatedListener) {
    }

    public void createOrUpdateProfileWithImage(final Profile profile, Uri imageUri, final OnProfileCreatedListener onProfileCreatedListener) {
        String imageTitle = ImageUtil.generateImageTitle(UploadImagePrefix.PROFILE, profile.getId());
//        UploadTask uploadTask = databaseHelper.uploadImage(imageUri, imageTitle);
//
//        if (uploadTask != null) {
//            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
//                    if (task.isSuccessful()) {
//                        task.getResult().getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Uri> task1) {
//                                if (task1.isSuccessful()) {
//                                    Uri downloadUrl = task1.getResult();
//                                    LogUtil.logDebug(TAG, "successful upload image, image url: " + String.valueOf(downloadUrl));
//
//                                    profile.setPhotoUrl(downloadUrl.toString());
//                                    ProfileInteractor.this.createOrUpdateProfile(profile, onProfileCreatedListener);
//                                } else {
//                                    onProfileCreatedListener.onProfileCreated(false);
//                                    LogUtil.logDebug(TAG, "createOrUpdateProfileWithImage, fail to getDownloadUrl");
//                                }
//                            }
//                        });
//                    } else {
//                        onProfileCreatedListener.onProfileCreated(false);
//                        LogUtil.logDebug(TAG, "createOrUpdateProfileWithImage, fail to upload image");
//                    }
//
//                }
//            });
//        } else {
//            onProfileCreatedListener.onProfileCreated(false);
//            LogUtil.logDebug(TAG, "fail to upload image");
//        }
    }

    public void isProfileExist(String id, final OnObjectExistListener<Profile> onObjectExistListener) {
    }

    public void getProfileSingleValue(String id, final OnObjectChangedListener<Profile> listener) {
    }

    public void updateProfileLikeCountAfterRemovingPost(Post post) {
//        DatabaseReference profileRef = databaseHelper
//                .getDatabaseReference()
//                .child(DatabaseHelper.PROFILES_DB_KEY + "/" + post.getAuthorId() + "/likesCount");
//        final long likesByPostCount = post.getLikesCount();
//
//        profileRef.runTransaction(new Transaction.Handler() {
//            @Override
//            public Transaction.Result doTransaction(MutableData mutableData) {
//                Integer currentValue = mutableData.getValue(Integer.class);
//                if (currentValue != null && currentValue >= likesByPostCount) {
//                    mutableData.setValue(currentValue - likesByPostCount);
//                }
//
//                return Transaction.success(mutableData);
//            }
//
//            @Override
//            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
//                LogUtil.logInfo(TAG, "Updating likes count transaction is completed.");
//            }
//        });

    }

    public void addRegistrationToken(String token, String userId) {
    }

    public void updateRegistrationToken(final String token) {
    }

    public void removeRegistrationToken(String token, String userId) {
    }


}
