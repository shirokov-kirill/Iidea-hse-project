package ru.project.iidea;

import androidx.annotation.NonNull;
import java.io.Serializable;
import static java.lang.Math.min;

public class Response implements Serializable {
    long projectHostId;
    long fromUserId;
    String projectName;

    Response(long projectHostId, long fromUserId, String projectName){
        this.projectHostId = projectHostId;
        this.fromUserId = fromUserId;
        this.projectName = projectName;
    }

    public long getProjectHostId() {
        return projectHostId;
    }

    public long getFromUserId() {
        return fromUserId;
    }

    public String getProjectName() {
        return projectName;
    }

    @NonNull
    @Override
    public String toString() {
        return "Отклик на проект" + projectName.substring(0, min(projectName.length(), 20));
    }
}
