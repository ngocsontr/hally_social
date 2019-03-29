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


package com.hally.influencerai.main.search.users;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.hally.influencerai.main.base.BasePresenter;
import com.hally.influencerai.main.base.BaseView;
import com.hally.influencerai.managers.FollowManager;
import com.hally.influencerai.managers.ProfileManager;
import com.hally.influencerai.managers.listeners.OnDataChangedListener;
import com.hally.influencerai.managers.listeners.OnRequestComplete;
import com.hally.influencerai.model.Profile;
import com.hally.influencerai.utils.LogUtil;
import com.hally.influencerai.views.FollowButton;

import java.util.List;

/**
 * Created by HallyTran on 08.06.18.
 */
public class SearchUsersPresenter extends BasePresenter<SearchUsersView> {
    private final FollowManager followManager;
    private String currentUserId;
    private Activity activity;
    private ProfileManager profileManager;

    public SearchUsersPresenter(Activity activity) {
        super(activity);
        this.activity = activity;

        followManager = FollowManager.getInstance(context);
        currentUserId = FirebaseAuth.getInstance().getUid();
        profileManager = ProfileManager.getInstance(context.getApplicationContext());
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

    private void followUser(String targetUserId) {
        ifViewAttached(BaseView::showProgress);
        followManager.followUser(activity, currentUserId, targetUserId, new OnRequestComplete() {
            @Override
            public void onComplete(boolean success) {
                SearchUsersPresenter.this.ifViewAttached(new ViewAction<SearchUsersView>() {
                    @Override
                    public void run(@NonNull SearchUsersView view) {
                        view.hideProgress();
                        if (success) {
                            view.updateSelectedItem();
                        } else {
                            LogUtil.logDebug(TAG, "followUser, success: " + false);
                        }
                    }
                });
            }
        });
    }

    public void unfollowUser(String targetUserId) {
        ifViewAttached(BaseView::showProgress);
        followManager.unfollowUser(activity, currentUserId, targetUserId, new OnRequestComplete() {
            @Override
            public void onComplete(boolean success) {
                SearchUsersPresenter.this.ifViewAttached(new ViewAction<SearchUsersView>() {
                    @Override
                    public void run(@NonNull SearchUsersView view) {
                        view.hideProgress();
                        if (success) {
                            view.updateSelectedItem();
                        } else {
                            LogUtil.logDebug(TAG, "unfollowUser, success: " + false);
                        }
                    }
                });
            }
        });
    }

    public void loadUsersWithEmptySearch() {
        search("");
    }

    public void search(String searchText) {
        if (checkInternetConnection()) {
            ifViewAttached(SearchUsersView::showLocalProgress);
            profileManager.search(searchText, new OnDataChangedListener<Profile>() {
                @Override
                public void onListChanged(List<Profile> list) {
                    SearchUsersPresenter.this.ifViewAttached(new ViewAction<SearchUsersView>() {
                        @Override
                        public void run(@NonNull SearchUsersView view) {
                            view.hideLocalProgress();
                            view.onSearchResultsReady(list);

                            if (list.isEmpty()) {
                                view.showEmptyListLayout();
                            }
                        }
                    });

                    LogUtil.logDebug(TAG, "search text: " + searchText);
                    LogUtil.logDebug(TAG, "found items count: " + list.size());
                }
            });
        } else {
            ifViewAttached(SearchUsersView::hideLocalProgress);
        }
    }

}
