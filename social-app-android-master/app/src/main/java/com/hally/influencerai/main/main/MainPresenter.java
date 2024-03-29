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

package com.hally.influencerai.main.main;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.View;

import com.hally.influencerai.R;
import com.hally.influencerai.enums.PostStatus;
import com.hally.influencerai.main.base.BasePresenter;
import com.hally.influencerai.main.editProfile.EditProfileActivity;
import com.hally.influencerai.main.postDetails.PostDetailsActivity;
import com.hally.influencerai.managers.PostManager;
import com.hally.influencerai.managers.listeners.OnObjectExistListener;
import com.hally.influencerai.managers.network.ApiUtils;
import com.hally.influencerai.model.LoginRes;
import com.hally.influencerai.model.Post;
import com.hally.influencerai.utils.LogUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by HallyTran on 03.05.18.
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
//        if (checkAuthorization()) {
//            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
//            ifViewAttached(new ViewAction<MainView>() {
//                @Override
//                public void run(@NonNull MainView view) {
//                    view.openProfileActivity(userId, null);
//                }
//            });
//        }

        ifViewAttached(new ViewAction<MainView>() {
            @Override
            public void run(@NonNull MainView view) {
                if (checkInternetConnection()) {
                    doAuthorization(view);
                }
            }
        });
    }

    private void doAuthorization(MainView view) {
        if (!ApiUtils.login(context, new Callback<LoginRes>() {
            @Override
            public void onResponse(Call<LoginRes> call, Response<LoginRes> response) {
                if (response.errorBody() != null) {
                    LogUtil.logDebug(TAG, "onResponse:error " + response.message());
                    view.showSnackBar(R.string.error_authentication);
                    view.startLoginActivity();
                    return;
                }
                LoginRes loginRes = response.body();
                LogUtil.logDebug(TAG, "onResponse:body() " + response.body());
                if (loginRes != null && loginRes.isAllowAccess()) {
                    Intent intent = new Intent(context, EditProfileActivity.class);
                    intent.putExtra(EditProfileActivity.SOCIAL_USER_EXTRA_KEY, loginRes.getUser());
                    context.startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<LoginRes> call, Throwable t) {
                LogUtil.logDebug(TAG, "onFailure:error " + t.getMessage());
                view.showSnackBar(R.string.error_authentication);
                view.startLoginActivity();
            }
        })) {
            view.startLoginActivity();
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
