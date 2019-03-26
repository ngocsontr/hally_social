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

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.hally.influencerai.main.editProfile.EditProfilePresenter;
import com.hally.influencerai.model.SocialUser;
import com.hally.influencerai.utils.PreferencesUtil;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.content.Context.LOCATION_SERVICE;

class CreateProfilePresenter extends EditProfilePresenter<CreateProfileView> {

    CreateProfilePresenter(Context context) {
        super(context);
    }

    public void buildProfile(SocialUser profile) {

        ifViewAttached(new ViewAction<CreateProfileView>() {
            @Override
            public void run(@NonNull CreateProfileView view) {
                view.buildProfile(profile);
            }
        });
    }

    public void initProfessionalList() {
        ifViewAttached(new ViewAction<CreateProfileView>() {
            @Override
            public void run(@NonNull CreateProfileView view) {
                view.createProfessionalList();
            }
        });
    }


    @Override
    protected void onProfileUpdatedSuccessfully() {
        super.onProfileUpdatedSuccessfully();
        PreferencesUtil.setProfileCreated(context, true);
        profileManager.addRegistrationToken(FirebaseInstanceId.getInstance().getToken(), profile.getId());
    }

    private static final long MIN_TIME = 100;
    private static final float MIN_DISTANCE = 10;

    public void initLocation() {
        ifViewAttached(new ViewAction<CreateProfileView>() {
            @Override
            public void run(@NonNull CreateProfileView view) {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        context, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) context,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION,}, 1);
                    return;
                }

                final LocationManager locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    // show dialog
                    buildAlertMessageNoGps();
                }

                List<String> providers = locationManager.getProviders(true);
                Log.d(TAG, "getLastKnownLocation " + providers);
                Location bestLocation = null;

                for (String provider : providers) {
                    Location l = locationManager.getLastKnownLocation(provider);
                    if (l == null) {
                        continue;
                    }
                    if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                        // Found best last known location: %s", l);
                        bestLocation = l;
                    }
                }
                Log.d(TAG, "getLastKnownLocation " + bestLocation);

                if (bestLocation != null) {
                    try {
                        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                        final int MAX = 1;
                        Address address = geocoder.getFromLocation(
                                bestLocation.getLatitude(), bestLocation.getLongitude(), MAX)
                                .get(MAX - 1);
                        String cityName = address.getAddressLine(0);
                        String stateName = address.getAddressLine(1);
                        String countryName = address.getAddressLine(2);
                        StringBuilder builder = new StringBuilder();
                        builder.append(TextUtils.isEmpty(countryName) ? "" : countryName)
                                .append(TextUtils.isEmpty(stateName) ? "" : stateName)
                                .append(TextUtils.isEmpty(cityName) ? "" : cityName);
                        view.setLocation(builder.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e(TAG, "getLastKnownLocation null ");
                    Criteria criteria = new Criteria();
                    criteria.setAccuracy(Criteria.ACCURACY_LOW);
                    criteria.setPowerRequirement(Criteria.POWER_LOW);
                    criteria.setAltitudeRequired(false);
                    criteria.setBearingRequired(false);
                    locationManager.requestLocationUpdates(
                            MIN_TIME, MIN_DISTANCE, criteria, new LocationListener() {
                                @Override
                                public void onLocationChanged(Location location) {
                                    locationManager.removeUpdates(this);
                                }

                                @Override
                                public void onStatusChanged(String provider, int status, Bundle extras) {
                                }

                                @Override
                                public void onProviderEnabled(String provider) {

                                }

                                @Override
                                public void onProviderDisabled(String provider) {

                                }
                            }, null);
                }
            }
        });

    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        context.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
}
