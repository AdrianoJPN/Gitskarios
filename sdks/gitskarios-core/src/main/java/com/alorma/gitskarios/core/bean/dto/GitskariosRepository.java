package com.alorma.gitskarios.core.bean.dto;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by a557114 on 08/09/2015.
 */
public class GitskariosRepository implements Parcelable {
    public int id;
    public GitskariosUser owner;
    public String name;
    public String full_name;
    public String description;
    public String html_url;
    public String external_url;
    public Date created_at;
    public String default_branch;
    public boolean isPrivate;
    public int stargazers_count = -1;
    public int subscribers_count = -1;
    public int forks_count;
    public GitskariosPermissions gitskariosPermissions;
    public boolean has_downloads;
    public boolean has_wiki;
    public boolean has_issues;
    public boolean has_merge_request;
    public String homepage;
    public int owner_id;
    public GitskariosRepository parent;

    public GitskariosRepository() {

    }

    protected GitskariosRepository(Parcel in) {
        id = in.readInt();
        owner = (GitskariosUser) in.readValue(GitskariosUser.class.getClassLoader());
        name = in.readString();
        full_name = in.readString();
        description = in.readString();
        html_url = in.readString();
        external_url = in.readString();
        long tmpCreated_at = in.readLong();
        created_at = tmpCreated_at != -1 ? new Date(tmpCreated_at) : null;
        default_branch = in.readString();
        isPrivate = in.readByte() != 0x00;
        stargazers_count = in.readInt();
        subscribers_count = in.readInt();
        forks_count = in.readInt();
        gitskariosPermissions = (GitskariosPermissions) in.readValue(GitskariosPermissions.class.getClassLoader());
        has_downloads = in.readByte() != 0x00;
        has_wiki = in.readByte() != 0x00;
        has_issues = in.readByte() != 0x00;
        has_merge_request = in.readByte() != 0x00;
        homepage = in.readString();
        owner_id = in.readInt();
        parent = (GitskariosRepository) in.readValue(GitskariosRepository.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeValue(owner);
        dest.writeString(name);
        dest.writeString(full_name);
        dest.writeString(description);
        dest.writeString(html_url);
        dest.writeString(external_url);
        dest.writeLong(created_at != null ? created_at.getTime() : -1L);
        dest.writeString(default_branch);
        dest.writeByte((byte) (isPrivate ? 0x01 : 0x00));
        dest.writeInt(stargazers_count);
        dest.writeInt(subscribers_count);
        dest.writeInt(forks_count);
        dest.writeValue(gitskariosPermissions);
        dest.writeByte((byte) (has_downloads ? 0x01 : 0x00));
        dest.writeByte((byte) (has_wiki ? 0x01 : 0x00));
        dest.writeByte((byte) (has_issues ? 0x01 : 0x00));
        dest.writeByte((byte) (has_merge_request ? 0x01 : 0x00));
        dest.writeString(homepage);
        dest.writeInt(owner_id);
        dest.writeValue(parent);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<GitskariosRepository> CREATOR = new Parcelable.Creator<GitskariosRepository>() {
        @Override
        public GitskariosRepository createFromParcel(Parcel in) {
            return new GitskariosRepository(in);
        }

        @Override
        public GitskariosRepository[] newArray(int size) {
            return new GitskariosRepository[size];
        }
    };
}