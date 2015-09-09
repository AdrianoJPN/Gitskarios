package com.alorma.gitskarios.core.bean.dto;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by a557114 on 08/09/2015.
 */
public class GitskariosUser implements Parcelable {
    public Integer id;
    public String name;
    public String login;
    public String avatar_url;
    public String company;
    public String location;
    public String email;
    public Date created_at;
    public int public_repos;
    public int public_gists;
    public GitskariosUserType type;

    @Override
    public String toString() {
        return "GitskariosUser{" +
                "login='" + login + '\'' +
                ", avatar_url='" + avatar_url + '\'' +
                ", company='" + company + '\'' +
                ", location='" + location + '\'' +
                ", email='" + email + '\'' +
                ", created_at=" + created_at +
                ", public_repos=" + public_repos +
                ", public_gists=" + public_gists +
                ", type=" + type +
                '}';
    }

    public GitskariosUser() {

    }

    protected GitskariosUser(Parcel in) {
        login = in.readString();
        avatar_url = in.readString();
        company = in.readString();
        location = in.readString();
        email = in.readString();
        long tmpCreated_at = in.readLong();
        created_at = tmpCreated_at != -1 ? new Date(tmpCreated_at) : null;
        public_repos = in.readInt();
        public_gists = in.readInt();
        String type = in.readString();
        if (type != null) {
            if (GitskariosUserType.User.toString().equals(type)) {
                this.type = GitskariosUserType.User;
            } else if (GitskariosUserType.Organization.toString().equals(type)) {
                this.type = GitskariosUserType.Organization;
            } else {
                this.type = GitskariosUserType.User;
            }
        } else {
            this.type = GitskariosUserType.User;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(login);
        dest.writeString(avatar_url);
        dest.writeString(company);
        dest.writeString(location);
        dest.writeString(email);
        dest.writeLong(created_at != null ? created_at.getTime() : -1L);
        dest.writeInt(public_repos);
        dest.writeInt(public_gists);
        dest.writeValue(type != null ? type.toString() : GitskariosUserType.User.toString());
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<GitskariosUser> CREATOR = new Parcelable.Creator<GitskariosUser>() {
        @Override
        public GitskariosUser createFromParcel(Parcel in) {
            return new GitskariosUser(in);
        }

        @Override
        public GitskariosUser[] newArray(int size) {
            return new GitskariosUser[size];
        }
    };
}
