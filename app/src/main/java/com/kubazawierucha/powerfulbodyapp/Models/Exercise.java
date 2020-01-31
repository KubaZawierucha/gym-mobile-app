package com.kubazawierucha.powerfulbodyapp.Models;

public class Exercise {

    private int id;
    private String name;
    private String description;
    private String muscleName;
    private int lastWeight;
    private int lastRepetitionsNumber;
    private int lastBreakTime;
    private int lastSeriesNumber;
    private String muscleGroupName;
    private String fstPicURL;
    private String secPicURL;
    private String thdPicURL;

    public Exercise(String name, String description, String muscleName, int lastWeight, int lastRepetitionsNumber,
                    int lastBreakTime, int lastSeriesNumber, String muscleGroupName, String fstPicURL,
                    String secPicURL, String thdPicURL) {
        this.name = name;
        this.description = description;
        this.muscleName = muscleName;
        this.lastWeight = lastWeight;
        this.lastRepetitionsNumber = lastRepetitionsNumber;
        this.lastBreakTime = lastBreakTime;
        this.lastSeriesNumber = lastSeriesNumber;
        this.muscleGroupName = muscleGroupName;
        this.fstPicURL = fstPicURL;
        this.secPicURL = secPicURL;
        this.thdPicURL = thdPicURL;
    }

    public Exercise(int id, String name, String description, String muscleName, int lastWeight, int lastRepetitionsNumber,
                    int lastBreakTime, int lastSeriesNumber, String muscleGroupName, String fstPicURL,
                    String secPicURL, String thdPicURL) {
        this(name, description, muscleName, lastWeight, lastRepetitionsNumber, lastBreakTime, lastSeriesNumber, muscleGroupName,
                fstPicURL, secPicURL, thdPicURL);
        this.id = id;
    }

    public Exercise(int id) {
        this.id = id;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMuscleName() {
        return muscleName;
    }

    public void setMuscleName(String muscleName) {
        this.muscleName = muscleName;
    }

    public int getLastWeight() {
        return lastWeight;
    }

    public void setLastWeight(int lastWeight) {
        this.lastWeight = lastWeight;
    }

    public int getLastRepetitionsNumber() {
        return lastRepetitionsNumber;
    }

    public void setLastRepetitionsNumber(int lastRepetitionsNumber) {
        this.lastRepetitionsNumber = lastRepetitionsNumber;
    }

    public int getLastBreakTime() {
        return lastBreakTime;
    }

    public void setLastBreakTime(int lastBreakTime) {
        this.lastBreakTime = lastBreakTime;
    }

    public int getLastSeriesNumber() {
        return lastSeriesNumber;
    }

    public void setLastSeriesNumber(int lastSeriesNumber) {
        this.lastSeriesNumber = lastSeriesNumber;
    }

    public String getMuscleGroupName() {
        return muscleGroupName;
    }

    public void setMuscleGroupName(String muscleGroupName) {
        this.muscleGroupName = muscleGroupName;
    }

    public String getFstPicURL() {
        return fstPicURL;
    }

    public void setFstPicURL(String fstPicURL) {
        this.fstPicURL = fstPicURL;
    }

    public String getSecPicURL() {
        return secPicURL;
    }

    public void setSecPicURL(String secPicURL) {
        this.secPicURL = secPicURL;
    }

    public String getThdPicURL() {
        return thdPicURL;
    }

    public void setThdPicURL(String thdPicURL) {
        this.thdPicURL = thdPicURL;
    }
}
