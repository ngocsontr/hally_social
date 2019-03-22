package com.hally.influencerai.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.hally.influencerai.enums.UserType;

/**
 * Created by HallyTran on 3/22/2019.
 * transon97uet@gmail.com
 */
public class SocialUser implements Parcelable {

    private UserType userType = UserType.OTHER;

    private String id = "";
    private String accessToken = "";
    private String email = "";
    private String username = "";
    private String description = "";
    private String website = "";
    private String avatar = "";
    private String fullName = "";
    private String gender = "";
    private String about = "";
    private String coverPicUrl = "";
    private String location = "";

    public SocialUser() {
        // userType = UserType.valueOf(in.readString());
        // dest.writeString(userType.name());
    }

    protected SocialUser(Parcel in) {
        userType = UserType.valueOf(in.readString());
        id = in.readString();
        accessToken = in.readString();
        email = in.readString();
        username = in.readString();
        description = in.readString();
        website = in.readString();
        avatar = in.readString();
        fullName = in.readString();
        gender = in.readString();
        about = in.readString();
        coverPicUrl = in.readString();
        location = in.readString();
    }

    public static final Creator<SocialUser> CREATOR = new Creator<SocialUser>() {
        @Override
        public SocialUser createFromParcel(Parcel in) {
            return new SocialUser(in);
        }

        @Override
        public SocialUser[] newArray(int size) {
            return new SocialUser[size];
        }
    };

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getAbout() {
        return about;
    }

    public void setCoverPicUrl(String coverPicUrl) {
        this.coverPicUrl = coverPicUrl;
    }

    public String getCoverPicUrl() {
        return coverPicUrl;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userType.name());
        dest.writeString(id);
        dest.writeString(accessToken);
        dest.writeString(email);
        dest.writeString(username);
        dest.writeString(description);
        dest.writeString(website);
        dest.writeString(avatar);
        dest.writeString(fullName);
        dest.writeString(gender);
        dest.writeString(about);
        dest.writeString(coverPicUrl);
        dest.writeString(location);
    }

    @Override
    public String toString() {
        return "SocialUser{" +
                "userType=" + userType +
                ", id='" + id + '\'' +
                ", accessToken='" + accessToken + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", description='" + description + '\'' +
                ", website='" + website + '\'' +
                ", avatar='" + avatar + '\'' +
                ", fullName='" + fullName + '\'' +
                ", gender='" + gender + '\'' +
                ", about='" + about + '\'' +
                ", coverPicUrl='" + coverPicUrl + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
