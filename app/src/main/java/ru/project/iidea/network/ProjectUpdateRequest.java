package ru.project.iidea.network;

class ProjectUpdateRequest {

    private String name;
    private String type;
    private String description;
    private String status;

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
