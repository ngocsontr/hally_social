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

package com.hally.influencerai.main.post.editPost;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.hally.influencerai.R;
import com.hally.influencerai.main.post.BaseCreatePostActivity;
import com.hally.influencerai.managers.PostManager;
import com.hally.influencerai.model.Post;
import com.hally.influencerai.utils.GlideApp;

public class EditPostActivity extends BaseCreatePostActivity<EditPostView, EditPostPresenter> implements EditPostView {
    private static final String TAG = EditPostActivity.class.getSimpleName();
    public static final String POST_EXTRA_KEY = "EditPostActivity.POST_EXTRA_KEY";
    public static final int EDIT_POST_REQUEST = 33;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Post post = (Post) getIntent().getSerializableExtra(POST_EXTRA_KEY);
        presenter.setPost(post);
        showProgress();
        fillUIFields(post);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.addCheckIsPostChangedListener();
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.closeListeners();
    }

    @NonNull
    @Override
    public EditPostPresenter createPresenter() {
        if (presenter == null) {
            return new EditPostPresenter(this);
        }
        return presenter;
    }

    private void fillUIFields(Post post) {
        titleEditText.setText(post.getTitle());
        descriptionEditText.setText(post.getDescription());
        loadPostDetailsImage(post.getImageTitle());
        hideProgress();
    }

    private void loadPostDetailsImage(String imageTitle) {
        PostManager.getInstance(this.getApplicationContext()).loadImageMediumSize(GlideApp.with(this),
                imageTitle,
                imageView,
                new PostManager.OnImageRequestListener() {
                    @Override
                    public void onImageRequestFinished() {
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_post, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.save:
                presenter.doSavePost(imageUri);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
