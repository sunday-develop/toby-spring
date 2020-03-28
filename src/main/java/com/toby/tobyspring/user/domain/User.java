package com.toby.tobyspring.user.domain;

public class User {
    String id;
    String name;
    String password;
    Grade grade;
    int login;
    int recomend;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public Grade getGrade() {
        return grade;
    }

    public int getLogin() {
        return login;
    }

    public int getRecomend() {
        return recomend;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    public void setLogin(int login) {
        this.login = login;
    }

    public void setRecomend(int recomend) {
        this.recomend = recomend;
    }

    public User() {

    }

    public User(String id, String name, String password, Grade grade, int login, int recomend) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.grade = grade;
        this.login = login;
        this.recomend = recomend;
    }
}
