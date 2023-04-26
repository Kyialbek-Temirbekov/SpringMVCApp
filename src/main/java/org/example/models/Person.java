package org.example.models;

import jakarta.validation.constraints.*;

public class Person {
    private int id;
    @NotEmpty(message = "name should not be empty")
    @Size(min=1, max=30, message = "name should be between 1 and 30 characters")
    private String name;
    @Min(value = 0, message = "age should be greater than 0")
    private int age;
    @NotEmpty(message = "email should not be empty")
    @Email(message = "email should be valid")
    private String email;
    @Pattern(regexp = "[A-Z]\\w+, [A-Z]\\w+, \\d{6}", message = "Your address should be in this format: Country, City, Postal code (6 digits)")
    private String address;

    public Person() {

    }
    public Person(int id, String name, int age, String email, String address) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.email = email;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
