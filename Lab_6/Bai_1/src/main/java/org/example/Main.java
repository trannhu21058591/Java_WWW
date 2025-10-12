package org.example;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
    private static ApplicationContext context;
    public static void main(String[] args) {
        context = new ClassPathXmlApplicationContext("beans.xml");

        Student student1 = context.getBean("student1", Student.class);
        System.out.println(student1);

        Student student2 = context.getBean("student2", Student.class);
        System.out.println(student2);

        Person person = context.getBean("person", Person.class);
        System.out.println(person);
    }
}