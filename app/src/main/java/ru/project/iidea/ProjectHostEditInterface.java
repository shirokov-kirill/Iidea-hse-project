package ru.project.iidea;

public interface ProjectHostEditInterface {
    void onBackButtonPressed();
    void onSaveProjectPressed(String name, String description, String state, String type, long id);
}
