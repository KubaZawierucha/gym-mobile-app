package com.kubazawierucha.powerfulbodyapp.models;

public class Muscle {

    private int id;
    private String formalName;
    private String simpleName;
    private String description;
    private String muscleGroupName;

    public Muscle(String formalName, String simpleName, String description, String muscleGroupName) {
        this.formalName = formalName;
        this.simpleName = simpleName;
        this.description = description;
        this.muscleGroupName = muscleGroupName;
    }

    public Muscle(int id, String formalName, String simpleName, String description, String muscleGroupName) {
        this(formalName, simpleName, description, muscleGroupName);
        this.id = id;
    }

    public Muscle(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFormalName() {
        return formalName;
    }

    public void setFormalName(String formalName) {
        this.formalName = formalName;
    }

    public String getSimpleName() {
        return simpleName;
    }

    public void setSimpleName(String simpleName) {
        this.simpleName = simpleName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMuscleGroupName() {
        return muscleGroupName;
    }

    public void setMuscleGroupName(String muscleGroupName) {
        this.muscleGroupName = muscleGroupName;
    }
}
