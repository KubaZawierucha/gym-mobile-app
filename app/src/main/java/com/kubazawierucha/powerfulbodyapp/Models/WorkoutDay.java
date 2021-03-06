package com.kubazawierucha.powerfulbodyapp.Models;

import java.util.List;

public class WorkoutDay {

    private int id;
    private String date;
    private String muscleGroupName;
    private List<String> exercisesNames;

    public WorkoutDay(String date) {
        this.date = date;
    }

    public WorkoutDay(String date, String muscleGroupName, List<String> exercisesNames) {
        this.date = date;
        this.muscleGroupName = muscleGroupName;
        this.exercisesNames = exercisesNames;
    }

    public WorkoutDay(int id, String date, String muscleGroupName, List<String> exercisesNames) {
        this(date, muscleGroupName, exercisesNames);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public List<String> getExercisesNames() {
        return exercisesNames;
    }
}
