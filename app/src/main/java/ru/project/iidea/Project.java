package ru.project.iidea;

public class Project {
    private ProjectType type;
    private String name;
    private long host_id;
    private ProjectState status;

    Project(ProjectType type, String name, long host_id, ProjectState status){
        this.type = type;
        this.name = name;
        this.host_id = host_id;
        this.status = status;
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

    public void setType(ProjectType type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(ProjectState status) {
        this.status = status;
    }
}
