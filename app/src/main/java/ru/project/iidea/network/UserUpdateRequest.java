package ru.project.iidea.network;

public class UserUpdateRequest {

    private final String description;
    private final String status;
    private final String phone;
    private final String vk;
    private final String inst;

    public UserUpdateRequest(String description, String status, String phone, String vk, String inst) {
        this.description = description;
        this.status = status;
        this.phone = phone;
        this.vk = vk;
        this.inst = inst;
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

    public String getVk() {
        return vk;
    }

    public String getInst() {
        return inst;
    }
}
