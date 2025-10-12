package org.example;

public class ICEngine {
    private float cylinderCapacity;
    private String type;

    public ICEngine() {
    }

    public ICEngine(float cylinderCapacity, String type) {
        this.cylinderCapacity = cylinderCapacity;
        this.type = type;
    }

    public float getCylinderCapacity() {
        return cylinderCapacity;
    }

    public void setCylinderCapacity(float cylinderCapacity) {
        this.cylinderCapacity = cylinderCapacity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public void start(){
        System.out.println("Engine is started");
    }
}
