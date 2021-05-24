package ru.project.iidea;

public interface ProjectHostEditInterface {
    public void onBackButtonPressed();
    public void onSaveProjectPressed(String name, String description, String state, String type, long id);
}
