package com.toby.tobyspring.user.domain;

public class User {
    String id;
    String name;
    String password;
    Grade grade;
    int login;
    int recommend;
    String email;

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

    public int getRecommend() {
        return recommend;
    }

    public String getEmail() {
        return email;
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

    public void setRecommend(int recommend) {
        this.recommend = recommend;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User() {

    }

    public User(String id, String name, String password, Grade grade, int login, int recommend, String email) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.grade = grade;
        this.login = login;
        this.recommend = recommend;
        this.email = email;
    }

    public void upgrade() {
        Grade nextGrade = this.grade.nextGrade();
        if (nextGrade == null) {
            throw new IllegalArgumentException(this.grade + "은 업그레이드가 불가능합니다.");
        } else {
            this.grade = nextGrade;
        }
    }
}
