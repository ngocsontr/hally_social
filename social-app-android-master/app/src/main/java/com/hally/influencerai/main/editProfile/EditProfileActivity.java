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
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.hally.influencerai.Constants;
import com.hally.influencerai.R;
import com.hally.influencerai.main.pickImageBase.PickImageActivity;
import com.hally.influencerai.model.User;
import com.hally.influencerai.utils.GlideApp;
import com.hally.influencerai.utils.ImageUtil;
import com.hally.influencerai.utils.LogUtil;
import com.nex3z.togglebuttongroup.MultiSelectToggleGroup;
import com.nex3z.togglebuttongroup.SingleSelectToggleGroup;
import com.nex3z.togglebuttongroup.button.LabelToggle;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rengwuxian.materialedittext.validation.RegexpValidator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by HallyTran on 3/22/2019.
 * transon97uet@gmail.com
 */
public class EditProfileActivity<V extends EditProfileView, P extends EditProfilePresenter<V>> extends
        PickImageActivity<V, P> implements EditProfileView {
    private static final String TAG = EditProfileActivity.class.getSimpleName();

    public static final String SOCIAL_USER_EXTRA_KEY = "SOCIAL_USER_EXTRA_KEY";
    private String[] PROFESSIONS;

    protected User user;
    // UI references.
    protected MaterialEditText emailEditText;
    protected MaterialEditText nameEditText;
    protected LabelToggle male;
    protected LabelToggle female;
    protected MaterialEditText locationEditText;
    protected MaterialEditText dobEditText;
    protected MaterialEditText desEditText;
    protected ImageView avatarImageView;
    protected ProgressBar avatarProgressBar;
    protected SingleSelectToggleGroup userTypeChoices;
    protected LabelToggle influencer;
    protected LabelToggle marketer;
    protected MultiSelectToggleGroup groupProfessional;
    TextWatcher dob_tw = new TextWatcher() {
        private String current = "";
        private String dateFormat = "YYYYMMDD";
        private Calendar cal = Calendar.getInstance();

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!s.toString().equals(current)) {
                String clean = s.toString().replaceAll("[^\\d.]|\\.", "");
                String cleanC = current.replaceAll("[^\\d.]|\\.", "");

                int cl = clean.length();
                int sel = cl;
                for (int i = 2; i <= cl && i < 6; i += 2) {
                    sel++;
                }
                //Fix for pressing delete next to a forward slash
                if (clean.equals(cleanC)) sel--;

                if (clean.length() < 8) {
                    clean = clean + dateFormat.substring(clean.length());
                } else {
                    //This part makes sure that when we finish entering numbers
                    //the date is correct, fixing it otherwise
                    int year = Integer.parseInt(clean.substring(0, 4));
                    int mon = Integer.parseInt(clean.substring(4, 6));
                    int day = Integer.parseInt(clean.substring(6, 8));

                    mon = mon < 1 ? 1 : mon > 12 ? 12 : mon;
                    cal.set(Calendar.MONTH, mon - 1);
                    year = (year < 1900) ? 1900 : (year > 2100) ? 2100 : year;
                    cal.set(Calendar.YEAR, year);
                    // ^ first set year for the line below to work correctly
                    //with leap years - otherwise, date e.g. 29/02/2012
                    //would be automatically corrected to 28/02/2012

                    day = (day > cal.getActualMaximum(Calendar.DATE))
                            ? cal.getActualMaximum(Calendar.DATE) : day;
                    clean = String.format("%02d%02d%02d", year, mon, day);
                }

                clean = String.format("%s-%s-%s",
                        clean.substring(0, 4),
                        clean.substring(4, 6),
                        clean.substring(6, 8));

                sel = sel < 0 ? 0 : sel;
                current = clean;
                dobEditText.setText(current);
                dobEditText.setSelection(sel < current.length() ? sel : current.length());
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        user = getIntent().getParcelableExtra(SOCIAL_USER_EXTRA_KEY);
        PROFESSIONS = getResources().getStringArray(R.array.professional_array);

        avatarProgressBar = findViewById(R.id.avatarProgressBar);
        avatarImageView = findViewById(R.id.imageView);
        emailEditText = findViewById(R.id.emailEditText);
        emailEditText.addValidator(new RegexpValidator(getString(R.string.error_invalid_email),
                Constants.EMAIL_PATTERN));
        nameEditText = findViewById(R.id.nameEditText);
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);
        dobEditText = findViewById(R.id.dobEditText);
        dobEditText.addValidator(new RegexpValidator(getString(R.string.dob_helper),
                Constants.DOB_PATTERN));
        dobEditText.addTextChangedListener(dob_tw);
        locationEditText = findViewById(R.id.locationEditText);
        desEditText = findViewById(R.id.descriptionEditText);
        userTypeChoices = findViewById(R.id.user_type_choices);
        influencer = findViewById(R.id.influencer);
        marketer = findViewById(R.id.marketer);
        groupProfessional = findViewById(R.id.group_professional);

        avatarImageView.setOnClickListener(this::onSelectImageClick);
        userTypeChoices.setOnCheckedChangeListener((group, checkedId)
                -> LogUtil.logInfo(TAG, "" + group + checkedId));
        groupProfessional.setOnCheckedChangeListener((group, checkedId, isChecked)
                -> LogUtil.logInfo(TAG, "" + group + group.getCheckedIds() + isChecked));

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
        presenter.loadProfile(user);
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
        for (String category : PROFESSIONS) {
            LabelToggle toggle = new LabelToggle(this);
            toggle.setText(category);
            groupProfessional.addView(toggle);
        }
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
                presenter.setIsUpdate(true);
                presenter.attemptCreateOrUpdateProfile(getUser());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void loadProfile(User user) {
        loadSocialItem();
        if (user.isInfluencer()) {
            influencer.setChecked(true);
            marketer.setVisibility(View.GONE);
        } else {
            marketer.setChecked(true);
            influencer.setVisibility(View.GONE);
        }
        setProfilePhoto(user.getAvatar());
        emailEditText.setText(user.getEmail());
        if (TextUtils.equals(getString(R.string.prompt_male), user.getGender())) {
            male.setChecked(true);
        } else {
            female.setChecked(true);
        }
        nameEditText.setText(user.getUsername());
        dobEditText.setText(user.getDateOfBirth());
        locationEditText.setText(user.getLocation());
        desEditText.setText(user.getDescription());
    }

    protected void loadSocialItem() {
//        if (user.getUserSocials() == null) return;
//        for (UserSocial userSocial : user.getUserSocials()) {
        ImageView socialImageView = null;
        switch (/*userSocial.getSocialType()*/1) {
            case Constants.UserType.FACEBOOK:
                socialImageView = findViewById(R.id.facebookImage);
                break;
            case Constants.UserType.TWITTER:
                socialImageView = findViewById(R.id.twitterImage);
                break;
            case Constants.UserType.INSTAGRAM:
                socialImageView = findViewById(R.id.instagramImage);
                break;
            case Constants.UserType.YOUTUBE:
                socialImageView = findViewById(R.id.youtubeImage);
                break;
            default:
        }
        if (socialImageView != null) {
            socialImageView.setVisibility(View.VISIBLE);
            ImageUtil.loadImage(GlideApp.with(this),
                    user.getAvatar()/*userSocial.getSocialAvatar()*/, socialImageView);
        }
//        }
    }

    protected User getUser() {
        user.setAvatar(user.getAvatar());
        user.setEmail(emailEditText.getText().toString());
        user.setUsername(nameEditText.getText().toString());
        user.setGender(getString(male.isChecked() ? R.string.prompt_male : R.string.prompt_female));
        user.setDateOfBirth(dobEditText.getText().toString());
        user.setLocation(locationEditText.getText().toString());
        user.setDescription(desEditText.getText().toString());
        user.setUserType(getUserType());
        user.setProfessions(getProfessions());
        return user;
    }

    protected List<String> getProfessions() {
        List<String> results = new ArrayList<>();
        for (int i = 0; i < groupProfessional.getChildCount(); i++) {
            LabelToggle toggle = (LabelToggle) groupProfessional.getChildAt(i);
            if (toggle.isChecked()) {
                results.add(toggle.getText().toString());
            }
        }
        return results;
    }

    private String getUserType() {
        int checkId = userTypeChoices.getCheckedId();
        return String.valueOf(checkId == R.id.marketer ? 1 : 2);
    }
}

