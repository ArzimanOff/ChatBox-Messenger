package com.arziman_off.chatbox;

public class User {
    private String id;
    private String name;
    private String lastName;
    private String dateOfBirth;
    private int age;
    private boolean onlineStatus;


    public User(String id, String name, String lastName, String dateOfBirth, int age, boolean onlineStatus) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.age = age;
        this.onlineStatus = onlineStatus;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public int getAge() {
        return age;
    }

    public boolean isOnlineStatus() {
        return onlineStatus;
    }
}
