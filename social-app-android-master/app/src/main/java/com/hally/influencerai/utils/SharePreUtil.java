/*
 * Copyright 2017
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

package com.hally.influencerai.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

public class SharePreUtil {

    private static final String TAG = SharePreUtil.class.getSimpleName();

    private static final String SHARED_PREFERENCES_NAME = "local_share_preferences";
    private static final String PREF_PARAM_USER_LOGIN_TOKEN = "pref_param_user_login_token";
    private static final String PREF_PARAM_IS_PROFILE_CREATED = "isProfileCreated";
    private static final String PREF_PARAM_IS_POSTS_WAS_LOADED_AT_LEAST_ONCE = "isPostsWasLoadedAtLeastOnce";

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public static String getLoginToken(Context context) {
        String token = getSharedPreferences(context).getString(
                PREF_PARAM_USER_LOGIN_TOKEN, "");
        return TextUtils.isEmpty(token) ? "" : CryptUtil.decrypt(token);
    }

    public static void saveLoginToken(Context context, String token) {
        getSharedPreferences(context).edit().putString(PREF_PARAM_USER_LOGIN_TOKEN,
                CryptUtil.encrypt(token)).apply();
    }

    public static void clearLoginToken(Context context) {
        getSharedPreferences(context).edit().putString(PREF_PARAM_USER_LOGIN_TOKEN, "").apply();
    }

    public static Boolean isProfileCreated(Context context) {
        return getSharedPreferences(context).getBoolean(PREF_PARAM_IS_PROFILE_CREATED,
                false);
    }

    public static Boolean isPostWasLoadedAtLeastOnce(Context context) {
        return getSharedPreferences(context).getBoolean(PREF_PARAM_IS_POSTS_WAS_LOADED_AT_LEAST_ONCE,
                false);
    }

    public static void setProfileCreated(Context context, Boolean isProfileCreated) {
        getSharedPreferences(context).edit().putBoolean(PREF_PARAM_IS_PROFILE_CREATED,
                isProfileCreated).apply();
    }

    public static void setPostWasLoadedAtLeastOnce(Context context, Boolean isPostWasLoadedAtLeastOnce) {
        getSharedPreferences(context).edit().putBoolean(PREF_PARAM_IS_POSTS_WAS_LOADED_AT_LEAST_ONCE,
                isPostWasLoadedAtLeastOnce).apply();
    }

    public static void clearPreferences(Context context) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.clear();
        editor.apply();
    }
}
