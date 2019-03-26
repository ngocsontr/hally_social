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

package com.hally.influencerai.main.editProfile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.hally.influencerai.R;
import com.hally.influencerai.main.pickImageBase.PickImageActivity;
import com.hally.influencerai.model.SocialUser;
import com.hally.influencerai.utils.GlideApp;
import com.hally.influencerai.utils.ImageUtil;
import com.hally.influencerai.utils.LogUtil;
import com.nex3z.togglebuttongroup.MultiSelectToggleGroup;
import com.nex3z.togglebuttongroup.SingleSelectToggleGroup;
import com.nex3z.togglebuttongroup.button.LabelToggle;

public class EditProfileActivity<V extends EditProfileView, P extends EditProfilePresenter<V>> extends PickImageActivity<V, P> implements EditProfileView {
    private static final String TAG = EditProfileActivity.class.getSimpleName();

    public static final String SOCIAL_USER_EXTRA_KEY = "SOCIAL_USER_EXTRA_KEY";

    protected SocialUser socialUser;
    // UI references.
    private EditText emailEditText;
    private EditText nameEditText;
    protected EditText locationEditText;
    private EditText desEditText;
    protected ImageView avatarImageView;
    private ProgressBar avatarProgressBar;
    private SingleSelectToggleGroup userTypeChoices;
    private MultiSelectToggleGroup groupProfessional;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        socialUser = getIntent().getParcelableExtra(SOCIAL_USER_EXTRA_KEY);

        avatarProgressBar = findViewById(R.id.avatarProgressBar);
        avatarImageView = findViewById(R.id.imageView);
        emailEditText = findViewById(R.id.emailEditText);
        nameEditText = findViewById(R.id.nameEditText);
        locationEditText = findViewById(R.id.locationEditText);
        desEditText = findViewById(R.id.descriptionEditText);
        userTypeChoices = findViewById(R.id.user_type_choices);
        groupProfessional = findViewById(R.id.group_professional);

        avatarImageView.setOnClickListener(this::onSelectImageClick);
        userTypeChoices.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                LogUtil.logInfo(TAG, "" + group + checkedId);
            }
        });
        groupProfessional.setOnCheckedChangeListener(new MultiSelectToggleGroup.OnCheckedStateChangeListener() {
            @Override
            public void onCheckedStateChanged(MultiSelectToggleGroup group, int checkedId, boolean isChecked) {
                LogUtil.logInfo(TAG, "" + group + group.getCheckedIds() + isChecked);
            }
        });

        initContent();
    }

    @NonNull
    @Override
    public P createPresenter() {
        if (presenter == null) {
            return (P) new EditProfilePresenter(this);
        }
        return presenter;
    }

    protected void initContent() {
        presenter.loadProfile();
        presenter.initProfessionalList();
    }

    @Override
    public ProgressBar getProgressView() {
        return avatarProgressBar;
    }

    @Override
    public ImageView getAvatarImageView() {
        return avatarImageView;
    }

    @Override
    public void onImagePikedAction() {
        startCropImageActivity();
    }

    @Override
    @SuppressLint("NewApi")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // handle result of pick image chooser
        super.onActivityResult(requestCode, resultCode, data);
        handleCropImageResult(requestCode, resultCode, data);
    }

    @Override
    public void setName(String username) {
        nameEditText.setText(username);
    }

    @Override
    public void setProfilePhoto(String photoUrl) {
        ImageUtil.loadImage(GlideApp.with(this), photoUrl, avatarImageView, new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                avatarProgressBar.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                avatarProgressBar.setVisibility(View.GONE);
                return false;
            }
        });
    }

    @Override
    public String getNameText() {
        return nameEditText.getText().toString();
    }

    @Override
    public void setNameError(@Nullable String string) {
        nameEditText.setError(string);
        nameEditText.requestFocus();
    }

    @Override
    public void createProfessionalList() {
        groupProfessional.removeAllViews();
        String[] proList = getResources().getStringArray(R.array.professional_array);
        for (String category : proList) {
            LabelToggle toggle = new LabelToggle(this);
            toggle.setText(category);
            groupProfessional.addView(toggle);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void buildProfile(SocialUser profile) {
        setProfilePhoto(profile.getAvatar());
        emailEditText.setText(profile.getEmail());
        nameEditText.setText(profile.getUsername());
        locationEditText.setText(profile.getLocation());
        desEditText.setText(profile.getDescription() + "\r\n" + profile.getAbout() + "\r\n" + profile.getWebsite());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.save:
                presenter.attemptCreateProfile(socialUser);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

