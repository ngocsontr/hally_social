
package com.hally.influencerai.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserSocial implements Parcelable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("link")
    @Expose
    private String link;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("deleted_at")
    @Expose
    private String deletedAt;
    @SerializedName("social_type")
    @Expose
    private String socialType;
    @SerializedName("access_token")
    @Expose
    private String accessToken;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("extra_data")
    @Expose
    private String extraData;
    @SerializedName("flatform_id")
    @Expose
    private String flatformId;
    private String socialAvatar;

    protected UserSocial(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        link = in.readString();
        email = in.readString();
        createdAt = in.readString();
        updatedAt = in.readString();
        deletedAt = in.readString();
        socialType = in.readString();
        accessToken = in.readString();
        userId = in.readString();
        extraData = in.readString();
        flatformId = in.readString();
        socialAvatar = in.readString();
    }

    public static final Creator<UserSocial> CREATOR = new Creator<UserSocial>() {
        @Override
        public UserSocial createFromParcel(Parcel in) {
            return new UserSocial(in);
        }

        @Override
        public UserSocial[] newArray(int size) {
            return new UserSocial[size];
        }
    };

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getSocialType() {
        return socialType;
    }

    public void setSocialType(String socialType) {
        this.socialType = socialType;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getExtraData() {
        return extraData;
    }

    public void setExtraData(String extraData) {
        this.extraData = extraData;
    }

    public String getFlatformId() {
        return flatformId;
    }

    public void setFlatformId(String flatformId) {
        this.flatformId = flatformId;
    }

    public String getSocialAvatar() {
        return socialAvatar;
    }

    public void setSocialAvatar(String socialAvatar) {
        this.socialAvatar = socialAvatar;
    }

    @Override
    public String toString() {
        return "UserSocial{" +
                "id=" + id +
                ", link='" + link + '\'' +
                ", email='" + email + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", deletedAt='" + deletedAt + '\'' +
                ", socialType='" + socialType + '\'' +
                ", accessToken='" + accessToken + '\'' +
                ", userId='" + userId + '\'' +
                ", extraData='" + extraData + '\'' +
                ", flatformId='" + flatformId + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        dest.writeString(link);
        dest.writeString(email);
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
        dest.writeString(deletedAt);
        dest.writeString(socialType);
        dest.writeString(accessToken);
        dest.writeString(userId);
        dest.writeString(extraData);
        dest.writeString(flatformId);
        dest.writeString(socialAvatar);
    }
}
