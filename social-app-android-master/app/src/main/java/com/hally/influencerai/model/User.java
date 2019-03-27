
package com.hally.influencerai.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class User {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("full_name")
    @Expose
    private String fullName;
    @SerializedName("date_of_birth")
    @Expose
    private String dateOfBirth;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("avatar")
    @Expose
    private String avatar;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("deleted_at")
    @Expose
    private String deletedAt;
    @SerializedName("last_login")
    @Expose
    private String lastLogin;
    @SerializedName("ip")
    @Expose
    private String ip;
    @SerializedName("is_active")
    @Expose
    private String isActive;
    @SerializedName("user_type")
    @Expose
    private String userType;
    @SerializedName("require_update_info")
    @Expose
    private String requireUpdateInfo;
    @SerializedName("user_socials")
    @Expose
    private List<UserSocial> userSocials = null;
    @SerializedName("sns_account_id")
    @Expose
    private String socialId;
    @SerializedName("sns_access_token")
    @Expose
    private String snsAccessToken;
    @SerializedName("link")
    @Expose
    private String link;
    @SerializedName("social_type")
    @Expose
    private String socialType;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(String deletedAt) {
        this.deletedAt = deletedAt;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getRequireUpdateInfo() {
        return requireUpdateInfo;
    }

    public void setRequireUpdateInfo(String requireUpdateInfo) {
        this.requireUpdateInfo = requireUpdateInfo;
    }

    public List<UserSocial> getUserSocials() {
        return userSocials;
    }

    public void setUserSocials(List<UserSocial> userSocials) {
        this.userSocials = userSocials;
    }

    public void setSocialId(String socialId) {
        this.socialId = socialId;
    }

    public String getSocialId() {
        return socialId;
    }

    public void setSnsAccessToken(String snsAccessToken) {
        this.snsAccessToken = snsAccessToken;
    }

    public String getSnsAccessToken() {
        return snsAccessToken;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLink() {
        return link;
    }

    public void setSocialType(String socialType) {
        this.socialType = socialType;
    }

    public String getSocialType() {
        return socialType;
    }
}
