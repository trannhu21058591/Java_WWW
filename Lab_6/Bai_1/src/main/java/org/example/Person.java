package org.example;

import java.util.List;

public class Person {
    private String name;
    private int age;
    private List<Address> addresses;
    private List<String> emails;

    public Person() {}

    public Person(String name, int age, List<Address> addresses, List<String> emails) {
        this.name = name;
        this.age = age;
        this.addresses = addresses;
        this.emails = emails;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public List<Address> getAddresses() { return addresses; }
    public void setAddresses(List<Address> addresses) { this.addresses = addresses; }

    public List<String> getEmails() { return emails; }
    public void setEmails(List<String> emails) { this.emails = emails; }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", addresses=" + addresses +
                ", emails=" + emails +
                '}';
    }
}