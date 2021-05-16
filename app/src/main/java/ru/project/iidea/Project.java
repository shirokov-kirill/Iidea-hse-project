package ru.project.iidea;

import java.io.Serializable;

public class Project implements Serializable {
    private long id;
    private ProjectType type;
    private String name;
    private String description;
    private long hostId;
    private ProjectState status;

    Project(long id, ProjectType type, String name, String description, long hostId, ProjectState status){
        this.id = id;
        this.type = type;
        this.name = name;
        this.hostId = hostId;
        this.status = status;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public ProjectType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public long getHostId() {
        return hostId;
    }

    public ProjectState getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public void setType(ProjectType type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(ProjectState status) {
        this.status = status;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
