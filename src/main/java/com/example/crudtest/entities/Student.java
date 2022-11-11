package com.example.crudtest.entities;

import lombok.AllArgsConstructor;

import javax.persistence.*;

@Entity
@Table
@AllArgsConstructor
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    private String name;
    private String surname;
    private boolean isWorking;

    public Student(){}

    public Student(String name, String surname, boolean isWorking) {
        this.setName(name);
        this.setSurname(surname);
        this.setWorking(isWorking);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public boolean isWorking() {
        return isWorking;
    }

    public void setWorking(boolean working) {
        isWorking = working;
    }
}
