package ru.project.iidea.network;

public class UserUpdateRequest {

    private final String description;
    private final String status;
    private final String phone;

    public UserUpdateRequest(String description, String status, String phone) {
        this.description = description;
        this.status = status;
        this.phone = phone;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public String getPhone() {
        return phone;
    }
}
