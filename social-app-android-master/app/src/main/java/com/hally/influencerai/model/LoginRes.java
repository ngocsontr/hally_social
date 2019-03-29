
package com.hally.influencerai.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginRes {

    @SerializedName("allow_access_user")
    @Expose
    private boolean allowAccess;
    @SerializedName("user")
    @Expose
    private User user;

    public boolean isAllowAccess() {
        return allowAccess;
    }

    public void setAllowAccess(boolean allowAccess) {
        this.allowAccess = allowAccess;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
