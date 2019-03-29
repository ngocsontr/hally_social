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
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;

import com.hally.influencerai.main.editProfile.EditProfilePresenter;
import com.hally.influencerai.main.editProfile.EditProfileView;
import com.hally.influencerai.model.User;
import com.hally.influencerai.utils.SharePreUtil;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by HallyTran on 3/22/2019.
 * transon97uet@gmail.com
 */
class CreateProfilePresenter extends EditProfilePresenter<CreateProfileView> {

    CreateProfilePresenter(Context context) {
        super(context);
    }

    public void buildProfile(User profile) {
        ifViewAttached(view -> view.buildProfile(profile));
    }

    public void initProfessionalList() {
        ifViewAttached(EditProfileView::createProfessionalList);
    }


    @Override
    protected void onProfileUpdatedSuccessfully() {
        super.onProfileUpdatedSuccessfully();
        SharePreUtil.setProfileCreated(context, true);
//        profileManager.addRegistrationToken(FirebaseInstanceId.getInstance().getToken(), profile.getId());
    }

    public void initLocation() {
        ifViewAttached(view -> {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    context, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            final LocationManager locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
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
                    String cityName = address.getSubLocality();
                    String stateName = address.getLocality();
                    String countryName = address.getCountryName();
                    String builder = (TextUtils.isEmpty(countryName) ? "" : countryName + ", ") +
                            (TextUtils.isEmpty(stateName) ? "" : stateName + ", ") +
                            (TextUtils.isEmpty(cityName) ? "" : cityName);
                    view.setLocation(builder);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
//                    Log.e(TAG, "getLastKnownLocation null ");
//                    Criteria criteria = new Criteria();
//                    criteria.setAccuracy(Criteria.ACCURACY_LOW);
//                    criteria.setPowerRequirement(Criteria.POWER_LOW);
//                    criteria.setAltitudeRequired(false);
//                    criteria.setBearingRequired(false);
//                    locationManager.requestLocationUpdates(
//                            100, 10, criteria, new LocationListener() {
//                                @Override
//                                public void onLocationChanged(Location location) {
//                                    locationManager.removeUpdates(this);
//                                }
//
//                                @Override
//                                public void onStatusChanged(String provider, int status, Bundle extras) {
//                                }
//
//                                @Override
//                                public void onProviderEnabled(String provider) {
//
//                                }
//
//                                @Override
//                                public void onProviderDisabled(String provider) {
//
//                                }
//                            }, null);
            }
        });
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> context.startActivity(
                        new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)))
                .setNegativeButton("No", (dialog, id) -> dialog.cancel());
        final AlertDialog alert = builder.create();
        alert.show();
    }
}
