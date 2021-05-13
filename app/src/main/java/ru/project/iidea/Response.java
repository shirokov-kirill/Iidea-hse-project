package ru.project.iidea;

import androidx.annotation.NonNull;
import java.io.Serializable;
import static java.lang.Math.min;

public class Response implements Serializable {
    long responseId;
    int projectId;
    int fromUserId;

    Response(long responseId, int projectId, int fromUserId){
        this.responseId = responseId;
        this.projectId = projectId;
        this.fromUserId = fromUserId;
    }

    public int getProjectId() {
        return projectId;
    }

    public int getFromUserId() {
        return fromUserId;
    }

}
