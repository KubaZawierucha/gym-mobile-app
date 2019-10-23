package com.kubazawierucha.powerfulbodyapp.UserProfile;

public enum BMIClassifier {
    SEVERE_THINNESS("Severe Thinness"),
    MODERATE_THINNESS("Moderate Thinness"),
    MILD_THINNESS("Mild Thinness"),
    NORMAL("Normal"),
    OVERWEIGHT("Overweight"),
    OBESE_CLASS_I("Obese"),
    OBESE_CLASS_II("Huge Obese"),
    OBESE_CLASS_III("Danegrous Obese");

    private String text;

    BMIClassifier(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return this.text;
    }
}
