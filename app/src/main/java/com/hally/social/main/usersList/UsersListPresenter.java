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

package com.hally.social.main.usersList;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.hally.social.R;
import com.hally.social.main.base.BasePresenter;
import com.hally.social.main.base.BaseView;
import com.hally.social.managers.FollowManager;
import com.hally.social.managers.listeners.OnDataChangedListener;
import com.hally.social.managers.listeners.OnRequestComplete;
import com.hally.social.utils.LogUtil;
import com.hally.social.views.FollowButton;

import java.util.List;

/**
 * Created by Alexey on 03.05.18.
 */

class UsersListPresenter extends BasePresenter<UsersListView> {

    private final FollowManager followManager;
    private String currentUserId;
    private Activity activity;

    UsersListPresenter(Activity activity) {
        super(activity);
        this.activity = activity;

        followManager = FollowManager.getInstance(context);
        currentUserId = FirebaseAuth.getInstance().getUid();
    }

    public void loadFollowings(String userID, boolean isRefreshing) {
        if (checkInternetConnection()) {
            if (!isRefreshing) {
                ifViewAttached(UsersListView::showLocalProgress);
            }

            FollowManager.getInstance(context).getFollowingsIdsList(userID, new OnDataChangedListener<String>() {
                @Override
                public void onListChanged(List<String> list) {
                    UsersListPresenter.this.ifViewAttached(new ViewAction<UsersListView>() {
                        @Override
                        public void run(@NonNull UsersListView view) {
                            view.hideLocalProgress();
                            view.onProfilesIdsListLoaded(list);
                            if (list.size() > 0) {
                                view.hideEmptyListMessage();
                            } else {
                                String message = context.getString(R.string.message_empty_list, context.getString(R.string.title_followings));
                                view.showEmptyListMessage(message);
                            }
                        }
                    });
                }
            });
        }
    }

    public void loadFollowers(String userID, boolean isRefreshing) {
        if (checkInternetConnection()) {
            if (!isRefreshing) {
                ifViewAttached(UsersListView::showLocalProgress);
            }

            FollowManager.getInstance(context).getFollowersIdsList(userID, new OnDataChangedListener<String>() {
                @Override
                public void onListChanged(List<String> list) {
                    UsersListPresenter.this.ifViewAttached(new ViewAction<UsersListView>() {
                        @Override
                        public void run(@NonNull UsersListView view) {
                            view.hideLocalProgress();
                            view.onProfilesIdsListLoaded(list);

                            if (list.size() > 0) {
                                view.hideEmptyListMessage();
                            } else {
                                String message = context.getString(R.string.message_empty_list, context.getString(R.string.title_followers));
                                view.showEmptyListMessage(message);
                            }

                        }
                    });
                }
            });
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
        ifViewAttached(BaseView::showProgress);
        followManager.followUser(activity, currentUserId, targetUserId, new OnRequestComplete() {
            @Override
            public void onComplete(boolean success) {
                UsersListPresenter.this.ifViewAttached(new ViewAction<UsersListView>() {
                    @Override
                    public void run(@NonNull UsersListView view) {
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
                UsersListPresenter.this.ifViewAttached(new ViewAction<UsersListView>() {
                    @Override
                    public void run(@NonNull UsersListView view) {
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