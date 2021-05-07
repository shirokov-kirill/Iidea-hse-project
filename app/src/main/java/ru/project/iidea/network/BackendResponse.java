package ru.project.iidea.network;

public class BackendResponse {

    private long id;
    private long fromUser;
    private long toProject;

    public BackendResponse(long id, long fromUser, long toProject) {
        this.id = id;
        this.fromUser = fromUser;
        this.toProject = toProject;
    }

    public long getId() {
        return id;
    }

    public long getFromUser() {
        return fromUser;
    }

    public long getToProject() {
        return toProject;
    }
}
