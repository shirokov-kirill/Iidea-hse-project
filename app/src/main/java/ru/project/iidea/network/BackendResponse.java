package ru.project.iidea.network;

public class BackendResponse {

    private final long id;
    private final long from;
    private final long to;

    public BackendResponse(long id, long fromUser, long toProject) {
        this.id = id;
        this.from = fromUser;
        this.to = toProject;
    }

    public long getId() {
        return id;
    }

    public long getFromUser() {
        return from;
    }

    public long getToProject() {
        return to;
    }
}
