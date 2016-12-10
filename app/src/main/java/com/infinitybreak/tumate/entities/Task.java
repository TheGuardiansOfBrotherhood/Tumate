package com.infinitybreak.tumate.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.infinitybreak.tumate.enums.TaskStatus;

public class Task implements Parcelable {

    private Long id;

    private String name;

    private Integer estimated;

    private Integer realized;

    private TaskStatus status;

    public Task() {
    }

    private Task(Parcel in) {
        setId(in.readLong());
        setName(in.readString());
        setEstimated(in.readInt());
        setRealized(in.readInt());
        setStatus(in.<TaskStatus>readParcelable(TaskStatus.class.getClassLoader()));
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getEstimated() {
        return estimated;
    }

    public void setEstimated(Integer estimated) {
        this.estimated = estimated;
    }

    public Integer getRealized() {
        return realized;
    }

    public void setRealized(Integer realized) {
        this.realized = realized;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", estimated=" + getEstimated() +
                ", realized=" + getRealized() +
                ", status=" + getStatus() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id.equals(task.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(getId());
        dest.writeString(getName());
        dest.writeInt(getEstimated());
        dest.writeInt(getRealized());
        dest.writeParcelable(getStatus(), flags);
    }
}
