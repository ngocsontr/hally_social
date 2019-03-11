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

package com.hally.influencerai.main.main;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.hally.influencerai.R;
import com.hally.influencerai.enums.PostStatus;
import com.hally.influencerai.main.base.BasePresenter;
import com.hally.influencerai.main.postDetails.PostDetailsActivity;
import com.hally.influencerai.managers.PostManager;
import com.hally.influencerai.managers.listeners.OnObjectExistListener;
import com.hally.influencerai.model.Post;

/**
 * Created by Alexey on 03.05.18.
 */

class MainPresenter extends BasePresenter<MainView> {

    private PostManager postManager;

    MainPresenter(Context context) {
        super(context);
        postManager = PostManager.getInstance(context);
    }


    void onCreatePostClickAction(View anchorView) {
        if (checkInternetConnection(anchorView)) {
            if (checkAuthorization()) {
                ifViewAttached(MainView::openCreatePostActivity);
            }
        }
    }

    void onPostClicked(final Post post, final View postView) {
        postManager.isPostExistSingleValue(post.getId(), new OnObjectExistListener<Post>() {
            @Override
            public void onDataChanged(boolean exist) {
                MainPresenter.this.ifViewAttached(new ViewAction<MainView>() {
                    @Override
                    public void run(@NonNull MainView view) {
                        if (exist) {
                            view.openPostDetailsActivity(post, postView);
                        } else {
                            view.showFloatButtonRelatedSnackBar(R.string.error_post_was_removed);
                        }
                    }
                });
            }
        });
    }

    void onProfileMenuActionClicked() {
        if (checkAuthorization()) {
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            ifViewAttached(new ViewAction<MainView>() {
                @Override
                public void run(@NonNull MainView view) {
                    view.openProfileActivity(userId, null);
                }
            });
        }
    }

    void onPostCreated() {
        ifViewAttached(new ViewAction<MainView>() {
            @Override
            public void run(@NonNull MainView view) {
                view.refreshPostList();
                view.showFloatButtonRelatedSnackBar(R.string.message_post_was_created);
            }
        });
    }

    void onPostUpdated(Intent data) {
        if (data != null) {
            ifViewAttached(new ViewAction<MainView>() {
                @Override
                public void run(@NonNull MainView view) {
                    PostStatus postStatus = (PostStatus) data.getSerializableExtra(PostDetailsActivity.POST_STATUS_EXTRA_KEY);
                    if (postStatus.equals(PostStatus.REMOVED)) {
                        view.removePost();
                        view.showFloatButtonRelatedSnackBar(R.string.message_post_was_removed);
                    } else if (postStatus.equals(PostStatus.UPDATED)) {
                        view.updatePost();
                    }
                }
            });
        }
    }

    void updateNewPostCounter() {
        Handler mainHandler = new Handler(context.getMainLooper());
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                MainPresenter.this.ifViewAttached(new ViewAction<MainView>() {
                    @Override
                    public void run(@NonNull MainView view) {
                        int newPostsQuantity = postManager.getNewPostsCounter();
                        if (newPostsQuantity > 0) {
                            view.showCounterView(newPostsQuantity);
                        } else {
                            view.hideCounterView();
                        }
                    }
                });
            }
        });
    }

    public void initPostCounter() {
        postManager.setPostCounterWatcher(new PostManager.PostCounterWatcher() {
            @Override
            public void onPostCounterChanged(int newValue) {
                MainPresenter.this.updateNewPostCounter();
            }
        });
    }
}
