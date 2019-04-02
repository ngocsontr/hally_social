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

package com.hally.influencerai.main.usersList;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.hally.influencerai.R;
import com.hally.influencerai.main.base.BasePresenter;
import com.hally.influencerai.views.FollowButton;

/**
 * Created by HallyTran on 03.05.18.
 */

class UsersListPresenter extends BasePresenter<UsersListView> {

    private String currentUserId;
    private Activity activity;

    UsersListPresenter(Activity activity) {
        super(activity);
        this.activity = activity;

    }

    public void loadFollowings(String userID, boolean isRefreshing) {
        if (checkInternetConnection()) {
            if (!isRefreshing) {
                ifViewAttached(UsersListView::showLocalProgress);
            }

        }
    }

    public void loadFollowers(String userID, boolean isRefreshing) {
        if (checkInternetConnection()) {
            if (!isRefreshing) {
                ifViewAttached(UsersListView::showLocalProgress);
            }

        }
    }

    public void onRefresh(String userId, int userListType) {
        loadUsersList(userId, userListType, true);
    }

    public void loadUsersList(String userId, int userListType, boolean isRefreshing) {
        if (userListType == UsersListType.FOLLOWERS) {
            loadFollowers(userId, isRefreshing);
        } else if (userListType == UsersListType.FOLLOWINGS) {
            loadFollowings(userId, false);
        }
    }

    public void chooseActivityTitle(int userListType) {
        ifViewAttached(new ViewAction<UsersListView>() {
            @Override
            public void run(@NonNull UsersListView view) {
                if (userListType == UsersListType.FOLLOWERS) {
                    view.setTitle(R.string.title_followers);
                } else if (userListType == UsersListType.FOLLOWINGS) {
                    view.setTitle(R.string.title_followings);
                }
            }
        });

    }

    private void followUser(String targetUserId) {
    }

    public void unfollowUser(String targetUserId) {
    }

    public void onFollowButtonClick(int state, String targetUserId) {
        if (checkInternetConnection() && checkAuthorization()) {
            if (state == FollowButton.FOLLOW_STATE || state == FollowButton.FOLLOW_BACK_STATE) {
                followUser(targetUserId);
            } else if (state == FollowButton.FOLLOWING_STATE) {
                unfollowUser(targetUserId);
            }
        }
    }
}
