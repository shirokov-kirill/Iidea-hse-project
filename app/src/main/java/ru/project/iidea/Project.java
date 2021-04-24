package ru.project.iidea;

import java.io.Serializable;

public class Project implements Serializable {
    private ProjectType type;
    private String name;
    private String description;
    private long host_id;
    private ProjectState status;

    Project(ProjectType type, String name, String description, long host_id, ProjectState status){
        this.type = type;
        this.name = name;
        this.host_id = host_id;
        this.status = status;
        this.description = description;
    }

    public ProjectType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public long getHost_id() {
        return host_id;
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
