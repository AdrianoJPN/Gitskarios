package com.alorma.gitskarios.core.bean.dto;

import android.os.Parcel;
import android.os.Parcelable;

public class GitskariosPermissions implements Parcelable {
	public boolean admin;
	public boolean push;
	public boolean pull;

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("Permissions{");
		sb.append("admin=").append(admin);
		sb.append(", push=").append(push);
		sb.append(", pull=").append(pull);
		sb.append('}');
		return sb.toString();
	}

	public GitskariosPermissions() {
		this.admin = false;
		this.push = false;
		this.pull = false;
	}

	protected GitskariosPermissions(Parcel in) {
		admin = in.readByte() != 0x00;
		push = in.readByte() != 0x00;
		pull = in.readByte() != 0x00;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeByte((byte) (admin ? 0x01 : 0x00));
		dest.writeByte((byte) (push ? 0x01 : 0x00));
		dest.writeByte((byte) (pull ? 0x01 : 0x00));
	}

	@SuppressWarnings("unused")
	public static final Parcelable.Creator<GitskariosPermissions> CREATOR = new Parcelable.Creator<GitskariosPermissions>() {
		@Override
		public GitskariosPermissions createFromParcel(Parcel in) {
			return new GitskariosPermissions(in);
		}

		@Override
		public GitskariosPermissions[] newArray(int size) {
			return new GitskariosPermissions[size];
		}
	};
}
