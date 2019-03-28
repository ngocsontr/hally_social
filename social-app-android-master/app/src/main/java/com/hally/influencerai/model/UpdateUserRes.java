package com.hally.influencerai.model;

import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateUserRes {
    private static final String IS_SUCCESS = "Success";

    @SerializedName("update_user_info")
    @Expose
    private String updateUserInfo;

    public String getUpdateUserInfo() {
        return updateUserInfo;
    }

    public void setUpdateUserInfo(String updateUserInfo) {
        this.updateUserInfo = updateUserInfo;
    }

    public boolean isSuccess() {
        return TextUtils.equals(getUpdateUserInfo(), IS_SUCCESS);
    }
}