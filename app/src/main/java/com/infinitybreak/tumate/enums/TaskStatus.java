package com.infinitybreak.tumate.enums;

import android.os.Parcel;
import android.os.Parcelable;

public enum TaskStatus implements Parcelable {
    TODO, DOING, DONE;

    public static final Creator<TaskStatus> CREATOR = new Creator<TaskStatus>() {
        @Override
        public TaskStatus createFromParcel(Parcel in) {
            return TaskStatus.values()[in.readInt()];
        }

        @Override
        public TaskStatus[] newArray(int size) {
            return new TaskStatus[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ordinal());
    }
}