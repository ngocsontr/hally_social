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

package com.hally.social.main.post.editPost;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;

import com.hally.social.R;
import com.hally.social.main.post.BaseCreatePostPresenter;
import com.hally.social.managers.PostManager;
import com.hally.social.managers.listeners.OnPostChangedListener;
import com.hally.social.model.Post;

/**
 * Created by Alexey on 03.05.18.
 */

class EditPostPresenter extends BaseCreatePostPresenter<EditPostView> {

    private Post post;

    EditPostPresenter(Context context) {
        super(context);
    }

    public void setPost(Post post) {
        this.post = post;
    }

    @Override
    protected int getSaveFailMessage() {
        return R.string.error_fail_update_post;
    }

    @Override
    protected boolean isImageRequired() {
        return false;
    }

    private void updatePostIfChanged(Post updatedPost) {
        if (post.getLikesCount() != updatedPost.getLikesCount()) {
            post.setLikesCount(updatedPost.getLikesCount());
        }

        if (post.getCommentsCount() != updatedPost.getCommentsCount()) {
            post.setCommentsCount(updatedPost.getCommentsCount());
        }

        if (post.getWatchersCount() != updatedPost.getWatchersCount()) {
            post.setWatchersCount(updatedPost.getWatchersCount());
        }

        if (post.isHasComplain() != updatedPost.isHasComplain()) {
            post.setHasComplain(updatedPost.isHasComplain());
        }
    }

    @Override
    protected void savePost(final String title, final String description) {
        ifViewAttached(new ViewAction<EditPostView>() {
            @Override
            public void run(@NonNull EditPostView view) {
                view.showProgress(R.string.message_saving);

                post.setTitle(title);
                post.setDescription(description);

                if (view.getImageUri() != null) {
                    postManager.createOrUpdatePostWithImage(view.getImageUri(), EditPostPresenter.this, post);
                } else {
                    postManager.createOrUpdatePost(post);
                    EditPostPresenter.this.onPostSaved(true);
                }
            }
        });
    }

    public void addCheckIsPostChangedListener() {
        PostManager.getInstance(context.getApplicationContext()).getPost(context, post.getId(), new OnPostChangedListener() {
            @Override
            public void onObjectChanged(Post obj) {
                if (obj == null) {
                    ifViewAttached(new ViewAction<EditPostView>() {
                        @Override
                        public void run(@NonNull EditPostView view) {
                            view.showWarningDialog(R.string.error_post_was_removed, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    view.openMainActivity();
                                    view.finish();
                                }
                            });
                        }
                    });
                } else {
                    updatePostIfChanged(obj);
                }
            }

            @Override
            public void onError(String errorText) {
                ifViewAttached(new ViewAction<EditPostView>() {
                    @Override
                    public void run(@NonNull EditPostView view) {
                        view.showWarningDialog(errorText, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                view.openMainActivity();
                                view.finish();
                            }
                        });
                    }
                });
            }
        });
    }

    public void closeListeners() {
        postManager.closeListeners(context);
    }
}