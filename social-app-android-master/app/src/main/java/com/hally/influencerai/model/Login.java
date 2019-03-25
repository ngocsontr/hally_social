
package com.hally.influencerai.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Login {

    @SerializedName("allow_access")
    @Expose
    private String allowAccess;
    @SerializedName("user")
    @Expose
    private User user;

    public String getAllowAccess() {
        return allowAccess;
    }

    public void setAllowAccess(String allowAccess) {
        this.allowAccess = allowAccess;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
