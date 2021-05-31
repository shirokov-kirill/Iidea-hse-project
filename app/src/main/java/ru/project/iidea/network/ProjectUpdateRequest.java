package ru.project.iidea.network;

class ProjectUpdateRequest {

    private final String name;
    private final String type;
    private final String description;
    private final String status;

    public ProjectUpdateRequest(String name, String type, String description, String status) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public String getType() {
        return type;
    }
}
