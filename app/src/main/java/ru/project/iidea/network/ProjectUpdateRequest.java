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
}
