package com.hally.influencerai.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by HallyTran on 3/22/2019.
 * transon97uet@gmail.com
 */
public class User implements Parcelable {
    private static final String INFLUENCER = "2";

    @SerializedName("id")
    @Expose
    private int id;
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
    private boolean requireUpdateInfo;
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
    private int socialType;
    @SerializedName("user_socials")
    @Expose
    private List<UserSocial> userSocials = null;
    @SerializedName("website")
    @Expose
    private String website;
    @SerializedName("about")
    @Expose
    private String about;
    @SerializedName("coverPicUrl")
    @Expose
    private String coverPicUrl;
    @SerializedName("professions")
    @Expose
    private List<String> professions;

    public User() {
    }

    protected User(Parcel in) {
        id = in.readInt();
        username = in.readString();
        fullName = in.readString();
        dateOfBirth = in.readString();
        gender = in.readString();
        country = in.readString();
        location = in.readString();
        email = in.readString();
        avatar = in.readString();
        description = in.readString();
        createdAt = in.readString();
        updatedAt = in.readString();
        deletedAt = in.readString();
        lastLogin = in.readString();
        ip = in.readString();
        isActive = in.readString();
        userType = in.readString();
        requireUpdateInfo = in.readByte() != 0;
        socialId = in.readString();
        snsAccessToken = in.readString();
        link = in.readString();
        socialType = in.readInt();
        website = in.readString();
        about = in.readString();
        coverPicUrl = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
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

    public boolean isRequireUpdateInfo() {
        return requireUpdateInfo;
    }

    public void setRequireUpdateInfo(boolean requireUpdateInfo) {
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

    public void setSocialType(int socialType) {
        this.socialType = socialType;
    }

    public int getSocialType() {
        return socialType;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getCoverPicUrl() {
        return coverPicUrl;
    }

    public void setCoverPicUrl(String coverPicUrl) {
        this.coverPicUrl = coverPicUrl;
    }

    public void setProfessions(List<String> professions) {
        this.professions = professions;
    }

    public List<String> getProfession() {
        return professions;
    }

    public boolean isInfluencer() {
        return TextUtils.equals(getUserType(), INFLUENCER);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(id);
        dest.writeString(username);
        dest.writeString(fullName);
        dest.writeString(dateOfBirth);
        dest.writeString(gender);
        dest.writeString(country);
        dest.writeString(location);
        dest.writeString(email);
        dest.writeString(avatar);
        dest.writeString(description);
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
        dest.writeString(deletedAt);
        dest.writeString(lastLogin);
        dest.writeString(ip);
        dest.writeString(isActive);
        dest.writeString(userType);
        dest.writeByte((byte) (requireUpdateInfo ? 1 : 0));
        dest.writeString(socialId);
        dest.writeString(snsAccessToken);
        dest.writeString(link);
        dest.writeInt(socialType);
        dest.writeString(website);
        dest.writeString(about);
        dest.writeString(coverPicUrl);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", fullName='" + fullName + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", gender='" + gender + '\'' +
                ", country='" + country + '\'' +
                ", location='" + location + '\'' +
                ", email='" + email + '\'' +
                ", avatar='" + avatar + '\'' +
                ", description='" + description + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", deletedAt='" + deletedAt + '\'' +
                ", lastLogin='" + lastLogin + '\'' +
                ", ip='" + ip + '\'' +
                ", isActive='" + isActive + '\'' +
                ", userType='" + userType + '\'' +
                ", requireUpdateInfo=" + requireUpdateInfo +
                ", socialId='" + socialId + '\'' +
                ", snsAccessToken='" + snsAccessToken + '\'' +
                ", link='" + link + '\'' +
                ", socialType=" + socialType +
                ", userSocials=" + userSocials +
                ", website='" + website + '\'' +
                ", about='" + about + '\'' +
                ", coverPicUrl='" + coverPicUrl + '\'' +
                ", professions=" + professions +
                '}';
    }
}
