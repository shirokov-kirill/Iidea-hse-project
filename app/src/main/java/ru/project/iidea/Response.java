package ru.project.iidea;

import androidx.annotation.NonNull;
import java.io.Serializable;
import static java.lang.Math.min;

public class Response implements Serializable {
    int projectHostId;
    int fromUserId;
    String projectName;

    Response(int projectHostId, int fromUserId, String projectName){
        this.projectHostId = projectHostId;
        this.fromUserId = fromUserId;
        this.projectName = projectName;
    }

    public int getProjectHostId() {
        return projectHostId;
    }

    public int getFromUserId() {
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
