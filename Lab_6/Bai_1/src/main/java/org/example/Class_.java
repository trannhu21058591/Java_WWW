package org.example;

public class Class_ {
    private String ClassID;
    private String ClassName;

    public Class_() {
    }

    public Class_(String classID, String className) {
        ClassID = classID;
        ClassName = className;
    }

    public String getClassID() {
        return ClassID;
    }

    public void setClassID(String classID) {
        ClassID = classID;
    }

    public String getClassName() {
        return ClassName;
    }

    public void setClassName(String className) {
        ClassName = className;
    }

    @Override
    public String toString() {
        return "Class_{" +
                "ClassID=" + ClassID +
                ", ClassName='" + ClassName + '\'' +
                '}';
    }
}
