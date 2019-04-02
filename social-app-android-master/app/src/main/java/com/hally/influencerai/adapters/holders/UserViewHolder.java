/*
 *  Copyright 2017
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.hally.influencerai.adapters.holders;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hally.influencerai.R;
import com.hally.influencerai.managers.ProfileManager;
import com.hally.influencerai.managers.listeners.OnObjectChangedListener;
import com.hally.influencerai.managers.listeners.OnObjectChangedListenerSimple;
import com.hally.influencerai.model.Profile;
import com.hally.influencerai.utils.GlideApp;
import com.hally.influencerai.utils.ImageUtil;
import com.hally.influencerai.views.FollowButton;

/**
 * Created by HallyTran on 03.05.18.
 */

public class UserViewHolder extends RecyclerView.ViewHolder {
    public static final String TAG = UserViewHolder.class.getSimpleName();

    private Context context;
    private ImageView photoImageView;
    private TextView nameTextView;
    private FollowButton followButton;

    private ProfileManager profileManager;

    private Activity activity;

    public UserViewHolder(View view, final Callback callback, Activity activity) {
        super(view);
        this.context = view.getContext();
        this.activity = activity;
        profileManager = ProfileManager.getInstance(context);

        nameTextView = view.findViewById(R.id.nameTextView);
        photoImageView = view.findViewById(R.id.photoImageView);
        followButton = view.findViewById(R.id.followButton);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = UserViewHolder.this.getAdapterPosition();
                if (callback != null && position != RecyclerView.NO_POSITION) {
                    callback.onItemClick(UserViewHolder.this.getAdapterPosition(), v);
                }
            }
        });

        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null) {
                    callback.onFollowButtonClick(UserViewHolder.this.getAdapterPosition(), followButton);
                }
            }
        });
    }

    public void bindData(String profileId) {
        profileManager.getProfileSingleValue(profileId, createProfileChangeListener());
    }

    public void bindData(Profile profile) {
        fillInProfileFields(profile);
    }

    private OnObjectChangedListener<Profile> createProfileChangeListener() {
        return new OnObjectChangedListenerSimple<Profile>() {
            @Override
            public void onObjectChanged(Profile obj) {
                fillInProfileFields(obj);
            }
        };
    }

    protected void fillInProfileFields(Profile profile) {
        nameTextView.setText(profile.getUsername());

        if (profile.getPhotoUrl() != null) {
            ImageUtil.loadImage(GlideApp.with(activity), profile.getPhotoUrl(), photoImageView);
        }
    }

    public interface Callback {
        void onItemClick(int position, View view);

        void onFollowButtonClick(int position, FollowButton followButton);
    }

}