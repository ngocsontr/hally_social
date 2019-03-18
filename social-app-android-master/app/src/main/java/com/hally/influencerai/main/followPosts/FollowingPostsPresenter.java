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

package com.hally.influencerai.main.followPosts;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.hally.influencerai.R;
import com.hally.influencerai.main.base.BasePresenter;
import com.hally.influencerai.managers.PostManager;
import com.hally.influencerai.managers.listeners.OnDataChangedListener;
import com.hally.influencerai.managers.listeners.OnObjectExistListener;
import com.hally.influencerai.managers.listeners.OnPostChangedListener;
import com.hally.influencerai.model.FollowingPost;
import com.hally.influencerai.model.Post;

import java.util.List;

/**
 * Created by Alexey on 03.05.18.
 */

class FollowingPostsPresenter extends BasePresenter<FollowPostsView> {

    private PostManager postManager;

    FollowingPostsPresenter(Context context) {
        super(context);
        postManager = PostManager.getInstance(context);
    }

    void onPostClicked(final String postId, final View postView) {
        postManager.isPostExistSingleValue(postId, new OnObjectExistListener<Post>() {
            @Override
            public void onDataChanged(boolean exist) {
                FollowingPostsPresenter.this.ifViewAttached(new ViewAction<FollowPostsView>() {
                    @Override
                    public void run(@NonNull FollowPostsView view) {
                        if (exist) {
                            view.openPostDetailsActivity(postId, postView);
                        } else {
                            view.showSnackBar(R.string.error_post_was_removed);
                        }
                    }
                });
            }
        });
    }

    public void loadFollowingPosts() {
        if (checkInternetConnection()) {
            if (getCurrentUserId() != null) {
                ifViewAttached(FollowPostsView::showLocalProgress);
                postManager.getFollowingPosts(getCurrentUserId(), new OnDataChangedListener<FollowingPost>() {
                    @Override
                    public void onListChanged(List<FollowingPost> list) {
                        FollowingPostsPresenter.this.ifViewAttached(new ViewAction<FollowPostsView>() {
                            @Override
                            public void run(@NonNull FollowPostsView view) {
                                view.hideLocalProgress();
                                view.onFollowingPostsLoaded(list);
                                view.showEmptyListMessage(list.isEmpty());
                            }
                        });
                    }
                });
            } else {
                ifViewAttached(new ViewAction<FollowPostsView>() {
                    @Override
                    public void run(@NonNull FollowPostsView view) {
                        view.showEmptyListMessage(true);
                        view.hideLocalProgress();
                    }
                });
            }
        } else {
            ifViewAttached(FollowPostsView::hideLocalProgress);
        }
    }

    public void onRefresh() {
        loadFollowingPosts();
    }

    public void onAuthorClick(String postId, View authorView) {
        postManager.getSinglePostValue(postId, new OnPostChangedListener() {
            @Override
            public void onObjectChanged(Post obj) {
                ifViewAttached(new ViewAction<FollowPostsView>() {
                    @Override
                    public void run(@NonNull FollowPostsView view) {
                        view.openProfileActivity(obj.getAuthorId(), authorView);
                    }
                });
            }

            @Override
            public void onError(String errorText) {
                ifViewAttached(new ViewAction<FollowPostsView>() {
                    @Override
                    public void run(@NonNull FollowPostsView view) {
                        view.showSnackBar(errorText);
                    }
                });
            }
        });
    }
}
