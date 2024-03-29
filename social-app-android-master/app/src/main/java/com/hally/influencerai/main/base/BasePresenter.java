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

package com.hally.influencerai.main.base;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.View;

import com.hally.influencerai.R;
import com.hally.influencerai.enums.ProfileStatus;
import com.hally.influencerai.managers.ProfileManager;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;

public class BasePresenter<T extends BaseView & MvpView> extends MvpBasePresenter<T> {

    protected String TAG = this.getClass().getSimpleName();

    protected Context context;
    private ProfileManager profileManager;

    public BasePresenter(Context context) {
        this.context = context;
        profileManager = ProfileManager.getInstance(context);
    }


    public void checkPermission() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,}, 1);
        }
    }

    public void checkPermission(String permission) {
        if (ActivityCompat.checkSelfPermission(context, permission)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{permission}, 1);
        }
    }


    public boolean checkInternetConnection() {
        return checkInternetConnection(null);
    }

    public boolean checkInternetConnection(@Nullable View anchorView) {
        boolean hasInternetConnection = hasInternetConnection();
        if (!hasInternetConnection) {
            ifViewAttached(new ViewAction<T>() {
                @Override
                public void run(@NonNull T view) {
                    if (anchorView != null) {
                        view.showSnackBar(anchorView, R.string.internet_connection_failed);
                    } else {
                        view.showSnackBar(R.string.internet_connection_failed);
                    }
                }
            });
        }

        return hasInternetConnection;
    }

    public boolean hasInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public boolean checkAuthorization() {
        ProfileStatus profileStatus = profileManager.checkProfile();
        if (profileStatus.equals(ProfileStatus.NOT_AUTHORIZED) || profileStatus.equals(ProfileStatus.NO_PROFILE)) {
            ifViewAttached(BaseView::startLoginActivity);
            return false;
        } else {
            return true;
        }
    }

    public boolean hasAuthorization() {
        ProfileStatus profileStatus = profileManager.checkProfile();
        if (profileStatus.equals(ProfileStatus.NOT_AUTHORIZED) || profileStatus.equals(ProfileStatus.NO_PROFILE)) {
            return false;
        } else {
            return true;
        }
    }

    public void doAuthorization(ProfileStatus status) {
        if (status.equals(ProfileStatus.NOT_AUTHORIZED) || status.equals(ProfileStatus.NO_PROFILE)) {
            ifViewAttached(BaseView::startLoginActivity);
        }
    }

    protected String getCurrentUserId() {
        return "";
    }

}
