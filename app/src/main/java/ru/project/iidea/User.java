package ru.project.iidea;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {
    private long id;
    private String surname;
    private String name;
    private String patronymic;
    private String dateOfBirth;
    private String email;
    private String phoneNumber;
    private String description;
    private UserState state;
    private List<ProjectType> subscriptions;
    private List<Project> projects;

    User(long id, String surname, String name, String patronymic, String dateOfBirth, String email, String phoneNumber, String description, UserState state, List<ProjectType> subscriptions, List<Project> projects){
        this.id = id;
        this.surname = surname;
        this.name = name;
        this.patronymic = patronymic;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.description = description;
        this.state = state;
        this.subscriptions = subscriptions;
        this.projects = projects;
    }

    public List<ProjectType> getSubscriptions() {
        return subscriptions;
    }

    public boolean addSubscription(ProjectType type){
        if(!subscriptions.contains(type)){
            subscriptions.add(type);
            return true;
        }
        return false;
    }

    public boolean removeSubscription(ProjectType type){
        if(subscriptions.contains(type)){
            return subscriptions.remove(type);
        }
        return false;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public boolean addProject(Project project){
        if(!projects.contains(project)){
            return projects.add(project);
        }
        return false;
    }

    public boolean removeProject(Project project){
        if(projects.contains(project)){
            return projects.remove(project);
        }
        return false;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setState(UserState state) {
        this.state = state;
    }

    public String getSurname() {
        return surname;
    }

    public String getName() {
        return name;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getDescription() {
        return description;
    }

    public UserState getState() {
        return state;
    }

    public long getId() {
        return id;
    }
}