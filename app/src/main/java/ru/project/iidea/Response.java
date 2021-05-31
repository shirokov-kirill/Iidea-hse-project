package ru.project.iidea;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Response implements Serializable {
    long id;
    long to;
    long from;

    Response(long responseId, long projectId, long fromUserId){
        this.id = responseId;
        this.to = projectId;
        this.from = fromUserId;
    }

    public long getProjectId() {
        return to;
    }

    public long getFromUserId() {
        return from;
    }

    @NonNull
    @Override
    public String toString() {
        return "От: " + from + ", на проект: " + to;
    }
}
