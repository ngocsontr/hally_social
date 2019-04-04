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

package com.hally.influencerai.main.editProfile.createProfile;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.hally.influencerai.R;
import com.hally.influencerai.main.editProfile.EditProfileActivity;
import com.hally.influencerai.model.User;

/**
 * Created by HallyTran on 3/22/2019.
 * transon97uet@gmail.com
 */
public class CreateProfileActivity extends
        EditProfileActivity<CreateProfileView, CreateProfilePresenter> implements
        CreateProfileView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
    }

    @Override
    protected void initContent() {
        presenter.buildProfile(user);
        presenter.initCategoryView();
        if (TextUtils.isEmpty(user.getLocation())) {
            presenter.initLocation();
        }
    }

    @NonNull
    @Override
    public CreateProfilePresenter createPresenter() {
        if (presenter == null) {
            return new CreateProfilePresenter(this);
        }
        return presenter;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.create_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.continueButton:
                hideKeyboard();
                presenter.attemptCreateOrUpdateProfile(getUser());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void buildProfile(User user) {
        loadSocialItem();
        setProfilePhoto(user.getAvatar());
        emailEditText.setText(user.getEmail());
        nameEditText.setText(user.getUsername());
        dobEditText.setText(user.getDateOfBirth());
        locationEditText.setText(user.getLocation());
        desEditText.setText(TextUtils.isEmpty(user.getDescription()) ? "" : user.getDescription()
                + (TextUtils.isEmpty(user.getAbout()) ? "" : "\n" + user.getAbout())
                + (TextUtils.isEmpty(user.getWebsite()) ? "" : "\n" + user.getWebsite()));
    }

    @Override
    public void setLocation(String location) {
        if (TextUtils.isEmpty(user.getLocation())) {
            user.setLocation(location);
            locationEditText.setText(user.getLocation());
        }
    }
}
