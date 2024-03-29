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

package com.hally.influencerai.main.post.createPost;

import android.content.Context;
import android.support.annotation.NonNull;

import com.hally.influencerai.R;
import com.hally.influencerai.main.post.BaseCreatePostPresenter;
import com.hally.influencerai.model.Post;

/**
 * Created by HallyTran on 03.05.18.
 */

public class CreatePostPresenter extends BaseCreatePostPresenter<CreatePostView> {

    public CreatePostPresenter(Context context) {
        super(context);
    }

    @Override
    protected int getSaveFailMessage() {
        return R.string.error_fail_create_post;
    }

    @Override
    protected void savePost(String title, String description) {
        ifViewAttached(new ViewAction<CreatePostView>() {
            @Override
            public void run(@NonNull CreatePostView view) {
                view.showProgress(R.string.message_creating_post);
                Post post = new Post();
                post.setTitle(title);
                post.setDescription(description);
//                post.setAuthorId(FirebaseAuth.getInstance().getCurrentUser().getUid());
                postManager.createOrUpdatePostWithImage(view.getImageUri(), CreatePostPresenter.this, post);
            }
        });
    }

    @Override
    protected boolean isImageRequired() {
        return true;
    }
}
